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
package org.qifu.core.config;

import java.util.List;

import org.qifu.base.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
/**
 * http://127.0.0.1:8088/swagger-ui/
 * http://127.0.0.1:8088/swagger-ui/index.html
 */
@Configuration
@OpenAPIDefinition
public class SwaggerConfig {
	
	private final String securitySchemeName = "bearerAuth";
	
	@Bean
	public OpenAPI customOpenAPI() {
		Contact contact = new Contact().name("Bill Chen").email("chen.xin.nien@gmail.com");
		License license = new License().name("Apache 2.0").url("http://www.apache.org/licenses/LICENSE-2.0");
		Info info = new Info().title("QiFu3").description("backend interface.").version("0.3").contact(contact).license(license);
		Components component = new Components().addSecuritySchemes(
				securitySchemeName, 
				new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme(Constants.TOKEN_PREFIX).bearerFormat("JWT")
		);
		SecurityRequirement securityReq = new SecurityRequirement().addList(securitySchemeName);
		return new OpenAPI().components(component).security(List.of(securityReq)).info(info);
	}
	
}

