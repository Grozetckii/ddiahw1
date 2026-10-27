package ru.grozetsky.ddiahw1.config;

import io.micrometer.core.instrument.config.MeterFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    public MeterFilter excludeHealthAndMetrics() {
        return MeterFilter.deny(id -> {
            if (!"http.server.requests".equals(id.getName())) {
                return false;
            }
            String uri = id.getTag("uri");
            return "/health".equals(uri) || "/metrics".equals(uri);
        });
    }
}
