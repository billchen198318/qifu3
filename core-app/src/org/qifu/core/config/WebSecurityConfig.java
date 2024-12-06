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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebMvc
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    BaseUserDetailsService baseUserDetailsService;
    
    @Autowired
    BaseAuthenticationSuccessHandler baseAuthenticationSuccessHandler;
    
    @Autowired
    DataSource dataSource;
    
    @Autowired
    BaseInfoConfigProperties baseInfoConfigProperties;

    @Autowired
    PasswordEncoder passwordEncoder;
    
    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    	return authenticationConfiguration.getAuthenticationManager();
    }    
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        
        authProvider.setUserDetailsService(this.baseUserDetailsService);
        authProvider.setPasswordEncoder(this.passwordEncoder);
        
        return authProvider;
    }        
    
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {   
        // 配置 HTTP 安全头
        http.headers().frameOptions().sameOrigin();
        
        // 配置 CORS 和 CSRF
        http.cors(Customizer.withDefaults()).csrf(csrf -> csrf.disable());
        
        // 配置请求授权
        http.authorizeHttpRequests(auth -> {
            for (String par : CoreAppConstants.getWebConfiginterceptorExcludePathPatterns()) {
                auth.requestMatchers(antMatcher(par)).permitAll();
            }
            auth.anyRequest().authenticated(); // 其他请求需认证
        });
        
        // 配置表单登录
        http.formLogin(form -> form
            .loginPage(CoreAppConstants.SYS_PAGE_LOGIN) // 设置登录页面
            .loginProcessingUrl("/login") // 登录请求 URL
            .successHandler(baseAuthenticationSuccessHandler) // 设置成功处理器
            .successForwardUrl("/index") // 登录成功后跳转 URL
            .permitAll() // 允许所有人访问登录页面
        );
        
        // 配置注销
        http.logout(logout -> logout.permitAll());
        
        // 配置 remember-me
        if (YesNo.YES.equals(this.baseInfoConfigProperties.getEnableAlwaysRememberMe())) {
            http.rememberMe()
                .key(this.getRememberMeKeyName())
                .alwaysRemember(true)
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(getTokenValiditySeconds())
                .authenticationSuccessHandler(baseAuthenticationSuccessHandler); // 配置成功处理器
        }
        
        return http.build();
    }    
    
//    @Bean
//    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {  
//    	//http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());
//    	http.headers().frameOptions().sameOrigin();
//    	http.cors().and().csrf().disable()
//                .formLogin()
//                .loginPage( CoreAppConstants.SYS_PAGE_LOGIN )
//                .loginProcessingUrl("/login")             
//                .permitAll()
//                //.defaultSuccessUrl("/index", true)
//                .successHandler(baseAuthenticationSuccessHandler)
//                .and()
//                .authorizeRequests()
//                .antMatchers( CoreAppConstants.getWebConfiginterceptorExcludePathPatterns() )                
//                .permitAll()
//                .anyRequest()
//                .authenticated();
//    	
//        // ------------------------------------------------------------
//        // for rember-me use , 2023-01-07 add
//        if (YesNo.YES.equals(this.baseInfoConfigProperties.getEnableAlwaysRememberMe())) {
//        	http
//            .rememberMe() 
//            .key(this.getRememberMeKeyName()) 
//            .alwaysRemember(true)
//            .tokenRepository(persistentTokenRepository()) 
//            .tokenValiditySeconds( getTokenValiditySeconds() ) 
//            //.rememberMeCookieName( Constants.APP_SITE_CURRENTID_COOKIE_NAME )
//            .userDetailsService(baseUserDetailsService)
//            .authenticationSuccessHandler(baseAuthenticationSuccessHandler);
//        }
//        // ------------------------------------------------------------
//            	
//    	http.exceptionHandling().authenticationEntryPoint(new BaseLoginUrlAuthenticationEntryPoint( CoreAppConstants.SYS_PAGE_LOGIN ));
//    	//http.sessionManagement().invalidSessionUrl( CoreAppConstants.SYS_PAGE_TAB_LOGIN_AGAIN );
//    }
    
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
    
}
