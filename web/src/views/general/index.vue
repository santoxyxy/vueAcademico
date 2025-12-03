<template>
  <div class="app-container">
    <!-- Barra de búsqueda y filtros -->
    <q-form class="row q-gutter-md items-end" @submit.prevent="handleQuery">
      <q-input
        filled
        label="Búsqueda"
        v-model="queryForm.blurry"
        clearable
        @clear="handleQuery"
        @keyup.enter="handleQuery"
        class="col-3"
      />

      <q-select
        filled
        label="Gestión"
        v-model="queryForm.gestion"
        :options="gestionesDisponibles.map(y => ({ label: y, value: y }))"
        clearable
        @update:model-value="handleQuery"
        class="col-2"
      />

      <div class="col-auto">
        <q-btn color="primary" icon="search" label="Buscar" @click="handleQuery" />
        <q-btn flat icon="refresh" label="Limpiar" @click="resetQuery" class="q-ml-sm" />
      </div>
    </q-form>

    <!-- Botones de acción -->
    <div class="row q-gutter-sm q-my-md">
      <q-btn
        v-if="hasPer('general:add')"
        color="primary"
        icon="add"
        label="Nuevo Registro"
        @click="handleAdd"
      />
      <q-btn
        color="secondary"
        icon="refresh"
        label="Actualizar"
        @click="handleQuery"
      />
    </div>

    <!-- Tabla de datos -->
    <q-table
      :rows="tableData"
      :columns="columns"
      row-key="ids"
      :loading="loading"
      flat
      bordered
      square
      class="shadow-1"
    >
      <template v-slot:body-cell-nickName="props">
        <q-td :props="props">
          <q-chip>{{ props.row.nickName }}</q-chip>
        </q-td>
      </template>

      <template v-slot:body-cell-tipo="props">
        <q-td :props="props">
          <q-chip :color="props.row.tipo === 'DOCENTE' ? 'green' : 'primary'" text-color="white">
            {{ props.row.tipo || 'N/A' }}
          </q-chip>
        </q-td>
      </template>

      <template v-slot:body-cell-gestion="props">
        <q-td :props="props">
          <q-chip color="orange" text-color="white">{{ props.row.gestion }}</q-chip>
        </q-td>
      </template>

      <template v-slot:body-cell-totalMaterias="props">
        <q-td :props="props">
          <q-badge color="green" :label="props.row.totalMaterias" />
        </q-td>
      </template>

      <template v-slot:body-cell-totalNotas="props">
        <q-td :props="props">
          <q-badge color="primary" :label="props.row.totalNotas" />
        </q-td>
      </template>

      <template v-slot:body-cell-enabled="props">
        <q-td :props="props">
          <q-chip :color="props.row.enabled ? 'green' : 'red'" text-color="white">
            {{ props.row.enabled ? 'Activo' : 'Inactivo' }}
          </q-chip>
        </q-td>
      </template>

      <template v-slot:body-cell-operaciones="props">
        <q-td :props="props">
          <q-btn dense color="primary" icon="visibility" label="Detalles" @click="handleDetail(props.row)" />
          <q-btn
            dense
            color="green"
            icon="edit"
            label="Editar"
            v-if="hasPer('general:edit')"
            class="q-ml-sm"
            @click="handleEdit(props.row)"
          />
          <q-btn
            dense
            color="red"
            icon="delete"
            label="Eliminar"
            v-if="hasPer('general:del')"
            class="q-ml-sm"
            @click="handleDelete(props.row)"
          />
        </q-td>
      </template>
    </q-table>

    <!-- Paginación -->
    <q-pagination
      v-if="total > 0"
      v-model="queryForm.currentPage"
      :max="Math.ceil(total / queryForm.size)"
      max-pages="5"
      boundary-numbers
      @update:model-value="getTableData"
      class="q-mt-md"
    />

    <!-- Dialog de Edición/Creación -->
    <edit-general
      v-if="editDialogVisible"
      v-model="editDialogVisible"
      :edit-data="editData"
      @refresh="getTableData"
    />

    <!-- Dialog de Detalles -->
    <detalle-usuario
      v-if="detalleDialogVisible"
      v-model="detalleDialogVisible"
      :usuario-data="selectedUsuario"
    />
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { Dialog, Notify } from 'quasar'
import { queryGeneralTable, delGeneral } from '@/api/general/general'
import { hasPer } from '@/utils/common'
import EditGeneral from './editGeneral.vue'
import DetalleUsuario from './detalleUsuario.vue'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const editDialogVisible = ref(false)
const detalleDialogVisible = ref(false)
const editData = ref(null)
const selectedUsuario = ref(null)

