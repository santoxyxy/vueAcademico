<template>
  <div class="app-container">
    <!-- Barra de búsqueda -->
    <div class="searchDiv">
      <el-input 
        class="searchInput" 
        v-model="state.blurry" 
        placeholder="Buscar por nombre de detalle"
        clearable
        @clear="getDmodalidadListFun"
        @keyup.enter="getDmodalidadListFun"
      />
      <el-button type="primary" icon="Search" @click="getDmodalidadListFun">
        Consultar
      </el-button>
      <el-button type="success" icon="Plus" @click="addDmodalidad" v-if="hasPer(['ROLE_ADMIN'])">
        Nuevo
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
      <el-table-column type="index" label="#" width="60" align="center" />
      
      <el-table-column prop="nombre" label="Nombre Detalle" min-width="180" show-overflow-tooltip />
      
      <el-table-column prop="nombreModalidad" label="Modalidad Padre" min-width="150" show-overflow-tooltip />
      
      <el-table-column prop="estado" label="Estado" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="row.estado === 1 ? 'success' : 'danger'" size="small">
            {{ row.estado === 1 ? 'Activo' : 'Inactivo' }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column label="Operaciones" width="180" align="center" fixed="right">
        <template #default="{ row }">
          <el-button 
            type="primary" 
            size="small" 
            icon="Edit"
            @click="editDmodalidad(row)"
            v-if="hasPer(['ROLE_ADMIN', 'ROLE_DOCENTE'])"
          >
            Editar
          </el-button>
          <el-popconfirm
            title="¿Confirmar eliminación?"
            @confirm="delDmodalidad(row.coddm)"
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
      @pagination="getDmodalidadListFun"
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
import Pagination from '@/components/Pagination.vue'
import EditDmodalidad from './editDmodalidad.vue'

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

// Eliminar
const delDmodalidad = (id) => {
  delDmodalidadApi(id).then((res) => {
    if (res.success) {
      successMsg(res.data)
      getDmodalidadListFun()
    } else {
      errorMsg(res.msg)
    }
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