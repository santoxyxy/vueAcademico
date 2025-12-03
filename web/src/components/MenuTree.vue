<template>
  <template v-for="menu in menuData" :key="menu.id">
    <!-- Si tiene hijos, es un submenú -->
    <el-sub-menu 
      v-if="menu.children && menu.children.length > 0" 
      :index="menu.path"
    >
      <template #title>
        <i :class="menu.icon"></i>
        <span>{{ menu.name }}</span>
      </template>

      <!-- Recursión para hijos -->
      <menu-tree :menu-data="menu.children"></menu-tree>
    </el-sub-menu>

    <!-- Si no tiene hijos, es un item simple -->
    <el-menu-item 
      v-else
      :index="menu.path"
      :route="menu.path"
      @click="handleMenuClick(menu)"
    >
      <i :class="menu.icon"></i>
      <template #title>{{ menu.name }}</template>
    </el-menu-item>
  </template>
</template>

<script setup>
import { useRouter } from 'vue-router';
import { useStore } from '../store';

// eslint-disable-next-line no-unused-vars
const props = defineProps({
  menuData: {
    type: Array,
    required: true,
    default: () => []
  }
});

const router = useRouter();
const store = useStore();

const handleMenuClick = (menu) => {
  console.log('🔖 Click en menú:', menu);
  
  // Agregar tab al store
  store.addTabAction({ 
    name: menu.name, 
    path: menu.path 
  });
  
  // Actualizar índice activo
  store.activeIndexAction(menu.name);
  
  // Navegar
  router.push({ path: menu.path });
};
</script>

<style scoped>
</style>