<template>
  <div class="app-container">
    <!-- Barra de búsqueda -->
    <div class="searchDiv">
      <q-input
        dense
        outlined
        v-model="state.blurry"
        placeholder="Buscar por nombre de detalle"
        clearable
        @clear="getDmodalidadListFun"
        @keyup.enter="getDmodalidadListFun"
        class="searchInput"
      >
        <template v-slot:append>
          <q-icon name="search" @click="getDmodalidadListFun" class="cursor-pointer" />
        </template>
      </q-input>

      <q-btn
        color="positive"
        icon="add"
        label="Nuevo"
        dense
        @click="addDmodalidad"
        v-if="hasPer(['ROLE_ADMIN'])"
      />
    </div>

    <!-- Tabla -->
    <q-table
      flat
      bordered
      :rows="state.tableData"
      row-key="coddm"
      :loading="state.loading"
      :columns="columns"
      hide-bottom
      wrap-cells
    >
      <!-- Estado -->
      <template v-slot:body-cell-estado="props">
        <q-td :props="props" class="text-center">
          <q-badge
            :color="props.row.estado === 1 ? 'green' : 'red'"
            :label="props.row.estado === 1 ? 'Activo' : 'Inactivo'"
            align="center"
            rounded
            dense
          />
        </q-td>
      </template>

      <!-- Operaciones -->
      <template v-slot:body-cell-actions="props">
        <q-td :props="props" class="text-center">
          <q-btn
            flat
            dense
            color="primary"
            icon="edit"
            label="Editar"
            @click="editDmodalidad(props.row)"
            v-if="hasPer(['ROLE_ADMIN', 'ROLE_DOCENTE'])"
          />
          <q-btn
            flat
            dense
            color="negative"
            icon="delete"
            label="Eliminar"
            @click="confirmDelete(props.row.coddm)"
            v-if="hasPer(['ROLE_ADMIN'])"
          />
        </q-td>
      </template>
    </q-table>

    <!-- Paginación -->
    <q-pagination
      v-model="state.current"
      :max="Math.ceil(state.total / state.size)"
      max-pages="7"
      boundary-numbers
      color="primary"
      class="q-mt-md"
      @update:model-value="getDmodalidadListFun"
    />

    <!-- Dialog de edición -->
    <EditDmodalidad
      v-model:dialogVisible="dialogVisible"
      :dmodalidadObj="currentDmodalidad"
      @get-list="getDmodalidadListFun"
    />
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { queryDmodalidadTable, delDmodalidad as delDmodalidadApi } from '@/api/dmodalidad/dmodalidad'
import { errorMsg, successMsg } from '@/utils/message'
import { hasPer } from '@/utils/common'
import EditDmodalidad from './editDmodalidad.vue'
import { Dialog } from 'quasar'

const state = reactive({
  blurry: '',
  tableData: [],
  current: 1,
  size: 10,
  total: 0,
  loading: false
})

const dialogVisible = ref(false)
const currentDmodalidad = ref({})

const columns = [
  { name: 'index', label: '#', align: 'center', field: (_, index) => index + 1, sortable: false },
  { name: 'nombre', label: 'Nombre Detalle', field: 'nombre', sortable: true },
  { name: 'nombreModalidad', label: 'Modalidad Padre', field: 'nombreModalidad', sortable: true },
  { name: 'estado', label: 'Estado', field: 'estado', sortable: false, align: 'center' },
  { name: 'actions', label: 'Operaciones', field: 'actions', sortable: false, align: 'center' }
]

// Obtener lista
const getDmodalidadListFun = (obj) => {
  if (obj && obj.page) state.current = obj.page
  if (obj && obj.limit) state.size = obj.limit

  state.loading = true
  const params = {
    blurry: state.blurry,
    size: state.size,
    currentPage: state.current
  }

  queryDmodalidadTable(params)
    .then((res) => {
      state.loading = false
      if (res.success) {
        state.tableData = res.data.records || []
        state.total = res.data.total || 0
      } else {
        errorMsg(res.msg)
      }
    })
    .catch(() => {
      state.loading = false
    })
}

// Agregar nuevo
const addDmodalidad = () => {
  currentDmodalidad.value = {}
  dialogVisible.value = true
}

// Editar
const editDmodalidad = (row) => {
  currentDmodalidad.value = { ...row }
  dialogVisible.value = true
}

// Confirmar eliminación
const confirmDelete = (id) => {
  Dialog.create({
    title: 'Confirmar eliminación',
    message: '¿Está seguro de eliminar este detalle de modalidad?',
    cancel: true,
    persistent: true,
    ok: { label: 'Sí, eliminar', color: 'negative' },
    
  }).onOk(() => {
    delDmodalidadApi(id).then((res) => {
      if (res.success) {
        successMsg(res.data)
        getDmodalidadListFun()
      } else {
        errorMsg(res.msg)
      }
    })
  })
}

onMounted(() => {
  getDmodalidadListFun()
})
</script>

<style scoped>
.app-container {
  padding: 20px;
}

.searchDiv {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.searchInput {
  width: 300px;
}
</style>
