<template>
  <div>
    <div class="searchDiv">
      <el-input v-model="state.filters.codmat" placeholder="Código Materia" clearable style="width: 150px;"></el-input>
      <el-input-number v-model="state.filters.gestion" :min="2020" :max="2050" placeholder="Gestión" style="width: 130px; margin-left: 10px;"></el-input-number>
      <el-input-number v-model="state.filters.idusuario" placeholder="ID Usuario" style="width: 150px; margin-left: 10px;" clearable></el-input-number>
      <el-button type="primary" @click="getNotasListFun" style="margin-left: 10px;">Consulta</el-button>
      <el-button type="success" @click="calcularNotaFinalFun" style="margin-left: 10px;" :disabled="!canCalculate">
        <el-icon><Calculator /></el-icon> Calcular Final
      </el-button>
      <el-button v-if="hasPer('notas:add')" @click="editNotaFun" style="float: right;">Nueva Nota</el-button>
      <el-button v-if="hasPer('notas:add')" type="warning" @click="registrarMasivasFun" style="float: right; margin-right: 10px;">Registro Masivo</el-button>
    </div>

    <!-- Alerta de Nota Final -->
    <el-alert v-if="state.notaFinal !== null"
              :title="`Nota Final: ${state.notaFinal} / 100`"
              :type="state.notaFinal >= 51 ? 'success' : 'error'"
              :closable="true"
              @close="state.notaFinal = null"
              style="margin: 10px 0;">
    </el-alert>

    <el-table :data="state.tableData" row-key="id" border height="calc(100vh - 240px)" max-height="calc(100vh - 240px)">
      <el-table-column label="N°" type="index" width="50"></el-table-column>
      <el-table-column label="Estudiante" prop="nombreEstudiante" width="180"></el-table-column>
      <el-table-column label="Materia" prop="nombreMateria" width="150"></el-table-column>
      <el-table-column label="Ítem" prop="nombreItem" width="120"></el-table-column>
      <el-table-column label="Paralelo" prop="nombreParalelo" width="100" align="center"></el-table-column>
      <el-table-column label="Nota" prop="nota" width="80" align="center">
        <template #default="scope">
          <el-tag :type="getNotaType(scope.row.nota)">
            {{ scope.row.nota }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="Ponderación" prop="ponderacion" width="110" align="center">
        <template #default="scope">
          {{ scope.row.ponderacion }}%
        </template>
      </el-table-column>
      <el-table-column label="Nota Ponderada" prop="notaPonderada" width="130" align="center">
        <template #default="scope">
          <strong>{{ calcularNotaPonderada(scope.row.nota, scope.row.ponderacion) }}</strong>
        </template>
      </el-table-column>
      <el-table-column label="Gestión" prop="gestion" width="90" align="center"></el-table-column>
      <el-table-column label="Funcionar" prop="option" width="200px" align="center" fixed="right">
        <template #default="scope">
          <el-button v-if="hasPer('notas:edit')" type="primary" size="small" @click="editNotaFun(JSON.parse(JSON.stringify(scope.row)))">Editar</el-button>
          <el-button v-if="hasPer('notas:del')" type="danger" size="small" @click="delNotaFun(scope.row)">Borrar</el-button>
        </template>
      </el-table-column>
    </el-table>

    <edit-nota v-model:dialog-visible="dialogVisible" :nota-obj="state.notaObj" @get-list="getNotasListFun"></edit-nota>
    <registro-masivo v-model:dialog-visible="dialogMasivo" @get-list="getNotasListFun"></registro-masivo>
  </div>
</template>

<script setup>
import { queryNotasTable, delNota, calcularNotaFinal } from "@/api/notas/notas";
import { errorMsg, infoMsg, successMsg } from "@/utils/message";
import { hasPer } from "@/utils/common";
import editNota from "./editNota";
import registroMasivo from "./registroMasivo";
import { onMounted, reactive, ref, computed } from "vue";
import { ElMessageBox } from "element-plus";

const dialogVisible = ref(false)
const dialogMasivo = ref(false)

const state = reactive({
  filters: {
    codmat: '',
    gestion: new Date().getFullYear(),
    idusuario: null
  },
  notaObj: {},
  tableData: [],
  notaFinal: null
})

const canCalculate = computed(() => {
  return state.filters.codmat && state.filters.gestion && state.filters.idusuario
})

onMounted(() => {
  getNotasListFun()
})

// Obtener lista de notas
const getNotasListFun = () => {
  const params = {
    gestion: state.filters.gestion || null,
    codmat: state.filters.codmat || null,
    idusuario: state.filters.idusuario || null
  }
  
  queryNotasTable(params).then(res => {
    if (res.success) {
      state.tableData = res.data
    } else {
      errorMsg(res.msg)
    }
  })
}

// Calcular nota ponderada
const calcularNotaPonderada = (nota, ponderacion) => {
  if (!nota || !ponderacion) return 0
  return ((nota * ponderacion) / 100).toFixed(2)
}

// Tipo de tag según nota
const getNotaType = (nota) => {
  if (nota >= 70) return 'success'
  if (nota >= 51) return 'warning'
  return 'danger'
}

// Calcular nota final
const calcularNotaFinalFun = () => {
  if (!canCalculate.value) {
    errorMsg('Por favor complete: Código Materia, Gestión y ID Usuario')
    return
  }

  calcularNotaFinal(
    state.filters.idusuario,
    state.filters.codmat,
    state.filters.gestion
  ).then(res => {
    if (res.success) {
      state.notaFinal = res.data
      successMsg(`Nota final calculada: ${res.data}`)
    } else {
      errorMsg(res.msg)
    }
  })
}

// Editar Nota
const editNotaFun = (row) => {
  dialogVisible.value = true
  state.notaObj = row.codmat ? row : { gestion: state.filters.gestion }
}

// Registro Masivo
const registrarMasivasFun = () => {
  dialogMasivo.value = true
}

// Eliminar Nota
const delNotaFun = (row) => {
  ElMessageBox.confirm(
    `Confirmar eliminar nota de ${row.nombreEstudiante} en ${row.nombreItem}？`,
    'Pista',
    {
      confirmButtonText: 'Seguro',
      cancelButtonText: 'Cancelar',
      type: 'warning'
    }
  ).then(() => {
    delNota(row.codmat, row.codi, row.coda, row.codp, row.gestion, row.idusuario).then(res => {
      if (res.success) {
        successMsg(res.data)
        getNotasListFun()
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