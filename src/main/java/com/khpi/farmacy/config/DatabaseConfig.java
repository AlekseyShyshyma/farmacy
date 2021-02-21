package com.khpi.farmacy.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(transactionManagerRef = "primaryDatabaseTransactionManager",
        entityManagerFactoryRef = "primaryEntityManagerFactory",
        basePackages = {"com.khpi.farmacy.repositories"})
public class DatabaseConfig {


    @Primary
    @Bean("primaryDatabaseProperties")
    @ConfigurationProperties("primary.db")
    public DataSourceProperties primaryDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean("primaryDataSource")
    public DataSource primaryDataSource(@Qualifier("primaryDatabaseProperties") DataSourceProperties dataSourceProperties){
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }


    @Bean("primaryDatabaseConfiguration")
    @ConfigurationProperties("primary.db.configuration")
    public Map<String, String> primaryDataSourceConfiguration() {
        return new HashMap<>();
    }


    @Primary
    @Bean(name = "primaryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory(
            EntityManagerFactoryBuilder builder, @Qualifier("primaryDataSource") DataSource dataSource,
            @Qualifier("primaryDatabaseConfiguration") Map<String, ?> properties) {

        return builder.dataSource(dataSource)
                .packages("com.khpi.farmacy.model")
                .persistenceUnit("PRIMARY_DATABASE")
                .properties(properties)
                .build();
    }

    @Primary
    @Bean(name = "primaryDatabaseTransactionManager")
    public PlatformTransactionManager primaryTransactionManager(@Qualifier("primaryEntityManagerFactory")
                                                                      EntityManagerFactory entityManagerFactory) {

        return new JpaTransactionManager(entityManagerFactory);
    }

}
