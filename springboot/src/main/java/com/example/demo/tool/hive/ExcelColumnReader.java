package com.example.demo.tool.hive;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext; 
import com.alibaba.excel.event.AnalysisEventListener; 
import com.alibaba.excel.read.builder.ExcelReaderBuilder; 
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder; 
import lombok.Data;
import java.io.File; 
import java.util.ArrayList; 
import java.util.List; 
import java.util.Map; 
 
@Data 
public class ExcelColumnReader {
 
    /**
     * 读取Excel列的入口方法
     * @param excelPath  Excel文件绝对路径
     * @param sheetName  Sheet名称
     * @param columnName 列名称（表头文本）
     * @return 包含该列所有数据的List（跳过空值）
     * @throws IllegalArgumentException 文件不存在或读取错误时抛出
     */
    public static List<String> getColumnData(String excelPath, String sheetName, String columnName) {
        // 1. 验证文件有效性 
        File excelFile = new File(excelPath);
        if (!excelFile.exists()  || !excelFile.isFile())  {
            throw new IllegalArgumentException("Excel文件不存在或路径无效: " + excelPath);
        }
 
        // 2. 创建监听器并读取数据
        ColumnDataListener listener = new ColumnDataListener(columnName);
        try {
            ExcelReaderBuilder readerBuilder = EasyExcel.read(excelFile,  listener);
            ExcelReaderSheetBuilder sheetBuilder = sheetName != null ? 
                readerBuilder.sheet(sheetName)  : 
                readerBuilder.sheet(); 
            sheetBuilder.doRead(); 
        } catch (Exception e) {
            throw new RuntimeException("Excel读取失败: " + e.getMessage(),  e);
        }
 
        // 3. 返回结果并处理异常情况 
        if (listener.isColumnMissing())  {
            throw new IllegalArgumentException("列名不存在: " + columnName);
        }
        return listener.getResult(); 
    }
 
    // 自定义监听器（核心处理逻辑）
    private static class ColumnDataListener extends AnalysisEventListener<Map<Integer, String>> {
        private final String targetColumn;
        private final List<String> columnData = new ArrayList<>();
        private Map<Integer, String> headerMap;
        private Integer targetColumnIndex;
        private boolean columnMissing = false;
 
        public ColumnDataListener(String targetColumn) {
            this.targetColumn  = targetColumn;
        }
 
        @Override
        public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
            headerMap = headMap;
            // 定位目标列的索引
            targetColumnIndex = headMap.entrySet().stream() 
                .filter(entry -> targetColumn.equals(entry.getValue())) 
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
            
            if (targetColumnIndex == null) {
                columnMissing = true;
            }
        }
 
        @Override
        public void invoke(Map<Integer, String> rowData, AnalysisContext context) {
            if (columnMissing || targetColumnIndex == null) return;
            
            // 提取目标列的值（跳过空值）
            String cellValue = rowData.get(targetColumnIndex); 
            if (cellValue != null && !cellValue.trim().isEmpty())  {
                columnData.add(cellValue); 
            }
        }
 
        @Override 
        public void doAfterAllAnalysed(AnalysisContext context) {
            // 读取完成后的清理操作（按需实现）
        }
 
        public List<String> getResult() {
            return columnData;
        }
 
        public boolean isColumnMissing() {
            return columnMissing;
        }
    }
}