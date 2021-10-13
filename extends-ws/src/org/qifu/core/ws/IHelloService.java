package org.qifu.core.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(targetNamespace = "http://webservice.example.qifu.org")
public interface IHelloService {
	
	@WebMethod
	String play(@WebParam(name = "test") String test);
	
}
