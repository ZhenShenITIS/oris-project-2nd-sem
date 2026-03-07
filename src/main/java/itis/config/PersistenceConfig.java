package itis.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.HikariCPSettings;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Configuration
@PropertySource("classpath:persistence.properties")
@EnableJpaRepositories("itis.repository")
@EnableTransactionManagement
public class PersistenceConfig implements EnvironmentAware {
    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public DataSource dataSource() {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(environment.getProperty("spring.datasource.url"));
        hikariDataSource.setUsername(environment.getProperty("spring.datasource.username"));
        hikariDataSource.setPassword(environment.getProperty("spring.datasource.password"));
        hikariDataSource.setDriverClassName(environment.getProperty("spring.datasource.driver-class-name"));
        return hikariDataSource;
    }


    @Bean
    public HibernateJpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.valueOf(environment.getProperty("spring.database")));
        vendorAdapter.setShowSql(true);
        vendorAdapter.setGenerateDdl(true);

        return vendorAdapter;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory(DataSource dataSource, HibernateJpaVendorAdapter jpaVendorAdapter) {

        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        entityManagerFactoryBean.setPackagesToScan("itis.model");
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.afterPropertiesSet();

        return entityManagerFactoryBean.getObject();
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    public PersistenceAnnotationBeanPostProcessor persistenceAnnotationBeanPostProcessor() {
        return new PersistenceAnnotationBeanPostProcessor();
    }


    //@Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) throws IOException {
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setPackagesToScan("itis.model");
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.dialect", environment.getProperty("hibernate.dialect"));
        sessionFactoryBean.setHibernateProperties(hibernateProperties);
        sessionFactoryBean.afterPropertiesSet();
        return sessionFactoryBean;
    }



//    @Bean
//    public PlatformTransactionManager transactionManager (LocalSessionFactoryBean sessionFactory) {
//        return new HibernateTransactionManager(sessionFactory.getObject());
//    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
