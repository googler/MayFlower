package com.paladin.action;
/**
 * Copyright (C) 2011 Erhu Inc.
 *
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

import com.paladin.bean.Motto;
import com.paladin.common.Constants;
import com.paladin.common.Tools;
import com.paladin.mvc.RequestContext;
import com.paladin.sys.db.QueryHelper;

import java.util.GregorianCalendar;
import java.util.List;

/**
 * 箴言功能点的业务逻辑
 *
 * @author: Erhu
 * @date: 11-4-28 上午9:53
 */
public class MottoAction extends BaseAction {
    /**
     * 随机箴言
     */
    private static List<Motto> randomMotto;
    /**
     * 上次获取箴言的时间(单位秒)
     */
    private static long updateTime;

    static {
        updateTime = new GregorianCalendar().getTimeInMillis() / 1000;
        generateRandomMotto(); // 类加载时获取箴言
    }

    @Override
    protected void index(RequestContext _reqCtxt) {

    }

    /**
     * 提供随机箴言(公共调用)
     */
    public static Motto getRandomMotto() {
        // 获取当前时间
        long time = new GregorianCalendar().getTimeInMillis() / 1000;
        // 若更新间隔超过指定值，则重新获取箴言并更新updateTime
        if (randomMotto == null || time - updateTime > Constants.MINUTE_UPDATE_MOTTO * 60) {
            updateTime = time;
            generateRandomMotto();
        }
        long index = Tools.random(0, Constants.NUM_RANDOM_MOTTO - 1);
        if (randomMotto.size() <= index)
            return null;
        return (Motto) (randomMotto.toArray())[(int) index];
    }

    /**
     * 从DB中提取箴言
     */
    private static void generateRandomMotto() {
        // 获取总记录数
        String sql = "SELECT COUNT(*) FROM MOTTO";
        long count = QueryHelper.stat(sql);
        // 在[0, count - 7]之间取一个数作为LIMIT的起点
        long begin = Math.abs(Tools.random(0, count - Constants.NUM_RANDOM_MOTTO));
        // 获取箴言
        sql = "SELECT * FROM MOTTO LIMIT ?, ?";
        randomMotto = QueryHelper.query(Motto.class, sql, new Object[]{begin, Constants.NUM_RANDOM_MOTTO});
    }
}
