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

import java.util.Arrays;
import java.util.List;

import org.qifu.base.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.annotations.Api;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * http://127.0.0.1:8088/swagger-ui/
 * http://127.0.0.1:8088/swagger-ui/index.html
 */
@Configuration
@EnableOpenApi // for swagger3
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
        		.apiInfo(apiInfo())       		
                .select()
                //.apis(RequestHandlerSelectors.basePackage("org.qifu.core.api"))
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build()
        		.securityContexts(Arrays.asList(securityContext()))
        		.securitySchemes(Arrays.asList(apiKey()));
    }
    
    private ApiKey apiKey() {
    	return new ApiKey(Constants.TOKEN_PREFIX, Constants.TOKEN_Authorization, "header");
    }    
    
    private SecurityContext securityContext() {
    	return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }
    
    private List<SecurityReference> defaultAuth() {
    	AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
    	AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
    	authorizationScopes[0] = authorizationScope;
    	return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
    }
    
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("qifu3")
                .description("qifu WEB API.")
                .termsOfServiceUrl("http://api.qifu.org/")
                .contact(new Contact("Bill", "https://github.com/billchen198318/qifu3", "chen.xin.nien@gmail.com"))
                .version("1.0")
                .build();
    }
	
}
