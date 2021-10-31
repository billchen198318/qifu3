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
package org.qifu.core.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qifu.base.Constants;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.util.TokenBuilderUtils;
import org.qifu.core.entity.TbSysCode;
import org.qifu.core.service.ISysCodeService;
import org.qifu.core.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.auth0.jwt.impl.PublicClaims;
import com.auth0.jwt.interfaces.Claim;

public class UserBuilderInterceptor implements HandlerInterceptor {
	
	protected Logger logger = LogManager.getLogger(UserBuilderInterceptor.class);
	
	@Autowired
	ISysCodeService<TbSysCode, String> sysCodeService;	
	
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (request.getRequestURL().indexOf(Constants.TOKEN_CHECK_URL_PATH) == -1) {
			return true;
		}
		String authorization = StringUtils.defaultString(request.getHeader( Constants.TOKEN_Authorization )).trim();
		if (!authorization.startsWith(Constants.TOKEN_PREFIX)) {
			logger.warn(">>> No authorization uri: " + request.getRequestURI() + " , remote-address: " + request.getRemoteAddr() + " , remote-port: " + request.getRemotePort());
			response.setCharacterEncoding( Constants.BASE_ENCODING );
			response.getWriter().print( "{ \"success\":\"N\",\"message\":\"No authorization head " + Constants.TOKEN_PREFIX + "\",\"login\":\"N\",\"isAuthorize\":\"N\" }" );
			response.getWriter().flush();
			response.getWriter().close();
			return false;
		}
		Map<String, Claim> claimToken = TokenBuilderUtils.verifyToken(authorization.replaceFirst(Constants.TOKEN_PREFIX, "").replaceAll(" ", ""));
		if (TokenBuilderUtils.existsInfo(claimToken)) {
			String clientId = StringUtils.defaultString( claimToken.get(PublicClaims.AUDIENCE).asString() );
			String roleIds = "";
			TbSysCode sysCode = new TbSysCode();
			sysCode.setCode(clientId);
			try {
				sysCode = sysCodeService.selectByUniqueKey(sysCode).getValueEmptyThrowMessage();	
				roleIds = StringUtils.defaultString( sysCode.getParam1() );
			} catch (ServiceException se) {
				se.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			UserUtils.setUserInfoForUserLocalUtils( claimToken.get(Constants.TOKEN_USER_PARAM_NAME).asString(), roleIds );
			logger.info("User builder from JWT Authorization header : " + claimToken.get(Constants.TOKEN_USER_PARAM_NAME).asString() + " , role: " + roleIds);
		}
		if ( UserUtils.getCurrentUser() == null ) {
			logger.warn(">>> No authorization uri: " + request.getRequestURI() + " , remote-address: " + request.getRemoteAddr() + " , remote-port: " + request.getRemotePort());
			response.setCharacterEncoding( Constants.BASE_ENCODING );
			response.getWriter().print( Constants.NO_LOGIN_JSON_DATA );
			response.getWriter().flush();
			response.getWriter().close();
			return false;
		}
		return true;
	}
	
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    	
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    	UserUtils.removeForUserLocalUtils();
    }
	
}
