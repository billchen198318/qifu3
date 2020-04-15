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

import org.qifu.core.util.CoreApiSupport;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api
@Controller
@RequestMapping(value = "/helloApi")
public class HelloApiController extends CoreApiSupport {
	
	private static final long serialVersionUID = -2710621780849674671L;
	
	@ApiOperation(value="測試", notes="測試用的接口")
    @ApiImplicitParam(name = "msg", value = "訊息字串", required = true, dataType = "String")
	@ResponseBody
	@GetMapping("/play")
	public String play(String msg) {
		return "測試=" + msg;
	}	
	
}
