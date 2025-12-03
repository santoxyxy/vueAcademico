<template>
  <q-dialog v-model="dialogVisible" persistent>
    <q-card style="min-width: 400px;">
      <q-card-section>
        <div class="text-h6">{{ state.title }}</div>
      </q-card-section>

      <q-card-section>
        <q-form ref="formRef" @submit.prevent="submitFun">
          <q-input
            filled
            v-model="state.form.nombre"
            label="Nombre"
            :rules="[val => !!val || 'Por favor ingrese nombre']"
          ></q-input>

          <q-option-group
            v-model="state.form.estado"
            type="radio"
            :options="estadoOptions"
            label="Estado"
            class="q-mt-md"
          ></q-option-group>
        </q-form>
      </q-card-section>

      <q-card-actions align="right">
        <q-btn flat label="Cancelar" color="primary" v-close-popup @click="closeFun"></q-btn>
        <q-btn flat label="Guardar" color="primary" @click="submitFun"></q-btn>
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script setup>
import { ref, reactive, watch, computed } from 'vue'
import { editParalelo } from '../../api/paralelo/paralelo'
import { successMsg, errorMsg } from '../../utils/message'

const props = defineProps({
  dialogVisible: Boolean,
  paraleloObj: Object
})

const emit = defineEmits(['update:dialogVisible', 'get-list'])

const dialogVisible = computed({
  get: () => props.dialogVisible,
  set: val => emit('update:dialogVisible', val)
})

const formRef = ref(null)

const estadoOptions = [
  { label: 'Activo', value: 1 },
  { label: 'Inactivo', value: 0 }
]

const state = reactive({
  title: 'Nuevo Paralelo',
  form: {
    codp: null,
    nombre: '',
    estado: 1
  }
})

watch(
  () => props.paraleloObj,
  (newVal) => {
    if (newVal && newVal.codp) {
      state.title = 'Editar Paralelo'
      state.form = { ...newVal }
    } else {
      state.title = 'Nuevo Paralelo'
      state.form = { codp: null, nombre: '', estado: 1 }
    }
  },
  { immediate: true, deep: true }
)

const closeFun = () => {
  formRef.value?.resetValidation?.()
  dialogVisible.value = false
}

const submitFun = async () => {
  if (!state.form.nombre) {
    errorMsg('Por favor ingrese nombre')
    return
  }

  try {
    const res = await editParalelo(state.form)
    if (res.success) {
      successMsg(res.data)
      emit('get-list')
      closeFun()
    } else {
      errorMsg(res.msg)
    }
  } catch (err) {
    errorMsg(err.message)
  }
}
</script>

<style scoped>
</style>
