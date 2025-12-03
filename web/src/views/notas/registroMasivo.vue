<template>
  <div>
    <q-dialog v-model="dialogVisible" persistent>
      <q-card style="min-width: 700px; max-width: 800px;">
        <q-card-section>
          <div class="text-h6">Registro Masivo de Notas</div>
        </q-card-section>

        <q-card-section>
          <q-banner dense class="q-mb-md" type="info">
            Ingrese las notas para todos los estudiantes del paralelo
          </q-banner>

          <q-form class="q-mb-md">
            <q-input
              dense
              outlined
              label="Código Materia"
              v-model="state.filters.codmat"
              placeholder="MAT101"
              class="q-mb-sm"
            />
            <div class="row q-gutter-sm">
              <div class="col-6">
                <q-input
                  dense
                  outlined
                  type="number"
                  label="Código Ítem"
                  v-model.number="state.filters.codi"
                  :min="1"
                  class="q-mb-sm"
                />
              </div>
              <div class="col-6">
                <q-input
                  dense
                  outlined
                  type="number"
                  label="Código Paralelo"
                  v-model.number="state.filters.coda"
                  :min="1"
                  class="q-mb-sm"
                />
              </div>
              <div class="col-6">
                <q-input
                  dense
                  outlined
                  type="number"
                  label="Gestión"
                  v-model.number="state.filters.gestion"
                  :min="2020"
                  :max="2050"
                  class="q-mb-sm"
                />
              </div>
            </div>

            <q-btn
              color="primary"
              label="+ Agregar Estudiante"
              @click="addRow"
              class="q-mt-sm"
            />
          </q-form>

          <q-table
            :rows="state.notasList"
            row-key="idusuario"
            flat
            dense
            virtual-scroll
            style="max-height: 400px;"
          >
            <q-tr v-for="(row, index) in state.notasList" :key="index">
              <q-td>
                <q-input
                  dense
                  type="number"
                  v-model.number="row.idusuario"
                  :min="1"
                  placeholder="ID Usuario"
                />
              </q-td>
              <q-td>
                <q-input
                  dense
                  type="number"
                  v-model.number="row.nota"
                  :min="0"
                  :max="100"
                />
              </q-td>
              <q-td>
                <q-input
                  dense
                  type="number"
                  v-model.number="row.coddm"
                  :min="1"
                />
              </q-td>
              <q-td>
                <q-input
                  dense
                  type="number"
                  v-model.number="row.codp"
                  :min="1"
                />
              </q-td>
              <q-td>
                <q-btn
                  dense
                  color="negative"
                  icon="delete"
                  round
                  flat
                  @click="removeRow(index)"
                />
              </q-td>
            </q-tr>
          </q-table>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat label="Cancelar" color="secondary" @click="closeFun" />
          <q-btn
            flat
            label="Guardar Todo ({{ state.notasList.length }} notas)"
            color="primary"
            @click="submitFun"
            :disable="state.notasList.length === 0"
          />
        </q-card-actions>
      </q-card>
    </q-dialog>
  </div>
</template>

<script setup>
import { reactive, computed } from 'vue'
import { registrarNotasMasivas } from '@/api/notas/notas'
import { successMsg, errorMsg } from '@/utils/message'

const props = defineProps({
  dialogVisible: Boolean
})
const emit = defineEmits(['update:dialogVisible', 'getList'])

const dialogVisible = computed({
  get: () => props.dialogVisible,
  set: val => emit('update:dialogVisible', val)
})

const state = reactive({
  filters: {
    codmat: '',
    codi: null,
    coda: null,
    gestion: new Date().getFullYear()
  },
  notasList: []
})

const addRow = () => {
  if (!state.filters.codmat || !state.filters.codi || !state.filters.coda) {
    errorMsg('Complete los filtros primero')
    return
  }
  state.notasList.push({
    codmat: state.filters.codmat,
    codi: state.filters.codi,
    coda: state.filters.coda,
    gestion: state.filters.gestion,
    idusuario: null,
    nota: 0,
    coddm: null,
    codp: null
  })
}

const removeRow = (index) => {
  state.notasList.splice(index, 1)
}

const closeFun = () => {
  state.notasList = []
  dialogVisible.value = false
}

const submitFun = () => {
  const invalid = state.notasList.some(
    n => !n.idusuario || !n.coddm || !n.codp
  )
  if (invalid) {
    errorMsg('Complete todos los campos requeridos')
    return
  }

  registrarNotasMasivas(state.notasList).then(res => {
    if (res.success) {
      successMsg(`${state.notasList.length} notas registradas exitosamente`)
      emit('getList')
      closeFun()
    } else {
      errorMsg(res.msg)
    }
  })
}
</script>

<style scoped>
.q-mb-sm {
  margin-bottom: 8px;
}
</style>
