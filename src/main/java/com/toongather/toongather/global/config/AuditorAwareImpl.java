package com.toongather.toongather.global.config;

import com.toongather.toongather.domain.member.domain.Member;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || authentication.isAuthenticated()){
            return Optional.empty();
        }

        return Optional.of(((Member)authentication.getPrincipal()).getName());
    }
}
