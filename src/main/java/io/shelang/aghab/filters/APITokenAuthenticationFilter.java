package io.shelang.aghab.filters;

import io.shelang.aghab.repository.UserRepository;
import io.shelang.aghab.role.Roles;
import io.smallrye.jwt.auth.principal.DefaultJWTCallerPrincipal;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.time.Instant;

@Provider
public class APITokenAuthenticationFilter implements ContainerRequestFilter {

  @Inject UserRepository userRepository;

  @Override
  public void filter(ContainerRequestContext ctx) throws IOException {
    final SecurityContext securityContext = ctx.getSecurityContext();
    if (securityContext != null) {
      DefaultJWTCallerPrincipal userPrincipal =
          (DefaultJWTCallerPrincipal) securityContext.getUserPrincipal();
      if (userPrincipal == null || !securityContext.isUserInRole(Roles.API)) return;

      userRepository
          .findByUsername(userPrincipal.getName())
          .ifPresent(
              user -> {
                boolean before =
                    user.getTokenIssueAt()
                        .isBefore(Instant.ofEpochSecond(userPrincipal.getIssuedAtTime()));
                if (!before) {
                  ctx.abortWith(Response.status(403).build());
                }
              });
    }
  }
}
