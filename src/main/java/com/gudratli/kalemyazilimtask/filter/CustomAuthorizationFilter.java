package com.gudratli.kalemyazilimtask.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class CustomAuthorizationFilter extends OncePerRequestFilter
{
    private static final Set<String> SERVLET_PATHS = Collections.unmodifiableSet(new HashSet<>(
            Arrays.asList(
                    "/user/login",
                    "/user/sign-up",
                    "/user/token/refresh",
                    "/swagger-ui/**",
                    "/v2/api-docs/**",
                    "/swagger-resources/**")));

    @Override
    protected void doFilterInternal (HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException
    {
        if (SERVLET_PATHS.contains(request.getServletPath()))
            filterChain.doFilter(request, response);
        else
        {
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            String prefix = "Bearer ";
            if (authorizationHeader != null && authorizationHeader.startsWith(prefix))
            {
                try
                {
                    String token = authorizationHeader.substring(prefix.length());
                    Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                    JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = jwtVerifier.verify(token);
                    String username = decodedJWT.getSubject();
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority(decodedJWT.getClaim("roles").asString()));
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                } catch (Exception ex)
                {
                    ex.printStackTrace();
                    response.setHeader("error", ex.getMessage());
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    Map<String, String> error = new HashMap<>();
                    error.put("error_message", ex.getMessage());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                }
            } else
                filterChain.doFilter(request, response);
        }
    }
}
