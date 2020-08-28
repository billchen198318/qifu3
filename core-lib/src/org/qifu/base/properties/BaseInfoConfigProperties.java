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
package org.qifu.base.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:appConfig.properties")
@ConfigurationProperties(prefix = "base")
public class BaseInfoConfigProperties {
	
	private String errorContact;
	
	private String verMsg;
	
	private String jsVerBuild;
	
	private String loginCaptchaCodeEnable;

	public String getErrorContact() {
		return errorContact;
	}

	public void setErrorContact(String errorContact) {
		this.errorContact = errorContact;
	}

	public String getVerMsg() {
		return verMsg;
	}

	public void setVerMsg(String verMsg) {
		this.verMsg = verMsg;
	}

	public String getJsVerBuild() {
		return jsVerBuild;
	}

	public void setJsVerBuild(String jsVerBuild) {
		this.jsVerBuild = jsVerBuild;
	}

	public String getLoginCaptchaCodeEnable() {
		return loginCaptchaCodeEnable;
	}

	public void setLoginCaptchaCodeEnable(String loginCaptchaCodeEnable) {
		this.loginCaptchaCodeEnable = loginCaptchaCodeEnable;
	}
	
}