package io.shelang.aghab.service.shorty.impl;

import io.shelang.aghab.service.shorty.Shorty;
import org.hashids.Hashids;

import javax.enterprise.context.ApplicationScoped;
import java.time.Instant;

@ApplicationScoped
public class HashIdShortyImpl implements Shorty {
    @Override
    public String generate() {
        var key = String.valueOf(Math.random() * Long.MAX_VALUE);
        var hashids = new Hashids(key);
        return hashids.encode(Instant.now().getEpochSecond());
    }
}
