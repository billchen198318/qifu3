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
package org.qifu.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.qifu.ui.UIComponent;
import org.qifu.ui.impl.If;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class IfTag extends TagSupport {
	private String scope = UIComponent.SCOPE_REQUEST;
	private String test = "";
	
	private If handler() {
		If ifTest = new If();
		ifTest.setServletRequestAttributes((ServletRequestAttributes)RequestContextHolder.getRequestAttributes());
		ifTest.setScope(this.scope);
		ifTest.setTest(this.test);
		return ifTest;
	}

	@Override
	public int doEndTag() throws JspException {
		return 0;
	}

	@Override
	public int doStartTag() throws JspException {
		If ifTest = this.handler();
		if (ifTest.getTestResult()) {
			ifTest = null;
			return EVAL_BODY_INCLUDE;
		}	
		ifTest = null;		
		return SKIP_BODY;
	}

	@Override
	public void release() {
		
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

}
