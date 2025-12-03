<template>
  <q-dialog v-model="visible" persistent maximized-width="600px" @show="openFun">
    <q-card>
      <q-card-section class="text-h6">
        {{ state.title }}
      </q-card-section>

      <q-card-section>
        <q-form ref="formRef" @submit.prevent="submitForm">
          <div class="row q-col-gutter-md">
            <div class="col-6">
              <q-input
                dense
                outlined
                type="number"
                v-model.number="state.form.gestion"
                label="Gestión"
                :min="2020"
                :max="2030"
              />
            </div>

            <div class="col-6">
              <q-select
                dense
                outlined
                v-model="state.form.codmat"
                label="Materia"
                :options="state.materiaList.map(item => ({ label: `${item.codmat} - ${item.nombre}`, value: item.codmat }))"
                use-input
                input-debounce="0"
                emit-value
                map-options
                clearable
              />
            </div>
          </div>

          <div class="row q-col-gutter-md q-mt-md">
            <div class="col-6">
              <q-select
                dense
                outlined
                v-model="state.form.codpar"
                label="Paralelo"
                :options="state.paraleloList.map(item => ({ label: item.nombre, value: item.codpar }))"
                clearable
              />
            </div>

            <div class="col-6">
              <q-input
                dense
                outlined
                type="number"
                v-model.number="state.form.codp"
                label="Periodo"
                :min="1"
                :max="2"
                placeholder="1 o 2"
              />
            </div>
          </div>

          <div class="q-mt-md">
            <q-select
              dense
              outlined
              v-model="state.form.ids"
              label="Docente"
              :options="state.docenteList.map(item => ({ label: `${item.nombre} ${item.ap || ''} ${item.am || ''}`, value: item.ids }))"
              use-input
              input-debounce="0"
              emit-value
              map-options
              clearable
            />
          </div>
        </q-form>
      </q-card-section>

      <q-card-actions align="right">
        <q-btn dense flat label="Reiniciar" @click="resetForm(formRef)" />
        <q-btn dense color="primary" label="Guardar" :loading="isLoading" @click="submitForm" />
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script setup>
import { reactive, ref, computed } from 'vue'
import { editProgra } from '@/api/progra/progra'
import { errorMsg, successMsg } from '@/utils/message'
import { resetForm } from '@/utils/common'

const props = defineProps({
  dialogVisible: { type: Boolean, required: true, default: false },
  prograObj: { type: Object, default: () => ({}) }
})

const emit = defineEmits(['update:dialogVisible', 'get-list'])

const visible = computed({
  get: () => props.dialogVisible,
  set: (val) => emit('update:dialogVisible', val)
})

const isLoading = ref(false)
const formRef = ref()

const state = reactive({
  title: 'Nueva Clase',
  form: {
    codpar: null,
    codp: 1,
    codmat: '',
    gestion: new Date().getFullYear(),
    ids: null
  },
  materiaList: [],
  paraleloList: [],
  docenteList: []
})

// Abrir dialog y cargar datos
const openFun = () => {
  resetForm(formRef.value)
  state.title = 'Nueva Clase'
  isLoading.value = false
  loadSelectOptions()

  if (props.prograObj.codpar) {
    state.title = 'Editar Clase'
    state.form = { ...props.prograObj }
  } else {
    state.form = {
      codpar: null,
      codp: 1,
      codmat: '',
      gestion: props.prograObj.gestion || new Date().getFullYear(),
      ids: null
    }
  }
}

// Cargar opciones para selects (ejemplo temporal)
const loadSelectOptions = () => {
  state.materiaList = [
    { codmat: 'MAT101', nombre: 'Matemáticas I' },
    { codmat: 'FIS101', nombre: 'Física I' }
  ]
  state.paraleloList = [
    { codpar: 1, nombre: 'A' },
    { codpar: 2, nombre: 'B' }
  ]
  state.docenteList = [
    { ids: 1, nombre: 'Juan', ap: 'Pérez', am: 'López' },
    { ids: 2, nombre: 'María', ap: 'García', am: 'Ruiz' }
  ]
}

// Guardar formulario
const submitForm = () => {
  formRef.value.validate().then((valid) => {
    if (valid) {
      isLoading.value = true
      editProgra(state.form).then((res) => {
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
</style>
