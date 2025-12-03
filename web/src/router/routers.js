import { createRouter, createWebHashHistory } from 'vue-router';
import Layout from '@/layout/index.vue';
import Home from '@/views/Home.vue';

// =====================================================
// RUTAS CONSTANTES (Acceso sin autenticación)
// =====================================================
export const constantRoutes = [
  {
    path: '/login',
    name: 'login',
    component: () => import('../login'),
    meta: { hidden: true }
  },
  {
    path: '/401',
    name: '401',
    component: () => import('../views/error/401'),
    meta: { hidden: true }
  },
  {
    path: '/404',
    name: '404',
    component: () => import('../views/error/404'),
    meta: { hidden: true }
  },
  {
    path: '/',
    component: Layout,
    name: 'LayoutView',
    redirect: '/home',
    children: [
      {
        path: 'home',
        name: 'Home',
        component: Home,
        meta: { title: 'Inicio', icon: 'home' }
      }
    ]
  }
];

// =====================================================
// RUTAS ASÍNCRONAS (Requieren autenticación)
// =====================================================
export const asyncRoutes = [
  
  // ─────────────────────────────────────────────────
  // 📚 GESTIÓN ACADÉMICA
  // ─────────────────────────────────────────────────
  {
    path: '/gestion-academica',
    component: Layout,
    name: 'GestionAcademica',
    meta: { title: 'Gestión Académica', icon: 'school', requiresAuth: true },
    children: [
      {
        path: 'general',
        name: 'General',
        component: () => import('@/views/general/index.vue'),
        meta: { title: 'Gestión General', icon: 'dashboard' }
      },
      {
        path: 'materia',
        name: 'Materia',
        component: () => import('@/views/materia/index.vue'),
        meta: { title: 'Materias', icon: 'book' }
      },
      {
        path: 'progra',
        name: 'Progra',
        component: () => import('@/views/progra/index.vue'),
        meta: { title: 'Clases Ofertadas', icon: 'calendar_today' }
      },
      {
        path: 'dicta',
        name: 'Dicta',
        component: () => import('@/views/dicta/index.vue'),
        meta: { title: 'Asignación Docentes', icon: 'person' }
      },
      {
        path: 'mapa',
        name: 'Mapa',
        component: () => import('@/views/mapa/index.vue'),
        meta: { title: 'Mapa Curricular', icon: 'map' }
      },
      {
        path: 'notas',
        name: 'Notas',
        component: () => import('@/views/notas/index.vue'),
        meta: { title: 'Registro de Notas', icon: 'grade' }
      }
    ]
  },

  // ─────────────────────────────────────────────────
  // ⚙️ CONFIGURACIÓN
  // ─────────────────────────────────────────────────
  {
    path: '/configuracion',
    component: Layout,
    name: 'Configuracion',
    meta: { title: 'Configuración', icon: 'settings', requiresAuth: true },
    children: [
      {
        path: 'items',
        name: 'Items',
        component: () => import('@/views/items/index.vue'),
        meta: { title: 'Ítems de Evaluación', icon: 'checklist' }
      },
      {
        path: 'itemat',
        name: 'Itemat',
        component: () => import('@/views/itemat/index.vue'),
        meta: { title: 'Ponderaciones', icon: 'percent' }
      },
      {
        path: 'niveles',
        name: 'Niveles',
        component: () => import('@/views/niveles/index.vue'),
        meta: { title: 'Niveles Académicos', icon: 'stairs' }
      },
      {
        path: 'paralelo',
        name: 'Paralelo',
        component: () => import('@/views/paralelo/index.vue'),
        meta: { title: 'Paralelos', icon: 'group' }
      },
      {
        path: 'modalidad',
        name: 'Modalidad',
        component: () => import('@/views/modalidad/index.vue'),
        meta: { title: 'Modalidades', icon: 'school' }
      },
      {
        path: 'dmodalidad',
        name: 'Dmodalidad',
        component: () => import('@/views/dmodalidad/index.vue'),
        meta: { title: 'Detalle Modalidades', icon: 'list' }
      },
      {
        path: 'clientes',
        name: 'Clientes',
        component: () => import('@/views/clientes/index.vue'),
        meta: { title: 'Gestión de Clientes', icon: 'business' }
      }
    ]
  },

  // ─────────────────────────────────────────────────
  // 👥 USUARIOS
  // ─────────────────────────────────────────────────
  {
    path: '/usuarios',
    component: Layout,
    name: 'Usuarios',
    meta: { title: 'Usuarios', icon: 'people', requiresAuth: true },
    children: [
      {
        path: 'gestion',
        name: 'UsuariosGestion',
        component: () => import('@/views/usuario/index.vue'),
        meta: { title: 'Gestión Usuarios', icon: 'manage_accounts' }
      },
      {
        path: 'sistema',
        name: 'UsuariosSistema',
        component: () => import('@/views/user/index.vue'),
        meta: { title: 'Usuarios Sistema', icon: 'admin_panel_settings' }
      }
    ]
  },

  // ─────────────────────────────────────────────────
  // 🔐 SISTEMA
  // ─────────────────────────────────────────────────
  {
    path: '/sistema',
    component: Layout,
    name: 'Sistema',
    meta: { title: 'Sistema', icon: 'settings', requiresAuth: true },
    children: [
      {
        path: 'menu',
        name: 'Menu',
        component: () => import('@/views/menu/index.vue'),
        meta: { title: 'Gestión de Menús', icon: 'menu' }
      },
      {
        path: 'role',
        name: 'Role',
        component: () => import('@/views/role/index.vue'),
        meta: { title: 'Roles', icon: 'badge' }
      },
      {
        path: 'log',
        name: 'Log',
        component: () => import('@/views/log/index.vue'),
        meta: { title: 'Logs del Sistema', icon: 'description' }
      }
    ]
  },

  // ─────────────────────────────────────────────────
  // 📦 MÓDULO 1
  // ─────────────────────────────────────────────────
  {
    path: '/modulo1',
    component: Layout,
    name: 'Modulo1',
    meta: { title: 'Módulo 1', icon: 'extension', requiresAuth: true },
    children: [
      {
        path: 'pedidos',
        name: 'Modulo1Pedidos',
        component: () => import('@/views/modulo1/index.vue'),
        meta: { title: 'Pedidos', icon: 'shopping_cart' }
      },
      {
        path: 'dclientes',
        name: 'Modulo1DClientes',
        component: () => import('@/views/modulo1/dclientes/index.vue'),
        meta: { title: 'Detalle Clientes', icon: 'person' }
      }
    ]
  },

  // ─────────────────────────────────────────────────
  // 🚨 CATCH-ALL
  // ─────────────────────────────────────────────────
  {
    path: '/:pathMatch(.*)*',
    redirect: '/404',
    meta: { hidden: true }
  }
];

// =====================================================
// CREACIÓN DEL ROUTER
// =====================================================
const router = createRouter({
  history: createWebHashHistory(),
  routes: constantRoutes.concat(asyncRoutes),
  scrollBehavior(to, from, savedPosition) {
    return savedPosition || { top: 0 };
  }
});

export default router;