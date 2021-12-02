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
package org.qifu.core.api;

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.QueryResult;
import org.qifu.base.model.YesNo;
import org.qifu.base.util.TokenBuilderUtils;
import org.qifu.core.entity.TbSysCode;
import org.qifu.core.service.ISysCodeService;
import org.qifu.core.util.CoreApiSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags = {"Get client info."}, description = "For Client get an access token.")
@RestController
@RequestMapping(value = "/api")
public class ClientApiController extends CoreApiSupport {
	
	private static final long serialVersionUID = 7605095088499829681L;
	
	@Autowired
	ISysCodeService<TbSysCode, String> sysCodeService;
	
	/**
	 * SELECT * FROM tb_sys_code WHERE CODE = '9TYM7TRuILqFk9XoR0v6Yx672'
	 * 
	 * @param programId		PROG001
	 * @param userId		tester
	 * @param clientId		9TYM7TRuILqFk9XoR0v6Yx672
	 * @return
	 */
	@ApiOperation(value="Client", notes="Info")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "programId", value = "program no/id", required = true, dataType = "string"),
		@ApiImplicitParam(name = "userId", value = "user no/id", required = true, dataType = "string"),
		@ApiImplicitParam(name = "clientId", value = "client no/id", required = true, dataType = "string")
	})
	//@ResponseBody
	@PostMapping("/client")
	public QueryResult<String> client(String programId, String userId, String clientId) {
		QueryResult<String> result = this.initResult();
		if (StringUtils.isBlank(programId) || StringUtils.isBlank(userId) || StringUtils.isBlank(clientId)) {
			result.setMessage( "No parameter value." );
			return result;
		}
		TbSysCode sysCode = new TbSysCode();
		sysCode.setCode(clientId);
		try {
			sysCode = sysCodeService.selectByUniqueKey(sysCode).getValueEmptyThrowMessage();
			String token = TokenBuilderUtils.createToken(programId, userId, StringUtils.defaultString(sysCode.getName()), clientId);
			if (!StringUtils.isBlank(token)) {
				result.setSuccess( YesNo.YES );
				result.setValue(token);
			}			
		} catch (ServiceException se) {
			result.setMessage( (se != null && se.getMessage() != null) ? se.getMessage() : "null" );
			se.printStackTrace();
		} catch (Exception e) {
			result.setMessage( (e != null && e.getMessage() != null) ? e.getMessage() : "null" );
			e.printStackTrace();
		}
		return result;
	}
	
}
