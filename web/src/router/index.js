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
// =====================================================
const ROUTE_CORRECTIONS = {
  // Configuración
  '/items': '/configuracion/items',
  '/paralelo': '/configuracion/paralelo',
  '/niveles': '/configuracion/niveles',
  '/modalidad': '/configuracion/modalidad',
  '/dmodalidad': '/configuracion/dmodalidad',
  '/itemat': '/configuracion/itemat',
  
  // Gestión Académica
  '/menu': '/gestion-academica/general',
  '/general': '/gestion-academica/general',
  '/materia': '/gestion-academica/materia',
  '/progra': '/gestion-academica/progra',
  '/dicta': '/gestion-academica/dicta',
  '/mapa': '/gestion-academica/mapa',
  '/notas': '/gestion-academica/notas',
  
  // Usuarios
  '/clientes': '/usuarios/clientes',
  '/usuario': '/usuarios/gestion',
  '/user': '/usuarios/sistema',
  
  // Sistema
  '/role': '/sistema/role',
  '/log': '/sistema/log',
  '/menu-config': '/sistema/menu',
  
  // Módulo 1
  '/modulo1': '/modulo1/pedidos',
  '/dclientes': '/modulo1/dclientes'
};

// =====================================================
// NORMALIZAR RUTAS DEL BACKEND
// =====================================================
function normalizeRoute(path) {
  if (!path) return path;
  
  // Si la ruta ya está en formato correcto, devolverla
  if (path.includes('/configuracion/') || 
      path.includes('/gestion-academica/') || 
      path.includes('/usuarios/') ||
      path.includes('/sistema/') ||
      path.includes('/modulo1/')) {
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
    
    // Redirigir de login a home si ya está autenticado
    if (to.path === '/login') {
      next({ path: '/home' });
      return;
    }

    // ─────────────────────────────────────────────────
    // CARGAR MENÚS SI NO ESTÁN CARGADOS
    // ─────────────────────────────────────────────────
    if (!store.isLoadMenu) {
      loadMenus(next, to);
      return;
    }

    // ─────────────────────────────────────────────────
    // VERIFICAR SI LA RUTA EXISTE
    // ─────────────────────────────────────────────────
    if (!hashRoute(to)) {
      console.log('⚠️ Ruta no encontrada, agregando rutas dinámicas...');
      addRoute();
      
      // Verificar nuevamente después de agregar rutas
      if (hashRoute(to)) {
        console.log('✅ Ruta encontrada después de agregar');
        next({ ...to, replace: true });
      } else {
        console.log('❌ Ruta no existe, redirigiendo a 404');
        next({ path: '/404' });
      }
      return;
    }

    // Ruta válida, continuar
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
        // Normalizar rutas antes de guardar
        const normalizedMenus = normalizeMenuPaths(res.data);
        
        store.routerAction(normalizedMenus);
        store.loadMenuAction(true);
        
        // Agregar rutas dinámicas
        addRoute();
        
        next({ ...to, replace: true });
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
// NORMALIZAR PATHS DE MENÚS
// =====================================================
function normalizeMenuPaths(menus) {
  return menus.map(menu => {
    const normalizedMenu = { ...menu };
    
    // Corregir ruta principal
    if (normalizedMenu.path) {
      normalizedMenu.path = normalizeRoute(normalizedMenu.path);
    }
    
    // Corregir rutas de hijos recursivamente
    if (normalizedMenu.children && normalizedMenu.children.length > 0) {
      normalizedMenu.children = normalizeMenuPaths(normalizedMenu.children);
    }
    
    return normalizedMenu;
  });
}

// =====================================================
// VERIFICAR SI UNA RUTA EXISTE
// =====================================================
export function hashRoute(to) {
  const found = router.getRoutes().find(route => route.path === to.path);
  const exists = !!found;
  
  if (!exists) {
    console.log('🔍 Ruta no encontrada:', to.path);
    console.log('📝 Rutas disponibles:', router.getRoutes().map(r => r.path));
  }
  
  return exists;
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

  routers.forEach(item => {
    if (!item.path) {
      console.warn('⚠️ Ruta sin path:', item);
      return;
    }

    try {
      // Normalizar el path del componente
      let componentPath = item.component;
      
      if (componentPath) {
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
      }
    } catch (error) {
      console.error(`❌ Error registrando ruta ${item.path}:`, error);
    }
  });

  console.log('✅ Rutas dinámicas agregadas');
  console.log('📋 Total de rutas:', router.getRoutes().length);
}

// =====================================================
// EXPORT ROUTER
// =====================================================
export default router;