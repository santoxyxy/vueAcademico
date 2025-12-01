<template>
  <div class="app-container">
    <!-- Filtros de búsqueda -->
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="state.queryForm" class="search-form">
        <el-form-item label="Gestión">
          <el-date-picker
            v-model="state.queryForm.gestion"
            type="year"
            placeholder="Seleccione año"
            format="YYYY"
            value-format="YYYY"
            style="width: 140px"
          />
        </el-form-item>
        
        <el-form-item label="Materia">
          <el-input
            v-model="state.queryForm.codmat"
            placeholder="Código materia"
            clearable
            style="width: 180px"
          />
        </el-form-item>

        <el-form-item label="Nivel">
          <el-select
            v-model="state.queryForm.codn"
            placeholder="Seleccione nivel"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="nivel in state.nivelesList"
              :key="nivel.codn"
              :label="nivel.nombre"
              :value="nivel.codn"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="Búsqueda">
          <el-input
            v-model="state.queryForm.blurry"
            placeholder="Materia, paralelo o docente"
            clearable
            style="width: 220px"
            @keyup.enter="getList"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" icon="Search" @click="getList">Buscar</el-button>
          <el-button icon="Refresh" @click="resetQuery">Limpiar</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- Tabla de datos -->
    <el-card shadow="never" class="table-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">Clases Ofertadas (PROGRA)</span>
          <el-button
            v-if="hasPer(['ADMIN'])"
            type="primary"
            icon="Plus"
            @click="handleAdd"
          >
            Nueva Clase
          </el-button>
        </div>
      </template>

      <el-table
        v-loading="state.loading"
        :data="state.tableData"
        border
        stripe
        style="width: 100%"
      >
        <el-table-column type="index" label="#" width="50" align="center" />
        
        <el-table-column prop="gestion" label="Gestión" width="90" align="center" />
        
        <el-table-column prop="codmat" label="Código" width="100" align="center" />
        
        <el-table-column prop="nombreMateria" label="Materia" min-width="200" show-overflow-tooltip />
        
        <el-table-column prop="nombreNivel" label="Nivel" width="120" align="center" />
        
        <el-table-column prop="nombreParalelo" label="Paralelo" width="100" align="center" />
        
        <el-table-column prop="nombreDocente" label="Docente" min-width="180" show-overflow-tooltip />
        
        <el-table-column prop="emailDocente" label="Email" min-width="200" show-overflow-tooltip />
        
        <el-table-column prop="cantidadEstudiantes" label="Estudiantes" width="100" align="center">
          <template #default="scope">
            <el-tag type="info">{{ scope.row.cantidadEstudiantes || 0 }}</el-tag>
          </template>
        </el-table-column>

        <el-table-column label="Operación" width="180" align="center" fixed="right">
          <template #default="scope">
            <el-button
              v-if="hasPer(['ADMIN', 'DOCENTE'])"
              type="primary"
              size="small"
              icon="Edit"
              @click="handleEdit(scope.row)"
            >
              Editar
            </el-button>
            <el-button
              v-if="hasPer(['ADMIN'])"
              type="danger"
              size="small"
              icon="Delete"
              @click="handleDelete(scope.row)"
            >
              Eliminar
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- Paginación -->
      <div class="pagination-container">
        <Pagination
          :total="state.total"
          v-model:currentPage="state.queryForm.currentPage"
          v-model:pageSize="state.queryForm.size"
          @pagination="getList"
        />
      </div>
    </el-card>

    <!-- Diálogo de edición -->
    <EditProgra
      v-model:dialogVisible="state.dialogVisible"
      :prograObj="state.currentProgra"
      @get-list="getList"
    />
  </div>
</template>

<script setup>
import { reactive, onMounted, ref } from 'vue'
import { getPrograTable, delProgra } from '../../api/progra/progra'
import { errorMsg, successMsg, confirmMsg } from '../../utils/message'
import { hasPer } from '../../utils/common'
import Pagination from '../../components/Pagination.vue'
import EditProgra from './editProgra.vue'
import { ElMessage } from 'element-plus'

const state = reactive({
  loading: false,
  dialogVisible: false,
  queryForm: {
    currentPage: 1,
    size: 10,
    gestion: new Date().getFullYear().toString(),
    codmat: '',
    codn: null,
    blurry: ''
  },
  tableData: [],
  total: 0,
  currentProgra: {},
  nivelesList: []
})

const getList = () => {
  state.loading = true
  getPrograTable(state.queryForm).then(res => {
    if (res.success) {
      state.tableData = res.data.records
      state.total = res.data.total
    } else {
      errorMsg(res.msg)
    }
    state.loading = false
  }).catch(() => {
    state.loading = false
  })
}

const resetQuery = () => {
  state.queryForm = {
    currentPage: 1,
    size: 10,
    gestion: new Date().getFullYear().toString(),
    codmat: '',
    codn: null,
    blurry: ''
  }
  getList()
}

const handleAdd = () => {
  state.currentProgra = {
    codpar: null,
    codp: 1,
    codmat: '',
    gestion: parseInt(state.queryForm.gestion) || new Date().getFullYear(),
    ids: null
  }
  state.dialogVisible = true
}

const handleEdit = (row) => {
  state.currentProgra = { ...row }
  state.dialogVisible = true
}

const handleDelete = (row) => {
  confirmMsg('¿Está seguro de eliminar esta clase?', () => {
    delProgra({
      codpar: row.codpar,
      codp: row.codp,
      codmat: row.codmat,
      gestion: row.gestion
    }).then(res => {
      if (res.success) {
        successMsg(res.data)
        getList()
      } else {
        errorMsg(res.msg)
      }
    })
  })
}

onMounted(() => {
  getList()
})
</script>

<style scoped>
.app-container {
  padding: 20px;
}

.search-card {
  margin-bottom: 20px;
}

.table-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  font-size: 16px;
  font-weight: bold;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.search-form {
  display: flex;
  flex-wrap: wrap;
}

:deep(.el-form-item) {
  margin-bottom: 10px;
}

:deep(.el-form-item__label) {
  font-size: 13px;
}
</style>