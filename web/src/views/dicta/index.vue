<template>
  <div class="app-container">
    <!-- Filtros de búsqueda -->
    <div class="searchDiv">
      <q-input
        v-model="state.blurry"
        placeholder="Buscar por materia o docente"
        clearable
        style="width: 250px"
        @keyup.enter="getDictaListFun"
      />

      <q-select
        v-model="state.gestion"
        :options="gestiones"
        placeholder="Gestión"
        clearable
        style="width: 120px"
      />

      <q-input
        v-model="state.codmat"
        placeholder="Código Materia"
        clearable
        style="width: 150px"
      />

      <q-input
        v-model.number="state.codn"
        type="number"
        placeholder="Nivel"
        :min="1"
        :max="10"
        style="width: 120px"
      />

      <q-btn
        color="primary"
        icon="search"
        label="Buscar"
        @click="getDictaListFun"
      />

      <!-- Botón nueva asignación -->
      <q-btn
        color="positive"
        icon="add"
        label="Nueva Asignación"
        @click="addDicta"
        v-if="hasPer(['ROLE_ADMIN'])"
        class="q-ml-sm"
      />
    </div>

    <!-- Tabla -->
    <q-table
      title="Asignaciones"
      :rows="state.tableData"
      :columns="columns"
      row-key="codmat"
      :loading="state.loading"
      flat
      bordered
      dense
      virtual-scroll
    >
      <!-- Periodo -->
      <template v-slot:body-cell-codp="props">
        <q-td :props="props">
          <q-chip
            :color="props.row.codp === 1 ? 'green' : 'orange'"
            text-color="white"
            dense
            outline
          >
            {{ props.row.codp }}° Periodo
          </q-chip>
        </q-td>
      </template>

      <!-- Tipo de docente -->
      <template v-slot:body-cell-tipoDocente="props">
        <q-td :props="props">
          <q-chip
            :color="getDocenteTagType(props.row.tipoDocente)"
            text-color="white"
            dense
            outline
          >
            {{ props.row.tipoDocente }}
          </q-chip>
        </q-td>
      </template>

      <!-- Email docente (botón mailto) -->
      <template v-slot:body-cell-emailDocente="props">
        <q-td :props="props">
          <q-btn
            :label="props.row.emailDocente"
            color="primary"
            flat
            dense
            :href="'mailto:' + props.row.emailDocente"
            target="_blank"
          />
        </q-td>
      </template>

      <!-- Acciones -->
      <template v-slot:body-cell-actions="props">
        <q-td :props="props">
          <q-btn
            color="primary"
            dense
            size="sm"
            icon="edit"
            label="Editar"
            @click="editDicta(props.row)"
            v-if="hasPer(['ROLE_ADMIN', 'ROLE_DOCENTE'])"
            class="q-mr-sm"
          />
          <q-btn
            color="negative"
            dense
            size="sm"
            icon="delete"
            label="Eliminar"
            @click="delDicta(props.row)"
            v-if="hasPer(['ROLE_ADMIN'])"
          />
        </q-td>
      </template>
    </q-table>

    <!-- Paginación -->
    <div class="q-mt-md flex justify-center">
      <q-pagination
        v-model="state.current"
        :max="Math.ceil(state.total / state.size) || 1"
        color="primary"
        @update:model-value="getDictaListFun"
      />
    </div>

    <!-- Dialog -->
    <EditDicta
      v-model:dialogVisible="dialogVisible"
      :dictaObj="currentDicta"
      @get-list="getDictaListFun"
    />
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { queryDictaTable, delDicta as delDictaApi } from '@/api/dicta/dicta'
import { errorMsg, successMsg } from '@/utils/message'
import { hasPer } from '@/utils/common'
import EditDicta from './editDicta.vue'

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
const currentDicta = ref({})

// Columnas de QTable
const columns = [
  { name: 'index', label: '#', field: (row, idx) => idx + 1, align: 'center', sortable: false },
  { name: 'gestion', label: 'Gestión', field: 'gestion', align: 'center' },
  { name: 'codmat', label: 'Código', field: 'codmat' },
  { name: 'nombreMateria', label: 'Materia', field: 'nombreMateria' },
  { name: 'nombreNivel', label: 'Nivel', field: 'nombreNivel' },
  { name: 'nombreParalelo', label: 'Paralelo', field: 'nombreParalelo', align: 'center' },
  { name: 'codp', label: 'Periodo', field: 'codp', align: 'center' },
  { name: 'nombreDocente', label: 'Docente', field: 'nombreDocente' },
  { name: 'tipoDocente', label: 'Tipo', field: 'tipoDocente' },
  { name: 'emailDocente', label: 'Email', field: 'emailDocente' },
  { name: 'actions', label: 'Operaciones', field: 'actions', align: 'center' }
]

// Generar últimos 5 años
const gestiones = Array.from({ length: 5 }, (_, i) => new Date().getFullYear() - i)

const getDictaListFun = (obj) => {
  if (obj && obj.page) state.current = obj.page
  if (obj && obj.limit) state.size = obj.limit

  state.loading = true
  const params = {
    blurry: state.blurry,
    gestion: state.gestion,
    codmat: state.codmat,
    codn: state.codn,
    size: state.size,
    currentPage: state.current   // ojo: revisa que tu backend espere currentPage o page
  }

  queryDictaTable(params)
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

const addDicta = () => {
  currentDicta.value = { gestion: state.gestion }
  dialogVisible.value = true
}

const editDicta = (row) => {
  currentDicta.value = { ...row }
  dialogVisible.value = true
}

const delDicta = (row) => {
  const params = {
    codpar: row.codpar,
    codp: row.codp,
    codmat: row.codmat,
    gestion: row.gestion
  }

  delDictaApi(params).then((res) => {
    if (res.success) {
      successMsg(res.data)
      getDictaListFun()
    } else {
      errorMsg(res.msg)
    }
  })
}

const getDocenteTagType = (tipo) => {
  const types = {
    Titular: 'green',
    Invitado: 'orange',
    Auxiliar: 'blue'
  }
  return types[tipo] || 'grey'
}

onMounted(() => {
  getDictaListFun()
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
</style>