package app.seven.flexisafses.config.security;

import app.seven.flexisafses.models.exception.BadRequestException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;


@Slf4j
@Component
public class JwtHelper {

    final Algorithm algorithm;

    @Autowired
    public JwtHelper( @Value("${jwt-secret}") String jwtSecret){
       algorithm = Algorithm.HMAC256(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }


   public String createAccessToken(UserDetails user, String issuer){

        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .withIssuer(issuer)
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toUnmodifiableList()))
                .sign(algorithm);

//        String refresh_token = JWT.create()
//                .withSubject(user.getUsername())
//                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
//                .withIssuer(request.getRequestURL().toString())
//                .sign(algorithm);

    }

   public String createRefreshToken(UserDetails user, String issuer) throws IOException, ServletException {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .withIssuer(issuer)
                .sign(algorithm);

    }

   public boolean verifyToken(String token) {
        try{
            JWTVerifier build = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = build.verify(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

   public String getUsername(String token) {
        JWTVerifier build = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = build.verify(token);
        return decodedJWT.getSubject();
    }

   public String[] getRoles(String token) {
        JWTVerifier build = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = build.verify(token);
        return  decodedJWT.getClaim("roles").asArray(String.class);
    }
}
