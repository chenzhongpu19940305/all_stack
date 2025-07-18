package com.algorithm;

import java.util.*;

/**
 * Dijkstra 最短路径算法实现
 * 用于在带权有向图中找到从源点到所有其他顶点的最短路径
 */
public class Dijkstra {
    
    /**
     * 图的邻接表表示
     */
    private static class Graph {
        private int V; // 顶点数
        private List<List<Edge>> adj; // 邻接表
        
        public Graph(int V) {
            this.V = V;
            adj = new ArrayList<>();
            for (int i = 0; i < V; i++) {
                adj.add(new ArrayList<>());
            }
        }
        
        /**
         * 添加边
         * @param from 起始顶点
         * @param to 目标顶点
         * @param weight 权重
         */
        public void addEdge(int from, int to, int weight) {
            adj.get(from).add(new Edge(to, weight));
        }
    }
    
    /**
     * 边的表示
     */
    private static class Edge {
        int to;
        int weight;
        
        public Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }
    
    /**
     * 优先队列节点
     */
    private static class Node implements Comparable<Node> {
        int vertex;
        int distance;
        
        public Node(int vertex, int distance) {
            this.vertex = vertex;
            this.distance = distance;
        }
        
        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.distance, other.distance);
        }
    }
    
    /**
     * Dijkstra 算法主方法
     * @param graph 图
     * @param source 源顶点
     * @return 从源点到所有顶点的最短距离数组
     */
    public static int[] dijkstra(Graph graph, int source) {
        int V = graph.V;
        int[] distances = new int[V];
        boolean[] visited = new boolean[V];
        
        // 初始化距离数组
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[source] = 0;
        
        // 使用优先队列（最小堆）来优化
        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.offer(new Node(source, 0));
        
        while (!pq.isEmpty()) {
            Node current = pq.poll();
            int u = current.vertex;
            
            // 如果已经访问过，跳过
            if (visited[u]) {
                continue;
            }
            
            visited[u] = true;
            
            // 遍历所有邻接顶点
            for (Edge edge : graph.adj.get(u)) {
                int v = edge.to;
                int weight = edge.weight;
                
                // 如果找到更短的路径，更新距离
                if (!visited[v] && distances[u] != Integer.MAX_VALUE && 
                    distances[u] + weight < distances[v]) {
                    distances[v] = distances[u] + weight;
                    pq.offer(new Node(v, distances[v]));
                }
            }
        }
        
        return distances;
    }
    
    /**
     * 带路径记录的 Dijkstra 算法
     * @param graph 图
     * @param source 源顶点
     * @return 包含距离和路径信息的对象
     */
    public static DijkstraResult dijkstraWithPath(Graph graph, int source) {
        int V = graph.V;
        int[] distances = new int[V];
        int[] previous = new int[V];
        boolean[] visited = new boolean[V];
        
        // 初始化
        Arrays.fill(distances, Integer.MAX_VALUE);
        Arrays.fill(previous, -1);
        distances[source] = 0;
        
        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.offer(new Node(source, 0));
        
        while (!pq.isEmpty()) {
            Node current = pq.poll();
            int u = current.vertex;
            
            if (visited[u]) {
                continue;
            }
            
            visited[u] = true;
            
            for (Edge edge : graph.adj.get(u)) {
                int v = edge.to;
                int weight = edge.weight;
                
                if (!visited[v] && distances[u] != Integer.MAX_VALUE && 
                    distances[u] + weight < distances[v]) {
                    distances[v] = distances[u] + weight;
                    previous[v] = u;
                    pq.offer(new Node(v, distances[v]));
                }
            }
        }
        
        return new DijkstraResult(distances, previous);
    }
    
    /**
     * Dijkstra 算法结果类
     */
    public static class DijkstraResult {
        public int[] distances;
        public int[] previous;
        
        public DijkstraResult(int[] distances, int[] previous) {
            this.distances = distances;
            this.previous = previous;
        }
        
        /**
         * 获取从源点到目标顶点的路径
         * @param target 目标顶点
         * @return 路径列表
         */
        public List<Integer> getPath(int target) {
            List<Integer> path = new ArrayList<>();
            if (distances[target] == Integer.MAX_VALUE) {
                return path; // 没有路径可达
            }
            
            // 从目标顶点回溯到源顶点
            for (int current = target; current != -1; current = previous[current]) {
                path.add(current);
            }
            
            // 反转路径
            Collections.reverse(path);
            return path;
        }
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 创建一个示例图
        Graph graph = new Graph(6);
        
        // 添加边 (from, to, weight)
        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 2, 2);
        graph.addEdge(1, 2, 1);
        graph.addEdge(1, 3, 5);
        graph.addEdge(2, 3, 8);
        graph.addEdge(2, 4, 10);
        graph.addEdge(3, 4, 2);
        graph.addEdge(3, 5, 6);
        graph.addEdge(4, 5, 3);
        
        int source = 0;
        
        // 运行 Dijkstra 算法
        DijkstraResult result = dijkstraWithPath(graph, source);
        
        System.out.println("从顶点 " + source + " 到各顶点的最短距离：");
        for (int i = 0; i < graph.V; i++) {
            if (result.distances[i] == Integer.MAX_VALUE) {
                System.out.println("到顶点 " + i + " 的距离：不可达");
            } else {
                System.out.println("到顶点 " + i + " 的距离：" + result.distances[i]);
                System.out.println("路径：" + result.getPath(i));
            }
        }
    }
} 