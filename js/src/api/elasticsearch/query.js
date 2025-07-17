// 这是一个模拟的后端API响应示例
// 实际使用时，这个文件应该由后端服务器提供

// 模拟API响应
const mockResponse = {
  "took": 15,
  "timed_out": false,
  "_shards": {
    "total": 5,
    "successful": 5,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": {
      "value": 1250,
      "relation": "eq"
    },
    "max_score": 1.0,
    "hits": [
      {
        "_index": "logs-2024.01.01",
        "_id": "1",
        "_score": 1.0,
        "_source": {
          "timestamp": "2024-01-01T10:00:00Z",
          "level": "INFO",
          "message": "应用启动成功",
          "service": "web-server",
          "user_id": "user123",
          "ip": "192.168.1.100"
        }
      },
      {
        "_index": "logs-2024.01.01",
        "_id": "2",
        "_score": 1.0,
        "_source": {
          "timestamp": "2024-01-01T10:01:00Z",
          "level": "ERROR",
          "message": "数据库连接失败",
          "service": "database",
          "user_id": "user456",
          "ip": "192.168.1.101"
        }
      },
      {
        "_index": "logs-2024.01.01",
        "_id": "3",
        "_score": 1.0,
        "_source": {
          "timestamp": "2024-01-01T10:02:00Z",
          "level": "WARN",
          "message": "内存使用率较高",
          "service": "monitor",
          "user_id": "system",
          "ip": "192.168.1.102"
        }
      }
    ]
  },
  "aggregations": {
    "level_stats": {
      "doc_count_error_upper_bound": 0,
      "sum_other_doc_count": 0,
      "buckets": [
        {
          "key": "INFO",
          "doc_count": 850
        },
        {
          "key": "ERROR",
          "doc_count": 250
        },
        {
          "key": "WARN",
          "doc_count": 150
        }
      ]
    },
    "service_stats": {
      "doc_count_error_upper_bound": 0,
      "sum_other_doc_count": 0,
      "buckets": [
        {
          "key": "web-server",
          "doc_count": 600
        },
        {
          "key": "database",
          "doc_count": 400
        },
        {
          "key": "monitor",
          "doc_count": 250
        }
      ]
    }
  }
};

// 如果需要在浏览器中测试，可以取消注释下面的代码
// window.mockElasticsearchResponse = mockResponse; 