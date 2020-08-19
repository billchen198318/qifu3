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
package org.qifu.base.controller;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.qifu.base.Constants;
import org.qifu.base.exception.BaseSysException;
import org.qifu.base.exception.ControllerException;
import org.qifu.base.model.CheckControllerFieldHandler;
import org.qifu.base.model.DefaultControllerJsonResultObj;
import org.qifu.base.model.PageOf;
import org.qifu.base.model.QueryControllerJsonResultObj;
import org.qifu.base.model.QueryResult;
import org.qifu.base.model.SearchValue;
import org.qifu.base.model.YesNo;
import org.qifu.util.SimpleUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import ognl.Ognl;
import ognl.OgnlException;

public abstract class BaseController {
	protected static final String PAGE_SYS_LOGIN = "system/login";
	protected static final String PAGE_SYS_SEARCH_NO_DATA = "system/searchNoData";
	protected static final String PAGE_SYS_LOGIN_AGAIN = "system/login_again";
	protected static final String PAGE_SYS_NO_AUTH = "system/auth1";
	protected static final String PAGE_SYS_ERROR = "system/error";
	protected static final String PAGE_SYS_WARNING = "system/warning";
	
	protected static final String REDIRECT_INDEX = "index";
	
	protected static final String YES = YesNo.YES;
	protected static final String NO = YesNo.NO;
	protected static final String EXCEPTION = ControllerException.PAGE_EXCEPTION_CODE;
	
	public String getPageRedirect(String url) {
		return "redirect:/" + url;
	}	
	
	public ModelMap getDefaultModelMap(ModelMap mm) {
	    ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	    HttpServletRequest request = servletRequestAttributes.getRequest();
	    //HttpServletResponse response = servletRequestAttributes.getResponse();
	    
		String basePath = this.getBasePath(request);
		mm.addAttribute("basePath", basePath);
		
		/*
		mm.addAttribute("errorContact", this.getErrorContact());
		mm.addAttribute("verMsg", this.getVerMsg());
		mm.addAttribute("jsVerBuild", this.getJsVerBuild());
		mm.addAttribute("loginCaptchaCodeEnable", this.getLoginCaptchaCodeEnable());
		mm.addAttribute("googleMapEnable", this.getGoogleMapEnable());
		mm.addAttribute("googleMapUrl", this.getGoogleMapUrl());
		mm.addAttribute("googleMapKey", this.getGoogleMapKey());
		mm.addAttribute("googleMapDefaultLat", this.getGoogleMapDefaultLat());
		mm.addAttribute("googleMapDefaultLng", this.getGoogleMapDefaultLng());
		mm.addAttribute("googleMapLanguage", this.getGoogleMapLanguage());
		mm.addAttribute("googleMapClientLocationEnable", this.getGoogleMapClientLocationEnable());
		mm.addAttribute("twitterEnable", this.getTwitterEnable());
		mm.addAttribute("isSuperRole", this.isSuperRole());
		mm.addAttribute("jqXhrType", this.getJqXhrType());
		mm.addAttribute("jqXhrTimeout", this.getJqXhrTimeout());
		mm.addAttribute("jqXhrCache", this.getJqXhrCache());
		mm.addAttribute("jqXhrAsync", this.getJqXhrAsync());
		mm.addAttribute("maxUploadSize", this.getMaxUploadSize());
		mm.addAttribute("maxUploadSizeMb", this.getMaxUploadSizeMb());
		*/
		
		return mm;
	}
	
//	public ModelMap getDefaultModelMap(String progId) {
//		ModelMap mm = this.getDefaultModelMap();
//		if (StringUtils.isBlank(progId)) {
//			return mm;
//		}
//		/*
//		mm.addAttribute("programId", progId);
//		mm.addAttribute("programName", MenuSupportUtils.getProgramName(progId));
//		*/
//		return mm;
//	}	
	
	/*
	public String getErrorContact() {
		return String.valueOf( Constants.getSettingsMap().get("basePage.errorContact") );
	}
	
	public String getVerMsg() {
		return String.valueOf( Constants.getSettingsMap().get("basePage.verMsg") );
	}
	
	public String getJsVerBuild() {
		return String.valueOf( Constants.getSettingsMap().get("basePage.jsVerBuild") );
	}
	
	public String getLoginCaptchaCodeEnable() {
		return Constants.getLoginCaptchaCodeEnable();
	}
	*/
	
