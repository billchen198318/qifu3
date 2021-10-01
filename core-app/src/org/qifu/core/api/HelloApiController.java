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
import org.qifu.base.model.YesNo;
import org.qifu.core.util.CoreApiSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api
@Controller
@RequestMapping(value = "/helloApi")
public class HelloApiController extends CoreApiSupport {
	
	private static final long serialVersionUID = -2710621780849674671L;
	
	@Autowired
	RedisTemplate<String, String> redisTemplate;
	
	class MessageData implements java.io.Serializable {
		private static final long serialVersionUID = -334170817589326234L;
		
		private String str;

		public String getStr() {
			return str;
		}

		public void setStr(String str) {
			this.str = str;
		}
		
	}
	
	@ApiOperation(value="測試", notes="測試用的接口")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "key", value = "編號", required = true, dataType = "String"),
		@ApiImplicitParam(name = "msg", value = "訊息字串", required = true, dataType = "String")
	})
	@ResponseBody
	@GetMapping("/play")
	public String play(String key, String msg) {
		if (StringUtils.isBlank(key)) {
			return YesNo.NO;
		}
		try {
			if (StringUtils.isBlank(msg)) {
				return this.redisTemplate.opsForValue().get(key);
			}
			if ( StringUtils.defaultString(this.redisTemplate.opsForValue().get(key)).length() > 1000 ) {
				return this.redisTemplate.opsForValue().get(key);
			}
			this.redisTemplate.opsForValue().append(key, StringUtils.defaultString(this.redisTemplate.opsForValue().get(key)) + msg);
			return this.redisTemplate.opsForValue().get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return YesNo.NO;
	}
	
}
