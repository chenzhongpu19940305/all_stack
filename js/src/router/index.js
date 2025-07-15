import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import About from '../views/About.vue'
import Contact from '../views/Contact.vue'
import KibanaQuery from '../views/KibanaQuery.vue'
import FeatureConfig from '../views/FeatureConfig.vue'
import Algorithms from '../views/Algorithms.vue'

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
    path: '/algorithms',
    name: 'Algorithms',
    component: Algorithms
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router 