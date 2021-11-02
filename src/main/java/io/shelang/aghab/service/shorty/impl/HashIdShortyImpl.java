package io.shelang.aghab.service.shorty.impl;

import io.shelang.aghab.exception.InvalidLengthOfHash;
import io.shelang.aghab.service.shorty.Shorty;
import org.hashids.Hashids;

import javax.enterprise.context.ApplicationScoped;
import java.time.Instant;

@ApplicationScoped
public class HashIdShortyImpl implements Shorty {
  @Override
  public String generate(int length) throws InvalidLengthOfHash {
    if (length < 1) throw new InvalidLengthOfHash();

    StringBuilder gen = new StringBuilder(randomString());

    while (gen.length() < length) {
      gen.append(randomString());
    }

    if (gen.length() > length) return gen.substring(0, length);

    return gen.toString();
  }

  private String randomString() {
    var key = String.valueOf(Math.random() * Long.MAX_VALUE);
    var hashids = new Hashids(key);
    return hashids.encode(Instant.now().getEpochSecond());
  }
}
