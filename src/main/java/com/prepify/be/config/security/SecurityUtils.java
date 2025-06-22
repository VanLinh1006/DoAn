package com.prepify.be.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.util.Objects;


@Slf4j
public class SecurityUtils {
    public static final String HEADER_STRING = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    @SneakyThrows
    public static void processAuthentication(HttpServletRequest request,
                                             HttpServletResponse response,
                                             FilterChain filterChain,
                                             ServerSecurityService serverSecurityService) {
        try {
            String header = request.getHeader(HEADER_STRING);
            String authToken;
            if (!StringUtils.isBlank(header) && header.startsWith(TOKEN_PREFIX)) {
                authToken = header.replace(TOKEN_PREFIX, "");
            } else {
                authToken = header;
            }
            UserDetails userDetails = null;
            if (!StringUtils.isEmpty(authToken)) {
                authToken = authToken.trim();
                userDetails = serverSecurityService.get(authToken);
            }

            if (Objects.isNull(userDetails)) {
                String path = request.getServletPath();
                if (path.startsWith("/api/user") || path.startsWith("/api/admin")) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Unauthorized");
                    response.getWriter().flush();
                    return;
                }

                filterChain.doFilter(request, response);
                return;
            }

            if (request.getRequestURI().startsWith("/api/admin")) {
                Boolean isRoleAdmin = userDetails.getAuthorities().stream()
                        .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
                if (isRoleAdmin.equals(Boolean.FALSE)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Unauthorized");
                    response.getWriter().flush();
                    return;
                }
            }

            UsernamePasswordAuthenticationToken
                    authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null,
                    userDetails.getAuthorities()
            );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.info("Security utils exception " + e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized");
            response.getWriter().flush();
        }
    }
}
