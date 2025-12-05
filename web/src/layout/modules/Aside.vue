<template>
  <el-menu
    :default-active="defaultActive"
    :unique-opened="true"
    router
    :collapse="isCollapse"
    background-color="#545c64"
    text-color="#fff"
  >
    <div class="logo" @click="changeCollapse">
      <el-image :src="require('../../assets/image/ems.png')" style="width: 40px;" />
    </div>

    <!-- Página de inicio fija -->
    <el-menu-item
      route="/home"
      index="/home"
      @click="openTab('Inicio', '/home')"
    >
      <i class="iconfont icon-home"></i>
      <span>Inicio</span>
    </el-menu-item>

    <!-- Menús dinámicos del backend -->
    <menu-tree :menu-data="menuList" />
  </el-menu>
</template>

<script setup>
import { useStore } from "../../store";
import { getMenuTree, getPermission } from "../../api/menu/sysMenu";
import { errorMsg } from "../../utils/message";
import { computed, onMounted, ref } from "vue";
import MenuTree from "../../components/MenuTree.vue";

const store = useStore();
const emit = defineEmits(["update:width"]);

const menuList = ref([]);
const isCollapse = ref(false);

// índice activo del menú: usamos el PATH
const defaultActive = computed(() => store.activeIndex);

// Mapa de correcciones (igual que en router/index.js)
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
  '/menu': '/sistema/menu',          // 👈 GMenu ahora va a Gestión de Menús
  '/menu-config': '/sistema/menu',
  '/role': '/sistema/role',
  '/log': '/sistema/log',

  // Módulo 1
  '/modulo1': '/modulo1/pedidos',
  '/dclientes': '/modulo1/dclientes'
};

const normalizePath = (path) => {
  if (!path) return path;
  if (ROUTE_CORRECTIONS[path]) {
    return ROUTE_CORRECTIONS[path];
  }
  return path;
};

const fixMenuPaths = (menus) => {
  return menus.map((m) => {
    if (m.path) {
      const original = m.path;
      m.path = normalizePath(m.path);
      if (original !== m.path) {
        console.log(`🔄 Ruta de menú corregida: ${original} → ${m.path}`);
      }
    }

    if (m.children && m.children.length > 0) {
      m.children = fixMenuPaths(m.children);
    }
    return m;
  });
};

onMounted(() => {
  getMenuTree().then((res) => {
    if (res.success) {
      menuList.value = fixMenuPaths(res.data);
    }
  });

  getPermission().then((res) => {
    if (res.success) {
      store.permissionAction(res.data);
    } else {
      errorMsg(res.msg);
    }
  });
});

const openTab = (name, path) => {
  store.addTabAction({ name, path });
  // usar PATH como índice activo (coincide con el index del menú)
  store.activeIndexAction(path || name);
};

const changeCollapse = () => {
  isCollapse.value = !isCollapse.value;
  emit("update:width", isCollapse.value ? "64px" : "230px");
};
</script>

<style scoped>
.el-menu {
  height: 100%;
}
.logo {
  display: flex;
  justify-content: center;
  align-items: center;
  color: white;
  font-size: 20px;
  height: 60px;
  cursor: pointer;
}
</style>
