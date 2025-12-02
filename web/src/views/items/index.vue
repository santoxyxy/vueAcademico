<template>
  <div>
    <div class="searchDiv">
      <el-input class="searchInput" v-model="state.blurry" placeholder="Por favor ingrese un nombre de ítem" clearable></el-input>
      <el-button type="primary" @click="getItemsListFun">Consulta</el-button>
      <el-button v-if="hasPer('items:add')" @click="editItemsFun" style="float: right;">Nuevo</el-button>
    </div>
    <el-table :data="state.tableData" row-key="codi" border height="calc(100vh - 180px)" max-height="calc(100vh - 180px)">
      <el-table-column label="Numero de Serie" type="index" width="60"></el-table-column>
      <el-table-column label="Código" prop="codi" width="100"></el-table-column>
      <el-table-column label="Nombre del Ítem" prop="nombre"></el-table-column>
      <el-table-column label="Estado" prop="estado" width="100" align="center">
        <template #default="scope">
          <el-tag :type="scope.row.estado === 1 ? 'success' : 'danger'">
            {{ scope.row.estado === 1 ? 'Activo' : 'Inactivo' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="Funcionar" prop="option" width="200px" align="center">
        <template #default="scope">
          <el-button v-if="hasPer('items:edit')" type="primary" @click="editItemsFun(JSON.parse(JSON.stringify(scope.row)))">Editar</el-button>
          <el-button v-if="hasPer('items:del')" type="danger" @click="delItemsFun(scope.row.codi, scope.row.nombre)">Borrar</el-button>
        </template>
      </el-table-column>
    </el-table>
    <edit-items v-model:dialog-visible="dialogVisible" :items-obj="state.itemsObj" @get-list="getItemsListFun"></edit-items>
  </div>
</template>

<script setup>
import { queryItemsTable, delItems } from "../../api/items/items";
import { errorMsg, infoMsg, successMsg } from "../../utils/message";
import { hasPer } from "../../utils/common";
import editItems from "./editItems";
import { onMounted, reactive, ref } from "vue";
import { ElMessageBox } from "element-plus";

const dialogVisible = ref(false)

const state = reactive({
  blurry: '',
  itemsObj: {},
  tableData: []
})

onMounted(() => {
  getItemsListFun()
})

// Obtener lista de ítems
const getItemsListFun = () => {
  queryItemsTable({ blurry: state.blurry }).then(res => {
    if (res.success) {
      state.tableData = res.data
    } else {
      errorMsg(res.msg)
    }
  })
}

// Editar Ítem
const editItemsFun = (row) => {
  dialogVisible.value = true
  state.itemsObj = row.codi ? row : {}
}

// Eliminar Ítem
const delItemsFun = (id, name) => {
  ElMessageBox.confirm('Confirmar para eliminar ítem 【' + name + '】？', 'Pista', {
    confirmButtonText: 'Seguro',
    cancelButtonText: 'Cancelar',
    type: 'warning'
  }).then(() => {
    delItems(id).then(res => {
      if (res.success) {
        successMsg(res.data)
        getItemsListFun()
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