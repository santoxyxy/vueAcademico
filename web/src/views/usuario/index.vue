<template>
  <div class="app-container">

    <q-table
      :rows="state.tableData"
      :columns="computedColumns"
      row-key="idusuario"
      flat
      bordered
      separator="horizontal"
    >

      <!-- 🔷 Encabezado personalizado -->
      <template v-slot:top>
        <div class="row items-center full-width q-pa-sm">
          <div class="text-h6">Listar Usuarios</div>

          <q-btn
            color="primary"
            icon="add"
            label="usuario"
            class="q-ml-auto"
            @click="onEdit"
          />
        </div>
      </template>

    </q-table>

    <!-- 🔻 Paginación existente -->
    <Pagination 
      :total="state.total"
      :page="state.current"
      :limit="state.size"
      @pagination="queryUsuarioTableFun"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from "vue"
import { queryUsuarioTable } from "../../api/usuario/usuario"
import { errorMsg } from "../../utils/message"
import Pagination from "../../components/Pagination.vue"

// 🔹 Estado
const state = reactive({
  blurry: "",
  tableData: [],
  current: 1,
  size: 10,
  total: 0,
})

// 🔹 Columnas configuradas manualmente
const columns = [
  { name: "idusuario", label: "Id Usuario", field: "idusuario", align: "center", sortable: true },
  { name: "nombre", label: "Nombre", field: "nombre", align: "left", sortable: true },
  { name: "ap", label: "Apellido Paterno", field: "ap", align: "left", sortable: true },
  { name: "am", label: "Apellido Materno", field: "am", align: "center", sortable: true },
]

// Solo se muestran estas columnas
const visibleColumns = ref(["idusuario", "nombre"])

// 🔹 Se filtra la lista original de columnas en base a visibleColumns
const computedColumns = computed(() =>
  columns.filter(col => visibleColumns.value.includes(col.name))
)

// 🔹 Lógica de obtener usuarios
const queryUsuarioTableFun = (obj) => {
  if (obj?.page) state.current = obj.page
  if (obj?.limit) state.size = obj.limit

  const params = {
    blurry: state.blurry,
    size: state.size,
    currentPage: state.current,
  }

  queryUsuarioTable(params)
    .then((res) => {
      if (res.success) {
        state.tableData = res.data.records
        state.total = res.data.total
      } else {
        errorMsg(res.msg)
      }
    })
    .catch(() => errorMsg("Error al cargar los datos"))
}

// Abrir modal de edición
const onEdit = () => {
  console.log("NUEVO USUARIO")
  // Aquí abrirías el diálogo QDialog
}

onMounted(() => {
  queryUsuarioTableFun()
})
</script>

<style scoped>
.app-container {
  padding: 20px;
}
</style>
