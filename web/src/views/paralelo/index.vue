<template>
  <q-page padding class="q-pa-md">
    <!-- Filtro y botones superiores -->
    <div class="row q-col-gutter-md q-mb-md">
      <q-input filled v-model="state.blurry" label="Ingrese un nombre de paralelo" class="col"></q-input>

      <q-btn color="primary" flat round icon="search" @click="getParaleloListFun">
        <q-tooltip>Consultar</q-tooltip>
      </q-btn>

      <q-btn v-if="hasPer('paralelo:add')" color="secondary" flat round icon="add" class="q-ml-md"
        @click="editParaleloFun()">
        <q-tooltip>Nuevo Paralelo</q-tooltip>
      </q-btn>
    </div>

    <!-- Tabla de paralelos -->
    <q-table
      title="Lista de Paralelos"
      :rows="state.tableData"
      row-key="codp"
      :columns="columns"
      flat
      bordered
    >
      <!-- Estado con chip -->
      <template v-slot:body-cell-estado="props">
        <q-chip :color="props.row.estado === 1 ? 'green' : 'red'" text-color="white">
          {{ props.row.estado === 1 ? 'Activo' : 'Inactivo' }}
        </q-chip>
      </template>

      <!-- Acciones con iconos -->
      <template v-slot:body-cell-option="props">
        <q-btn 
          v-if="hasPer('paralelo:edit')" 
          color="primary" flat round icon="edit"
          @click="editParaleloFun(props.row)"
        >
          <q-tooltip>Editar</q-tooltip>
        </q-btn>

        <q-btn 
          v-if="hasPer('paralelo:del')" 
          color="negative" flat round icon="delete" class="q-ml-sm"
          @click="delParaleloFun(props.row.codp, props.row.nombre)"
        >
          <q-tooltip>Borrar</q-tooltip>
        </q-btn>
      </template>
    </q-table>

    <!-- Componente de diálogo -->
    <edit-paralelo 
      v-model:dialog-visible="dialogVisible" 
      :paralelo-obj="state.paraleloObj" 
      @get-list="getParaleloListFun" 
    />
  </q-page>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { hasPer } from '../../utils/common'
import { queryParaleloTable, delParalelo } from '../../api/paralelo/paralelo'
import { errorMsg, successMsg, infoMsg } from '../../utils/message'
import editParalelo from './editParalelo'

const dialogVisible = ref(false)

const state = reactive({
  blurry: '',
  paraleloObj: {},
  tableData: []
})

// 🔥 COLUMNA REPARADA PARA MOSTRAR BOTONES
const columns = [
  { name: 'index', label: 'N°', field: (row, index) => index + 1, align: 'center' },
  { name: 'codp', label: 'Código', field: 'codp', align: 'center' },
  { name: 'nombre', label: 'Nombre del Paralelo', field: 'nombre' },
  { name: 'estado', label: 'Estado', field: 'estado', align: 'center' },

  // 👇 ESTA ES LA COLUMNA QUE ACTIVA LOS BOTONES
  { 
    name: 'option',
    label: 'Acciones',
    field: 'codp',   // CAMBIO CRUCIAL
    align: 'center'
  }
]

onMounted(() => {
  getParaleloListFun()
})

const getParaleloListFun = () => {
  queryParaleloTable({ blurry: state.blurry }).then(res => {
    if (res.success) {
      state.tableData = res.data.records
    } else {
      errorMsg(res.msg)
    }
  })
}

const editParaleloFun = (row) => {
  dialogVisible.value = true
  state.paraleloObj = row?.codp ? row : {}
}

const delParaleloFun = (id, name) => {
  if (confirm(`Confirmar para eliminar paralelo 【${name}】？`)) {
    delParalelo(id).then(res => {
      if (res.success) {
        successMsg(res.data)
        getParaleloListFun()
      } else {
        errorMsg(res.msg)
      }
    })
  } else {
    infoMsg('Operación Cancelada')
  }
}
</script>

<style scoped>
</style>
