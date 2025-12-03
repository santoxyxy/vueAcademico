<template>
  <q-page padding class="q-pa-md">
    <!-- Filtros de búsqueda -->
    <div class="row q-col-gutter-sm q-mb-md" style="flex-wrap: wrap; align-items: center;">
      <q-input
        dense
        outlined
        v-model="state.blurry"
        label="Buscar por materia o docente"
        clearable
        style="width: 250px"
        @keyup.enter="getPrograListFun"
      />

      <q-select
        dense
        outlined
        v-model="state.gestion"
        :options="gestiones"
        label="Gestión"
        clearable
        style="width: 120px"
      />

      <q-input
        dense
        outlined
        v-model="state.codmat"
        label="Código Materia"
        clearable
        style="width: 150px"
      />

      <q-input
        dense
        outlined
        type="number"
        v-model.number="state.codn"
        label="Nivel"
        min="1"
        max="10"
        style="width: 120px"
      />

      <q-btn
        dense
        color="primary"
        icon="search"
        label="Buscar"
        @click="getPrograListFun"
      />

      <q-btn
        dense
        color="secondary"
        icon="add"
        label="Nueva Clase"
        v-if="hasPer(['ROLE_ADMIN'])"
        @click="addProgra"
      />
    </div>

    <!-- Tabla -->
    <q-table
      title="Lista de Clases"
      :rows="state.tableData"
      :columns="columns"
      row-key="codmat"
      flat
      bordered
      :loading="state.loading"
      class="q-mb-md"
    >
      <template v-slot:body-cell-tipoDocente="props">
        <q-chip :color="getDocenteTagType(props.row.tipoDocente)" text-color="white" dense>
          {{ props.row.tipoDocente }}
        </q-chip>
      </template>

      <template v-slot:body-cell-cantidadEstudiantes="props">
        <q-chip color="info" text-color="white" dense>
          {{ props.row.cantidadEstudiantes || 0 }}
        </q-chip>
      </template>

      <template v-slot:body-cell-operaciones="props">
        <div class="row items-center justify-center q-gutter-sm">
          <q-btn
            dense
            color="primary"
            icon="edit"
            label="Editar"
            size="sm"
            v-if="hasPer(['ROLE_ADMIN', 'ROLE_DOCENTE'])"
            @click="editProgra(props.row)"
          />
          <q-btn
            dense
            color="negative"
            icon="delete"
            label="Eliminar"
            size="sm"
            v-if="hasPer(['ROLE_ADMIN'])"
            @click="delProgra(props.row)"
          />
        </div>
      </template>
    </q-table>

    <!-- Paginación -->
    <Pagination
      :total="state.total"
      :page="state.current"
      :limit="state.size"
      @pagination="getPrograListFun"
    />

    <!-- Dialog -->
    <EditProgra
      v-model:dialog-visible="dialogVisible"
      :prograObj="currentProgra"
      @get-list="getPrograListFun"
    />
  </q-page>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { queryPrograTable, delProgra as delPrograApi } from '@/api/progra/progra'
import { errorMsg, successMsg } from '@/utils/message'
import { hasPer } from '@/utils/common'
import Pagination from '@/components/Pagination.vue'
import EditProgra from './editProgra.vue'

const state = reactive({
  blurry: '',
  gestion: new Date().getFullYear(),
  codmat: '',
  codn: null,
  tableData: [],
  current: 1,
  size: 10,
  total: 0,
  loading: false
})

const dialogVisible = ref(false)
const currentProgra = ref({})

// Generar últimos 5 años para el select
const gestiones = Array.from({ length: 5 }, (_, i) => new Date().getFullYear() - i)

// Columnas de la tabla
const columns = [
  { name: 'index', label: '#', field: (row, index) => index + 1, align: 'center', style: 'width: 60px;' },
  { name: 'gestion', label: 'Gestión', field: 'gestion', align: 'center', style: 'width: 100px;' },
  { name: 'codmat', label: 'Código', field: 'codmat', style: 'width: 100px;' },
  { name: 'nombreMateria', label: 'Materia', field: 'nombreMateria', style: 'min-width: 200px;' },
  { name: 'nombreNivel', label: 'Nivel', field: 'nombreNivel', style: 'width: 100px;' },
  { name: 'nombreParalelo', label: 'Paralelo', field: 'nombreParalelo', align: 'center', style: 'width: 100px;' },
  { name: 'nombreDocente', label: 'Docente', field: 'nombreDocente', style: 'min-width: 180px;' },
  { name: 'tipoDocente', label: 'Tipo', field: 'tipoDocente', style: 'width: 100px;' },
  { name: 'cantidadEstudiantes', label: 'Estudiantes', field: 'cantidadEstudiantes', align: 'center', style: 'width: 110px;' },
  { name: 'operaciones', label: 'Operaciones', field: 'operaciones', align: 'center', style: 'width: 180px;' }
]

const getPrograListFun = (obj) => {
  if (obj?.page) state.current = obj.page
  if (obj?.limit) state.size = obj.limit

  state.loading = true
  const params = {
    blurry: state.blurry,
    gestion: state.gestion,
    codmat: state.codmat,
    codn: state.codn,
    size: state.size,
    currentPage: state.current
  }

  queryPrograTable(params)
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

const addProgra = () => {
  currentProgra.value = { gestion: state.gestion }
  dialogVisible.value = true
}

const editProgra = (row) => {
  currentProgra.value = { ...row }
  dialogVisible.value = true
}

const delProgra = (row) => {
  const params = {
    codpar: row.codpar,
    codp: row.codp,
    codmat: row.codmat,
    gestion: row.gestion
  }

  delPrograApi(params).then((res) => {
    if (res.success) {
      successMsg(res.data)
      getPrograListFun()
    } else {
      errorMsg(res.msg)
    }
  })
}

const getDocenteTagType = (tipo) => {
  const types = {
    'Titular': 'positive',
    'Invitado': 'warning',
    'Auxiliar': 'info'
  }
  return types[tipo] || ''
}

onMounted(() => {
  getPrograListFun()
})
</script>

<style scoped>
</style>
