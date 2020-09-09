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
import org.qifu.base.controller.IPageNamespaceProvide;
import org.qifu.base.exception.AuthorityException;
import org.qifu.base.exception.ControllerException;
import org.qifu.base.exception.ServiceException;
import org.qifu.core.entity.TbSys;
import org.qifu.core.entity.TbSysIcon;
import org.qifu.core.entity.TbSysProg;
import org.qifu.core.logic.ISystemProgramLogicService;
import org.qifu.core.service.ISysIconService;
import org.qifu.core.service.ISysProgService;
import org.qifu.core.service.ISysService;
import org.qifu.core.util.IconUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SysProgramController extends BaseControllerSupport implements IPageNamespaceProvide {
	
	@Autowired
	ISysProgService<TbSysProg, String> sysProgService;
	
	@Autowired
	ISysService<TbSys, String> sysService;
	
	@Autowired
	ISysIconService<TbSysIcon, String> sysIconService;
	
	@Autowired
	ISystemProgramLogicService systemProgramLogicService;
	
	@Override
	public String viewPageNamespace() {
		return "sys_prog";
	}	
	
	private void init(String type, ModelMap mm) throws AuthorityException, ControllerException, ServiceException, Exception {
		mm.put( "sysMap", this.sysService.findSysMap(true) );
		mm.put( "iconMap", IconUtils.getIconsSelectData() );		
	}
	
	private void fetch(ModelMap mm, String oid) throws AuthorityException, ControllerException, ServiceException, Exception {
		/*
		DefaultResult<SysProgVO> result = this.sysProgService.findObjectByOid(sysProg);
		if (result.getValue() == null) {
			throw new ControllerException(result.getSystemMessage().getValue());
		}
		sysProg = result.getValue();
		mv.addObject("sysProg", sysProg);
		
		TbSysIcon sysIcon = new TbSysIcon();
		sysIcon.setIconId(sysProg.getIcon());
		DefaultResult<TbSysIcon> iconResult = this.sysIconService.findEntityByUK(sysIcon);
		if (iconResult.getValue() == null) {
			throw new ControllerException( iconResult.getSystemMessage().getValue() );
		}
		sysIcon = iconResult.getValue();		
		mv.addObject("iconSelectOid", sysIcon.getOid());
		
		TbSys sys = new TbSys();
		sys.setSysId(sysProg.getProgSystem());
		DefaultResult<TbSys> sysResult = this.sysService.findEntityByUK(sys);
		if (sysResult.getValue() == null) {
			throw new ControllerException( sysResult.getSystemMessage().getValue() );
		}
		sys = sysResult.getValue();
		mv.addObject("sysSelectOid", sys.getOid());
		 */
	}
	
	@RequestMapping("/sysProgramPage")
	public String mainPage(ModelMap mm, HttpServletRequest request) {
		String viewName = this.viewMainPage();
		this.getDefaultModelMap(mm, "CORE_PROG001D0002Q");
		try {
			this.init("mainPage", mm);
		} catch (AuthorityException e) {
			viewName = this.getAuthorityExceptionPage(e, mm);
		} catch (ControllerException | ServiceException e) {
			viewName = this.getServiceOrControllerExceptionPage(e, mm);
		} catch (Exception e) {
			viewName = this.getExceptionPage(e, mm);
		}
		return viewName;
	}
	
	@RequestMapping("/sysProgramCreatePage")
	public String createPage(ModelMap mm, HttpServletRequest request) {
		String viewName = this.viewCreatePage();
		this.getDefaultModelMap(mm, "CORE_PROG001D0002A");
		try {
			this.init("createPage", mm);
		} catch (AuthorityException e) {
			viewName = this.getAuthorityExceptionPage(e, mm);
		} catch (ControllerException | ServiceException e) {
			viewName = this.getServiceOrControllerExceptionPage(e, mm);
		} catch (Exception e) {
			viewName = this.getExceptionPage(e, mm);
		}
		return viewName;
	}	
	
	@RequestMapping("/sysProgramEditPage")
	public String editPage(ModelMap mm, HttpServletRequest request, @RequestParam(name="oid") String oid) {
		String viewName = this.viewEditPage();
		this.getDefaultModelMap(mm, "CORE_PROG001D0002E");
		try {
			this.init("editPage", mm);
			this.fetch(mm, oid);
		} catch (AuthorityException e) {
			viewName = this.getAuthorityExceptionPage(e, mm);
		} catch (ControllerException | ServiceException e) {
			viewName = this.getServiceOrControllerExceptionPage(e, mm);
		} catch (Exception e) {
			viewName = this.getExceptionPage(e, mm);
		}	
		return viewName;
	}		
	
}
