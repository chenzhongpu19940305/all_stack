import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import KibanaQuery from '../views/KibanaQuery.vue'
import ESFieldSearch from '../views/ESFieldSearch.vue'
import QueryList from '../views/QueryList.vue'
import Gallery from '../views/Gallery.vue'
import VideoGallery from '../views/VideoGallery.vue'
import MindMap from '../views/MindMap.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/kibana-query',
    name: 'KibanaQuery',
    component: KibanaQuery
  },
  {
    path: '/gallery',
    name: 'Gallery',
    component: Gallery
  },
  {
    path: '/video-gallery',
    name: 'VideoGallery',
    component: VideoGallery
  },
  {
    path: '/es-field-search',
    name: 'ESFieldSearch',
    component: ESFieldSearch
  },
  {
    path: '/query-list',
    name: 'QueryList',
    component: QueryList
  },
  {
    path: '/mind-map',
    name: 'MindMap',
    component: MindMap
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router