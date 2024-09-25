package com.swyp6.familytravel.auth.filters;

import com.swyp6.familytravel.auth.jwt.JwtTokenUtils;
import com.swyp6.familytravel.auth.service.JpaUserDetailManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenUtils jwtTokenUtils;
    private final JpaUserDetailManager manager;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(authHeader != null && authHeader.startsWith("Bearer "))
        {
            String token = authHeader.split(" ")[1];

            if(jwtTokenUtils.validate(token))
            {
                SecurityContext context = SecurityContextHolder.createEmptyContext();

                String username= jwtTokenUtils.parseClaims(token).getSubject();

                UserDetails userDetails = manager.loadUserByUsername(username);
                for(GrantedAuthority authority : userDetails.getAuthorities()){
                    log.info(authority.getAuthority());
                }

                AbstractAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
//                                CustomUserDetails.builder()
//                                        .userane(username)
//                                        .build(),
                                userDetails,
                                token, userDetails.getAuthorities()
                        );

                context.setAuthentication(authenticationToken);
                SecurityContextHolder.setContext(context);

            }
            else
            {
                log.warn("jwt validation failed");
            }
        }

        filterChain.doFilter(request,response);
    }
}
