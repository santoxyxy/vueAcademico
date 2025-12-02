<template>
  <div>
    <div class="searchDiv">
      <el-input class="searchInput" v-model="state.filters.codmat" placeholder="Código de Materia" clearable style="width: 200px;"></el-input>
      <el-input-number v-model="state.filters.gestion" :min="2020" :max="2050" placeholder="Gestión" style="width: 150px; margin-left: 10px;"></el-input-number>
      <el-button type="primary" @click="getItematListFun" style="margin-left: 10px;">Consulta</el-button>
      <el-button v-if="hasPer('itemat:add')" @click="editItematFun" style="float: right;">Nueva Configuración</el-button>
    </div>

    <!-- Alerta de validación -->
    <el-alert v-if="state.showValidation && state.filters.codmat && state.filters.gestion"
              :title="state.validationMsg"
              :type="state.validationType"
              :closable="false"
              style="margin: 10px 0;">
    </el-alert>

    <el-table :data="state.tableData" row-key="id" border height="calc(100vh - 240px)" max-height="calc(100vh - 240px)">
      <el-table-column label="Numero de Serie" type="index" width="60"></el-table-column>
      <el-table-column label="Código Materia" prop="codmat" width="120"></el-table-column>
      <el-table-column label="Nombre Materia" prop="nombreMateria"></el-table-column>
      <el-table-column label="Ítem" prop="nombreItem" width="150"></el-table-column>
      <el-table-column label="Ponderación (%)" prop="ponderacion" width="130" align="center">
        <template #default="scope">
          <el-tag :type="scope.row.ponderacion > 0 ? 'success' : 'info'">
            {{ scope.row.ponderacion }}%
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="Gestión" prop="gestion" width="100" align="center"></el-table-column>
      <el-table-column label="Estado" prop="estado" width="100" align="center">
        <template #default="scope">
          <el-tag :type="scope.row.estado === 1 ? 'success' : 'danger'">
            {{ scope.row.estado === 1 ? 'Activo' : 'Inactivo' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="Funcionar" prop="option" width="200px" align="center">
        <template #default="scope">
          <el-button v-if="hasPer('itemat:edit')" type="primary" size="small" @click="editItematFun(JSON.parse(JSON.stringify(scope.row)))">Editar</el-button>
          <el-button v-if="hasPer('itemat:del')" type="danger" size="small" @click="delItematFun(scope.row)">Borrar</el-button>
        </template>
      </el-table-column>
    </el-table>

    <edit-itemat v-model:dialog-visible="dialogVisible" :itemat-obj="state.itematObj" @get-list="getItematListFun"></edit-itemat>
  </div>
</template>

<script setup>
import { queryItematTable, delItemat, validatePonderaciones } from "../../api/itemat/itemat";
import { errorMsg, infoMsg, successMsg } from "../../utils/message";
import { hasPer } from "../../utils/common";
import editItemat from "./editItemat";
import { onMounted, reactive, ref, watch } from "vue";
import { ElMessageBox } from "element-plus";

const dialogVisible = ref(false)

const state = reactive({
  filters: {
    codmat: '',
    gestion: new Date().getFullYear()
  },
  itematObj: {},
  tableData: [],
  showValidation: false,
  validationMsg: '',
  validationType: 'success'
})

onMounted(() => {
  getItematListFun()
})

// Validar automáticamente cuando cambien los filtros
watch(() => [state.filters.codmat, state.filters.gestion], () => {
  if (state.filters.codmat && state.filters.gestion) {
    validatePonderacionesFun()
  } else {
    state.showValidation = false
  }
})

// Obtener lista de configuraciones
const getItematListFun = () => {
  const params = {
    gestion: state.filters.gestion || null,
    codmat: state.filters.codmat || null
  }
  
  queryItematTable(params).then(res => {
    if (res.success) {
      state.tableData = res.data
      if (state.filters.codmat && state.filters.gestion) {
        validatePonderacionesFun()
      }
    } else {
      errorMsg(res.msg)
    }
  })
}

// Validar que sumen 100%
const validatePonderacionesFun = () => {
  if (!state.filters.codmat || !state.filters.gestion) return

  validatePonderaciones(state.filters.codmat, state.filters.gestion).then(res => {
    state.showValidation = true
    if (res.success) {
      state.validationMsg = '✓ Las ponderaciones suman 100% correctamente'
      state.validationType = 'success'
    } else {
      state.validationMsg = '⚠ Las ponderaciones NO suman 100%'
      state.validationType = 'warning'
    }
  }).catch(() => {
    state.showValidation = false
  })
}

// Editar Itemat
const editItematFun = (row) => {
  dialogVisible.value = true
  state.itematObj = row.codmat ? row : { gestion: state.filters.gestion }
}

// Eliminar Itemat
const delItematFun = (row) => {
  ElMessageBox.confirm(
    `Confirmar para eliminar configuración de ${row.nombreItem} para ${row.nombreMateria}？`,
    'Pista',
    {
      confirmButtonText: 'Seguro',
      cancelButtonText: 'Cancelar',
      type: 'warning'
    }
  ).then(() => {
    delItemat(row.codmat, row.codi, row.gestion).then(res => {
      if (res.success) {
        successMsg(res.data)
        getItematListFun()
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
.searchDiv {
  margin-bottom: 15px;
}
</style>