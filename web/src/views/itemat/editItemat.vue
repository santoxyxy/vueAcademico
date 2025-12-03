<template>
  <div>
    <q-dialog v-model="dialogVisible" persistent maximized-se="false" :maximized="false">
      <q-card style="min-width: 600px">
        <q-card-section>
          <div class="text-h6">{{ state.title }}</div>
        </q-card-section>

        <q-card-section>
          <q-form ref="formRef" @submit.prevent="submitFun" class="q-gutter-md">
            <q-input
              filled
              label="Código Materia"
              v-model="state.form.codmat"
              :disable="!!state.form.codmat && isEdit"
              lazy-rules
              :rules="[val => !!val || 'Por favor ingrese código de materia']"
            />

            <q-select
              filled
              label="Ítem"
              v-model="state.form.codi"
              :options="state.itemsList.map(i => ({ label: i.nombre, value: i.codi }))"
              emit-value
              map-options
              @update:model-value="handleItemChange"
              lazy-rules
              :rules="[val => !!val || 'Por favor seleccione un ítem']"
            />

            <q-input
              filled
              label="Gestión"
              type="number"
              v-model.number="state.form.gestion"
              :min="2020"
              :max="2050"
              lazy-rules
              :rules="[val => !!val || 'Por favor ingrese gestión']"
            />

            <q-input
              filled
              label="Ponderación (%)"
              type="number"
              v-model.number="state.form.ponderacion"
              :min="0"
              :max="100"
              lazy-rules
              :rules="[
                val => val !== null && val !== undefined || 'Por favor ingrese ponderación',
                val => val >= 0 && val <= 100 || 'Debe estar entre 0 y 100'
              ]"
              @input="calculateTotal"
            />
            <q-banner
              v-if="state.totalPonderacion > 0"
              dense
              rounded
              :class="state.totalPonderacion === 100 ? 'bg-positive text-white' : 'bg-warning text-black'"
              class="q-mt-sm"
            >
              Total configurado: {{ state.totalPonderacion }}%
            </q-banner>

            <q-option-group
              type="radio"
              label="Estado"
              v-model="state.form.estado"
              :options="[
                { label: 'Activo', value: 1 },
                { label: 'Inactivo', value: 0 }
              ]"
            />
          </q-form>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat label="Cancelar" color="primary" @click="closeFun" />
          <q-btn label="Guardar" color="primary" @click="submitFun" />
        </q-card-actions>
      </q-card>
    </q-dialog>
  </div>
</template>

<script setup>
import { reactive, ref, computed, watch } from 'vue'
import { editItemat, getItemsByMateria } from '@/api/itemat/itemat'
import { getItemsList } from '@/api/items/items'
import { errorMsg, successMsg, warningMsg } from '@/utils/message'

const props = defineProps({
  dialogVisible: Boolean,
  itematObj: Object
})

const emit = defineEmits(['update:dialogVisible', 'getList'])

const dialogVisible = computed({
  get: () => props.dialogVisible,
  set: val => emit('update:dialogVisible', val)
})

const formRef = ref(null)
const state = reactive({
  title: 'Nueva Configuración',
  form: {
    codmat: '',
    codi: null,
    gestion: new Date().getFullYear(),
    ponderacion: 0,
    estado: 1
  },
  itemsList: [],
  totalPonderacion: 0
})

const isEdit = computed(() => !!(state.form.codmat && state.form.codi && state.form.gestion))

// Cargar lista de ítems disponibles
const loadItemsList = () => {
  getItemsList('').then(res => {
    if (res.success) state.itemsList = res.data
  })
}

// Calcular total de ponderaciones
const calculateTotal = () => {
  if (!state.form.codmat || !state.form.gestion) return

  getItemsByMateria(state.form.codmat, state.form.gestion).then(res => {
    if (res.success && res.data) {
      const currentP = state.form.ponderacion || 0
      const existingTotal = res.data
        .filter(item => item.codi !== state.form.codi)
        .reduce((sum, item) => sum + (item.ponderacion || 0), 0)
      state.totalPonderacion = existingTotal + currentP
    }
  })
}

const handleItemChange = () => {
  calculateTotal()
}

watch(
  () => props.itematObj,
  newVal => {
    loadItemsList()
    if (newVal && newVal.codmat && newVal.codi) {
      state.title = 'Editar Configuración'
      state.form = { ...newVal }
    } else {
      state.title = 'Nueva Configuración'
      state.form = {
        codmat: newVal?.codmat || '',
        codi: null,
        gestion: newVal?.gestion || new Date().getFullYear(),
        ponderacion: 0,
        estado: 1
      }
    }
    calculateTotal()
  },
  { deep: true, immediate: true }
)

watch(
  () => [state.form.codmat, state.form.gestion, state.form.ponderacion],
  calculateTotal
)

// Cerrar
const closeFun = () => {
  formRef.value?.resetValidation?.()
  state.totalPonderacion = 0
  dialogVisible.value = false
}

// Guardar
const submitFun = () => {
  formRef.value.validate?.().then(valid => {
    if (valid) {
      if (state.totalPonderacion !== 100) {
        warningMsg(`Las ponderaciones suman ${state.totalPonderacion}%. Se recomienda que sumen 100%`)
      }
      editItemat(state.form).then(res => {
        if (res.success) {
          successMsg(res.data)
          emit('getList')
          closeFun()
        } else {
          errorMsg(res.msg)
        }
      })
    }
  })
}
</script>

<style scoped>
</style>
