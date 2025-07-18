package com.example.demo.tool.hive;

import java.io.IOException;
import java.nio.charset.StandardCharsets; 
import java.nio.file.Files; 
import java.nio.file.Paths; 
import java.util.List; 
import java.util.stream.Collectors; 
 
public class SqlGeneratorV2 {
 
    /**
     * 生成无分号分隔的SQL文件 
     * @param sqlFilePath     模板文件路径（需包含表占位符）
     * @param placeholder     表名占位标识符（如{{table}}）
     * @param tableList       动态表名集合 
     * @param outputFile      输出文件路径 
     */
    public static void generateCleanSql(String sqlFilePath, 
                                       String placeholder,
                                       List<String> tableList,
                                       String outputFile) throws IOException {
        // 参数有效性验证 
        if (sqlFilePath == null || placeholder == null || outputFile == null) {
            throw new IllegalArgumentException("关键参数缺失");
        }
        if (tableList == null || tableList.isEmpty())  {
            throw new IllegalArgumentException("表名列表为空");
        }
 
        // 读取模板内容（强制UTF-8编码）
        byte[] templateBytes = Files.readAllBytes(Paths.get(sqlFilePath)); 
        String template = new String(templateBytes, StandardCharsets.UTF_8);
 
        // 占位符存在性验证 
        if (!template.contains(placeholder))  {
            throw new IllegalArgumentException("占位符 " + placeholder + " 未在模板中发现");
        }
 
        // 构建无分号SQL集合 
        List<String> sqlList = tableList.stream() 
            .distinct()
            .map(table -> template.replace(placeholder,  table).trim())
            .collect(Collectors.toList()); 
 
        // 生成纯换行分隔内容 
        String output = String.join("\n",  sqlList);
        
        // 确保目录存在后写入 
        Files.createDirectories(Paths.get(outputFile).getParent()); 
        Files.write(Paths.get(outputFile),  output.getBytes(StandardCharsets.UTF_8)); 
    }
}