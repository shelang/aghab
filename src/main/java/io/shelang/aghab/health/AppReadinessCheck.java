package io.shelang.aghab.health;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;

import io.quarkus.redis.client.RedisClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;

@Readiness
@ApplicationScoped
@SuppressWarnings("deprecation")
public class AppReadinessCheck implements HealthCheck {

    @Inject
    DataSource dataSource;

    @Inject
    RedisClient redisClient;

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("Aghab Application Readiness");

        boolean databaseUp = checkDatabase();
        boolean redisUp = checkRedis();

        responseBuilder.withData("database", databaseUp ? "UP" : "DOWN")
                .withData("redis", redisUp ? "UP" : "DOWN");

        if (databaseUp && redisUp) {
            return responseBuilder.up().build();
        } else {
            return responseBuilder.down().build();
        }
    }

    private boolean checkDatabase() {
        try (Connection connection = dataSource.getConnection()) {
            return connection.isValid(3);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean checkRedis() {
        try {
            return redisClient.ping(java.util.Collections.emptyList()).toString().equalsIgnoreCase("PONG");
        } catch (Exception e) {
            return false;
        }
    }
}
