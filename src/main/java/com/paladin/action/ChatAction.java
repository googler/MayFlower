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
 * Chat Action
 * 
 * @author Erhu
 * @since Mar 17th, 2011
 */
public class ChatAction extends BaseAction {

	public void index(final RequestContext _reqCtxt) {
		if (_reqCtxt.sessionAttr("user") != null) {
			forward(_reqCtxt, "/html/chat/room.jsp");
		} else {
			forward(_reqCtxt, "/login?r=/chat");
		}
	}
}
