<template>
  <q-dialog v-model="dialogVisible" persistent>
    <q-card style="min-width: 400px; max-width: 500px;">
      <q-card-section>
        <div class="text-h6">{{ form.id ? 'Editar Cliente' : 'Nuevo Cliente' }}</div>
      </q-card-section>

      <q-card-section>
        <q-form ref="formRef" @submit.prevent="handleSubmit">
          <q-input
            v-model="form.Compañia"
            label="Compañía"
            :rules="[val => !!val || 'Compañía es requerida']"
            dense
            outlined
            class="q-mb-sm"
          />

          <q-input
            v-model="form.Apellidos"
            label="Apellidos"
            dense
            outlined
            class="q-mb-sm"
          />

          <q-input
            v-model="form.Nombre"
            label="Nombre"
            :rules="[val => !!val || 'Nombre es requerido']"
            dense
            outlined
            class="q-mb-sm"
          />

          <q-input
            v-model="form.Direccion"
            label="Dirección"
            dense
            outlined
            class="q-mb-sm"
          />

          <q-input
            v-model="form.Ciudad"
            label="Ciudad"
            dense
            outlined
          />
        </q-form>
      </q-card-section>

      <q-card-actions align="right">
        <q-btn flat label="Cancelar" color="primary" @click="handleClose" />
        <q-btn
          label="Guardar"
          color="primary"
          :loading="submitting"
          @click="handleSubmit"
        />
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script setup>
import { ref, reactive, watch } from 'vue'
import { Notify } from 'quasar'

// Props
const props = defineProps({
  dialogVisible: Boolean,
  clienteObj: { type: Object, default: () => ({}) }
})

// Emits
const emit = defineEmits(['update:dialogVisible', 'getList'])

// Refs y estado
const formRef = ref()
const submitting = ref(false)

const form = reactive({
  id: '',
  Compañia: '',
  Apellidos: '',
  Nombre: '',
  Direccion: '',
  Ciudad: ''
})

// Sincronizar props con form
watch(
  () => props.clienteObj,
  (newVal) => {
    if (newVal && Object.keys(newVal).length) {
      Object.assign(form, newVal)
    } else {
      Object.keys(form).forEach(key => (form[key] = ''))
    }
  },
  { immediate: true }
)

// Control de diálogo
const dialogVisible = ref(props.dialogVisible)
watch(() => props.dialogVisible, val => dialogVisible.value = val)
watch(dialogVisible, val => emit('update:dialogVisible', val))

// Métodos
const handleClose = () => {
  dialogVisible.value = false
  if (formRef.value) formRef.value.resetValidation()
}

const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    const valid = await formRef.value.validate()
    if (!valid) {
      Notify.create({ type: 'warning', message: 'Complete los campos requeridos' })
      return
    }

    submitting.value = true

    // Simulación de API
    console.log('Datos del cliente:', form)

    Notify.create({
      type: 'positive',
      message: form.id ? 'Cliente actualizado (simulado)' : 'Cliente creado (simulado)'
    })

    emit('getList')
    handleClose()
  } catch (err) {
    console.error(err)
    Notify.create({ type: 'negative', message: 'Error al guardar' })
  } finally {
    submitting.value = false
  }
}
</script>
