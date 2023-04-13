package com.movie.booker.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RequestFilter extends OncePerRequestFilter {

    private final TokenUtil tokenUtil;
    private final JwtUserDetailsService jwtUserDetailsService;

    @Value("${jwt.http.request.header}")
    private String tokenHeader;

    String username = null;
    String jwtToken = null;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
                final String requestTokenHeader = request.getHeader(this.tokenHeader);
                if(requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
                    jwtToken = requestTokenHeader.substring(7);
                    try {
                        username = tokenUtil.getUserNameFromToken(jwtToken);
                    } catch (ExpiredJwtException e) {
                        System.out.println("Token expired!");
                    } catch (Exception e) {
                        e.printStackTrace(System.out);
                    }
                } else {
                    System.out.println("Invalid token!");
                }

                if (null != username &&SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
                    if (tokenUtil.validateToken(jwtToken, userDetails)) {
                       UsernamePasswordAuthenticationToken
                       authenticationToken = new UsernamePasswordAuthenticationToken(
                       userDetails, null,
                       userDetails.getAuthorities());
                       authenticationToken.setDetails(new
                       WebAuthenticationDetailsSource().buildDetails(request));
                       SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                 }
                
                filterChain.doFilter(request, response);

    }
    
}
