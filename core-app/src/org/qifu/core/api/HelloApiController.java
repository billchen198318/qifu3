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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;

@Api(tags = {"TEST."}, description = "Test hello world.")
@Controller
@RequestMapping(value = "/api/hello")
public class HelloApiController extends CoreApiSupport {
	
	private static final long serialVersionUID = -2710621780849674671L;
	
	@Autowired
	RedisTemplate<String, String> redisTemplate;
	
	@ApiOperation(value="測試del", notes="測試用的接口del", authorizations={ @Authorization(value="Bearer") })
	@ApiImplicitParams({
		@ApiImplicitParam(name = "key", value = "編號", required = true, dataType = "String")
	})
	@ResponseBody
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
	/*
	@ApiImplicitParams({
		@ApiImplicitParam(name = "key", value = "編號", required = true, dataType = "String"),
		@ApiImplicitParam(name = "msg", value = "訊息字串", required = true, dataType = "String")
	})
	*/
	@ResponseBody
	@GetMapping("/play")
	public QueryResult<String> play(@ApiParam TestModel data) {
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
	
}
