<template>
  <div>
    <div class="searchDiv">
      <el-input class="searchInput" v-model="state.blurry" placeholder="Por favor ingrese un nombre de nivel" clearable></el-input>
      <el-button type="primary" @click="getNivelesListFun">Consulta</el-button>
      <el-button v-if="hasPer('niveles:add')" @click="editNivelesFun" style="float: right;">Nuevo</el-button>
    </div>
    <el-table :data="state.tableData" row-key="codn" border height="calc(100vh - 180px)" max-height="calc(100vh - 180px)">
      <el-table-column label="Numero de Serie" type="index" width="60"></el-table-column>
      <el-table-column label="Código" prop="codn" width="100"></el-table-column>
      <el-table-column label="Nombre del Nivel" prop="nombre"></el-table-column>
      <el-table-column label="Estado" prop="estado" width="100" align="center">
        <template #default="scope">
          <el-tag :type="scope.row.estado === 1 ? 'success' : 'danger'">
            {{ scope.row.estado === 1 ? 'Activo' : 'Inactivo' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="Funcionar" prop="option" width="200px" align="center">
        <template #default="scope">
          <el-button v-if="hasPer('niveles:edit')" type="primary" @click="editNivelesFun(JSON.parse(JSON.stringify(scope.row)))">Editar</el-button>
          <el-button v-if="hasPer('niveles:del')" type="danger" @click="delNivelesFun(scope.row.codn, scope.row.nombre)">Borrar</el-button>
        </template>
      </el-table-column>
    </el-table>
    <edit-niveles v-model:dialog-visible="dialogVisible" :niveles-obj="state.nivelesObj" @get-list="getNivelesListFun"></edit-niveles>
  </div>
</template>

<script setup>
import { queryNivelesTable, delNiveles } from "@/api/niveles/niveles";
import { errorMsg, infoMsg, successMsg } from "@/utils/message";
import { hasPer } from "@/utils/common";
import editNiveles from "./editNiveles";
import { onMounted, reactive, ref } from "vue";
import { ElMessageBox } from "element-plus";

const dialogVisible = ref(false)

const state = reactive({
  blurry: '',
  nivelesObj: {},
  tableData: []
})

onMounted(() => {
  getNivelesListFun()
})

// Obtener lista de niveles
const getNivelesListFun = () => {
  queryNivelesTable({ blurry: state.blurry }).then(res => {
    if (res.success) {
      state.tableData = res.data
    } else {
      errorMsg(res.msg)
    }
  })
}

// Editar Nivel
const editNivelesFun = (row) => {
  dialogVisible.value = true
  state.nivelesObj = row.codn ? row : {}
}

// Eliminar Nivel
const delNivelesFun = (id, name) => {
  ElMessageBox.confirm('Confirmar para eliminar nivel 【' + name + '】？', 'Pista', {
    confirmButtonText: 'Seguro',
    cancelButtonText: 'Cancelar',
    type: 'warning'
  }).then(() => {
    delNiveles(id).then(res => {
      if (res.success) {
        successMsg(res.data)
        getNivelesListFun()
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