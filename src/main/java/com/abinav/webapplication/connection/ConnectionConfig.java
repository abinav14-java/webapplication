package com.abinav.webapplication.connection;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class ConnectionConfig {

	private static final Logger logger = LoggerFactory.getLogger(ConnectionConfig.class);

	@Value("${spring.datasource.url}")
	private String dbUrl;

	@Value("${spring.datasource.username}")
	private String dbUsername;

	@Value("${spring.datasource.password}")
	private String dbPassword;

	@Bean
	public DataSource dataSource() {
		HikariDataSource dataSource = null;
		try {
			dataSource = new HikariDataSource();
			dataSource.setJdbcUrl(dbUrl);
			dataSource.setUsername(dbUsername);
			dataSource.setPassword(dbPassword);

			dataSource.setMaximumPoolSize(10);
			dataSource.setMinimumIdle(5);
			dataSource.setIdleTimeout(30000);
			dataSource.setMaxLifetime(1800000);

			return dataSource;
		} catch (Exception e) {
			logger.error("Error creating DataSource bean: ", e);
			return null;
		}
	}

}
