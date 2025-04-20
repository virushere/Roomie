package com.roomatefinder.demo.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration(exclude = {
        HibernateJpaAutoConfiguration.class
})
public class JpaConfig {
    // This class is intentionally empty
    // Its purpose is to disable JPA auto-configuration
}