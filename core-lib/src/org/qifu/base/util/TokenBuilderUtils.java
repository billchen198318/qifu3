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
package org.qifu.base.util;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.Constants;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

public class TokenBuilderUtils {
	
	/*
	public static void main(String args[]) {
		String token = "";
		Map<String, Claim> claims = null;
		System.out.println( token = createToken("hello", "bill", "N", "", "00-11-22-33") );
		System.out.println( claims = verifyToken(token) );
		System.out.println( existsInfo(claims) );
	}
	*/
	
	public static boolean existsInfo(Map<String, Claim> claims) {
		if (null == claims) {
			return false;
		}
		return (claims.get(Constants.TOKEN_PROG_ID_NAME) != null && claims.get(Constants.TOKEN_USER_PARAM_NAME) != null);
	}
	
	public static Map<String, Claim> verifyToken(String token /*, TokenValidateFlagCheck flagCheck */ ) {
		if (StringUtils.isBlank(token)) {
			return null;
		}
		DecodedJWT jwt = null;
		try {
			JWTVerifier verifier = JWT.require(Algorithm.HMAC256(Constants.TOKEN_SECRET)).withIssuer(Constants.TOKEN_ISSUER).build();
			jwt = verifier.verify(token);
		} catch (SignatureVerificationException sve) {
			sve.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (jwt != null && jwt.getClaims() != null) ? jwt.getClaims() : null;
	}	
	
	public static String createToken(String programId, String userId, String subject, String clientId /*, TokenStoreBuilder store*/) {
		Date iatDate = new Date();
		
	    // expire time
	    Calendar setTime = Calendar.getInstance();
	    setTime.add(Calendar.MINUTE, Constants.TOKEN_EXPIRED_INTERVAL);
	    Date expiresDate = setTime.getTime();
	    
	    // Header Message
	    Map<String, Object> headerMap = new HashMap<String, Object>();
	    headerMap.put("alg", "HS256");
	    headerMap.put("typ", "JWT");
	    
	    //String audience = "QiFu3-app";
	    //String subject = "QiFu3";
	    String token = JWT.create()
	    		.withHeader(headerMap)
	    		.withIssuer(Constants.TOKEN_ISSUER)
	    		.withAudience(clientId)
	    		.withSubject(subject)
	    		.withIssuedAt(iatDate)
	    		.withExpiresAt(expiresDate)
	    		.withClaim(Constants.TOKEN_PROG_ID_NAME, programId)
	    		.withClaim(Constants.TOKEN_USER_PARAM_NAME, StringUtils.defaultString(userId))
	    		.sign(Algorithm.HMAC256(Constants.TOKEN_SECRET));
	    /*
	    if (null != store) {
	    	store.save(programId, userId, token, isAdmin, subject, audience, iatDate, expiresDate);
	    }
	    */
	    return token;
	}
	
}
