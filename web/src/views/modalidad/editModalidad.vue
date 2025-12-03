<template>
  <q-dialog v-model="visible" persistent>
    <q-card style="min-width: 400px; max-width: 500px;">
      <q-card-section class="text-h6">
        {{ state.title }}
      </q-card-section>

      <q-form ref="formRef" @submit.prevent="submitForm">
        <q-card-section class="q-pt-none">
          <!-- Nombre -->
          <q-input
            v-model="state.form.nombre"
            label="Nombre"
            :rules="[val => !!val || 'El nombre es obligatorio', val => (val && val.length >= 3 && val.length <= 100) || 'Longitud entre 3 y 100 caracteres']"
            maxlength="100"
            counter
            dense
            outlined
          />

          <!-- Estado -->
          <q-option-group
            v-model="state.form.estado"
            label="Estado"
            type="radio"
            inline
            class="q-mt-md"
            :options="estadoOptions"
            :rules="[val => val !== null || 'El estado es obligatorio']"
          />
        </q-card-section>

        <!-- Footer -->
        <q-card-actions align="right">
          <q-btn flat label="Reiniciar" color="grey" @click="resetForm(formRef)" />
          <q-btn
            label="Guardar"
            color="primary"
            :loading="isLoading"
            @click="submitForm"
          />
        </q-card-actions>
      </q-form>
    </q-card>
  </q-dialog>
</template>

<script setup>
import { reactive, ref, computed, watch } from 'vue'
import { editModalidad } from '@/api/modalidad/modalidad'
import { successMsg, errorMsg } from '@/utils/message'
import { resetForm } from '@/utils/common'


const props = defineProps({
  dialogVisible: { type: Boolean, required: true, default: false },
  modalidadObj: { type: Object, default: () => ({}) }
})

const emit = defineEmits(['update:dialogVisible', 'get-list'])

const visible = computed({
  get: () => props.dialogVisible,
  set: (val) => emit('update:dialogVisible', val)
})

const isLoading = ref(false)
const formRef = ref()

const estadoOptions = [
  { label: 'Activo', value: 1, icon: 'check_circle', color: 'green' },
  { label: 'Inactivo', value: 0, icon: 'cancel', color: 'red' }
]

const state = reactive({
  title: 'Nueva Modalidad',
  form: {
    codm: null,
    nombre: '',
    estado: 1
  }
})

// Inicialización cada vez que se abre
watch(visible, (val) => {
  if (val) openFun()
})

const openFun = () => {
  resetForm(formRef.value)
  state.title = 'Nueva Modalidad'
  isLoading.value = false

  if (props.modalidadObj.codm) {
    state.title = 'Editar Modalidad'
    state.form = { ...props.modalidadObj }
  } else {
    state.form = { codm: null, nombre: '', estado: 1 }
  }
}

const submitForm = () => {
  formRef.value.validate().then((valid) => {
    if (valid) {
      isLoading.value = true
      editModalidad(state.form).then((res) => {
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
</script>

<style scoped>
.q-card-section {
  display: flex;
  flex-direction: column;
}
</style>
