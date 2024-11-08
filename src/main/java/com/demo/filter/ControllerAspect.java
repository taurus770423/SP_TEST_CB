package com.demo.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Aspect
public class ControllerAspect {
    private final HttpServletRequest httpServletRequest;

    private final HttpServletResponse httpServletResponse;

    @Before("execution(* com.demo.controller.*.*(..))")
    public void log(JoinPoint joinPoint) {
        String method = httpServletRequest.getMethod();
        String path = httpServletRequest.getServletPath();

        Object[] args = joinPoint.getArgs();
        StringBuilder params = new StringBuilder();
        for (Object arg : args) {
            if (arg != null) {
                params.append(arg.getClass().getSimpleName()).append(": ").append(arg.toString()).append(", ");
            } else {
                params.append("null, ");
            }
        }

        if (!params.isEmpty()) {
            params.setLength(params.length() - 2);
        }

        log.info("[{}] {} {}", method, path, params.toString());
    }
}
