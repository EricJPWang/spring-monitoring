package de.lulunpengpeng.storage.person;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

@Configuration
@Slf4j
@Profile("!test")
public class FlywayConfig {

    private final String datasourceURL;
    private final String datasourceUser;
    private final String datasourcePwd;

    public FlywayConfig(final Environment env,
                        @Value("${spring.flyway.url}") String datasourceURL,
                        @Value("${spring.flyway.user}") String datasourceUser,
                        @Value("${spring.flyway.password}") String datasourcePwd) {
        this.datasourceURL = datasourceURL;
        this.datasourceUser = datasourceUser;
        this.datasourcePwd = datasourcePwd;
    }

    @Bean(initMethod = "migrate")
    public Flyway flyway() {
        return new Flyway(
                Flyway.configure()
                        .baselineOnMigrate(false)
                        .dataSource(datasourceURL, datasourceUser, datasourcePwd));
    }
}
