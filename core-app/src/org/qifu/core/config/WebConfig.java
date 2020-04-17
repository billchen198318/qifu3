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

import org.qifu.base.CoreAppConstants;
import org.qifu.base.interceptor.MDCInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
	
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
        .addResourceHandler( CoreAppConstants.WebConfig_resource )
        .addResourceLocations( CoreAppConstants.WebConfig_resourceLocations );
    }
    
    @Bean
    MDCInterceptor MDCInterceptor() {
    	return new MDCInterceptor();
    }
    
    /*
    @Bean
    UserBuilderInterceptor UserBuilderInterceptor() {
    	return new UserBuilderInterceptor();
    }
    */
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(MDCInterceptor())
        	.addPathPatterns("/*", "/**")
        	.excludePathPatterns( CoreAppConstants.WebConfig_interceptorExcludePathPatterns );
        
        /*
        registry.addInterceptor(UserBuilderInterceptor())
        	.addPathPatterns("/*", "/**")
        	.excludePathPatterns( Constants.getWebConfiginterceptorExcludePathPatterns() );
        */  
    }
    
}
