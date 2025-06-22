package com.prepify.be.config.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public interface ServerSecurityService {
    UserDetails get(String token);
}

