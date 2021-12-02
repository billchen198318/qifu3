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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.QueryResult;
import org.qifu.base.model.YesNo;
import org.qifu.core.util.CoreApiSupport;
import org.qifu.core.vo.TestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@Api(tags = {"TEST."}, description = "Test hello world.")
@RestController
@RequestMapping(value = "/api/hello")
public class HelloApiController extends CoreApiSupport {
	
	private static final long serialVersionUID = -2710621780849674671L;
	
	@Autowired
	RedisTemplate<String, String> redisTemplate;
	
	@ApiOperation(value="測試del", notes="測試用的接口del", authorizations={ @Authorization(value="Bearer") })
	@ApiImplicitParams({
		@ApiImplicitParam(name = "key", value = "編號", required = true, dataType = "string")
	})
	//@ResponseBody
	@DeleteMapping("/delPlay")
	public String delPlay(String key) {
		String flag = YesNo.NO;
		if (StringUtils.isBlank(key)) {
			return YesNo.NO;
		}		
		if (this.redisTemplate.delete(key)) {
			flag = YesNo.YES;
		}
		return flag;
	}
	
	@ApiOperation(value="測試", notes="測試用的接口", authorizations={ @Authorization(value="Bearer") })
	//@ResponseBody
	@PostMapping(value = "/play", produces = {MediaType.APPLICATION_JSON_VALUE})
	public QueryResult<String> play(@RequestBody TestModel data) {
		QueryResult<String> result = this.initResult();
		if (null == data || StringUtils.isBlank(data.getKey())) {
			this.noSuccessResult(result, BaseSystemMessage.parameterBlank());
			return result;
		}
		try {
			if (StringUtils.isBlank(data.getMsg())) {
				this.successResult(result, this.redisTemplate.opsForValue().get(data.getKey()));
				return result;
			}
			if ( StringUtils.defaultString(this.redisTemplate.opsForValue().get(data.getKey())).length() > 1000 ) {
				this.successResult(result, this.redisTemplate.opsForValue().get(data.getKey()));
				return result;
			}
			Map<String, String> dataMap = new HashMap<String, String>();
			dataMap.put("str", data.getMsg());
			ObjectMapper om = new ObjectMapper();
			String val = om.writeValueAsString(dataMap);
			this.redisTemplate.opsForValue().append(data.getKey(), val);
			this.successResult(result, val);
		} catch (Exception e) {
			e.printStackTrace();
			this.noSuccessResult(result, e);
		}
		return result;
	}
	
	@ApiOperation(value="測試2", notes="測試用的接口2", authorizations={ @Authorization(value="Bearer") })
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "編號", required = true, dataType = "string")
	})
	//@ResponseBody
	@GetMapping(value = "/testPV/{id}")
	public QueryResult<String> testPV(@PathVariable String id) {
		QueryResult<String> result = this.initResult();
		result.setValue(id);
		result.setMessage( "hello." );
		result.setSuccess( YES );
		return result;
	}
	
}
