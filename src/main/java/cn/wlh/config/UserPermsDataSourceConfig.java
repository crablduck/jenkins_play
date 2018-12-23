package cn.wlh.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
// 扫描 Mapper 接口并容器管理
@MapperScan(basePackages = UserPermsDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "userPermsSqlSessionFactory")
public class UserPermsDataSourceConfig {

    static final String PACKAGE = "cn.wlh.mapper";
    static final String MAPPER_LOCATION = "classpath:mapper/*.xml";

    @Resource
    private Environment env;

    @Autowired(required = false)
    private Interceptor[] interceptors;


    @Bean(name = "userPermsDataSource")
    @Primary
    public DataSource userPermsDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(env.getProperty("user.perms.datasource.driverClassName"));
        dataSource.setUrl(env.getProperty("user.perms.datasource.url"));
        dataSource.setUsername(env.getProperty("user.perms.datasource.username"));
        dataSource.setPassword(env.getProperty("user.perms.datasource.password"));
        dataSource.setInitialSize(env.getProperty("user.perms.datasource.initialSize",Integer.class));
        dataSource.setMinIdle(env.getProperty("user.perms.datasource.minIdle",Integer.class));
        dataSource.setMinIdle(env.getProperty("user.perms.datasource.maxActive",Integer.class));
        dataSource.setTimeBetweenEvictionRunsMillis(env.getProperty("user.perms.datasource.timeBetweenEvictionRunsMillis",Long.class));
        dataSource.setMinEvictableIdleTimeMillis(env.getProperty("user.perms.datasource.minEvictableIdleTimeMillis",Long.class));
        dataSource.setValidationQuery(env.getProperty("user.perms.datasource.validationQuery"));
        dataSource.setTestWhileIdle(env.getProperty("user.perms.datasource.testWhileIdle",Boolean.class));
        dataSource.setTestOnBorrow(env.getProperty("user.perms.datasource.testOnBorrow",Boolean.class));
        dataSource.setTestOnReturn(env.getProperty("user.perms.datasource.testOnReturn",Boolean.class));
        return dataSource;
    }

    @Bean(name = "userPermsTransactionManager")
    @Primary
    public DataSourceTransactionManager userPermsTransactionManager() {
        return new DataSourceTransactionManager(userPermsDataSource());
    }

    @Bean(name = "userPermsSqlSessionFactory")
    @Primary
    public MybatisSqlSessionFactoryBean userPermsSqlSessionFactory(@Qualifier("userPermsDataSource") DataSource userPermsDataSource)
            throws Exception {
        MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        mybatisSqlSessionFactoryBean.setDataSource(userPermsDataSource);
        mybatisSqlSessionFactoryBean.setTypeAliasesPackage("cn.wlh.model*");
        mybatisSqlSessionFactoryBean.setPlugins(this.interceptors);
        mybatisSqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(UserPermsDataSourceConfig.MAPPER_LOCATION));
        return mybatisSqlSessionFactoryBean;
    }
}