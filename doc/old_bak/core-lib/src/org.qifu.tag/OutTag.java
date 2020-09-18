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

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.qifu.base.model.YesNo;
import org.qifu.ui.UIComponent;
import org.qifu.ui.impl.Out;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class OutTag extends TagSupport {
	private String scope = UIComponent.SCOPE_REQUEST;
	private String value = "";
	private String escapeHtml = YesNo.NO;
	private String escapeJavaScript = YesNo.NO;	
	
	private Out handler() {
		Out out = new Out();
		out.setServletRequestAttributes((ServletRequestAttributes)RequestContextHolder.getRequestAttributes());
		out.setScope(this.scope);
		out.setValue(this.value);
		out.setEscapeHtml(this.escapeHtml);
		out.setEscapeJavaScript(this.escapeJavaScript);
		return out;
	}

	@Override
	public int doEndTag() throws JspException {
		Out out = this.handler();
		try {
			this.pageContext.getOut().write( out.getHtml() );
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}	
		out = null;
		return 0;
	}

	@Override
	public int doStartTag() throws JspException {
		return 0;
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getEscapeHtml() {
		return escapeHtml;
	}

	public void setEscapeHtml(String escapeHtml) {
		this.escapeHtml = escapeHtml;
	}

	public String getEscapeJavaScript() {
		return escapeJavaScript;
	}

	public void setEscapeJavaScript(String escapeJavaScript) {
		this.escapeJavaScript = escapeJavaScript;
	}
	
}
