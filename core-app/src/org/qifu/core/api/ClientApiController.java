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
import org.qifu.base.Constants;
import org.qifu.base.model.QueryResult;
import org.qifu.base.model.YesNo;
import org.qifu.base.util.TokenBuilderUtils;
import org.qifu.core.util.CoreApiSupport;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api
@Controller
@RequestMapping(value = "/api")
public class ClientApiController extends CoreApiSupport {
	
	private static final long serialVersionUID = 7605095088499829681L;
	
	@ApiOperation(value="Client", notes="Info")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "clientId", value = "client no/id", required = true, dataType = "String")
	})
	@ResponseBody
	@PostMapping("/client")
	public QueryResult<String> client(String clientId) {
		QueryResult<String> result = this.initResult();
		if (StringUtils.isBlank(clientId)) {
			result.setMessage( "No clientId parameter value." );
			return result;
		}
		// need modify first to check this clientId parameter from you settings config.
		String token = TokenBuilderUtils.createToken("The Client Id", Constants.SYSTEM_BACKGROUND_USER, YesNo.NO, "ClientId", clientId);
		if (!StringUtils.isBlank(token)) {
			result.setSuccess( YesNo.YES );
			result.setValue(token);
		}
		return result;
	}
	
}
