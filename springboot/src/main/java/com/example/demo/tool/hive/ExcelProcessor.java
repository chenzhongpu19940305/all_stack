package com.example.demo.tool.hive;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext; 
import com.alibaba.excel.event.AnalysisEventListener; 
import com.alibaba.excel.write.metadata.WriteSheet; 
import java.io.File; 
import java.util.*; 
 
public class ExcelProcessor {
 
    /**
     * 处理 Excel 数据 
     * @param filePath    Excel 文件路径 (e.g. "/data/report.xlsx") 
     * @param sheetName   Sheet 名称 (e.g. "Sheet1")
     * @param inputHeader  输入列表头名称 (e.g. "原始数据")
     * @param outputHeader 输出列表头名称 (e.g. "处理结果")
     * @param processor    数据处理函数 (输入 String, 输出 String)
     */
    public static void processExcel(String filePath, 
                                   String sheetName, 
                                   String inputHeader, 
                                   String outputHeader,
                                   java.util.function.Function<String,  String> processor) {
        
        // 1. 读取数据并处理 
        List<Map<String, Object>> dataList = new ArrayList<>();
        EasyExcel.read(new  File(filePath), new AnalysisEventListener<Map<Integer, String>>() {
            private Map<Integer, String> headerMap = new HashMap<>();
            
            @Override 
            public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
                this.headerMap  = headMap;  // 捕获表头映射关系 
            }
 
            @Override
            public void invoke(Map<Integer, String> rowData, AnalysisContext context) {
                Map<String, Object> processedRow = new LinkedHashMap<>();
                
                // 复制原始数据 
                rowData.forEach((colIndex,  value) -> {
                    String header = headerMap.get(colIndex); 
                    processedRow.put(header,  value);
                });
                
                // 处理目标列数据 
                String inputValue = String.valueOf(processedRow.get(inputHeader)); 
                processedRow.put(outputHeader,  processor.apply(inputValue)); 
                
                dataList.add(processedRow); 
            }
 
            @Override 
            public void doAfterAllAnalysed(AnalysisContext context) {
                // 处理完成后自动执行 
            }
        }).sheet(sheetName).doRead();
 
        // 2. 构建表头映射 
        Set<String> allHeaders = new LinkedHashSet<>();
        if (!dataList.isEmpty())  {
            allHeaders.addAll(dataList.get(0).keySet()); 
        }
        List<List<String>> excelHeaders = new ArrayList<>();
        allHeaders.forEach(header  -> 
            excelHeaders.add(Collections.singletonList(header)) 
        );
 
        // 3. 写回处理结果
        File outputFile = new File(filePath);
        try (com.alibaba.excel.ExcelWriter  excelWriter = EasyExcel.write(outputFile).build())  {
            WriteSheet writeSheet = EasyExcel.writerSheet(sheetName) 
                    .head(excelHeaders)
                    .build();
            excelWriter.write(dataList,  writeSheet);
        }
    }
}