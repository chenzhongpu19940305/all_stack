package com.example.demo.tool.hive;

import java.io.IOException;
import java.nio.charset.StandardCharsets; 
import java.nio.file.Files; 
import java.nio.file.Path; 
import java.nio.file.Paths; 
import java.util.List; 
import java.util.stream.Collectors; 
 
public class SqlBatchGenerator {
 
    /**
     * 批量生成SQL文件（每个表名生成独立SQL语句）
     * @param templatePath   SQL模板文件路径 
     * @param placeholder    表名占位符（如：${table}）
     * @param tableList      表名列表（每个元素生成一个SQL）
     * @param outputPath     输出文件路径
     */
    public static void generateBatchSql(String templatePath, 
                                       String placeholder,
                                       List<String> tableList,
                                       String outputPath) throws IOException {
        // 参数校验 
        validateInput(templatePath, placeholder, tableList, outputPath);
        
        // 读取模板内容
        String sqlTemplate = readTemplate(templatePath);
        
        // 批量生成SQL 
        List<String> sqlStatements = tableList.stream() 
            .map(table -> generateSingleSql(sqlTemplate, placeholder, table))
            .collect(Collectors.toList()); 
        
        // 构建纯换行分隔的内容 
        String outputContent = String.join("\n",  sqlStatements);
        
        // 确保输出目录存在
        createOutputDirectory(outputPath);
        
        // 写入文件
        Files.write(Paths.get(outputPath),  outputContent.getBytes(StandardCharsets.UTF_8)); 
    }
 
    private static void validateInput(String templatePath, String placeholder, 
                                     List<String> tableList, String outputPath) {
        if (templatePath == null || templatePath.trim().isEmpty())  {
            throw new IllegalArgumentException("SQL模板路径不能为空");
        }
        if (placeholder == null || placeholder.trim().isEmpty())  {
            throw new IllegalArgumentException("占位符不能为空");
        }
        if (tableList == null || tableList.isEmpty())  {
            throw new IllegalArgumentException("表名列表不能为空");
        }
        if (outputPath == null || outputPath.trim().isEmpty())  {
            throw new IllegalArgumentException("输出路径不能为空");
        }
    }
 
    private static String readTemplate(String templatePath) throws IOException {
        Path path = Paths.get(templatePath); 
        if (!Files.exists(path))  {
            throw new IOException("模板文件不存在: " + templatePath);
        }
        return new String(Files.readAllBytes(path),  StandardCharsets.UTF_8);
    }
 
    private static String generateSingleSql(String template, String placeholder, String tableName) {
        if (!template.contains(placeholder))  {
            throw new IllegalArgumentException("模板中未找到占位符: " + placeholder);
        }
        return template.replace(placeholder,  tableName);
    }
 
    private static void createOutputDirectory(String outputPath) throws IOException {
        Path parentDir = Paths.get(outputPath).getParent(); 
        if (parentDir != null && !Files.exists(parentDir))  {
            Files.createDirectories(parentDir); 
        }
    }
}