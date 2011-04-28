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
package com.paladin.action;

import com.paladin.mvc.RequestContext;

/**
 * Game Action
 *
 * @author Erhu
 * @since Mar 27th, 2011
 */
public class GameAction extends BaseAction {

    public void index(final RequestContext _reqCtxt) {
        if (_reqCtxt.sessionAttr("user") != null) {
            forward(_reqCtxt, "html/game/linkup/linkup.jsp");
        } else {
            forward(_reqCtxt, "/login?r=/game");
        }
    }

    /**
     * 连连看
     */
    public void linkup(final RequestContext _reqCtxt) {
        index(_reqCtxt);
    }
}
