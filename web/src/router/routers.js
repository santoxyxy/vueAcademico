import {createRouter, createWebHashHistory} from "vue-router";
import Layout from '../layout/index'
import Home from '../views/Home'

export const routerMap = [
    {
        path: '/login',
        name: 'login',
        component: () => import('../login')
    },
    {
        path: '/401',
        name: '401',
        component: () => import('../views/error/401')
    },
    {
        path: '/404',
        name: '404',
        component: () => import('../views/error/404')
    },
    {
  path: '/progra',
  component: () => import('@/views/progra/index.vue'),
  name: 'Progra',
  meta: { title: 'Clases Ofertadas' }
},
    {
  path: '/general',
  component: () => import('@/views/general/index.vue'),
  name: 'General',
  meta: {
    title: 'Gestión General',
    icon: 'Calendar',
    requiresAuth: true
  }
},
    {
        path: '/Layout',
        name: 'Layout',
        component: Layout,
        children: [
            {
                path: '/home',
                name: 'Inicio',
                component: Home
            }
        ]
    }
]

//  crear ruta
const routers = createRouter({
    history: createWebHashHistory(),
    routes: routerMap
})

export default routers