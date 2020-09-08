/* 
 * Copyright 2019-2021 qifu of copyright Chen Xin Nien
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
package org.qifu.controller;

import javax.servlet.http.HttpServletRequest;

import org.qifu.base.controller.BaseControllerSupport;
import org.qifu.base.exception.AuthorityException;
import org.qifu.base.exception.ControllerException;
import org.qifu.base.exception.ServiceException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SysSiteController extends BaseControllerSupport {
	
	protected static final String PKG_NAME = "sys_site";
	
	private void init(String type) throws AuthorityException, ControllerException, ServiceException {
		
	}
	
	@RequestMapping("/sysSitePage")
	public String mainPage(ModelMap mm, HttpServletRequest request) {
		String _page = PKG_NAME + "/" + "main-page";
		this.getDefaultModelMap(mm, "CORE_PROG001D0001Q");
		try {
			this.init("mainPage");
		} catch (AuthorityException e) {
			_page = this.getAuthorityExceptionPage(e, mm);
		} catch (ControllerException | ServiceException e) {
			_page = this.getServiceOrControllerExceptionPage(e, mm);
		} catch (Exception e) {
			_page = this.getExceptionPage(e, mm);
		}
		return this.viewPage(_page);
	}
	
	@RequestMapping("/sysSiteCreatePage")
	public String createPage(ModelMap mm, HttpServletRequest request) {
		String _page = PKG_NAME + "/" + "create-page";
		this.getDefaultModelMap(mm, "CORE_PROG001D0001A");
		try {
			this.init("createPage");
		} catch (AuthorityException e) {
			_page = this.getAuthorityExceptionPage(e, mm);
		} catch (ControllerException | ServiceException e) {
			_page = this.getServiceOrControllerExceptionPage(e, mm);
		} catch (Exception e) {
			_page = this.getExceptionPage(e, mm);
		}
		return this.viewPage(_page);
	}	
	
	@RequestMapping("/sysSiteEditPage")
	public String editPage(ModelMap mm, HttpServletRequest request) {
		String _page = PKG_NAME + "/" + "edit-page";
		this.getDefaultModelMap(mm, "CORE_PROG001D0001E");
		try {
			this.init("editPage");
		} catch (AuthorityException e) {
			_page = this.getAuthorityExceptionPage(e, mm);
		} catch (ControllerException | ServiceException e) {
			_page = this.getServiceOrControllerExceptionPage(e, mm);
		} catch (Exception e) {
			_page = this.getExceptionPage(e, mm);
		}	
		return this.viewPage(_page);
	}	
	
}
