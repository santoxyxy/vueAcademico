<template>
  <div>
    <div class="searchDiv row items-center q-gutter-sm">
      <q-input
        filled
        v-model="state.filters.codmat"
        label="Código de Materia"
        clearable
        style="width: 200px"
      />

      <q-input
        filled
        v-model.number="state.filters.gestion"
        type="number"
        label="Gestión"
        :min="2020"
        :max="2050"
        style="width: 150px"
      />

      <q-btn color="primary" label="Consulta" @click="getItematListFun" />

      <q-btn
        v-if="hasPer('itemat:add')"
        label="Nueva Configuración"
        color="secondary"
        class="q-ml-auto"
        @click="editItematFun"
      />
    </div>

    <!-- Alerta de validación -->
    <q-banner
      v-if="state.showValidation && state.filters.codmat && state.filters.gestion"
      :class="state.validationType === 'success' ? 'bg-positive text-white' : 'bg-warning text-black'"
      dense
      rounded
      class="q-mb-md"
    >
      {{ state.validationMsg }}
    </q-banner>

    <q-table
      :rows="state.tableData"
      row-key="id"
      flat
      bordered
      dense
      :columns="columns"
      virtual-scroll
      style="height: calc(100vh - 240px)"
    >
      <template v-slot:body-cell-ponderacion="props">
        <q-td :props="props" class="text-center">
          <q-badge
            :color="props.row.ponderacion > 0 ? 'positive' : 'info'"
            align="middle"
          >
            {{ props.row.ponderacion }}%
          </q-badge>
        </q-td>
      </template>

      <template v-slot:body-cell-estado="props">
        <q-td :props="props" class="text-center">
          <q-badge :color="props.row.estado === 1 ? 'positive' : 'negative'" align="middle">
            {{ props.row.estado === 1 ? 'Activo' : 'Inactivo' }}
          </q-badge>
        </q-td>
      </template>

      <template v-slot:body-cell-option="props">
        <q-td :props="props" class="text-center">
          <q-btn
            v-if="hasPer('itemat:edit')"
            size="sm"
            color="primary"
            label="Editar"
            class="q-mr-sm"
            @click="editItematFun(JSON.parse(JSON.stringify(props.row)))"
          />
          <q-btn
            v-if="hasPer('itemat:del')"
            size="sm"
            color="negative"
            label="Borrar"
            @click="delItematFun(props.row)"
          />
        </q-td>
      </template>
    </q-table>

    <edit-itemat
      v-model:dialog-visible="dialogVisible"
      :itemat-obj="state.itematObj"
      @get-list="getItematListFun"
    />
  </div>
</template>

<script setup>
import { ref, reactive, watch, onMounted } from 'vue'
import { queryItematTable, delItemat, validatePonderaciones } from '@/api/itemat/itemat'
import { errorMsg, infoMsg, successMsg } from '@/utils/message'
import { hasPer } from '@/utils/common'
import editItemat from './editItemat'
import { Dialog } from 'quasar'

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

const columns = [
  { name: 'index', label: 'Nº', field: (row, index) => index + 1, align: 'center', style: 'width: 60px' },
  { name: 'codmat', label: 'Código Materia', field: 'codmat', style: 'width: 120px' },
  { name: 'nombreMateria', label: 'Nombre Materia', field: 'nombreMateria' },
  { name: 'nombreItem', label: 'Ítem', field: 'nombreItem', style: 'width: 150px' },
  { name: 'ponderacion', label: 'Ponderación (%)', field: 'ponderacion', style: 'width: 130px', align: 'center' },
  { name: 'gestion', label: 'Gestión', field: 'gestion', style: 'width: 100px', align: 'center' },
  { name: 'estado', label: 'Estado', field: 'estado', style: 'width: 100px', align: 'center' },
  { name: 'option', label: 'Funcionar', field: 'option', style: 'width: 200px', align: 'center' }
]

// Cargar lista al montar
onMounted(() => {
  getItematListFun()
})

// Validación automática al cambiar filtros
watch([() => state.filters.codmat, () => state.filters.gestion], () => {
  if (state.filters.codmat && state.filters.gestion) {
    validatePonderacionesFun()
  } else {
    state.showValidation = false
  }
})

// Obtener lista de configuraciones
const getItematListFun = () => {
  const params = {
    codmat: state.filters.codmat || null,
    gestion: state.filters.gestion || null
  }

  queryItematTable(params).then(res => {
    if (res.success) {
      state.tableData = res.data
      if (state.filters.codmat && state.filters.gestion) validatePonderacionesFun()
    } else {
      errorMsg(res.msg)
    }
  })
}

// Validar que sumen 100%
const validatePonderacionesFun = () => {
  if (!state.filters.codmat || !state.filters.gestion) return

  validatePonderaciones(state.filters.codmat, state.filters.gestion)
    .then(res => {
      state.showValidation = true
      if (res.success) {
        state.validationMsg = '✓ Las ponderaciones suman 100% correctamente'
        state.validationType = 'success'
      } else {
        state.validationMsg = '⚠ Las ponderaciones NO suman 100%'
        state.validationType = 'warning'
      }
    })
    .catch(() => {
      state.showValidation = false
    })
}

// Editar Itemat
const editItematFun = (row) => {
  dialogVisible.value = true
  state.itematObj = row?.codmat ? row : { gestion: state.filters.gestion }
}

// Eliminar Itemat
const delItematFun = (row) => {
  Dialog.create({
    title: 'Pista',
    message: `Confirmar para eliminar configuración de ${row.nombreItem} para ${row.nombreMateria}？`,
    cancel: true,
    persistent: true
  }).onOk(() => {
    delItemat(row.codmat, row.codi, row.gestion).then(res => {
      if (res.success) {
        successMsg(res.data)
        getItematListFun()
      } else {
        errorMsg(res.msg)
      }
    })
  }).onCancel(() => {
    infoMsg('Operación Cancelada')
  })
}
</script>

<style scoped>
.searchDiv {
  margin-bottom: 15px;
}
</style>
