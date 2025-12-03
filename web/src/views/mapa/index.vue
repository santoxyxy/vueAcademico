<template>
  <div class="q-pa-md">
    <!-- Barra de búsqueda -->
    <div class="searchDiv">
      <q-input
        v-model="state.blurry"
        placeholder="Buscar por materia o paralelo"
        clearable
        @keyup.enter="getMapaListFun"
        class="searchInput"
      />

      <q-select
        v-model="state.gestion"
        :options="state.gestionOptions"
        label="Gestión"
        style="width: 150px"
        emit-value
        map-options
        @update:model-value="getMapaListFun"
      />

      <q-btn color="primary" @click="getMapaListFun">
        Consulta
      </q-btn>

      <q-btn
        color="positive"
        v-if="hasPer('mapa:add')"
        @click="editMapaFun()"
        style="margin-left: auto;"
      >
        Nuevo
      </q-btn>
    </div>

    <!-- Tabla -->
    <q-table
      :rows="state.tableData"
      :columns="columns"
      row-key="codmat"
      flat
      bordered
      :pagination="{ rowsPerPage: 10 }"
      virtual-scroll
      style="height: calc(100vh - 180px)"
    >
      <!-- Estado -->
      <template v-slot:body-cell-estado="props">
        <q-td :props="props">
          <q-badge
            :color="props.row.estado === 1 ? 'positive' : 'negative'"
            :label="props.row.estado === 1 ? 'Activo' : 'Inactivo'"
          />
        </q-td>
      </template>

      <!-- Acciones -->
      <template v-slot:body-cell-actions="props">
        <q-td :props="props" class="q-gutter-sm">
          <q-btn
            color="primary"
            flat
            dense
            label="Editar"
            v-if="hasPer('mapa:edit')"
            @click="editMapaFun({ ...props.row })"
          />
          <q-btn
            color="negative"
            flat
            dense
            label="Borrar"
            v-if="hasPer('mapa:del')"
            @click="delMapaFun(props.row)"
          />
        </q-td>
      </template>
    </q-table>

    <!-- Modal de edición -->
    <edit-mapa
      v-model:dialog-visible="dialogVisible"
      :mapa-obj="state.mapaObj"
      :gestion="state.gestion"
      @get-list="getMapaListFun"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getMapaTable, delMapa } from '../../api/mapa/mapa'
import { errorMsg, infoMsg, successMsg } from '../../utils/message'
import { hasPer } from '../../utils/common'
import EditMapa from './editMapa.vue'
import { Dialog } from 'quasar'

const state = reactive({
  blurry: '',
  gestion: new Date().getFullYear(),
  tableData: [],
  mapaObj: {},
  gestionOptions: []
})

const dialogVisible = ref(false)

onMounted(() => {
  generateGestionOptions()
  getMapaListFun()
})

// Generar opciones de gestión
const generateGestionOptions = () => {
  const currentYear = new Date().getFullYear()
  for (let i = -2; i <= 2; i++) {
    state.gestionOptions.push(currentYear + i)
  }
}

// Columnas de tabla
const columns = [
  { name: 'codmat', label: 'Código Materia', field: 'codmat', align: 'left' },
  { name: 'nombreMateria', label: 'Materia', field: 'nombreMateria', align: 'left' },
  { name: 'nombreParalelo', label: 'Paralelo', field: 'nombreParalelo', align: 'left' },
  { name: 'nombreNivel', label: 'Nivel', field: 'nombreNivel', align: 'left' },
  { name: 'gestion', label: 'Gestión', field: 'gestion', align: 'center' },
  { name: 'estado', label: 'Estado', field: 'estado', align: 'center' },
  { name: 'actions', label: 'Funcionar', field: 'actions', align: 'center' }
]

// Obtener lista
const getMapaListFun = () => {
  getMapaTable({
    blurry: state.blurry,
    gestion: state.gestion
  }).then(res => {
    if (res.success) {
      state.tableData = res.data.records || res.data
    } else {
      errorMsg(res.msg)
    }
  })
}

// Editar / Nuevo
const editMapaFun = (row) => {
  state.mapaObj = row ? row : {}
  dialogVisible.value = true
}

// Eliminar
const delMapaFun = (row) => {
  Dialog.create({
    title: 'Confirmar eliminación',
    message: `Confirmar eliminar asignación ${row.nombreMateria} - ${row.nombreParalelo}?`,
    cancel: true,
    persistent: true
  }).onOk(() => {
    delMapa(row.codmat, row.codpar, row.gestion).then(res => {
      if (res.success) {
        successMsg(res.data)
        getMapaListFun()
      } else {
        errorMsg(res.msg)
      }
    })
  }).onCancel(() => {
    infoMsg('Operación cancelada')
  })
}
</script>

<style scoped>
.searchDiv {
  display: flex;
  gap: 16px;
  margin-bottom: 16px;
  align-items: center;
}

.searchInput {
  width: 400px;
}
</style>
