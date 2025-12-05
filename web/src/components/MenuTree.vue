<template>
  <template v-for="(menu, idx) in menuData" :key="menu.id || menu.path || idx">
    <!-- SUBMENÚ -->
    <el-sub-menu
      v-if="menu.children && menu.children.length > 0"
      :index="getMenuIndex(menu, idx)"
    >
      <template #title>
        <i :class="menu.icon"></i>
        <span>{{ menu.name }}</span>
      </template>

      <!-- Recursión -->
      <menu-tree :menu-data="menu.children" />
    </el-sub-menu>

    <!-- ÍTEM -->
    <el-menu-item
      v-else
      :index="getMenuIndex(menu, idx)"
      :route="menu.path"
      @click="handleMenuClick(menu)"
    >
      <i :class="menu.icon"></i>
      <span>{{ menu.name }}</span>
    </el-menu-item>
  </template>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useStore } from '../store'

defineProps({
  menuData: {
    type: Array,
    required: true,
    default: () => []
  }
})


const router = useRouter()
const store = useStore()

// Siempre devolver un índice STRING válido
const getMenuIndex = (menu, idx) => {
  if (menu.path) return String(menu.path)
  if (menu.name) return String(menu.name)
  if (menu.id) return String(menu.id)
  return `menu-${idx}`
}

const handleMenuClick = (menu) => {
  console.log('🔖 Click en menú:', menu)

  store.addTabAction({
    name: menu.name,
    path: menu.path
  })

  // importante: usar path como índice activo
  store.activeIndexAction(menu.path || menu.name)

  if (menu.path) {
    router.push({ path: menu.path })
  } else {
    console.warn('⚠️ Menú sin path:', menu)
  }
}
</script>

<style scoped>
</style>
