package app.seven.flexisafses.config.security;

import app.seven.flexisafses.models.pojo.AppUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Configuration
@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtHelper jwtHelper;
    AuthenticationManager authenticationManager;
    public CustomAuthenticationFilter(@Autowired AuthenticationManager authenticationManager, @Autowired JwtHelper jwtHelper) {
        super(authenticationManager);
        this.jwtHelper = jwtHelper;
        this.authenticationManager = authenticationManager;

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");


        log.debug("Authentication : " +  request.getParameterMap());
        log.debug("Authentication : " + username);
        log.debug("Authentication  : " + password);

        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);

        log.debug("Authentication failed " + failed.getMessage());
    }

    @SneakyThrows
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {


        log.debug("Successful Authentication");
        log.debug(
                authResult.toString());
        log.debug("Successful Authentication 1");
//
//        if(!authResult.isAuthenticated()){
//            throw new BadRequestException("Invalid username or password");
//        }
        AppUser user = (AppUser) authResult.getPrincipal();



        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", jwtHelper.createAccessToken(user, request.getRequestURL().toString()));
        tokens.put("refresh_token", jwtHelper.createRefreshToken(user, request.getRequestURL().toString()));

        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }
}
