package org.qifu.base.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:appConfig.properties")
@ConfigurationProperties(prefix = "mock")
public class MockEnableConfigProperties {
	
	private String loginEnable;

	public String getLoginEnable() {
		return loginEnable;
	}

	public void setLoginEnable(String loginEnable) {
		this.loginEnable = loginEnable;
	}
	
}
