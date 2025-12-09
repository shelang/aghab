package io.shelang.aghab.service.ratelimiter;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.keys.KeyCommands;
import io.quarkus.redis.datasource.value.ValueCommands;
import io.shelang.aghab.exception.MaxLoginRetry;
import java.time.Duration;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class RateLimiterImpl implements RateLimiter {

  private final ValueCommands<String, Long> countCommands;
  private final KeyCommands<String> keyCommands;
  @ConfigProperty(name = "app.login.attempt.block.duration", defaultValue = "2h")
  Duration loginAttemptBlockDuration;
  @ConfigProperty(name = "app.login.attempt.max.count", defaultValue = "6")
  int loginAttemptMaxCount;

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  public RateLimiterImpl(RedisDataSource ds) {
    this.countCommands = ds.value(Long.class);
    this.keyCommands = ds.key();
  }

  private String getKey(String username) {
    return "login:attempt:" + username;
  }

  public void handleLoginAttempt(String username) {
    String key = getKey(username);
    long incr = countCommands.incr(key);
    if (incr > loginAttemptMaxCount) {
      throw new MaxLoginRetry();
    }
    keyCommands.expire(key, loginAttemptBlockDuration.toSeconds());
  }

  public void removeAttemptLog(String username) {
    keyCommands.del(getKey(username));
  }
}
