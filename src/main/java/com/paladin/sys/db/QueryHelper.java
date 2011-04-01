/**
 * OSChina 使用的是 dbutils 这个JDBC的封装类库来进行数据库操作。
 * 而 QueryHelper 则是在 dbutils 的基础上进行一级简单的封装，
 * 提供一些常用的数据库操作方法和对数据缓存的支持。数据库连接的释放方法请看这里。
 */
package com.paladin.sys.db;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.ArrayUtils;

/**
 * 数据库查询助手
 * 
 * @author Erhu <br>
 */
@SuppressWarnings("unchecked")
public class QueryHelper {
	private final static QueryRunner RUNNER = new QueryRunner();
	private final static ColumnListHandler COLUMN_LIST_HANDLER = new ColumnListHandler() {
		@Override
		protected Object handleRow(ResultSet rs) throws SQLException {
			Object obj = super.handleRow(rs);
			if (obj instanceof BigInteger)
				return ((BigInteger) obj).longValue();
			return obj;
		}
	};
	private final static ScalarHandler SCALAR_HANDLER = new ScalarHandler() {
		@Override
		public Object handle(ResultSet rs) throws SQLException {
			Object obj = super.handle(rs);
			if (obj instanceof BigInteger)
				return ((BigInteger) obj).longValue();
			return obj;
		}
	};

	private final static List<Class<?>> PRIMITIVE_CLASSES = new ArrayList<Class<?>>() {
		private static final long serialVersionUID = 1L;
		{
			add(Long.class);
			add(Integer.class);
			add(String.class);
			add(java.util.Date.class);
			add(java.sql.Date.class);
			add(java.sql.Timestamp.class);
		}
	};

	private final static boolean IS_PRIMITIVE(Class<?> cls) {
		return cls.isPrimitive() || PRIMITIVE_CLASSES.contains(cls);
	}

	/**
	 * 获取数据库连接
	 * 
	 * @return
	 */
	public static Connection getConnection() {
		try {
			return DBManager.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 读取某个对象
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public static <T> T read(Class<T> beanClass, String sql, Object... params) {
		try {
			return (T) RUNNER.query(getConnection(), sql, IS_PRIMITIVE(beanClass) ? SCALAR_HANDLER : new BeanHandler(
					beanClass), params);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBManager.closeConnection();
		}
		return null;
	}

	/**
	 * 对象查询
	 * 
	 * @param <T>
	 * @param beanClass
	 * @param sql
	 * @param params
	 * @return
	 */
	public static <T> List<T> query(Class<T> beanClass, String sql, Object... params) {
		try {
			return (List<T>) RUNNER.query(getConnection(), sql, IS_PRIMITIVE(beanClass) ? COLUMN_LIST_HANDLER
					: new BeanListHandler(beanClass), params);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBManager.closeConnection();
		}
		return null;
	}

	/**
	 * 分页查询
	 * 
	 * @param <T>
	 * @param beanClass
	 * @param _sql
	 * @param _currentPage
	 * @param count
	 * @param params
	 * @return
	 */
	public static <T> List<T> query_slice(Class<T> beanClass, String _sql, int _currentPage, int count,
			Object... params) {
		if (_currentPage < 0 || count < 0)
			throw new IllegalArgumentException("Illegal parameter of 'page' or 'count', Must be positive.");
		int from = (_currentPage - 1) * count;
		count = (count > 0) ? count : Integer.MAX_VALUE;
		return query(beanClass, _sql + " LIMIT ?, ?", ArrayUtils.addAll(params, new Integer[] { from, count }));
	}

	/**
	 * 执行统计查询语句，语句的执行结果必须只返回一个数值
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public static long stat(String sql, Object... params) {
		try {
			Number num = (Number) RUNNER.query(getConnection(), sql, SCALAR_HANDLER, params);
			return (num != null) ? num.longValue() : -1;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		} finally {
			DBManager.closeConnection();
		}
	}

	/**
	 * 执行INSERT/UPDATE/DELETE语句
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public static int update(String sql, Object... params) {
		try {
			return RUNNER.update(getConnection(), sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBManager.closeConnection();
		}
		return -1;
	}

	/**
	 * 批量执行指定的SQL语句
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public static int[] batch(String sql, Object[][] params) {
		try {
			return RUNNER.batch(getConnection(), sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBManager.closeConnection();
		}
		return null;
	}
}