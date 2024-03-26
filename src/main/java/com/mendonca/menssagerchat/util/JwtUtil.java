package com.mendonca.menssagerchat.util;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import com.mendonca.menssagerchat.model.UserMessenger;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;


@Service
public class JwtUtil {

	String SECRET_KEY = "cd+Pr1js+w2qfT2BoCD+tPcYp9LbjpmhSMEJqUob1mcxZ7+Wmik4AYdjX+DlDjmE4yporzQ9tm7v3z/j+QbdYg==";
	
	
	public String generateToken(UserMessenger userMessenger)   {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims,userMessenger.getUserName());
	}
	
	private String createToken(Map<String, Object> claims,String subject )   {
		
		return Jwts
				.builder()
				.setClaims(claims)
				.setSubject(subject)
		        .setIssuedAt(new Date(System.currentTimeMillis()))
		        .setExpiration(new Date(System.currentTimeMillis()+ 1000*60*60*10 ))
		        .signWith(SignatureAlgorithm.HS512,TextCodec.BASE64.encode(SECRET_KEY.getBytes())).compact();
	}
	
	public Boolean validateToken(String token,UserMessenger userMessenger) {
		final String username = extractUserName(token);
		return (userMessenger.getUserName().equals(username) && !isTokenExpired(token));
	}
	
	public String extractUserName(String token) {
		return extractClaim(token,Claims::getSubject);
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims) ;
	}

	private Claims extractAllClaims(String token) {	
	   return Jwts.parser().setSigningKey(TextCodec.BASE64.encode(SECRET_KEY.getBytes())).parseClaimsJws(token).getBody();
	}
	
	public Date extractExpiration(String token) {
		return extractClaim(token, claim -> claim.getExpiration());
	}
	
	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	
}
