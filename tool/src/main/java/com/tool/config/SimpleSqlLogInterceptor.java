package com.tool.config;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;

/**
 * 简化版MyBatis SQL日志拦截器
 * 用于打印SQL语句和参数
 * 
 * @author tool-service
 * @version 1.0.0
 */
@Intercepts({
    @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class SimpleSqlLogInterceptor implements Interceptor {
    
    private static final Logger logger = LoggerFactory.getLogger(SimpleSqlLogInterceptor.class);
    
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs()[1];
        
        String sqlId = mappedStatement.getId();
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        
        // 获取原始SQL
        String sql = boundSql.getSql();
        
        // 打印SQL日志
        logger.info("=== MyBatis SQL日志 ===");
        logger.info("SQL ID: {}", sqlId);
        logger.info("原始SQL: {}", sql);
        logger.info("参数: {}", parameter);
        logger.info("========================");
        
        // 执行原方法
        Object result = invocation.proceed();
        
        // 打印结果
        if (result instanceof List) {
            logger.info("查询结果数量: {}", ((List<?>) result).size());
        } else if (result instanceof Integer) {
            logger.info("影响行数: {}", result);
        }
        
        return result;
    }
    
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }
    
    @Override
    public void setProperties(Properties properties) {
        // 可以在这里设置拦截器的属性
    }
} 