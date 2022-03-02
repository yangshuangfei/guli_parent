package com.stitch.service.base.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import javafx.print.PageRange;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement  //支持事务
@Configuration
@MapperScan("com.stitch.service.*.mapper")
public class MybatisPlusConfig {
    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        return new PaginationInterceptor();
    }
}
