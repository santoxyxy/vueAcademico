<template>
  <div class="app-container">

    <!-- Barra superior -->
    <div class="toolbar">
      <el-button type="primary" icon="Plus" @click="onEdit()">Nuevo Cliente</el-button>

      <el-input
        v-model="state.blurry"
        placeholder="Buscar"
        clearable
        @keyup.enter="getModulo1DClientesTableFun"
        class="search-input"
      >
        <template #append>
          <el-button icon="Search" @click="getModulo1DClientesTableFun" />
        </template>
      </el-input>
    </div>

    <!-- TABLA -->
    <el-table
      v-loading="loading"
      :data="state.tableData"
      border
      style="width: 100%"
      height="75vh"
    >
      <el-table-column
        prop="idcliente"
        label="ID Cliente"
        width="120"
        align="center"
        sortable
        fixed="left"
      />

      <el-table-column
        prop="nombre"
        label="Nombre"
        align="left"
        sortable
      />

      <el-table-column
        label="Acciones"
        align="center"
        width="180"
        fixed="right"
      >
        <template #default="{ row }">
          <el-button
            size="small"
            type="primary"
            link
            icon="Edit"
            @click="onEdit(row)"
          >
            Editar
          </el-button>

          <el-button
            size="small"
            type="danger"
            link
            icon="Document"
            @click="onPedidos(row)"
          >
            Detalle
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- PAGINACIÓN -->
    <el-pagination
      background
      layout="prev, pager, next, jumper"
      :page-size="state.size"
      :current-page="state.current"
      :total="state.total"
      @current-change="(page)=>{state.current=page; getModulo1DClientesTableFun()}"
    />

    <!-- MODAL DE PEDIDOS -->
    <pedidoDclientes
      v-model="pedidoDialogVisible"
      :cliente-obj="state.selectedCliente"
      @close="pedidoDialogVisible = false"
    />

    <!-- MODAL DE EDICIÓN -->
    <editDClientes
      v-model="clienteDialogVisible"
      :cliente-obj="state.selectedCliente"
      @close="onDialogClose"
    />
  </div>
</template>
<script setup>
import { reactive, ref, onMounted } from 'vue'
import { getModulo1DClientesTable } from '../../../api/modulo1/modulo1'
import { errorMsg } from '../../../utils/message'

import pedidoDclientes from './pedidoDclientes.vue'
import editDClientes from './editDClientes.vue'

const state = reactive({
  blurry: '',
  tableData: [],
  userObj: {},
  current: 1,
  size: 10,
  total: 0,
  selectedCliente: null,
})

const loading = ref(false)
const pedidoDialogVisible = ref(false)
const clienteDialogVisible = ref(false)

// 🔹 Cargar tabla
const getModulo1DClientesTableFun = () => {
  loading.value = true

  const params = {
    blurry: state.blurry,
    size: state.size,
    currentPage: state.current,
  }

  getModulo1DClientesTable(params)
    .then((res) => {
      loading.value = false
      if (res.success) {
        state.tableData = res.data.records
        state.total = res.data.total
      } else errorMsg(res.msg)
    })
    .catch(() => {
      loading.value = false
      errorMsg('Error al cargar los datos')
    })
}

// 🔹 Ver pedidos
const onPedidos = (row) => {
  if (!row || !row.idcliente) return
  state.selectedCliente = { ...row }
  pedidoDialogVisible.value = true
}

// 🔹 Editar o crear cliente
const onEdit = (row) => {
  state.selectedCliente = row ? { ...row } : {}
  clienteDialogVisible.value = true
}

// 🔹 Al cerrar diálogo, recargar
const onDialogClose = () => {
  clienteDialogVisible.value = false
  getModulo1DClientesTableFun()
}

onMounted(() => {
  getModulo1DClientesTableFun()
})
</script>
<style scoped>
.app-container {
  padding: 20px;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  margin-bottom: 15px;
}

.search-input {
  width: 250px;
}

.el-table th {
  background: #00b4ff !important;
  color: #fff;
  font-weight: bold;
}

.el-table__fixed,
.el-table__fixed-right {
  background: #e0f7ff !important;
}
</style>
