/* 
 * Copyright 2012-2017 qifu of copyright Chen Xin Nien
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * -----------------------------------------------------------------------
 * 
 * author: 	Chen Xin Nien
 * contact: chen.xin.nien@gmail.com
 * 
 */
package org.qifu.ui.impl;

import org.qifu.ui.UIComponent;
import org.qifu.ui.UIComponentValueUtils;
import org.springframework.web.context.request.ServletRequestAttributes;

public class Else implements UIComponent {
	private ServletRequestAttributes servletRequestAttributes = null;
	
	@Override
	public void setId(String id) {
		
	}
	
	@Override
	public String getId() {
		return "";
	}
	
	@Override
	public void setName(String name) {
		
	}
	
	@Override
	public String getName() {
		return "";
	}
	
	@Override
	public String getScript() throws Exception {
		return "";
	}

	@Override
	public String getHtml() throws Exception {
		return "";
	}

	@Override
	public void setServletRequestAttributes(ServletRequestAttributes servletRequestAttributes) {
		this.servletRequestAttributes = servletRequestAttributes;
	}

	public Boolean getTestResult() {
		if ( !UIComponentValueUtils.foundIfResult(this.servletRequestAttributes) ) { // 之前沒有 IfTag 或 ElseifTag , 所以 Else 不會成立
			return false;
		}
		if ( UIComponentValueUtils.getIfResult(this.servletRequestAttributes) ) { // 之前 IfTag 或 ElseifTag 是 true , 所以 Else 不會成立
			UIComponentValueUtils.removeIfResult(this.servletRequestAttributes);
			return false;
		}
		UIComponentValueUtils.removeIfResult(this.servletRequestAttributes);
		return true;
	}
	
}
