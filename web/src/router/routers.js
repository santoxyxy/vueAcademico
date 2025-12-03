import { createRouter, createWebHashHistory } from 'vue-router';

// Importa el Layout y Home
import Layout from '@/layout/index.vue'; 
import Home from '@/views/Home.vue'; 

// -----------------------------------------------------
// 1. RUTAS CONSTANTES (No necesitan autenticación)
// -----------------------------------------------------
export const constantRoutes = [
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

    // Home con Layout
    {
        path: '/',
        component: Layout,
        name: 'LayoutView',
        redirect: '/home',
        children: [
            {
                path: 'home',
                name: 'Inicio',
                component: Home,
                meta: { title: 'Inicio', icon: 'Home' }
            }
        ]
    }
];

// -----------------------------------------------------
// 2. RUTAS ASÍNCRONAS (Requieren autenticación)
// -----------------------------------------------------
export const asyncRoutes = [

    // ---------- MÓDULO: Gestión Académica ----------
    {
        path: '/gestion-academica',
        component: Layout,
        meta: { title: 'Gestión Académica', icon: 'Setting', requiresAuth: true },
        children: [
            {
                path: 'general',
                name: 'General',
                component: () => import('@/views/general/index.vue'),
                meta: { title: 'Gestión General', icon: 'Calendar', requiresAuth: true }
            },
            {
                path: 'progra',
                name: 'Progra',
                component: () => import('@/views/progra/index.vue'),
                meta: { title: 'Clases Ofertadas', icon: 'Book' }
            },
            {
                path: 'dicta',
                name: 'Dicta',
                component: () => import('@/views/dicta/index.vue'),
                meta: { title: 'Asignación Docentes-Materias', icon: 'User' }
            },
            {
                path: 'mapa',
                name: 'Mapa',
                component: () => import('@/views/mapa/index.vue'),
                meta: { title: 'Oferta Académica (MAPA)', icon: 'Table' }
            },
            {
                path: 'materia',
                name: 'Materia',
                component: () => import('@/views/materia/index.vue'),
                meta: { title: 'Gestión de Materias', icon: 'List' }
            },
            {
                path: 'notas',
                name: 'Notas',
                component: () => import('@/views/notas/index.vue'),
                meta: { title: 'Sistema de Calificaciones', icon: 'Document', requiresAuth: true }
            },
        ]
    },

    // ---------- MÓDULO: Configuración ----------
    {
        path: '/configuracion',
        component: Layout,
        meta: { title: 'Configuración', icon: 'Setting' },
        children: [

            // CRUD de Ítems y niveles
            {
                path: 'items',
                name: 'Items',
                component: () => import('@/views/items/index.vue'),
                meta: { title: 'Gestión de Ítems', icon: 'Wallet' }
            },
            {
                path: 'itemat',
                name: 'Itemat',
                component: () => import('@/views/itemat/index.vue'),
                meta: { title: 'Configuración de Ponderaciones', icon: 'Scale' }
            },
            {
                path: 'niveles',
                name: 'Niveles',
                component: () => import('@/views/niveles/index.vue'),
                meta: { title: 'Gestión de Niveles', icon: 'Trophy', requiresAuth: true }
            },
            {
                path: 'paralelo',
                name: 'Paralelo',
                component: () => import('@/views/paralelo/index.vue'),
                meta: { title: 'Gestión de Paralelos', icon: 'Grid' }
            },

            // Modalidades
            {
                path: 'modalidad',
                name: 'Modalidad',
                component: () => import('@/views/modalidad/index.vue'),
                meta: { title: 'Modalidades Académicas', icon: 'List' }
            },
            {
                path: 'dmodalidad',
                name: 'Dmodalidad',
                component: () => import('@/views/dmodalidad/index.vue'),
                meta: { title: 'Detalles de Modalidades', icon: 'ListCheck' }
            },

            // CRUD que agregamos (Clientes y Usuarios)
            {
                path: 'clientes',
                name: 'Clientes',
                component: () => import('@/views/clientes/index.vue'),
                meta: { title: 'Gestión de Clientes', icon: 'Users' }
            },
            {
                path: 'usuarios',
                name: 'Usuarios',
                component: () => import('@/views/usuario/index.vue'),
                meta: { title: 'Gestión de Usuarios', icon: 'User' }
            }
        ]
    },

    // ---------- Catch-all 404 ----------
    {
        path: '/:pathMatch(.*)*',
        redirect: '/404',
        meta: { hidden: true }
    }
];

// -----------------------------------------------------
// 3. CREACIÓN DEL ROUTER
// -----------------------------------------------------
const routers = createRouter({
    history: createWebHashHistory(),
    routes: constantRoutes.concat(asyncRoutes)
});

export default routers;
