<template>
  <q-dialog v-model="dialogVisible" persistent>
    <q-card style="min-width: 400px">
      <q-card-section>
        <div class="text-h6">{{ state.title }}</div>
      </q-card-section>

      <q-form ref="formRef" @submit.prevent="submitFun">
        <q-card-section>
          <q-input
            filled
            v-model="state.form.nombre"
            label="Nombre"
            placeholder="Ingrese nombre del ítem (ej: Examen Final, Trabajos)"
            :rules="[val => !!val || 'Por favor ingrese nombre']"
            lazy-rules
          />

          <q-option-group
            v-model="state.form.estado"
            label="Estado"
            :options="[
              { label: 'Activo', value: 1 },
              { label: 'Inactivo', value: 0 }
            ]"
            type="radio"
            inline
            class="q-mt-md"
          />
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat label="Cancelar" color="grey" v-close-popup @click="closeFun" />
          <q-btn label="Guardar" color="primary" @click="submitFun" />
        </q-card-actions>
      </q-form>
    </q-card>
  </q-dialog>
</template>

<script setup>
import { reactive, ref, watch, computed } from 'vue'
import { editItems } from '@/api/items/items'
import { errorMsg, successMsg } from '@/utils/message'

const props = defineProps({
  dialogVisible: Boolean,
  itemsObj: Object
})

const emit = defineEmits(['update:dialogVisible', 'getList'])

const dialogVisible = computed({
  get: () => props.dialogVisible,
  set: (val) => emit('update:dialogVisible', val)
})

const formRef = ref(null)

const state = reactive({
  title: 'Nuevo Ítem',
  form: {
    codi: null,
    nombre: '',
    estado: 1
  }
})

// Actualizar formulario al abrir
watch(
  () => props.itemsObj,
  (newVal) => {
    if (newVal && newVal.codi) {
      state.title = 'Editar Ítem'
      state.form = { ...newVal }
    } else {
      state.title = 'Nuevo Ítem'
      state.form = { codi: null, nombre: '', estado: 1 }
    }
  },
  { deep: true, immediate: true }
)

// Cerrar Dialog
const closeFun = () => {
  dialogVisible.value = false
  formRef.value?.resetValidation && formRef.value.resetValidation()
}

// Guardar
const submitFun = () => {
  if (!state.form.nombre) {
    errorMsg('Por favor ingrese nombre')
    return
  }

  editItems(state.form).then((res) => {
    if (res.success) {
      successMsg(res.data)
      emit('getList')
      closeFun()
    } else {
      errorMsg(res.msg)
    }
  })
}
</script>

<style scoped>
</style>
