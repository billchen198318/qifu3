package org.qifu.core.ws.impl;

import javax.jws.WebService;

import org.apache.commons.lang3.StringUtils;
import org.qifu.core.ws.IHelloService;
import org.springframework.stereotype.Component;

@WebService(
		serviceName = "helloService", 
		targetNamespace = "http://webservice.example.qifu.org", 
		endpointInterface = "org.qifu.core.ws.IHelloService")
@Component
public class HelloServiceImpl implements IHelloService {

	@Override
	public String play(String test) {
		return StringUtils.defaultString(test) + "-" + System.currentTimeMillis();
	}
	
}