const queryForm = reactive({
  blurry: '',
  gestion: null,
  currentPage: 1,
  size: 10
})

const gestionesDisponibles = computed(() => {
  const currentYear = new Date().getFullYear()
  return Array.from({ length: 5 }, (_, i) => currentYear - i)
})

const columns = [
  { name: 'index', label: '#', field: (row, i) => i + 1, align: 'center', style: 'width: 50px' },
  { name: 'username', label: 'Usuario', field: 'username', align: 'left', style: 'width: 120px' },
  { name: 'nickName', label: 'Nombre', field: 'nickName', align: 'left', style: 'width: 150px' },
  { name: 'nombreCompleto', label: 'Nombre Completo', field: 'nombreCompleto', align: 'left' },
  { name: 'email', label: 'Email', field: 'email', align: 'left' },
  { name: 'tipo', label: 'Tipo', field: 'tipo', align: 'center', style: 'width: 120px' },
  { name: 'gestion', label: 'Gestión', field: 'gestion', align: 'center', style: 'width: 100px' },
  { name: 'totalMaterias', label: 'Materias', field: 'totalMaterias', align: 'center', style: 'width: 90px' },
  { name: 'totalNotas', label: 'Notas', field: 'totalNotas', align: 'center', style: 'width: 90px' },
  { name: 'enabled', label: 'Estado', field: 'enabled', align: 'center', style: 'width: 100px' },
  { name: 'operaciones', label: 'Operaciones', align: 'center', style: 'width: 280px' }
]

const getTableData = async () => {
  loading.value = true
  try {
    const params = { ...queryForm }
    const { data } = await queryGeneralTable(params)
    if (data.success) {
      tableData.value = data.data.records
      total.value = data.data.total
    }
  } catch (error) {
    Notify.create({ type: 'negative', message: 'Error al obtener datos: ' + error.message })
  } finally {
    loading.value = false
  }
}

const handleQuery = () => {
  queryForm.currentPage = 1
  getTableData()
}

const resetQuery = () => {
  queryForm.blurry = ''
  queryForm.gestion = null
  handleQuery()
}

const handleAdd = () => {
  editData.value = null
  editDialogVisible.value = true
}

const handleEdit = (row) => {
  editData.value = { ...row }
  editDialogVisible.value = true
}

const handleDetail = (row) => {
  selectedUsuario.value = { ...row }
  detalleDialogVisible.value = true
}

const handleDelete = async (row) => {
  try {
    await Dialog.create({
      title: 'Confirmar eliminación',
      message: `¿Está seguro de eliminar el registro de ${row.username} en la gestión ${row.gestion}?`,
      cancel: true,
      persistent: true
    })
    const { data } = await delGeneral(row.ids, row.gestion)
    if (data.success) {
      Notify.create({ type: 'positive', message: 'Registro eliminado exitosamente' })
      getTableData()
    } else {
      Notify.create({ type: 'negative', message: data.msg })
    }
  } catch (error) {
    // Cancelado o error
  }
}

onMounted(() => {
  getTableData()
})
</script>

<style scoped>
.app-container {
  padding: 20px;
}
</style>
