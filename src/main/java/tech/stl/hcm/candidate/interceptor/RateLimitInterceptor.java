package tech.stl.hcm.candidate.interceptor;

import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private final Bucket bucket;

    public RateLimitInterceptor(Bucket bucket) {
        this.bucket = bucket;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        if (bucket.tryConsume(1)) {
            return true;
        }
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        return false;
    }
} 