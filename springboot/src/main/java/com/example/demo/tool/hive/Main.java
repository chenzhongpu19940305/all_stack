package com.example.demo.tool.hive;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String excelPath = "/data/report.xlsx"; 
        String sheetName = "SalesData";
        String columnName = "ProductID";
        
        try {
            List<String> productIds = ExcelColumnReader.getColumnData(
                excelPath, sheetName, columnName
            );
            System.out.println(" 读取到" + productIds.size()  + "条数据:");
            productIds.forEach(System.out::println); 
        } catch (Exception e) {
            System.err.println(" 错误: " + e.getMessage()); 
        }
    }
}