import { createRouter, createWebHashHistory } from 'vue-router';
// Importa el Layout y Home
import Layout from '@/layout/index.vue'; 
import Home from '@/views/Home.vue'; 

// 1. RUTAS CONSTANTES (No usan el Layout, ni requieren autenticación)
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
    // Rutas con Layout que representan la página de inicio
    {
        path: '/',
        component: Layout,
        name: 'LayoutView',
        redirect: '/home',
        children: [
            {
                path: 'home',
                name: 'Inicio',
                component: Home, // Usamos el componente Home importado
                meta: { title: 'Inicio', icon: 'Home' }
            }
        ]
    }
];

// 2. RUTAS ASÍNCRONAS (Módulos principales que usan el Layout)
export const asyncRoutes = [
    // --- Módulos Académicos/Configuración ---
    {
        path: '/gestion-academica', // Agrupación lógica
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
                meta: { title: 'Asignación Docentes-Materias', icon: 'User' },
            },
            {
                path: 'mapa',
                name: 'Mapa',
                component: () => import('@/views/mapa/index.vue'),
                meta: { title: 'Oferta Académica (MAPA)', icon: 'Table' },
            },
            {
                path: 'materia', // Nota: Tu estructura original estaba anidada incorrectamente aquí.
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
    
    // --- Módulos de Ítems y Modalidades ---
    {
        path: '/configuracion',
        component: Layout,
        meta: { title: 'Configuración', icon: 'Setting' },
        children: [
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
            {
                path: 'modalidad',
                name: 'Modalidad',
                component: () => import('@/views/modalidad/index.vue'),
                meta: { title: 'Modalidades Académicas', icon: 'List' },
            },
            {
                path: 'dmodalidad',
                name: 'Dmodalidad',
                component: () => import('@/views/dmodalidad/index.vue'),
                meta: { title: 'Detalles de Modalidades', icon: 'ListCheck' },
            },
        ]
    },

    // --- Catch-all 404 ---
    // Esta debe ser la última ruta.
    {
        path: '/:pathMatch(.*)*', 
        redirect: '/404', 
        meta: { hidden: true } 
    }
];

// 3. CREACIÓN DEL ROUTER
const routers = createRouter({
    history: createWebHashHistory(),
    // Unimos las rutas constantes y asíncronas para la configuración inicial
    routes: constantRoutes.concat(asyncRoutes) 
});

export default routers;