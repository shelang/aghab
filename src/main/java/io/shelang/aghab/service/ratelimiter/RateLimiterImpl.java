package io.shelang.aghab.service.ratelimiter;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.keys.KeyCommands;
import io.quarkus.redis.datasource.string.StringCommands;
import io.shelang.aghab.exception.MaxLoginRetry;
import java.time.Duration;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class RateLimiterImpl implements RateLimiter {

  private final StringCommands<String, Long> countCommands;
  private final KeyCommands<String> keyCommands;
  @ConfigProperty(name = "app.login.attempt.block.duration", defaultValue = "2h")
  Duration loginAttemptBlockDuration;
  @ConfigProperty(name = "app.login.attempt.max.count", defaultValue = "6")
  int loginAttemptMaxCount;

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  public RateLimiterImpl(RedisDataSource ds) {
    this.countCommands = ds.string(Long.class);
    this.keyCommands = ds.key();
  }

  public void
  handleLoginAttempt(String username) {
    var key = "login:attempt:" + username;
    long incr = countCommands.incr(key);
    if (incr > loginAttemptMaxCount) {
      throw new MaxLoginRetry();
    }
    keyCommands.expire(key, loginAttemptBlockDuration.toSeconds());
  }
}
