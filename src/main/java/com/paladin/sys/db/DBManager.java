/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.paladin.sys.db;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * 数据库管理
 *
 * @author Winter Lau (http://my.oschina.net/javayou)
 * @modify Erhu
 * @modifyDate 2010-2-2 下午10:18:50
 */
public class DBManager {

    private static final Log log = LogFactory.getLog(DBManager.class);
    private static final ThreadLocal<Connection> conns = new ThreadLocal<Connection>();
    private static DataSource dataSource;
    private static boolean show_sql = false;

    static {
        try {
            DbUtils.loadDriver("com.mysql.jdbc.Driver");
            // initDataSourceC3p0();
            // initDataSourceFromXml("net/paladin/sys/db/dataSource.xml", "dataSource");
            initDataSourceFromXml("com/paladin/sys/db/dataSource.xml", "bonecpDataSource");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * init DataSouce by spring
     */
    private static final void initDataSourceFromXml(String _xmpPath, String _beanName) {
        try {
            ApplicationContext factory = new ClassPathXmlApplicationContext(new String[]{_xmpPath});
            dataSource = (DataSource) factory.getBean(_beanName);
            Connection conn = getConnection();
            DatabaseMetaData mdm = conn.getMetaData();
            log.info("Connected to " + mdm.getDatabaseProductName() + " " + mdm.getDatabaseProductVersion());
            closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * close dataSource
     */
    public static final void closeDataSource() {
        try {
            dataSource.getClass().getMethod("close").invoke(dataSource);
        } catch (Exception e) {
            log.error("Unable to destroy DataSource!!! ", e);
        }
    }

    public static final Connection getConnection() {
        Connection conn = conns.get();
        try {
            if (conn == null || conn.isClosed()) {
                conn = dataSource.getConnection();
                conns.set(conn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (show_sql && !Proxy.isProxyClass(conn.getClass())) ? new DebugConnection(conn).getConnection() : conn;
    }

    /**
     * 关闭连接
     */
    public static final void closeConnection() {
        Connection conn = conns.get();
        try {
            if (conn != null && !conn.isClosed()) {
                conn.setAutoCommit(true);
                conn.close();
            }
        } catch (SQLException e) {
            log.error("Unable to close connection!!! ", e);
        }
        conns.set(null);
    }

    /**
     * 用于跟踪执行的SQL语句
     */
    static class DebugConnection implements InvocationHandler {
        private static final Log log = LogFactory.getLog(DebugConnection.class);

        private Connection conn = null;

        public DebugConnection(Connection conn) {
            this.conn = conn;
        }

        /**
         * Returns the conn.
         */
        public Connection getConnection() {
            return (Connection) Proxy.newProxyInstance(conn.getClass().getClassLoader(), conn.getClass()
                    .getInterfaces(), this);
        }

        public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
            try {
                String method = m.getName();
                if ("prepareStatement".equals(method) || "createStatement".equals(method))
                    log.info("SQL: " + args[0]);
                return m.invoke(conn, args);
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
        }
    }
}
