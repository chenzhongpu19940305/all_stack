// 模拟字段API响应
// 实际使用时，这个文件应该由后端服务器提供

// 模拟字段数据
const mockFieldsData = {
  "logs-*": {
    filterFields: [
      "timestamp",
      "level", 
      "message",
      "service",
      "user_id",
      "ip",
      "status_code",
      "response_time",
      "request_method",
      "request_path"
    ],
    aggregationFields: [
      "level",
      "service", 
      "status_code",
      "user_id",
      "request_method"
    ]
  },
  "metrics-*": {
    filterFields: [
      "timestamp",
      "metric_name",
      "metric_value",
      "host",
      "application",
      "environment"
    ],
    aggregationFields: [
      "metric_name",
      "host",
      "application",
      "environment"
    ]
  },
  "events-*": {
    filterFields: [
      "timestamp",
      "event_type",
      "event_source",
      "severity",
      "user_id",
      "session_id"
    ],
    aggregationFields: [
      "event_type",
      "event_source",
      "severity"
    ]
  }
};

// 如果需要在浏览器中测试，可以取消注释下面的代码
// window.mockFieldsData = mockFieldsData; 