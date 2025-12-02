<template>
  <div class="app-container">
    <!-- Filtros de búsqueda -->
    <div class="searchDiv">
      <el-input 
        v-model="state.blurry" 
        placeholder="Buscar por materia o docente"
        clearable
        style="width: 250px"
        @keyup.enter="getDictaListFun"
      />
      
      <el-select 
        v-model="state.gestion" 
        placeholder="Gestión"
        clearable
        style="width: 120px"
      >
        <el-option 
          v-for="year in gestiones" 
          :key="year" 
          :label="year" 
          :value="year"
        />
      </el-select>

      <el-input 
        v-model="state.codmat" 
        placeholder="Código Materia"
        clearable
        style="width: 150px"
      />

      <el-input-number 
        v-model="state.codn" 
        placeholder="Nivel"
        :min="1"
        :max="10"
        controls-position="right"
        style="width: 120px"
      />

      <el-button type="primary" icon="Search" @click="getDictaListFun">
        Buscar
      </el-button>
      
      <el-button type="success" icon="Plus" @click="addDicta" v-if="hasPer(['ROLE_ADMIN'])">
        Nueva Asignación
      </el-button>
    </div>

    <!-- Tabla -->
    <el-table 
      :data="state.tableData" 
      v-loading="state.loading" 
      border 
      stripe
    >
      <el-table-column type="index" label="#" width="60" align="center" />
      
      <el-table-column prop="gestion" label="Gestión" width="100" align="center" />
      
      <el-table-column prop="codmat" label="Código" width="100" />
      
      <el-table-column prop="nombreMateria" label="Materia" min-width="200" show-overflow-tooltip />
      
      <el-table-column prop="nombreNivel" label="Nivel" width="100" />
      
      <el-table-column prop="nombreParalelo" label="Paralelo" width="100" align="center" />
      
      <el-table-column prop="codp" label="Periodo" width="90" align="center">
        <template #default="{ row }">
          <el-tag :type="row.codp === 1 ? 'success' : 'warning'" size="small">
            {{ row.codp }}° Periodo
          </el-tag>
        </template>
      </el-table-column>
      
      <el-table-column prop="nombreDocente" label="Docente" min-width="180" show-overflow-tooltip />
      
      <el-table-column prop="tipoDocente" label="Tipo" width="100">
        <template #default="{ row }">
          <el-tag :type="getDocenteTagType(row.tipoDocente)" size="small">
            {{ row.tipoDocente }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="emailDocente" label="Email" min-width="180" show-overflow-tooltip>
        <template #default="{ row }">
          <el-link :href="`mailto:${row.emailDocente}`" type="primary" :underline="false">
            {{ row.emailDocente }}
          </el-link>
        </template>
      </el-table-column>

      <el-table-column label="Operaciones" width="180" align="center" fixed="right">
        <template #default="{ row }">
          <el-button 
            type="primary" 
            size="small" 
            icon="Edit"
            @click="editDicta(row)"
            v-if="hasPer(['ROLE_ADMIN', 'ROLE_DOCENTE'])"
          >
            Editar
          </el-button>
          <el-popconfirm
            title="¿Eliminar esta asignación?"
            @confirm="delDicta(row)"
            v-if="hasPer(['ROLE_ADMIN'])"
          >
            <template #reference>
              <el-button type="danger" size="small" icon="Delete">
                Eliminar
              </el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <!-- Paginación -->
    <Pagination 
      :total="state.total" 
      :page="state.current" 
      :limit="state.size" 
      @pagination="getDictaListFun"
    />

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
import Pagination from '@/components/Pagination.vue'
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

// Generar últimos 5 años para el select
const gestiones = ref(
  Array.from({ length: 5 }, (_, i) => new Date().getFullYear() - i)
)

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
    currentPage: state.current
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
    'Titular': 'success',
    'Invitado': 'warning',
    'Auxiliar': 'info'
  }
  return types[tipo] || ''
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