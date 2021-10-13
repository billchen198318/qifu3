package org.qifu.core.config;

import java.util.Arrays;

import javax.xml.ws.Endpoint;

import org.apache.cxf.Bus;
import org.apache.cxf.ext.logging.LoggingInInterceptor;
import org.apache.cxf.ext.logging.LoggingOutInterceptor;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.qifu.core.ws.IHelloService;
import org.qifu.core.ws.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class CxfConfig {
	
	@Autowired
	private Bus bus;
	
	@Autowired
	private IHelloService helloService;
	
	@Autowired
	private ITestService testService;
	
	@Bean
	public LoggingInInterceptor loggingInInterceptor() {
		return new LoggingInInterceptor();
	}
	
	@Bean
	public LoggingOutInterceptor loggingOutInterceptor() {
		return new LoggingOutInterceptor();
	}	
	
	@Bean
	public Endpoint helloEndpoint() {
		EndpointImpl endpoint = new EndpointImpl(this.bus, this.helloService);
		endpoint.publish("/hello");
		endpoint.getInInterceptors().add( this.loggingInInterceptor() );
		endpoint.getOutInterceptors().add( this.loggingOutInterceptor() );
		return endpoint;
	}
	
	@Bean
	public Endpoint testEndpoint() {
		EndpointImpl endpoint = new EndpointImpl(this.bus, this.testService);
		endpoint.publish("/test");
		endpoint.getInInterceptors().add( this.loggingInInterceptor() );
		endpoint.getOutInterceptors().add( this.loggingOutInterceptor() );
		return endpoint;
	}	
	
	@Bean
	public ServletRegistrationBean<CXFServlet> cxfServlet() {
		ServletRegistrationBean<CXFServlet> srb = new ServletRegistrationBean<CXFServlet>();
		srb.setServlet(new CXFServlet());
		srb.setUrlMappings(Arrays.asList("/services/*"));
		return srb;
	}	
	
}
