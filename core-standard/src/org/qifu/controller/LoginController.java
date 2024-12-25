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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.controller.BaseControllerSupport;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.YesNo;
import org.qifu.base.properties.MockEnableConfigProperties;
import org.qifu.core.entity.TbSysCode;
import org.qifu.core.service.ISysCodeService;
import org.qifu.core.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController extends BaseControllerSupport {
	
	@Autowired
	MockEnableConfigProperties mockEnableConfigProperties;
	
	@Autowired
	ISysCodeService<TbSysCode, String> sysCodeService;
	
	@RequestMapping("/loginPage")
	public String loginPage(ModelMap mm, HttpServletRequest request) {
		mm.put("errMsg", "");
		if ( request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION") != null ) {
			String errMsg = ((Exception) request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION")).getMessage();
			request.getSession().removeAttribute("SPRING_SECURITY_LAST_EXCEPTION");
			mm.put("errMsg", errMsg);
		}
		return PAGE_SYS_LOGIN;
	}
	
	// 暫時為了啟用 http csrf csrfTokenRepository 配置這個method, 登出用
	@RequestMapping("/logout")
	public String logout(ModelMap mm, HttpServletRequest request) {
		mm.put("errMsg", "");
		HttpSession session = request.getSession(false);
	    if (session != null) {
	        session.invalidate();
	    }
		return PAGE_SYS_LOGIN;
	}	
	
	private boolean hasMockCode(String mockId) throws ServiceException, Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("param1", mockId);
		return sysCodeService.count(param) > 0;
	}
	
	@RequestMapping("/mockLoginPage")
	public String mockLogin(ModelMap mm, HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (!YesNo.YES.equals(mockEnableConfigProperties.getLoginEnable())) {
			return PAGE_SYS_LOGIN;
		}
		String mockId = request.getParameter("mockId");
		String uId = request.getParameter("uId");
		if (StringUtils.isBlank(mockId) || StringUtils.isBlank(uId)) {
			return PAGE_SYS_LOGIN;
		}
		try {
			if (this.hasMockCode(mockId)) {
				mm.put("uId", uId);
				mm.put("mPw", "mock_pass");
				UserUtils.hasLoginByMock(mockId);
				return "view/mock_login";
			}
		} catch (ServiceException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return PAGE_SYS_LOGIN;
	}	
	
}
