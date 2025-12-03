<template>
  <el-container>
    <el-aside :width="width">
      <Aside :width="width" @update:width="width = $event"></Aside>
    </el-aside>
    <el-container>
      <el-header>
        <Header></Header>
      </el-header>
      <el-main>
        <Main></Main>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import Header from "./modules/Header";
import Main from "./modules/Main";
import Aside from "./modules/Aside";
import { useStore } from "../store";
import { useRouter } from "vue-router";
import { onBeforeMount, ref } from "vue";

const store = useStore();
const router = useRouter();

onBeforeMount(() => {
  // Después de actualizar la página, borre todas las pestañas abiertas
  store.clearTabAction();
  
  // Luego guarde la página de inicio en la lista de pestañas abiertas
  store.addTabAction({ path: '/home', name: 'home' });
  
  // Y diseñar la página de inicio como un valor de activación
  store.activeIndexAction('home');
  
  // Finalmente abre la página de inicio
  router.push({ path: '/home' });
});

const width = ref('230px');
</script>

<style scoped>
.el-aside {
  height: calc(100vh);
}
.el-header {
  display: flex;
  align-items: center;
}
.el-main {
  padding: 0;
}
</style>