<template>
  <div>
    <!-- Filtros y acciones -->
    <div class="row q-col-gutter-md q-mb-md items-center">
      <q-input
        dense
        outlined
        v-model="state.blurry"
        label="Por favor ingrese un nombre de nivel"
        clearable
        class="col"
        @keyup.enter="getNivelesListFun"
      />
      <q-btn dense color="primary" label="Consulta" @click="getNivelesListFun" />
      <q-btn
        dense
        color="secondary"
        label="Nuevo"
        v-if="hasPer('niveles:add')"
        class="q-ml-md"
        @click="editNivelesFun"
      />
    </div>

    <!-- Tabla -->
    <q-table
      flat
      bordered
      :rows="state.tableData"
      row-key="codn"
      :columns="columns"
      row-height="28"
      class="shadow-0"
    >
      <template v-slot:body-cell-estado="props">
        <q-chip
          dense
          :color="props.row.estado === 1 ? 'green' : 'red'"
          text-color="white"
        >
          {{ props.row.estado === 1 ? 'Activo' : 'Inactivo' }}
        </q-chip>
      </template>

      <template v-slot:body-cell-option="props">
        <q-btn
          dense
          color="primary"
          size="sm"
          label="Editar"
          v-if="hasPer('niveles:edit')"
          @click="editNivelesFun({ ...props.row })"
        />
        <q-btn
          dense
          color="negative"
          size="sm"
          label="Borrar"
          class="q-ml-sm"
          v-if="hasPer('niveles:del')"
          @click="delNivelesFun(props.row.codn, props.row.nombre)"
        />
      </template>
    </q-table>

    <!-- Dialog de edición -->
    <edit-niveles
      v-model:dialog-visible="dialogVisible"
      :niveles-obj="state.nivelesObj"
      @get-list="getNivelesListFun"
    />
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { queryNivelesTable, delNiveles } from '@/api/niveles/niveles'
import { errorMsg, infoMsg, successMsg } from '@/utils/message'
import { hasPer } from '@/utils/common'
import EditNiveles from './editNiveles'
import { Dialog } from 'quasar'

const dialogVisible = ref(false)

const state = reactive({
  blurry: '',
  nivelesObj: {},
  tableData: []
})

const columns = [
  { name: 'index', label: 'Numero de Serie', field: (row, index) => index + 1, align: 'center', style: 'width: 60px' },
  { name: 'codn', label: 'Código', field: 'codn', align: 'center', style: 'width: 100px' },
  { name: 'nombre', label: 'Nombre del Nivel', field: 'nombre' },
  { name: 'estado', label: 'Estado', field: 'estado', align: 'center', style: 'width: 100px' },
  { name: 'option', label: 'Funcionar', field: 'option', align: 'center', style: 'width: 200px' }
]

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
  Dialog.create({
    title: 'Confirmar',
    message: `Confirmar para eliminar nivel 【${name}】？`,
    ok: { label: 'Seguro', color: 'primary' },
    cancel: { label: 'Cancelar', color: 'grey-5' },
    color: 'warning'
  }).onOk(() => {
    delNiveles(id).then(res => {
      if (res.success) {
        successMsg(res.data)
        getNivelesListFun()
      } else {
        errorMsg(res.msg)
      }
    })
  }).onCancel(() => {
    infoMsg('Operacion Cancelada')
  })
}
</script>

<style scoped>
.searchDiv {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}
</style>
