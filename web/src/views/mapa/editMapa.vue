<template>
  <q-dialog v-model="visible" persistent>
    <q-card style="min-width: 400px; max-width: 500px">
      <q-card-section>
        <div class="text-h6">
          {{ isEdit ? 'Editar Asignación' : 'Nueva Asignación' }}
        </div>
      </q-card-section>

      <q-card-section>
        <q-form @submit.prevent="handleSubmit" ref="formRef" class="q-gutter-md">

          <!-- Materia -->
          <q-select
            v-model="state.mapaObj.codmat"
            :options="state.materiaOptions"
            option-label="label"
            option-value="value"
            label="Materia"
            :disable="!!state.mapaObj.codmat"
            use-input
            fill-input
            input-debounce="300"
            required
          />

          <!-- Paralelo -->
          <q-select
            v-model="state.mapaObj.codpar"
            :options="state.paraleloOptions"
            option-label="label"
            option-value="value"
            label="Paralelo"
            :disable="!!state.mapaObj.codpar"
            required
          />

          <!-- Gestión -->
          <q-input
            v-model.number="state.mapaObj.gestion"
            type="number"
            label="Gestión"
            :disable="!!state.mapaObj.gestion"
            :min="2020"
            :max="2030"
            required
          />

          <!-- Estado -->
          <q-select
            v-model="state.mapaObj.estado"
            :options="[
              { label: 'Activo', value: 1 },
              { label: 'Inactivo', value: 0 }
            ]"
            label="Estado"
          />
        </q-form>
      </q-card-section>

      <q-card-actions align="right">
        <q-btn flat label="Cancelar" color="secondary" @click="handleClose" />
        <q-btn flat label="Guardar" color="primary" @click="handleSubmit" />
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script setup>
import { ref, reactive, computed, watch } from 'vue'
import { editMapa } from '../../api/mapa/mapa'
import { getMateriaList } from '../../api/materia/materia'
import { getParaleloList } from '../../api/paralelo/paralelo'
import { errorMsg, successMsg } from '../../utils/message'

const props = defineProps({
  dialogVisible: Boolean,
  mapaObj: Object,
  gestion: Number
})
const emit = defineEmits(['update:dialogVisible', 'get-list'])

const visible = computed({
  get: () => props.dialogVisible,
  set: (val) => emit('update:dialogVisible', val)
})

const isEdit = computed(() => !!props.mapaObj?.codmat)

const state = reactive({
  mapaObj: {},
  materiaOptions: [],
  paraleloOptions: []
})

const formRef = ref(null)

// Cargar opciones de materias
const getMateriaListFun = async () => {
  const res = await getMateriaList({ blurry: '' })
  if (res.success) {
    state.materiaOptions = res.data.map(m => ({
      label: `${m.codmat} - ${m.nombre}`,
      value: m.codmat
    }))
  }
}

// Cargar opciones de paralelos
const getParaleloListFun = async () => {
  const res = await getParaleloList({ blurry: '' })
  if (res.success) {
    state.paraleloOptions = res.data.map(p => ({
      label: p.nombre,
      value: p.codp
    }))
  }
}

// Inicializar objeto
const updateObj = () => {
  state.mapaObj = props.mapaObj?.codmat
    ? { ...props.mapaObj }
    : { codmat: null, codpar: null, gestion: props.gestion, estado: 1 }
}

watch(() => props.dialogVisible, val => {
  if (val) {
    updateObj()
    getMateriaListFun()
    getParaleloListFun()
  }
})

// Guardar
const handleSubmit = async () => {
  if (!state.mapaObj.codmat || !state.mapaObj.codpar || !state.mapaObj.gestion) {
    errorMsg('Todos los campos son obligatorios')
    return
  }

  const res = await editMapa(state.mapaObj)
  if (res.success) {
    successMsg(res.data)
    handleClose()
    emit('get-list')
  } else {
    errorMsg(res.msg)
  }
}

// Cerrar diálogo
const handleClose = () => {
  visible.value = false
}
</script>

<style scoped>
.q-gutter-md {
  display: flex;
  flex-direction: column;
}
</style>
