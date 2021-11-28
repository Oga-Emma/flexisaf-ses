package app.seven.flexisafses.config.security;

import app.seven.flexisafses.models.exception.BadRequestException;
import app.seven.flexisafses.util.response.ErrorResponse;
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

        if(request.getServletPath().equals("/login") || request.getMethod().equals("OPTIONS")){
            filterChain.doFilter(request, response);
        }else{
              String authorization = request.getHeader(AUTHORIZATION);
              if(authorization != null && authorization.startsWith(getBearer_())){
                  try{
                      String token = authorization.substring(getBearer_().length());
                      if(jwtHelper.verifyToken(token)){
                          String username = jwtHelper.getUsername(token);

                          Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                          stream(jwtHelper.getRoles(token)).forEach(
                                  role ->  authorities.add(new SimpleGrantedAuthority(role))
                          );

                          UsernamePasswordAuthenticationToken authenticationToken =
                                  new UsernamePasswordAuthenticationToken(username, null, authorities );

                          SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                          filterChain.doFilter(request, response);
                      }else{
                          throw new BadRequestException("Invalid or expired token");
                      }
                  }catch (Exception exception){

                      response.setContentType("application/json");
                      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

                      response.getWriter().write(new ObjectMapper().writeValueAsString(new ErrorResponse("Invalid or expired token")));
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
