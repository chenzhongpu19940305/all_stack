// 路由配置文件
import Home from '../views/Home.vue'
import Gallery from '../views/Gallery.vue'
import VideoGallery from '../views/VideoGallery.vue'
import CodeSnippets from '../views/CodeSnippets.vue'
import MindMap from '../views/MindMap.vue'
import EdrawMind from '../views/EdrawMind.vue'
import RouteTest from '../views/RouteTest.vue'
import EdrawMindHome from '../views/EdrawMindHome.vue'
import Docs from '../views/Docs.vue'

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
    path: '/code-snippets',
    name: 'CodeSnippets',
    component: CodeSnippets,
    meta: {
      title: '代码片段库',
      icon: '💻',
      description: '代码片段管理'
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
    path: '/docs',
    name: 'Docs',
    component: Docs,
    meta: {
      title: '文档库',
      icon: '📚',
      description: 'Word/Excel/PDF/PPT 文档管理'
    }
  },
  {
    path: '/edraw-mind',
    name: 'EdrawMindHome',
    component: EdrawMindHome,
    meta: {
      title: 'EdrawMind专业版',
      icon: '🎨',
      description: '专业思维导图首页'
    }
  },
  {
    path: '/edraw-mind/editor',
    name: 'EdrawMind',
    component: EdrawMind,
    meta: {
      title: 'EdrawMind编辑器',
      icon: '🧩',
      description: '专业思维导图编辑器'
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