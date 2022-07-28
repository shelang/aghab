package io.shelang.aghab.service.ratelimiter;

public interface RateLimiter {
  void handleLoginAttempt(String username);
  void removeAttemptLog(String username);
}
