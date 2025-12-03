<template>
  <el-dialog
    :model-value="dialogVisible"
    width="500px"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    @close="cerrarDialogo"
  >
    <template #title>
      {{ form.idcliente ? 'Editar Cliente' : 'Nuevo Cliente' }}
    </template>

    <el-form :model="form" label-width="140px" class="form-body">
      <el-form-item label="Nombre">
        <el-input v-model="form.nombre" placeholder="Nombre" />
      </el-form-item>

      <el-form-item label="Apellidos">
        <el-input v-model="form.apellidos" placeholder="Apellidos" />
      </el-form-item>

      <el-form-item label="Compañía">
        <el-input v-model="form.compania" placeholder="Compañía" />
      </el-form-item>

      <el-form-item label="Cargo">
        <el-input v-model="form.cargo" placeholder="Cargo" />
      </el-form-item>

      <el-form-item label="Teléfono">
        <el-input v-model="form.telefono" placeholder="Teléfono" />
      </el-form-item>

      <el-form-item label="Correo electrónico">
        <el-input v-model="form.email" placeholder="Correo electrónico" />
      </el-form-item>
    </el-form>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="cerrarDialogo">Cancelar</el-button>
        <el-button type="primary" @click="guardarCliente">Guardar</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { reactive, watch, computed } from 'vue'
import { editModulo1 } from '../../../api/modulo1/modulo1'
import { successMsg, errorMsg } from '../../../utils/message'

// Props
const props = defineProps({
  modelValue: { type: Boolean, default: false },
  clienteObj: { type: Object, default: () => ({}) },
})

// Emits
const emit = defineEmits(['update:modelValue', 'close'])

// Vincular el v-model al diálogo
const dialogVisible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val),
})

// Datos del formulario
const form = reactive({
  idcliente: null,
  nombre: '',
  apellidos: '',
  compania: '',
  cargo: '',
  telefono: '',
  email: '',
})

// Cargar datos al editar
watch(
  () => props.clienteObj,
  (nuevo) => {
    if (nuevo) Object.assign(form, nuevo)
  },
  { immediate: true }
)

// Limpiar datos si es nuevo
watch(
  () => props.modelValue,
  (visible) => {
    if (visible && !props.clienteObj?.idcliente) {
      Object.keys(form).forEach((k) => (form[k] = ''))
      form.idcliente = null
    }
  }
)

// Cerrar diálogo
const cerrarDialogo = () => {
  emit('update:modelValue', false)
  emit('close')
}

// Guardar cliente
const guardarCliente = async () => {
  try {
    const res = await editModulo1(form)

    if (res.success) {
      successMsg(
        form.idcliente
          ? 'Cliente actualizado correctamente'
          : 'Cliente registrado correctamente'
      )
    } else {
      errorMsg(res.msg || 'Error al guardar cliente')
    }

    cerrarDialogo()
  } catch (err) {
    console.error(err)
    errorMsg('Error al guardar cliente')
  }
}
</script>

<style scoped>
.form-body {
  padding-right: 10px;
}
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>
