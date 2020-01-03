package com.chundengtai.base.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JavaWebToken {

    //该方法使用HS256算法和Secret:bankgl生成signKey
    private static Key getKeyInstance() {
        //We will sign our JavaWebToken with our ApiKey secret
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(JwtConstant.JWT_SECERT);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        return signingKey;
    }

    //使用HS256签名算法和生成的signingKey最终的Token,claims中是有效载荷
    public static String createJavaWebToken(Map<String, Object> claims) {
        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256, getKeyInstance()).compact();
    }

    //解析Token，同时也能验证Token，当验证失败返回null
    public static Map<String, Object> parserJavaWebToken(String jwt) {
        try {
            Map<String, Object> jwtClaims =
                    Jwts.parser().setSigningKey(getKeyInstance()).parseClaimsJws(jwt).getBody();
            return jwtClaims;
        } catch (Exception e) {
            log.error("json web token verify failed");
            return null;
        }
    }

    public static void main(String[] args) {
        Map token1 = JavaWebToken.parserJavaWebToken("");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> claims = new HashMap<>(7);
        claims.put("created_time", System.currentTimeMillis());
        System.out.println(claims);
        String token = JavaWebToken.createJavaWebToken(claims);
        String str = "eyJhbGciOiJIUzI1NiJ9.eyJtZWNoYW50SWQiOi0xLCJsZXZlbCI6MSwib3JkZXJJZCI6NTcxLCJvcmRlclNuIjoiNDE4ODU1MTkxOTIxNzMzNjMyIiwiY29tcGxldGVUaW1lIjpudWxsLCJjb25maXJtVGltZSI6bnVsbCwicmVtYXJrIjoi5LiA57qn6L-U5L2j57uT566XIiwidG9rZW4iOm51bGwsImdvbGRVc2VySWQiOjEyNSwiYnV5VXNlcklkIjoxMzUsIm1vbmV5IjowLjk5MTAsImdvb2RzUHJpY2UiOjkuOTEsIm5pY2tuYW1lIjoiNlllUjVweW8iLCJjcmVhdGVkVGltZSI6MTU3Nzc5MjQ3MDU2NSwiaWQiOm51bGwsInN0YXR1cyI6MjAxfQ.HEMy8UsML4O4fyUZtBDoTlsUQQmDq0pCW6TjPDxPBLI";

        String s = "eyJhbGciOiJIUzI1NiJ9.eyJtZWNoYW50SWQiOi0xLCJsZXZlbCI6MSwib3JkZXJJZCI6NTc1LCJvcmRlclNuIjoiNDE4ODc4MjI0NzUzNDA1OTUyIiwiY29tcGxldGVUaW1lIjpudWxsLCJjb25maXJtVGltZSI6bnVsbCwicmVtYXJrIjoi5LiA57qn6L-U5L2j57uT566XIiwidG9rZW4iOm51bGwsImdvbGRVc2VySWQiOjEyNSwiYnV5VXNlcklkIjoxMzUsIm1vbmV5IjowLjk5MTAsImdvb2RzUHJpY2UiOjkuOTEsIm5pY2tuYW1lIjoiNlllUjVweW8iLCJjcmVhdGVkVGltZSI6MTU3Nzc5Nzk2NDE5MSwiaWQiOm51bGwsInN0YXR1cyI6MjAxfQ.HXKqvTE75CtRK47--vl2_F9Z2tbN9RmqypE-259yr4k";
        String ss = "eyJhbGciOiJIUzI1NiJ9.eyJtZWNoYW50SWQiOi0xLCJsZXZlbCI6MSwib3JkZXJJZCI6NTc2LCJvcmRlclNuIjoiNDE5NDM3NTUwMTEwOTQ1MjgwIiwiY29tcGxldGVUaW1lIjpudWxsLCJjb25maXJtVGltZSI6bnVsbCwicmVtYXJrIjoi5LiA57qn6L-U5L2j57uT566XIiwidG9rZW4iOm51bGwsImdvbGRVc2VySWQiOjEyNSwiYnV5VXNlcklkIjoxMzUsIm1vbmV5IjowLjAwMjAsImdvb2RzUHJpY2UiOjAuMDIsIm5pY2tuYW1lIjoiNlllUjVweW8iLCJjcmVhdGVkVGltZSI6MTU3NzkzMTMzMDYzNiwiaWQiOm51bGwsInN0YXR1cyI6MjAxfQ.dqICs7wGnxuvBSFfxuc0sKxcCjfxgydCAaiRVii9VJw";
        System.out.println(token);
        System.out.println(JavaWebToken.parserJavaWebToken(token));
    }
}