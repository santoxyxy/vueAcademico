<template>
  <div>
    <!-- Filtros de búsqueda -->
    <div class="searchDiv row q-gutter-sm items-center">
      <q-input dense outlined v-model="state.filters.codmat" label="Código Materia" clearable style="width: 150px;" />
      <q-input dense outlined type="number" v-model.number="state.filters.gestion" label="Gestión" :min="2020" :max="2050" style="width: 130px;" />
      <q-input dense outlined type="number" v-model.number="state.filters.idusuario" label="ID Usuario" clearable style="width: 150px;" />

      <q-btn color="primary" label="Consulta" @click="getNotasListFun" />
      <q-btn color="secondary" label="Calcular Final" :disable="!canCalculate" @click="calcularNotaFinalFun" icon="calculate" />
      <q-btn v-if="hasPer('notas:add')" color="primary" label="Nueva Nota" @click="editNotaFun" class="q-ml-auto" />
      <q-btn v-if="hasPer('notas:add')" color="warning" label="Registro Masivo" @click="registrarMasivasFun" />
    </div>

    <!-- Alerta de Nota Final -->
    <q-banner v-if="state.notaFinal !== null" :class="state.notaFinal >= 51 ? 'bg-positive text-white' : 'bg-negative text-white'" class="q-mb-md">
      Nota Final: {{ state.notaFinal }} / 100
      <q-btn flat dense icon="close" class="float-right" @click="state.notaFinal = null" />
    </q-banner>

    <!-- Tabla de notas -->
    <q-table
      :rows="state.tableData"
      row-key="id"
      flat
      bordered
      virtual-scroll
      :rows-per-page-options="[10, 20, 50]"
      :table-style="{ height: 'calc(100vh - 240px)' }"
    >
      <template v-slot:body-cell-index="props">
        {{ props.pageIndex + 1 }}
      </template>

      <q-tr v-for="row in state.tableData" :key="row.id">
        <q-td>{{ state.tableData.indexOf(row) + 1 }}</q-td>
        <q-td>{{ row.nombreEstudiante }}</q-td>
        <q-td>{{ row.nombreMateria }}</q-td>
        <q-td>{{ row.nombreItem }}</q-td>
        <q-td class="text-center">{{ row.nombreParalelo }}</q-td>
        <q-td class="text-center">
          <q-badge :color="getNotaType(row.nota)" align="top">{{ row.nota }}</q-badge>
        </q-td>
        <q-td class="text-center">{{ row.ponderacion }}%</q-td>
        <q-td class="text-center"><strong>{{ calcularNotaPonderada(row.nota, row.ponderacion) }}</strong></q-td>
        <q-td class="text-center">{{ row.gestion }}</q-td>
        <q-td class="text-center">
          <q-btn v-if="hasPer('notas:edit')" color="primary" size="sm" label="Editar" @click="editNotaFun(JSON.parse(JSON.stringify(row)))" />
          <q-btn v-if="hasPer('notas:del')" color="negative" size="sm" label="Borrar" @click="delNotaFun(row)" />
        </q-td>
      </q-tr>
    </q-table>

    <!-- Modales -->
    <edit-nota v-model:dialog-visible="dialogVisible" :nota-obj="state.notaObj" @get-list="getNotasListFun" />
    <registro-masivo v-model:dialog-visible="dialogMasivo" @get-list="getNotasListFun" />
  </div>
</template>

<script setup>
import { reactive, ref, computed, onMounted } from 'vue'
import { queryNotasTable, delNota, calcularNotaFinal } from '@/api/notas/notas'
import { successMsg, errorMsg, infoMsg } from '@/utils/message'
import { hasPer } from '@/utils/common'
import editNota from './editNota.vue'
import registroMasivo from './registroMasivo.vue'

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

const canCalculate = computed(() => state.filters.codmat && state.filters.gestion && state.filters.idusuario)

onMounted(() => getNotasListFun())

const getNotasListFun = () => {
  queryNotasTable(state.filters).then(res => {
    if (res.success) {
      state.tableData = res.data
    } else errorMsg(res.msg)
  })
}

const calcularNotaPonderada = (nota, ponderacion) => {
  if (!nota || !ponderacion) return 0
  return ((nota * ponderacion) / 100).toFixed(2)
}

const getNotaType = (nota) => {
  if (nota >= 70) return 'positive'
  if (nota >= 51) return 'warning'
  return 'negative'
}

const calcularNotaFinalFun = () => {
  if (!canCalculate.value) return errorMsg('Por favor complete: Código Materia, Gestión y ID Usuario')
  calcularNotaFinal(state.filters.idusuario, state.filters.codmat, state.filters.gestion).then(res => {
    if (res.success) {
      state.notaFinal = res.data
      successMsg(`Nota final calculada: ${res.data}`)
    } else errorMsg(res.msg)
  })
}

const editNotaFun = (row) => {
  dialogVisible.value = true
  state.notaObj = row?.codmat ? row : { gestion: state.filters.gestion }
}

const registrarMasivasFun = () => dialogMasivo.value = true

const delNotaFun = (row) => {
  if (confirm(`Confirmar eliminar nota de ${row.nombreEstudiante} en ${row.nombreItem}?`)) {
    delNota(row.codmat, row.codi, row.coda, row.codp, row.gestion, row.idusuario).then(res => {
      if (res.success) {
        successMsg(res.data)
        getNotasListFun()
      } else errorMsg(res.msg)
    })
  } else infoMsg('Operacion Cancelada')
}
</script>

<style scoped>
.searchDiv {
  margin-bottom: 15px;
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  align-items: center;
}
</style>
