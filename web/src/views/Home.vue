<template>
  <q-page class="home">
    <img alt="Taller 1 logo" src="../assets/image/ems.png">
    <HelloWorld :msg="msg"/>
  </q-page>
</template>

<script setup>
import HelloWorld from '../components/HelloWorld.vue'
import { ref } from 'vue'

const msg = ref('Taller 1')
</script>

<style scoped>
.home {
  text-align: center;
}
</style>
