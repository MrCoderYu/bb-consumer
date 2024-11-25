package io.bbex.bb.server.config;

import com.zaxxer.hikari.metrics.prometheus.PrometheusHistogramMetricsTrackerFactory;
import io.bbex.base.mysql.BBMysqlDataSource;
import io.bbex.exchange.mybatis.interceptor.MybatisPerfInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import tk.mybatis.mapper.autoconfigure.SpringBootVFS;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "io.bbex.bb.common.mapper", sqlSessionTemplateRef = "primarySqlSessionTemplate")
public class PrimaryDataSourceConfig {

    @Bean(name = "primaryDatasource")
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    @Primary
    public DataSource primaryDatasource(PrometheusHistogramMetricsTrackerFactory prometheusHistogramMetricsTrackerFactory) {
        BBMysqlDataSource dataSource = DataSourceBuilder.create().type(BBMysqlDataSource.class).build();
        dataSource.setPoolName("bb-server");
        dataSource.setMetricsTrackerFactory(prometheusHistogramMetricsTrackerFactory);
        return dataSource;
    }

    @Bean(name = "primarySqlSessionFactory")
    @Primary
    public SqlSessionFactory primarySqlSessionFactory(@Qualifier("primaryDatasource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setVfs(SpringBootVFS.class);
        factory.setDataSource(dataSource);
        factory.setPlugins(new MybatisPerfInterceptor[]{new MybatisPerfInterceptor()});
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        factory.setConfiguration(configuration);
        return factory.getObject();
    }

    @Bean(name = "transactionManager")
    @Primary
    public DataSourceTransactionManager transactionManager(@Qualifier("primaryDatasource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "primarySqlSessionTemplate")
    @Primary
    public SqlSessionTemplate primarySqlSessionTemplate(@Qualifier("primarySqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name = "primaryJdbcTemplate")
    public JdbcTemplate primaryJdbcTemplate(@Qualifier("primaryDatasource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
