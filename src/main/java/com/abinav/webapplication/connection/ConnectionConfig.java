package com.abinav.webapplication.connection;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableConfigurationProperties(DataSourceProperties.class)
public class ConnectionConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.hikari")
    public HikariDataSource dataSource(DataSourceProperties properties) {
        HikariDataSource dataSource = properties
            .initializeDataSourceBuilder()
            .type(HikariDataSource.class)
            .build();
        return dataSource;
    }
}

