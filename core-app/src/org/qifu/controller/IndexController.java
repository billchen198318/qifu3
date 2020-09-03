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

import org.qifu.base.controller.BaseController;
import org.qifu.base.exception.ServiceException;
import org.qifu.core.model.MenuResult;
import org.qifu.core.util.MenuSupportUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController extends BaseController {
	
	@RequestMapping({"/", "/index"})
	public String index(ModelMap mm, HttpServletRequest request) {
		this.getDefaultModelMap(mm);
		MenuResult menuResult = null;
		try {
			menuResult = MenuSupportUtils.getMenuData( this.getBasePath(request) );
		} catch (ServiceException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (menuResult == null) {
			menuResult = new MenuResult();
		}
		mm.put("menuResult", menuResult);
		return REDIRECT_INDEX;
	}
	
}
