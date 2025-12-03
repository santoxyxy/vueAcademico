<template>
  <q-dialog v-model="visible" persistent>
    <q-card style="min-width: 400px; max-width: 500px;">
      <q-card-section class="text-h6">
        {{ state.title }}
      </q-card-section>

      <q-form ref="formRef" @submit.prevent="submitForm" class="q-gutter-md">
        <!-- Nombre Detalle -->
        <q-input
          v-model="state.form.nombre"
          label="Nombre Detalle"
          placeholder="Ingrese el nombre del detalle"
          :rules="[val => !!val || 'El nombre es obligatorio']"
          outlined
        />

        <!-- Modalidad Padre -->
        <q-select
          v-model="state.form.codm"
          label="Modalidad Padre"
          placeholder="Seleccione modalidad"
          :options="state.modalidadList.map(m => ({ label: m.nombre, value: m.codm }))"
          emit-value
          map-options
          :rules="[val => !!val || 'Debe seleccionar una modalidad']"
          outlined
        />

        <!-- Estado -->
        <q-option-group
          v-model="state.form.estado"
          label="Estado"
          type="radio"
          inline
          :options="estadoOptions"
          :rules="[val => val !== null || 'El estado es obligatorio']"
        />
      </q-form>

      <q-card-actions align="right">
        <q-btn flat label="Reiniciar" color="grey" @click="resetForm(formRef)" />
        <q-btn
          label="Guardar"
          color="primary"
          :loading="isLoading"
          @click="submitForm"
        />
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script setup>
import { reactive, ref, computed, watch } from 'vue'
import { editDmodalidad } from '@/api/dmodalidad/dmodalidad'
import { errorMsg, successMsg } from '@/utils/message'
import { resetForm } from '@/utils/common'

const props = defineProps({
  dialogVisible: { type: Boolean, required: true, default: false },
  dmodalidadObj: { type: Object, default: () => ({}) }
})

const emit = defineEmits(['update:dialogVisible', 'get-list'])

const visible = computed({
  get: () => props.dialogVisible,
  set: val => emit('update:dialogVisible', val)
})

const isLoading = ref(false)
const formRef = ref()

const estadoOptions = [
  { label: 'Activo', value: 1 },
  { label: 'Inactivo', value: 0 }
]

const state = reactive({
  title: 'Nuevo Detalle',
  form: {
    coddm: null,
    nombre: '',
    codm: null,
    estado: 1
  },
  modalidadList: []
})

const openFun = () => {
  resetForm(formRef.value)
  state.title = 'Nuevo Detalle'
  isLoading.value = false

  getModalidadList()

  if (props.dmodalidadObj.coddm) {
    state.title = 'Editar Detalle'
    state.form = { ...props.dmodalidadObj }
  } else {
    state.form = { coddm: null, nombre: '', codm: null, estado: 1 }
  }
}

// Ejemplo de datos de modalidades; reemplazar con API real
const getModalidadList = () => {
  state.modalidadList = [
    { codm: 1, nombre: 'Presencial' },
    { codm: 2, nombre: 'Virtual' },
    { codm: 3, nombre: 'Híbrida' }
  ]
}

const submitForm = () => {
  formRef.value.validate().then(valid => {
    if (valid) {
      isLoading.value = true
      editDmodalidad(state.form).then(res => {
        if (res.success) {
          successMsg(res.data)
          visible.value = false
          emit('get-list')
        } else {
          errorMsg(res.msg)
        }
        isLoading.value = false
      })
    }
  })
}

// Abrir diálogo automáticamente al cambiar prop
watch(() => props.dialogVisible, val => {
  if (val) openFun()
})
</script>

<style scoped>
.q-card-section {
  display: flex;
  flex-direction: column;
}
</style>
