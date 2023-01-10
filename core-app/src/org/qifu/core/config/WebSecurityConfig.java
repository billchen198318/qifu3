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

import javax.sql.DataSource;

import org.qifu.base.CoreAppConstants;
import org.qifu.base.model.YesNo;
import org.qifu.base.properties.BaseInfoConfigProperties;
import org.qifu.base.service.impl.BaseUserDetailsService;
import org.qifu.core.support.BaseAuthenticationSuccessHandler;
import org.qifu.core.support.BaseLoginUrlAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    BaseUserDetailsService baseUserDetailsService;
    
    @Autowired
    BaseAuthenticationSuccessHandler baseAuthenticationSuccessHandler;
    
    @Autowired
    DataSource dataSource;
    
    @Autowired
    BaseInfoConfigProperties baseInfoConfigProperties;

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	//http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());
    	http.headers().frameOptions().sameOrigin();
    	http.cors().and().csrf().disable()
                .formLogin()
                .loginPage( CoreAppConstants.SYS_PAGE_LOGIN )
                .loginProcessingUrl("/login")             
                .permitAll()
                //.defaultSuccessUrl("/index", true)
                .successHandler(baseAuthenticationSuccessHandler)
                .and()
                .authorizeRequests()
                .antMatchers( CoreAppConstants.getWebConfiginterceptorExcludePathPatterns() )                
                .permitAll()
                .anyRequest()
                .authenticated();
    	
        // ------------------------------------------------------------
        // for rember-me use , 2023-01-07 add
        if (YesNo.YES.equals(this.baseInfoConfigProperties.getEnableAlwaysRememberMe())) {
        	http
            .rememberMe() 
            .key(this.getRememberMeKeyName()) 
            .alwaysRemember(true)
            .tokenRepository(persistentTokenRepository()) 
            .tokenValiditySeconds( getTokenValiditySeconds() ) 
            //.rememberMeCookieName( Constants.APP_SITE_CURRENTID_COOKIE_NAME )
            .userDetailsService(baseUserDetailsService)
            .authenticationSuccessHandler(baseAuthenticationSuccessHandler);
        }
        // ------------------------------------------------------------
            	
    	http.exceptionHandling().authenticationEntryPoint(new BaseLoginUrlAuthenticationEntryPoint( CoreAppConstants.SYS_PAGE_LOGIN ));
    	//http.sessionManagement().invalidSessionUrl( CoreAppConstants.SYS_PAGE_TAB_LOGIN_AGAIN );
    }
    
    // for rember-me use , 2023-01-07 add
    @Bean
    PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices() {
        PersistentTokenBasedRememberMeServices p =
                new PersistentTokenBasedRememberMeServices(
                        this.getRememberMeKeyName(),
                        baseUserDetailsService,
                        persistentTokenRepository()
                );
        //p.setCookieName( Constants.APP_SITE_CURRENTID_COOKIE_NAME );
        p.setTokenValiditySeconds( getTokenValiditySeconds() );
        return p;
    }    
    
    // for rember-me use , 2023-01-07 add
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl persistentTokenRepository = new JdbcTokenRepositoryImpl();
        persistentTokenRepository.setDataSource(dataSource);
        return persistentTokenRepository;
    }    
    
    // for rember-me use , 2023-01-07 add
    private int getTokenValiditySeconds() {
    	return 86400 * 7;
    }
    
    private String getRememberMeKeyName() {
    	return "uniqueAndSecret";
    }
    
    /*
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
//        configuration.setAllowedOrigins(Arrays.asList("*"));
//        configuration.setAllowedMethods(Arrays.asList("*"));
//        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    */
    
}
