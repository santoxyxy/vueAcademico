<template>
  <div class="app-container">
    <!-- Barra de búsqueda -->
    <div class="searchDiv">
      <q-input
        dense
        outlined
        v-model="state.blurry"
        label="Buscar por nombre de modalidad"
        clearable
        class="searchInput"
        @keyup.enter="getModalidadListFun"
        @clear="getModalidadListFun"
      />
      <q-btn
        dense
        color="primary"
        icon="search"
        label="Consultar"
        @click="getModalidadListFun"
      />
      <q-btn
        dense
        color="secondary"
        icon="add"
        label="Nueva Modalidad"
        v-if="hasPer(['ROLE_ADMIN'])"
        @click="addModalidad"
      />
    </div>

    <!-- Tabla -->
    <q-table
      flat
      bordered
      :rows="state.tableData"
      :columns="columns"
      row-key="codm"
      :loading="state.loading"
      row-height="40"
      class="shadow-0"
    >
      <!-- Estado -->
      <template v-slot:body-cell-estado="props">
        <q-chip
          dense
          :color="props.row.estado === 1 ? 'green' : 'red'"
          text-color="white"
          outline
        >
          {{ props.row.estado === 1 ? 'Activo' : 'Inactivo' }}
        </q-chip>
      </template>

      <!-- Operaciones -->
      <template v-slot:body-cell-option="props">
        <q-btn
          dense
          color="primary"
          size="sm"
          icon="edit"
          label="Editar"
          v-if="hasPer(['ROLE_ADMIN', 'ROLE_DOCENTE'])"
          @click="editModalidad(props.row)"
        />
        <q-btn
          dense
          color="negative"
          size="sm"
          icon="delete"
          label="Eliminar"
          class="q-ml-sm"
          v-if="hasPer(['ROLE_ADMIN'])"
          @click="delModalidad(props.row.codm)"
        />
      </template>
    </q-table>

    <!-- Paginación -->
    <q-pagination
      v-model="state.current"
      :max="Math.ceil(state.total / state.size)"
      max-pages="7"
      color="primary"
      @update:model-value="getModalidadListFun"
      class="q-mt-md"
    />

    <!-- Dialog de edición -->
    <EditModalidad
      v-model:dialogVisible="dialogVisible"
      :modalidadObj="currentModalidad"
      @get-list="getModalidadListFun"
    />
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { queryModalidadTable, delModalidad as delModalidadApi } from '@/api/modalidad/modalidad'
import { errorMsg, successMsg } from '@/utils/message'
import { hasPer } from '@/utils/common'
import EditModalidad from './editModalidad.vue'

const state = reactive({
  blurry: '',
  tableData: [],
  current: 1,
  size: 10,
  total: 0,
  loading: false
})

const dialogVisible = ref(false)
const currentModalidad = ref({})

// Columnas para q-table
const columns = [
  { name: 'index', label: '#', field: (row, index) => index + 1, align: 'center', style: 'width: 60px' },
  { name: 'codm', label: 'Código', field: 'codm', align: 'center', style: 'width: 120px' },
  { name: 'nombre', label: 'Nombre', field: 'nombre' },
  { name: 'estado', label: 'Estado', field: 'estado', align: 'center', style: 'width: 120px' },
  { name: 'option', label: 'Operaciones', field: 'option', align: 'center', style: 'width: 200px' }
]

// Obtener lista
const getModalidadListFun = (obj) => {
  if (obj && obj.page) state.current = obj.page
  state.loading = true
  const params = {
    blurry: state.blurry,
    size: state.size,
    currentPage: state.current
  }

  queryModalidadTable(params)
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
const addModalidad = () => {
  currentModalidad.value = {}
  dialogVisible.value = true
}

// Editar
const editModalidad = (row) => {
  currentModalidad.value = { ...row }
  dialogVisible.value = true
}

// Eliminar
const delModalidad = (id) => {
  import('quasar').then(({ Dialog }) => {
    Dialog.create({
      title: 'Confirmar',
      message: '¿Confirmar eliminación?',
      ok: { label: 'Sí', color: 'primary' },
      cancel: { label: 'Cancelar', color: 'grey-5' },
      color: 'warning'
    }).onOk(() => {
      delModalidadApi(id).then((res) => {
        if (res.success) {
          successMsg(res.data)
          getModalidadListFun()
        } else {
          errorMsg(res.msg)
        }
      })
    })
  })
}

onMounted(() => {
  getModalidadListFun()
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
  flex-wrap: wrap;
}

.searchInput {
  width: 300px;
}
</style>
