<template>
  <div class="app-container">
    <!-- Barra de búsqueda y filtros -->
    <el-form :inline="true" :model="queryForm" class="demo-form-inline">
      <el-form-item label="Búsqueda">
        <el-input 
          v-model="queryForm.blurry" 
          placeholder="Usuario, email, nombre..." 
          clearable
          @clear="handleQuery"
          @keyup.enter="handleQuery"
        />
      </el-form-item>

      <el-form-item label="Gestión">
        <el-select 
          v-model="queryForm.gestion" 
          placeholder="Todas" 
          clearable
          @change="handleQuery"
        >
          <el-option 
            v-for="year in gestionesDisponibles" 
            :key="year" 
            :label="year" 
            :value="year" 
          />
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">
          Buscar
        </el-button>
        <el-button icon="Refresh" @click="resetQuery">
          Limpiar
        </el-button>
      </el-form-item>
    </el-form>

    <!-- Botones de acción -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-if="hasPer('general:add')"
          type="primary"
          icon="Plus"
          @click="handleAdd"
        >
          Nuevo Registro
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="RefreshRight"
          @click="handleQuery"
        >
          Actualizar
        </el-button>
      </el-col>
    </el-row>

    <!-- Tabla de datos -->
    <el-table 
      v-loading="loading" 
      :data="tableData" 
      border 
      stripe
      style="width: 100%"
    >
      <el-table-column type="index" label="#" width="50" align="center" />
      
      <el-table-column prop="username" label="Usuario" width="120" />
      
      <el-table-column prop="nickName" label="Nombre" width="150">
        <template #default="{ row }">
          <el-tag>{{ row.nickName }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="nombreCompleto" label="Nombre Completo" min-width="180" />

      <el-table-column prop="email" label="Email" min-width="180" />

      <el-table-column prop="tipo" label="Tipo" width="120" align="center">
        <template #default="{ row }">
          <el-tag 
            :type="row.tipo === 'DOCENTE' ? 'success' : 'primary'"
            effect="dark"
          >
            {{ row.tipo || 'N/A' }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="gestion" label="Gestión" width="100" align="center">
        <template #default="{ row }">
          <el-tag type="warning">{{ row.gestion }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="totalMaterias" label="Materias" width="90" align="center">
        <template #default="{ row }">
          <el-badge :value="row.totalMaterias" class="item" type="success" />
        </template>
      </el-table-column>

      <el-table-column prop="totalNotas" label="Notas" width="90" align="center">
        <template #default="{ row }">
          <el-badge :value="row.totalNotas" class="item" type="primary" />
        </template>
      </el-table-column>

      <el-table-column prop="enabled" label="Estado" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="row.enabled ? 'success' : 'danger'">
            {{ row.enabled ? 'Activo' : 'Inactivo' }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column label="Operaciones" width="280" fixed="right" align="center">
        <template #default="{ row }">
          <el-button
            type="primary"
            size="small"
            icon="View"
            @click="handleDetail(row)"
          >
            Detalles
          </el-button>
          
          <el-button
            v-if="hasPer('general:edit')"
            type="success"
            size="small"
            icon="Edit"
            @click="handleEdit(row)"
          >
            Editar
          </el-button>
          
          <el-button
            v-if="hasPer('general:del')"
            type="danger"
            size="small"
            icon="Delete"
            @click="handleDelete(row)"
          >
            Eliminar
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- Paginación -->
    <Pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryForm.currentPage"
      v-model:limit="queryForm.size"
      @pagination="getTableData"
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
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { queryGeneralTable, delGeneral } from '@/api/general/general'
import { hasPer } from '@/utils/common'
import Pagination from '@/components/Pagination.vue'
import EditGeneral from './editGeneral.vue'
import DetalleUsuario from './detalleUsuario.vue'

// Estado
const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const editDialogVisible = ref(false)
const detalleDialogVisible = ref(false)
const editData = ref(null)
const selectedUsuario = ref(null)

// Formulario de consulta
const queryForm = reactive({
  blurry: '',
  gestion: null,
  currentPage: 1,
  size: 10
})

// Gestiones disponibles (últimos 5 años desde actual)
const gestionesDisponibles = computed(() => {
  const currentYear = new Date().getFullYear()
  return Array.from({ length: 5 }, (_, i) => currentYear - i)
})

// Obtener datos de la tabla
const getTableData = async () => {
  loading.value = true
  try {
    const params = {
      blurry: queryForm.blurry,
      gestion: queryForm.gestion,
      currentPage: queryForm.currentPage,
      size: queryForm.size
    }
    
    const { data } = await queryGeneralTable(params)
    
    if (data.success) {
      tableData.value = data.data.records
      total.value = data.data.total
    }
  } catch (error) {
    ElMessage.error('Error al obtener datos: ' + error.message)
  } finally {
    loading.value = false
  }
}

// Buscar
const handleQuery = () => {
  queryForm.currentPage = 1
  getTableData()
}

// Limpiar búsqueda
const resetQuery = () => {
  queryForm.blurry = ''
  queryForm.gestion = null
  handleQuery()
}

// Agregar nuevo
const handleAdd = () => {
  editData.value = null
  editDialogVisible.value = true
}

// Editar
const handleEdit = (row) => {
  editData.value = { ...row }
  editDialogVisible.value = true
}

// Ver detalles
const handleDetail = (row) => {
  selectedUsuario.value = { ...row }
  detalleDialogVisible.value = true
}

// Eliminar
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `¿Está seguro de eliminar el registro de ${row.username} en la gestión ${row.gestion}?`,
      'Confirmar eliminación',
      {
        confirmButtonText: 'Eliminar',
        cancelButtonText: 'Cancelar',
        type: 'warning'
      }
    )

    const { data } = await delGeneral(row.ids, row.gestion)
    
    if (data.success) {
      ElMessage.success('Registro eliminado exitosamente')
      getTableData()
    } else {
      ElMessage.error(data.msg)
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('Error al eliminar: ' + error.message)
    }
  }
}

// Inicialización
onMounted(() => {
  getTableData()
})
</script>

<style scoped>
.app-container {
  padding: 20px;
}

.demo-form-inline .el-form-item {
  margin-right: 10px;
}

.mb8 {
  margin-bottom: 8px;
}

.el-badge {
  margin-left: 10px;
}
</style>