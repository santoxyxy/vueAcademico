import router from "./routers";
import { useStore } from "../store";
import { queryAllMenu } from "../api/menu/sysMenu";
import { errorMsg } from "../utils/message";

// =====================================================
// LISTA BLANCA DE RUTAS (no requieren autenticación)
// =====================================================
const whiteList = ['/login', '/401', '/404'];

// =====================================================
// MAPA DE CORRECCIÓN DE RUTAS
// (path que viene del backend → path real del front)
// =====================================================
const ROUTE_CORRECTIONS = {
  // Configuración
  '/items': '/configuracion/items',
  '/paralelo': '/configuracion/paralelo',
  '/niveles': '/configuracion/niveles',
  '/modalidad': '/configuracion/modalidad',
  '/dmodalidad': '/configuracion/dmodalidad',
  '/itemat': '/configuracion/itemat',
  '/clientes': '/configuracion/clientes',
  
  // Gestión Académica
  '/general': '/gestion-academica/general',
  '/materia': '/gestion-academica/materia',
  '/progra': '/gestion-academica/progra',
  '/dicta': '/gestion-academica/dicta',
  '/mapa': '/gestion-academica/mapa',
  '/notas': '/gestion-academica/notas',
  
  // Usuarios
  '/usuario': '/usuarios/gestion',
  '/user': '/usuarios/sistema',
  
  // Sistema
  '/menu': '/sistema/menu',        // 👈 AQUÍ ESTÁ LA CLAVE PARA GMENU
  '/menu-config': '/sistema/menu', // por si el backend manda este
  '/role': '/sistema/role',
  '/log': '/sistema/log',
  
  // Módulo 1
  '/modulo1': '/modulo1/pedidos',
  '/dclientes': '/modulo1/dclientes'
};

// =====================================================
// NORMALIZAR RUTAS DEL BACKEND
// =====================================================
function normalizeRoute(path) {
  if (!path) return path;
  
  // Si la ruta ya está en formato "bueno", devolverla
  if (
    path.includes('/configuracion/') || 
    path.includes('/gestion-academica/') || 
    path.includes('/usuarios/') ||
    path.includes('/sistema/') ||
    path.includes('/modulo1/')
  ) {
    return path;
  }
  
  // Aplicar correcciones conocidas
  if (ROUTE_CORRECTIONS[path]) {
    return ROUTE_CORRECTIONS[path];
  }
  
  return path;
}

// =====================================================
// GUARDIA DE NAVEGACIÓN
// =====================================================
router.beforeEach((to, from, next) => {
  console.log('🚀 Navegando a:', to.path);
  
  const store = useStore();

  // ─────────────────────────────────────────────────
  // CASO 1: Usuario autenticado
  // ─────────────────────────────────────────────────
  if (store.token) {
    
    // Si ya está logueado y va a /login → redirigir a /home
    if (to.path === '/login') {
      next({ path: '/home' });
      return;
    }

    // Cargar menús solo una vez
    if (!store.isLoadMenu) {
      loadMenus(next, to);
      return;
    }

    // Menús ya cargados → seguir normal
    next();
    return;
  }

  // ─────────────────────────────────────────────────
  // CASO 2: Usuario NO autenticado
  // ─────────────────────────────────────────────────
  if (whiteList.includes(to.path)) {
    next();
  } else {
    next({ path: '/login' });
  }
});

// =====================================================
// CARGAR MENÚS DEL BACKEND
// =====================================================
export function loadMenus(next, to) {
  const store = useStore();
  
  queryAllMenu()
    .then(res => {
      console.log("📋 Menús del backend:", res.data);
      
      if (res.success && res.data && res.data.length > 0) {
        // Normalizar rutas antes de guardar en el store
        const normalizedMenus = normalizeMenuPaths(res.data);
        
        store.routerAction(normalizedMenus);
        store.loadMenuAction(true);
        
        // Agregar rutas dinámicas
        addRoute();
        
        // Redirigir correctamente
        if (to.path === '/home' || to.path === '/') {
          next({ path: '/home' });
        } else {
          next({ ...to, replace: true });
        }
      } else {
        errorMsg('No se encontraron menús para el usuario');
        next({ path: '/login' });
      }
    })
    .catch(err => {
      console.error('❌ Error cargando menús:', err);
      errorMsg('Error al cargar menús');
      next({ path: '/login' });
    });
}

// =====================================================
// NORMALIZAR PATHS DE MENÚS (estructura completa)
// =====================================================
function normalizeMenuPaths(menus) {
  return menus.map(menu => {
    const normalizedMenu = { ...menu };
    
    // Corregir ruta principal
    if (normalizedMenu.path) {
      const originalPath = normalizedMenu.path;
      normalizedMenu.path = normalizeRoute(normalizedMenu.path);
      
      if (originalPath !== normalizedMenu.path) {
        console.log(`🔄 Ruta corregida: ${originalPath} → ${normalizedMenu.path}`);
      }
    }
    
    // Corregir rutas de hijos recursivamente
    if (normalizedMenu.children && normalizedMenu.children.length > 0) {
      normalizedMenu.children = normalizeMenuPaths(normalizedMenu.children);
    }
    
    return normalizedMenu;
  });
}

// =====================================================
// AGREGAR RUTAS DINÁMICAS
// =====================================================
export function addRoute() {
  const store = useStore();
  const routers = store.routers;

  if (!routers || routers.length === 0) {
    console.log('⚠️ No hay rutas para agregar');
    return;
  }

  console.log('➕ Agregando rutas dinámicas:', routers);

  function processMenus(menuList) {
    menuList.forEach(item => {
      if (!item.path) {
        console.warn('⚠️ Ruta sin path:', item);
        return;
      }

      if (item.component) {
        try {
          let componentPath = item.component;
          
          // Asegurar extensión .vue
          if (!componentPath.endsWith('.vue')) {
            componentPath = componentPath + '.vue';
          }

          // Remover prefijo /views/ si existe
          componentPath = componentPath.replace(/^\/views\//, '');

          console.log(`📄 Registrando ruta: ${item.path} -> ${componentPath}`);

          router.addRoute('LayoutView', {
            path: item.path,
            name: item.name || item.path.replace(/\//g, '-'),
            component: () => import(`@/views/${componentPath}`),
            meta: {
              title: item.title || item.name,
              icon: item.icon,
              permission: item.permission
            }
          });
        } catch (error) {
          console.error(`❌ Error registrando ruta ${item.path}:`, error);
        }
      }

      if (item.children && item.children.length > 0) {
        processMenus(item.children);
      }
    });
  }

  processMenus(routers);

  console.log('✅ Rutas dinámicas agregadas');
  console.log('📋 Total de rutas:', router.getRoutes().length);
  console.log('📋 Rutas registradas:', router.getRoutes().map(r => r.path));
}

export default router;
