package io.shelang.aghab.config;

import io.quarkus.flyway.FlywayConfigurationCustomizer;
import jakarta.inject.Singleton;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.flywaydb.core.internal.database.postgresql.PostgreSQLConfigurationExtension;

@Singleton
public class UsersCustomizer implements FlywayConfigurationCustomizer {

    @Override
    public void customize(FluentConfiguration configuration) {
        PostgreSQLConfigurationExtension configurationExtension = configuration
                .getPluginRegister()
                .getPlugin(PostgreSQLConfigurationExtension.class);
        configurationExtension.setTransactionalLock(false);
    }
}
