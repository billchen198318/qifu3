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

import org.qifu.ui.impl.Else;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class ElseTag extends TagSupport {
	
	private Else handler() {
		Else elseComp = new Else();
		elseComp.setServletRequestAttributes((ServletRequestAttributes)RequestContextHolder.getRequestAttributes());
		return elseComp;
	}

	@Override
	public int doEndTag() throws JspException {
		return 0;
	}

	@Override
	public int doStartTag() throws JspException {
		Else elseComp = this.handler();
		if (elseComp.getTestResult()) {
			elseComp = null;
			return EVAL_BODY_INCLUDE;
		}	
		elseComp = null;		
		return SKIP_BODY;
	}

	@Override
	public void release() {
		
	}
	
}
