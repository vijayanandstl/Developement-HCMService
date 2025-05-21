package tech.stl.hcm.candidate.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);
        MDC.put("requestURI", request.getRequestURI());
        MDC.put("requestMethod", request.getMethod());
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, 
            @NonNull Object handler, Exception ex) {
        MDC.clear();
    }
} 