	public String generateOid() {
		return SimpleUtils.getUUIDStr();
	}	
	
	public String getBasePath(HttpServletRequest request) {
		return request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
	}
	
	protected String defaultString(String str) {
		return StringUtils.defaultString(str);
	}
	
	protected void setPageMessage(HttpServletRequest request, String pageMessage) {
		if (null!=pageMessage && pageMessage.length()>=Constants.MAX_SYS_DESCRIPTION_LENGTH) {
			pageMessage=pageMessage.substring(0, Constants.MAX_SYS_DESCRIPTION_LENGTH);
		}
		request.setAttribute(Constants.PAGE_MESSAGE, pageMessage);
	}	
	
	/*
	protected void setPageErrorContact(HttpServletRequest request) {
		request.setAttribute("errorContact", this.getErrorContact());
	}
	*/
	
	protected void setPageMessage(ModelMap mm, String pageMessage) {
		if (null!=pageMessage && pageMessage.length()>=Constants.MAX_SYS_DESCRIPTION_LENGTH) {
			pageMessage=pageMessage.substring(0, Constants.MAX_SYS_DESCRIPTION_LENGTH);
		}
		mm.addAttribute(Constants.PAGE_MESSAGE, pageMessage);
	}
	
//	protected void setPageErrorContact(ModelMap mm) {
//		mv.addObject("errorContact", this.getErrorContact());
//	}	
	
	protected String getNowDate() {
		return SimpleUtils.getStrYMD("/");
	}
	
	protected String getNowDate2() {
		return SimpleUtils.getStrYMD("-");
	}		
	
	protected <T> DefaultControllerJsonResultObj<T> getDefaultJsonResult(String progId) {
		DefaultControllerJsonResultObj<T> result = DefaultControllerJsonResultObj.build();
		this.setResultDefaultValue(result, progId);
		return result;
	}
	
	protected <T> QueryControllerJsonResultObj<T> getQueryJsonResult(String progId) {
		QueryControllerJsonResultObj<T> result = QueryControllerJsonResultObj.build();
		this.setResultDefaultValue(result, progId);
		return result;
	}	
	
	private void setResultDefaultValue(DefaultControllerJsonResultObj<?> result, String progId) {
		/*
		if (!StringUtils.isBlank(this.getAccountId())) {
			result.setLogin( YES );
			Subject subject = this.getSubject();
			if (subject.hasRole(Constants.SUPER_ROLE_ALL) || subject.hasRole(Constants.SUPER_ROLE_ADMIN)) {
				result.setIsAuthorize( YES );
			}
			if (subject.isPermitted(progId)) {
				result.setIsAuthorize( YES );
			}
			if (!YES.equals(result.getIsAuthorize())) {
				result.setMessage( "no authorize!" );
			}
		} else {
			result.setMessage( "Please login!" );
		}
		*/		
	}
	
	protected boolean isAuthorizeAndLoginFromControllerJsonResult(DefaultControllerJsonResultObj<?> result) {
		if (YES.equals(result.getIsAuthorize()) && YES.equals(result.getLogin())) {
			return true;
		}
		return false;
	}
	
	protected void exceptionResult(DefaultControllerJsonResultObj<?> result, Exception e) {
		e.printStackTrace();
		result.setMessage( e.getMessage().toString() );
		result.setSuccess( EXCEPTION );
	}
	
	protected String getExceptionMessage(Exception e) {
		String str = "";
		if (e != null && e.getMessage() != null) {
			str = e.getMessage();
		}
		if (e != null && e.getMessage() == null) {
			str = e.toString();
		}
		if (e == null) {
			str = "null undefined";
		}
		return str;
	}
	
	protected String getAuthorityExceptionPage(BaseSysException e, HttpServletRequest request) {
		this.setPageMessage(request, this.getExceptionMessage(e));
		return PAGE_SYS_NO_AUTH;
	}
	
