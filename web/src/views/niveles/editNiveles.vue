<template>
  <q-dialog v-model="dialogVisible" persistent>
    <q-card style="min-width: 400px; max-width: 500px;">
      <q-card-section>
        <div class="text-h6">{{ state.title }}</div>
      </q-card-section>

      <q-card-section>
        <q-form ref="formRef" @submit.prevent="submitFun">
          <q-input
            dense
            outlined
            v-model="state.form.nombre"
            label="Nombre"
            placeholder="Ingrese nombre del nivel (ej: Primaria, Secundaria)"
            :rules="[val => !!val || 'Por favor ingrese nombre']"
          />

          <div class="q-mt-md">
            <q-radio-group
              v-model="state.form.estado"
              inline
              :options="estadoOptions"
              :rules="[val => val !== null || 'Por favor seleccione estado']"
            />
          </div>
        </q-form>
      </q-card-section>

      <q-card-actions align="right">
        <q-btn flat label="Cancelar" color="grey" @click="closeFun" />
        <q-btn
          label="Guardar"
          color="primary"
          :loading="isLoading"
          @click="submitFun"
        />
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script setup>
import { reactive, ref, watch, computed } from 'vue'
import { editNiveles } from '@/api/niveles/niveles'
import { successMsg, errorMsg } from '@/utils/message'

const props = defineProps({
  dialogVisible: Boolean,
  nivelesObj: Object
})

const emit = defineEmits(['update:dialogVisible', 'get-list'])

const dialogVisible = computed({
  get: () => props.dialogVisible,
  set: val => emit('update:dialogVisible', val)
})

const formRef = ref(null)
const isLoading = ref(false)

const estadoOptions = [
  { label: 'Activo', value: 1 },
  { label: 'Inactivo', value: 0 }
]

const state = reactive({
  title: 'Nuevo Nivel',
  form: {
    codn: null,
    nombre: '',
    estado: 1
  }
})

watch(
  () => props.nivelesObj,
  (newVal) => {
    if (newVal && newVal.codn) {
      state.title = 'Editar Nivel'
      state.form = { ...newVal }
    } else {
      state.title = 'Nuevo Nivel'
      state.form = { codn: null, nombre: '', estado: 1 }
    }
  },
  { immediate: true, deep: true }
)

// Cerrar Dialog
const closeFun = () => {
  formRef.value?.resetValidation()
  dialogVisible.value = false
}

// Guardar
const submitFun = () => {
  formRef.value.validate().then((valid) => {
    if (valid) {
      isLoading.value = true
      editNiveles(state.form).then((res) => {
        if (res.success) {
          successMsg(res.data)
          emit('get-list')
          closeFun()
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
.q-radio-group {
  display: flex;
  gap: 15px;
}
</style>
