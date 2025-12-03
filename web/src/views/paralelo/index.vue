<template>
  <q-page padding class="q-pa-md">
    <div class="row q-col-gutter-md q-mb-md">
      <q-input filled v-model="state.blurry" label="Ingrese un nombre de paralelo" class="col"></q-input>
      <q-btn color="primary" label="Consulta" @click="getParaleloListFun"></q-btn>
      <q-btn v-if="hasPer('paralelo:add')" color="secondary" label="Nuevo" @click="editParaleloFun" class="q-ml-md"></q-btn>
    </div>

    <q-table
      title="Lista de Paralelos"
      :rows="state.tableData"
      row-key="codp"
      :columns="columns"
      flat
      bordered
    >
      <template v-slot:body-cell-estado="props">
        <q-chip :color="props.row.estado === 1 ? 'green' : 'red'" text-color="white">
          {{ props.row.estado === 1 ? 'Activo' : 'Inactivo' }}
        </q-chip>
      </template>

      <template v-slot:body-cell-option="props">
        <q-btn v-if="hasPer('paralelo:edit')" color="primary" label="Editar" size="sm"
          @click="editParaleloFun(props.row)">
        </q-btn>
        <q-btn v-if="hasPer('paralelo:del')" color="negative" label="Borrar" size="sm" class="q-ml-sm"
          @click="delParaleloFun(props.row.codp, props.row.nombre)">
        </q-btn>
      </template>
    </q-table>

    <edit-paralelo v-model:dialog-visible="dialogVisible" :paralelo-obj="state.paraleloObj" @get-list="getParaleloListFun" />
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

const columns = [
  { name: 'index', label: 'N°', field: (row, index) => index + 1, align: 'center' },
  { name: 'codp', label: 'Código', field: 'codp', align: 'center' },
  { name: 'nombre', label: 'Nombre del Paralelo', field: 'nombre' },
  { name: 'estado', label: 'Estado', field: 'estado', align: 'center' },
  { name: 'option', label: 'Acciones', field: 'option', align: 'center' }
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
