// 路由配置文件
import Home from '../views/Home.vue'
import KibanaQuery from '../views/KibanaQuery.vue'
import ESFieldSearch from '../views/ESFieldSearch.vue'
import QueryList from '../views/QueryList.vue'
import Gallery from '../views/Gallery.vue'
import CodeGallery from '../views/CodeGallery.vue'
import VideoGallery from '../views/VideoGallery.vue'
import MindMap from '../views/MindMap.vue'
import Introduction from '../views/Introduction.vue'
import RouteTest from '../views/RouteTest.vue'

// 路由配置
export const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
    meta: {
      title: '首页',
      icon: '🏠',
      description: '企业知识管理系统首页'
    }
  },
  {
    path: '/gallery',
    name: 'Gallery',
    component: Gallery,
    meta: {
      title: 'AI问答记录',
      icon: '🤖',
      description: '智能问答知识库'
    }
  },
  {
    path: '/code-gallery',
    name: 'CodeGallery',
    component: CodeGallery,
    meta: {
      title: '代码片段管理',
      icon: '💻',
      description: '代码片段存储与分享'
    }
  },
  {
    path: '/video-gallery',
    name: 'VideoGallery',
    component: VideoGallery,
    meta: {
      title: '视频管理',
      icon: '🎥',
      description: '视频文件管理'
    }
  },
  {
    path: '/kibana-query',
    name: 'KibanaQuery',
    component: KibanaQuery,
    meta: {
      title: 'ES查询',
      icon: '🔍',
      description: 'Elasticsearch查询工具'
    }
  },
  {
    path: '/es-field-search',
    name: 'ESFieldSearch',
    component: ESFieldSearch,
    meta: {
      title: '字段搜索',
      icon: '📊',
      description: 'ES字段搜索工具'
    }
  },
  {
    path: '/query-list',
    name: 'QueryList',
    component: QueryList,
    meta: {
      title: '查询列表',
      icon: '📋',
      description: '查询历史记录'
    }
  },
  {
    path: '/mind-map',
    name: 'MindMap',
    component: MindMap,
    meta: {
      title: '思维导图',
      icon: '🧠',
      description: '思维导图工具'
    }
  },
  {
    path: '/introduction',
    name: 'Introduction',
    component: Introduction,
    meta: {
      title: '使用说明',
      icon: '📖',
      description: '系统使用说明'
    }
  },
  {
    path: '/route-test',
    name: 'RouteTest',
    component: RouteTest,
    meta: {
      title: '路由测试',
      icon: '🚀',
      description: '测试所有路由功能'
    }
  }
]

// 获取导航菜单数据
export function getNavigationMenu() {
  return routes.map(route => ({
    path: route.path,
    name: route.name,
    title: route.meta.title,
    icon: route.meta.icon,
    description: route.meta.description
  }))
}

// 根据路径获取路由信息
export function getRouteByPath(path) {
  return routes.find(route => route.path === path)
}

// 根据名称获取路由信息
export function getRouteByName(name) {
  return routes.find(route => route.name === name)
} 