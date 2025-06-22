package com.prepify.be.filter;

import com.prepify.be.config.security.SecurityUtils;
import com.prepify.be.config.security.ServerSecurityService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final ServerSecurityService serverSecurityService;

    public JwtAuthenticationFilter(@Qualifier("SecurityServiceImpl") ServerSecurityService serverSecurityService) {
        this.serverSecurityService = serverSecurityService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) {
        SecurityUtils.processAuthentication(request, response, filterChain, serverSecurityService);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return !path.startsWith("/api/user")
//                && !path.startsWith("/api/auth")
                && !path.startsWith("/api/admin")
                && !path.startsWith("/api/test")
//                && !path.startsWith("/api/lesson/detail")
                && !path.startsWith("/api/lesson");
    }
}
