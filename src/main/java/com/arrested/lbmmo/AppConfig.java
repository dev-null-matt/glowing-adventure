package com.arrested.lbmmo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.ejb.HibernatePersistence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import com.jolbox.bonecp.BoneCPDataSource;

@Configuration
@EnableJpaRepositories("com.arrested.lbmmo.persistence.repository")
public class AppConfig {
	
    private static final String PROPERTY_NAME_HIBERNATE_DIALECT = "hibernate.dialect";
    private static final String PROPERTY_NAME_HIBERNATE_FORMAT_SQL = "hibernate.format_sql";
    private static final String PROPERTY_NAME_HIBERNATE_NAMING_STRATEGY = "hibernate.ejb.naming_strategy";
    private static final String PROPERTY_NAME_HIBERNATE_SHOW_SQL = "hibernate.show_sql";
    
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws ClassNotFoundException {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();

        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setPackagesToScan("com.arrested.lbmmo.persistence.entity");
        entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistence.class);

        Properties jpaProterties = new Properties();
        jpaProterties.put(PROPERTY_NAME_HIBERNATE_DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
        jpaProterties.put(PROPERTY_NAME_HIBERNATE_FORMAT_SQL, "true");
        jpaProterties.put(PROPERTY_NAME_HIBERNATE_NAMING_STRATEGY, "org.hibernate.cfg.ImprovedNamingStrategy");
        jpaProterties.put(PROPERTY_NAME_HIBERNATE_SHOW_SQL, "false");

        entityManagerFactoryBean.setJpaProperties(jpaProterties);

        return entityManagerFactoryBean;
    }
	
    @Bean
    public JpaTransactionManager transactionManager() throws ClassNotFoundException {
        JpaTransactionManager transactionManager = new JpaTransactionManager();

        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());

        return transactionManager;
    }
    
    @Bean
    public DataSource dataSource() {
    	
    	BoneCPDataSource dataSource = new BoneCPDataSource();

    	Properties applicationProperties = new Properties();
    	
    	try {
    		String filePath = getClass().getClassLoader().getResource("config/application.properties").getFile();
    		applicationProperties.load(new FileInputStream(filePath));
    	
        	dataSource.setDriverClass(applicationProperties.getProperty("spring.datasource.driverClassName"));
        	dataSource.setJdbcUrl(applicationProperties.getProperty("spring.datasource.url"));
        	dataSource.setUsername(applicationProperties.getProperty("spring.datasource.username"));
        	dataSource.setPassword(applicationProperties.getProperty("spring.datasource.password"));
    	} catch (IOException e) {
    		throw new IllegalStateException("application properties could not be found");
    	}
    	
        return dataSource;
    }
}
