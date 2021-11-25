package app.seven.flexisafses.config.security;

import app.seven.flexisafses.models.exception.BadRequestException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Log4j2
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    private final JwtHelper jwtHelper;

    public CustomAuthorizationFilter(JwtHelper jwtHelper) {
        super();
        this.jwtHelper = jwtHelper;
    }

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.debug("URL => " + request.getServletPath());
        if(request.getServletPath().equals("/login") || request.getMethod().equals("OPTIONS")){
            filterChain.doFilter(request, response);
        }else{
              String authorization = request.getHeader(AUTHORIZATION);
              if(authorization != null && authorization.startsWith(getBearer_())){
                  try{
                      String token = authorization.substring(getBearer_().length());
                      if(jwtHelper.verifyToken(token)){
                          jwtHelper.getUsername(token);

                          Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                          stream(jwtHelper.getRoles(token)).forEach(
                                  role ->  authorities.add(new SimpleGrantedAuthority(role))
                          );
                          UsernamePasswordAuthenticationToken authenticationToken =
                                  new UsernamePasswordAuthenticationToken(jwtHelper.getUsername(token), null, authorities );

                          SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                          filterChain.doFilter(request, response);
                      }
                  }catch (Exception exception){
                      response.setHeader("error", exception.getMessage());

                      Map<String, String> error = new HashMap<>();
                      error.put("message", exception.getMessage());

                      response.setContentType(APPLICATION_JSON_VALUE);
                      new ObjectMapper().writeValue(response.getOutputStream(), error);

                  }
              }else{
                  throw new BadRequestException("Invalid or expired token");
              }
        }
    }

    private String getBearer_() {
        return "Bearer ";
    }
}
