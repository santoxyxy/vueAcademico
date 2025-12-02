<template>
  <div class="app-container">
    <!-- Barra de búsqueda -->
    <div class="searchDiv">
      <el-input 
        class="searchInput" 
        v-model="state.blurry" 
        placeholder="Buscar por nombre de modalidad"
        clearable
        @clear="getModalidadListFun"
        @keyup.enter="getModalidadListFun"
      />
      <el-button type="primary" icon="Search" @click="getModalidadListFun">
        Consultar
      </el-button>
      <el-button type="success" icon="Plus" @click="addModalidad" v-if="hasPer(['ROLE_ADMIN'])">
        Nueva Modalidad
      </el-button>
    </div>

    <!-- Tabla -->
    <el-table 
      :data="state.tableData" 
      v-loading="state.loading" 
      border 
      stripe
      style="width: 100%"
    >
      <el-table-column type="index" label="#" width="80" align="center" />
      
      <el-table-column prop="codm" label="Código" width="120" align="center" />
      
      <el-table-column prop="nombre" label="Nombre" min-width="250" show-overflow-tooltip />
      
      <el-table-column prop="estado" label="Estado" width="120" align="center">
        <template #default="{ row }">
          <el-tag :type="row.estado === 1 ? 'success' : 'danger'" size="small">
            {{ row.estado === 1 ? 'Activo' : 'Inactivo' }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column label="Operaciones" width="200" align="center" fixed="right">
        <template #default="{ row }">
          <el-button 
            type="primary" 
            size="small" 
            icon="Edit"
            @click="editModalidad(row)"
            v-if="hasPer(['ROLE_ADMIN', 'ROLE_DOCENTE'])"
          >
            Editar
          </el-button>
          <el-popconfirm
            title="¿Confirmar eliminación?"
            @confirm="delModalidad(row.codm)"
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
      @pagination="getModalidadListFun"
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
import Pagination from '@/components/Pagination.vue'
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

// Obtener lista
const getModalidadListFun = (obj) => {
  if (obj && obj.page) state.current = obj.page
  if (obj && obj.limit) state.size = obj.limit

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
  delModalidadApi(id).then((res) => {
    if (res.success) {
      successMsg(res.data)
      getModalidadListFun()
    } else {
      errorMsg(res.msg)
    }
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
}

.searchInput {
  width: 300px;
}
</style>