	protected String getAuthorityExceptionPage(BaseSysException e, ModelMap mm) {
		this.setPageMessage(mm, this.getExceptionMessage(e));
		return PAGE_SYS_NO_AUTH;
	}	
	
	protected String getServiceOrControllerExceptionPage(BaseSysException e, HttpServletRequest request) {
		this.setPageMessage(request, this.getExceptionMessage(e));
		return PAGE_SYS_SEARCH_NO_DATA;
	}
		
	protected String getServiceOrControllerExceptionPage(BaseSysException e, ModelMap mm) {
		this.setPageMessage(mm, this.getExceptionMessage(e));
		return PAGE_SYS_SEARCH_NO_DATA;
	}	
	
	protected String getExceptionPage(Exception e, HttpServletRequest request) {
		e.printStackTrace();
		this.setPageMessage(request, this.getExceptionMessage(e));
		return PAGE_SYS_ERROR;
	}
	
	protected String getExceptionPage(Exception e, ModelMap mm) {
		e.printStackTrace();
		this.setPageMessage(mm, this.getExceptionMessage(e));
		return PAGE_SYS_ERROR;
	}
	
	protected void fillObjectFromRequest(HttpServletRequest request, Object root) {
		Enumeration<String> pNames = request.getParameterNames();
		while (pNames.hasMoreElements()) {
			String key = pNames.nextElement();
			Object value = request.getParameter(key);
			try {
				Ognl.setValue(key, root, value);
			} catch (OgnlException e) {
				//e.printStackTrace();
			}
		}
	}
	
	protected PageOf getPageOf(HttpServletRequest request) {
		PageOf pageOf = new PageOf();
		fillObjectFromRequest(request, pageOf);
		return pageOf;
	}
	
	protected SearchValue getSearchValue(HttpServletRequest request) {
		SearchValue searchValue = new SearchValue();
		fillObjectFromRequest(request, searchValue);
		return searchValue;
	}	
	
	/*
	protected <T> void setQueryGridJsonResult(QueryControllerJsonResultObj<T> jsonResult, QueryResult<T> queryResult, PageOf pageOf) {
		if (queryResult.getValue() != null) {
			jsonResult.setValue( queryResult.getValue() );
			jsonResult.setPageOfCountSize( queryResult.getRowCount() );
			jsonResult.setPageOfSelect( NumberUtils.toInt(pageOf.getSelect(), 1) );
			jsonResult.setPageOfShowRow( NumberUtils.toInt(pageOf.getShowRow(), PageOf.Rows[0]) );
			jsonResult.setPageOfSize( NumberUtils.toInt(pageOf.getSize(), 1) );
			jsonResult.setSuccess(YesNo.YES);
		} else {
			jsonResult.setMessage( queryResult.getSystemMessage().getValue() );
		}		
	}
	*/
	
	protected <T> CheckControllerFieldHandler<T> getCheckControllerFieldHandler(DefaultControllerJsonResultObj<T> result) {
		return CheckControllerFieldHandler.build(result);
	}
	
	protected boolean noSelect(String selectValue) {
		if (StringUtils.isBlank(selectValue) || Constants.HTML_SELECT_NO_SELECT_ID.equals(selectValue)) {
			return true;
		}
		return false;
	}
	
	protected Map<String, String> getPleaseSelectMap(boolean pleaseSelect) {
		Map<String, String> dataMap = new LinkedHashMap<String, String>();
		if (pleaseSelect) {
			dataMap.put(Constants.HTML_SELECT_NO_SELECT_ID, Constants.HTML_SELECT_NO_SELECT_NAME);
		}
		return dataMap;
	}
	
	protected List<String> transformAppendKeyStringToList(String appendOid) {
		List<String> list = new LinkedList<String>();
		if (StringUtils.isBlank(appendOid)) {
			return list;
		}
		String tmp[] = appendOid.split(Constants.ID_DELIMITER);
		for (int i=0; tmp != null && i < tmp.length; i++) {
			if (StringUtils.isBlank(tmp[i])) {
				continue;
			}
			if (list.contains(tmp[i])) {
				continue;
			}
			list.add(tmp[i]);
		}
		return list;
	}	
	
}
