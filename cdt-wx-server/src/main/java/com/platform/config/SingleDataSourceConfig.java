package com.platform.config;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.parsers.BlockAttackSqlParser;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.extension.plugins.SqlExplainInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据源配置
 */
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = {"com.platform.dao"})
public class SingleDataSourceConfig {
    
    /**
     * mybatis-plus SQL执行效率插件【生产环境可以关闭】
     */
    @Bean
    @Profile({"local","dev"})
    public PerformanceInterceptor performanceInterceptor() {
    	PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
        //格式化sql语句
        /*Properties properties = new Properties();
        properties.setProperty("format", "true");
        performanceInterceptor.setProperties(properties);*/
        performanceInterceptor.setFormat(true);
        performanceInterceptor.setMaxTime(180000);
        return performanceInterceptor;
    }
    

    /**
     * mybatis-plus分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
    	PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
    	List<ISqlParser> sqlParserList = new ArrayList<ISqlParser>();
        // 攻击 SQL 阻断解析器、加入解析链
        sqlParserList.add(new BlockAttackSqlParser());
        paginationInterceptor.setSqlParserList(sqlParserList);
    	//paginationInterceptor.setLocalPage(true);// 开启 PageHelper 的支持
        return paginationInterceptor;
    }

    
    /**
     * 乐观锁mybatis插件
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }


    /**
     * 注入sql注入器
     */
    @Bean
    public ISqlInjector sqlInjector() {
    	return new LogicSqlInjector();
    }


    /**
     * 如果是对全表的删除或更新操作，就会终止该操作
     * @return
     */
    @Bean
    public SqlExplainInterceptor sqlExplainInterceptor() {
        return new SqlExplainInterceptor();
    }
}
