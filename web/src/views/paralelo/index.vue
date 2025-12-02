<template>
  <div>
    <div class="searchDiv">
      <el-input class="searchInput" v-model="state.blurry" placeholder="Por favor ingrese un nombre de paralelo" clearable></el-input>
      <el-button type="primary" @click="getParaleloListFun">Consulta</el-button>
      <el-button v-if="hasPer('paralelo:add')" @click="editParaleloFun" style="float: right;">Nuevo</el-button>
    </div>
    <el-table :data="state.tableData" row-key="codp" border height="calc(100vh - 180px)" max-height="calc(100vh - 180px)">
      <el-table-column label="Numero de Serie" type="index" width="60"></el-table-column>
      <el-table-column label="Código" prop="codp" width="100"></el-table-column>
      <el-table-column label="Nombre del Paralelo" prop="nombre"></el-table-column>
      <el-table-column label="Estado" prop="estado" width="100" align="center">
        <template #default="scope">
          <el-tag :type="scope.row.estado === 1 ? 'success' : 'danger'">
            {{ scope.row.estado === 1 ? 'Activo' : 'Inactivo' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="Funcionar" prop="option" width="200px" align="center">
        <template #default="scope">
          <el-button v-if="hasPer('paralelo:edit')" type="primary" @click="editParaleloFun(JSON.parse(JSON.stringify(scope.row)))">Editar</el-button>
          <el-button v-if="hasPer('paralelo:del')" type="danger" @click="delParaleloFun(scope.row.codp, scope.row.nombre)">Borrar</el-button>
        </template>
      </el-table-column>
    </el-table>
    <edit-paralelo v-model:dialog-visible="dialogVisible" :paralelo-obj="state.paraleloObj" @get-list="getParaleloListFun"></edit-paralelo>
  </div>
</template>

<script setup>
import { queryParaleloTable, delParalelo } from "../../api/paralelo/paralelo";
import { errorMsg, infoMsg, successMsg } from "../../utils/message";
import { hasPer } from "../../utils/common";
import editParalelo from "./editParalelo";
import { onMounted, reactive, ref } from "vue";
import { ElMessageBox } from "element-plus";

const dialogVisible = ref(false)

const state = reactive({
  blurry: '',
  paraleloObj: {},
  tableData: []
})

onMounted(() => {
  getParaleloListFun()
})

// Obtener lista de paralelos
const getParaleloListFun = () => {
  queryParaleloTable({ blurry: state.blurry }).then(res => {
    if (res.success) {
      state.tableData = res.data
    } else {
      errorMsg(res.msg)
    }
  })
}

// Editar Paralelo
const editParaleloFun = (row) => {
  dialogVisible.value = true
  state.paraleloObj = row.codp ? row : {}
}

// Eliminar Paralelo
const delParaleloFun = (id, name) => {
  ElMessageBox.confirm('Confirmar para eliminar paralelo 【' + name + '】？', 'Pista', {
    confirmButtonText: 'Seguro',
    cancelButtonText: 'Cancelar',
    type: 'warning'
  }).then(() => {
    delParalelo(id).then(res => {
      if (res.success) {
        successMsg(res.data)
        getParaleloListFun()
      } else {
        errorMsg(res.msg)
      }
    })
  }).catch(() => {
    infoMsg('Operacion Cancelada')
  })
}
</script>

<style scoped>
</style>