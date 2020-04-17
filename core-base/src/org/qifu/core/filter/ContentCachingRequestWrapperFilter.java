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
package org.qifu.core.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.Constants;
import org.qifu.base.CoreAppConstants;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

import com.auth0.jwt.interfaces.Claim;
import com.fasterxml.jackson.databind.ObjectMapper;

@Order(1)
@Component
@WebFilter(urlPatterns = {"/*" })
public class ContentCachingRequestWrapperFilter implements Filter {	
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) request);
		chain.doFilter(requestWrapper, response);	
		String uri = StringUtils.defaultString( requestWrapper.getRequestURI() ).trim();
		List<String> excludePathPatternsList = CoreAppConstants.getContentCachingRequestWrapperFilterExcludePathPatterns();
		if (null != excludePathPatternsList && excludePathPatternsList.contains(uri)) {
			return;
		}
		
		String queryStr = "";
		try {
			queryStr = StringUtils.defaultString(this.getQueryString(requestWrapper, requestWrapper));
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		uri = StringUtils.substring(uri, 0, Constants.ContentCachingRequestWrapperFilter_MAX_URI_SIZE);
		queryStr = StringUtils.substring(queryStr, 0, Constants.ContentCachingRequestWrapperFilter_MAX_QUERY_STRING_SIZE);
		this.logEvent(requestWrapper.getHeader( Constants.Authorization ), uri, queryStr);
		*/
	}
	
	@Override
	public void destroy() {
		
	}
	
	private void logEvent(String authToken, String uri, String queryString) {		
		/*
		Map<String, Claim> claimMap = null;
		if (!StringUtils.isBlank(authToken)) {
			claimMap = TokenBuilderUtil.verifyToken(authToken, null);
		}
		String userId = "no-sign";
		String programId = "unknown";
		String tokenCurrentId = "no-token";
		if (null != claimMap && claimMap.size() > 0) {
			tokenCurrentId = claimMap.get(Constants.TOKEN_CURRENT_ID_NAME).asString();
			userId = claimMap.get(Constants.WWUNION_USER_PARAM_NAME).asString();
			programId = claimMap.get(Constants.TOKEN_PROG_ID_NAME).asString();
		}
		SysApieventLog sysApieventLog = new SysApieventLog();
		sysApieventLog.setTokenCurrentId(tokenCurrentId);
		sysApieventLog.setProgramId(programId);
		sysApieventLog.setUserId(userId);
		sysApieventLog.setUri(uri);
		sysApieventLog.setQueryStr(queryString);
		boolean createUserInfo = false;
		try {
			@SuppressWarnings("unchecked")
			ISysApieventLogService<SysApieventLog, BigDecimal> sysApieventLogService = Application.context.getBean(ISysApieventLogService.class);
			if (UserLocalUtils.getUserInfo() == null) {
				UserInfo userObj = new UserInfo();
				userObj.setUserId( Constants.DEFAULT_SYSTEM_USERID );
				UserLocalUtils.setUserInfo( userObj );
				createUserInfo = true;
			}
			sysApieventLogService.insertIgnoreUK(sysApieventLog);
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (createUserInfo) {
				UserLocalUtils.remove();
			}
		}
		*/
	}
	
	private String getQueryString(ContentCachingRequestWrapper requestWrapper, ServletRequest request) throws UnsupportedEncodingException, Exception {
		String queryStr = new String(requestWrapper.getContentAsByteArray(), request.getCharacterEncoding());
		if (StringUtils.isBlank(queryStr)) {
			queryStr = requestWrapper.getQueryString();
		}
		if (StringUtils.isBlank(queryStr)) {
			Map<String, String> paramMap = new HashMap<String, String>();
			Enumeration<String> paramNames = requestWrapper.getParameterNames();
			while (paramNames.hasMoreElements()) {
				String p = paramNames.nextElement();
				String v = requestWrapper.getParameter(p);
				paramMap.put(p, v);
			}
			queryStr = new ObjectMapper().writeValueAsString(paramMap);
		}
		return queryStr;
	}
	
}
