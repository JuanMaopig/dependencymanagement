package com.sinux.utils;

import java.util.Date;
import java.util.UUID;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sinux.modules.entity.SysUser;

/**
 * 
* <p>Title: JwtUtil</p>  
* <p>Description: Jwt工具包，主要用于生成token，验证token</p>  
* @author yexj  
* @date 2019年5月30日
 */
public class JwtUtil {
    
	/**
	 * 
	 * <p>Title: createToken</p>  
	 * <p>Description: 生成jwt token</p>  
	 * @author yexj  
	 * @date 2019年5月30日  
	 * @param ttlMillis 过期时间
	 * @param user 登录用户
	 * @return 返回jwt token 生成token失败后返回null
	 */
    public static String createToken(long ttlMillis, SysUser user){
        try {
        	Algorithm algorithm = Algorithm.HMAC256(user.getSalt());
        	long now = System.currentTimeMillis();
            JWTCreator.Builder builder = JWT.create()
                    //设置私有声明
                    .withClaim("id", user.getId())
                    .withClaim("username", user.getUsername())
                    .withClaim("password", user.getPassword())
                    //设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                    .withJWTId(UUID.randomUUID().toString())
                    //设置JWT签发者
                    .withIssuer(user.getUsername())
                    //设置JWT所面向的用户
                    .withSubject("im-server")
                    //定义在什么时间之前，该jwt都是不可用的.
                    .withNotBefore(new Date(now))
                    //设置jwt的签发时间
                    .withIssuedAt(new Date(now))
                    //设置过期时间为200毫秒
                    .withExpiresAt(new Date(now+ttlMillis));
            return builder.sign(algorithm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 
     * <p>Title: VerificationToken</p>  
     * <p>Description: 验证jwt token的有效性</p>  
     * @author yexj  
     * @date 2019年5月30日  
     * @param token jet token
     * @param user 登录用户
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean verificationToken(String token, SysUser user) {
    	boolean flag = true;
    	try {
	    	Algorithm algorithm = Algorithm.HMAC256(user.getSalt());
	        JWTVerifier verifier = JWT.require(algorithm)
	            .withIssuer(user.getUsername())
	            .build();
	        verifier.verify(token);
	        flag = true;
    	}catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
    	return flag;
    }


    
    public static void main(String[] args) {
    	SysUser user = new SysUser();
    	user.setId(1L);
    	user.setPassword("123456");
    	user.setUsername("yxj");
    	user.setSalt("avdfdfd");
//    	String jwt = createToken(200,user);
//    	System.out.println(jwt);
    	String jwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJpbS1zZXJ2ZXIiLCJwYXNzd29yZCI6IjEyMzQ1NiIsIm5iZiI6MTU1OTE5NzIyMCwiaXNzIjoieXhqIiwiaWQiOjEsImV4cCI6MTU1OTE5NzIyMCwiaWF0IjoxNTU5MTk3MjIwLCJqdGkiOiI2ODJmYmRmMy03ZjU0LTRlOWMtODExZS05YWY5NzkyMTZkZTgiLCJ1c2VybmFtZSI6Inl4aiJ9.L1RsGHUH41A3FrSjV4jj9IklvFk-Q2vOPpJROn3jWuk";
    	boolean isSuccess = verificationToken(jwt,user);
    	if(isSuccess) {
    		DecodedJWT decode = JWT.decode(jwt);
    		System.out.println(decode.getClaims().get("password").asString());
    	}else {
    		System.out.println("验证失败");
    	}
    }
}
