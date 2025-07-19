import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import About from '../views/About.vue'
import Contact from '../views/Contact.vue'
import KibanaQuery from '../views/KibanaQuery.vue'
import FeatureConfig from '../views/FeatureConfig.vue'
import FeatureModule from '../views/FeatureModule.vue'
import Algorithms from '../views/Algorithms.vue'

import Gallery from '../views/Gallery.vue'
import VideoGallery from '../views/VideoGallery.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/about',
    name: 'About',
    component: About
  },
  {
    path: '/contact',
    name: 'Contact',
    component: Contact
  },
  {
    path: '/kibana-query',
    name: 'KibanaQuery',
    component: KibanaQuery
  },
  {
    path: '/feature-config',
    name: 'FeatureConfig',
    component: FeatureConfig
  },
  {
    path: '/feature-module',
    name: 'FeatureModule',
    component: FeatureModule
  },
  {
    path: '/algorithms',
    name: 'Algorithms',
    component: Algorithms
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
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router 