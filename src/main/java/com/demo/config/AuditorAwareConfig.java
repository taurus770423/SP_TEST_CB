package com.demo.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * entity listener
 */
@Component("auditorAware")
public class AuditorAwareConfig implements AuditorAware<String> {
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Override
    @NonNull
    public Optional<String> getCurrentAuditor() {
        String creator = (String) httpServletRequest.getSession().getAttribute("sign.creator");
        if(StringUtils.hasText(creator)) return Optional.of(creator);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()) {
            return Optional.of("system");
        }

        return Optional.empty();
    }

}