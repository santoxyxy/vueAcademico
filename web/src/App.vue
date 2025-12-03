<template>
  <q-layout view="lHh Lpr lFf">
    <!-- Header opcional -->
    <q-header elevated>
      <q-toolbar>
        <q-toolbar-title>
          Mi Aplicación
        </q-toolbar-title>
      </q-toolbar>
    </q-header>

    <!-- Contenedor de páginas -->
    <q-page-container>
      <router-view />
    </q-page-container>

    <!-- Footer opcional -->
    <q-footer elevated>
      <div class="text-center">© 2025 Taller</div>
    </q-footer>
  </q-layout>
</template>

<script setup>
</script>

<style>
/* Puedes dejar estilos globales aquí */
</style>
