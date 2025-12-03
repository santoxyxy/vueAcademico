<template>
  <q-dialog v-model="visible" persistent :maximized="false">
    <q-card style="min-width: 600px">
      <q-card-section>
        <div class="text-h6">{{ state.title }}</div>

        <q-form ref="formRef" @submit.prevent="submitForm" class="q-gutter-md">
          <q-row class="q-col-gutter-md">
            <q-col cols="12" sm="6">
              <q-input-number
                v-model="state.form.gestion"
                label="Gestión"
                :min="2020"
                :max="2030"
                outlined
                dense
                style="width: 100%"
                :rules="[val => val ? true : 'La gestión es obligatoria']"
              />
            </q-col>

            <q-col cols="12" sm="6">
              <q-select
                v-model="state.form.codp"
                label="Periodo"
                :options="[{label:'1° Periodo', value:1},{label:'2° Periodo', value:2}]"
                outlined
                dense
                style="width: 100%"
                :rules="[val => val ? true : 'El periodo es obligatorio']"
              />
            </q-col>
          </q-row>

          <q-select
            v-model="state.form.codmat"
            label="Materia"
            :options="state.materiaList.map(item => ({ label: `${item.codmat} - ${item.nombre}`, value: item.codmat }))"
            option-value="value"
            option-label="label"
            emit-value
            map-options
            outlined
            dense
            style="width: 100%"
            @input="onMateriaChange"
            :rules="[val => val ? true : 'Debe seleccionar una materia']"
          />

          <q-select
            v-model="state.form.codpar"
            label="Paralelo"
            :options="state.paraleloList.map(item => ({ label: item.nombre, value: item.codpar }))"
            option-value="value"
            option-label="label"
            emit-value
            map-options
            outlined
            dense
            style="width: 100%"
            :rules="[val => val ? true : 'Debe seleccionar un paralelo']"
          />

          <q-select
            v-model="state.form.ids"
            label="Docente"
            :options="state.docenteList.map(item => ({ 
              label: `${item.nombre} ${item.ap || ''} ${item.am || ''} (${item.tipo || 'N/A'})`, 
              value: item.ids,
              color: getDocenteTagType(item.tipo)
            }))"
            option-value="value"
            option-label="label"
            emit-value
            map-options
            outlined
            dense
            style="width: 100%"
            :rules="[val => val ? true : 'Debe seleccionar un docente']"
          />
        </q-form>
      </q-card-section>

      <q-card-actions align="right">
        <q-btn flat label="Reiniciar" @click="resetForm(formRef)" />
        <q-btn
          color="primary"
          label="Guardar"
          :loading="isLoading"
          @click="submitForm"
        />
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script setup>
import { reactive, ref, computed } from 'vue'
import { editDicta } from '@/api/dicta/dicta'
import { errorMsg, successMsg } from '@/utils/message'
import { resetForm } from '@/utils/common'

const props = defineProps({
  dialogVisible: { type: Boolean, required: true, default: false },
  dictaObj: { type: Object, default: () => ({}) }
})

const emit = defineEmits(['update:dialogVisible', 'get-list'])

const visible = computed({
  get: () => props.dialogVisible,
  set: val => emit('update:dialogVisible', val)
})

const isLoading = ref(false)
const formRef = ref()

const state = reactive({
  title: 'Nueva Asignación',
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

import { watch } from 'vue'

watch(visible, (val) => {
  if (val) {
    resetForm(formRef.value)
    state.title = props.dictaObj.codpar ? 'Editar Asignación' : 'Nueva Asignación'
    isLoading.value = false

    state.form = props.dictaObj.codpar
      ? { ...props.dictaObj }
      : {
          codpar: null,
          codp: 1,
          codmat: '',
          gestion: props.dictaObj.gestion || new Date().getFullYear(),
          ids: null
        }

    loadSelectOptions()
  }
})


// Datos de ejemplo
const loadSelectOptions = () => {
  state.materiaList = [
    { codmat: 'MAT101', nombre: 'Matemáticas I', codn: 1 },
    { codmat: 'FIS101', nombre: 'Física I', codn: 1 },
    { codmat: 'MAT201', nombre: 'Matemáticas II', codn: 2 }
  ]

  state.paraleloList = [
    { codpar: 1, nombre: 'A' },
    { codpar: 2, nombre: 'B' },
    { codpar: 3, nombre: 'C' }
  ]

  state.docenteList = [
    { ids: 1, nombre: 'Juan', ap: 'Pérez', am: 'López', tipo: 'Titular' },
    { ids: 2, nombre: 'María', ap: 'García', am: 'Ruiz', tipo: 'Invitado' },
    { ids: 3, nombre: 'Carlos', ap: 'Fernández', am: '', tipo: 'Auxiliar' }
  ]
}

const onMateriaChange = codmat => {
  console.log('Materia seleccionada:', codmat)
}

const getDocenteTagType = tipo => {
  const types = { 'Titular': 'green', 'Invitado': 'orange', 'Auxiliar': 'blue' }
  return types[tipo] || 'grey'
}

const submitForm = () => {
  formRef.value.validate().then(valid => {
    if (valid) {
      isLoading.value = true
      editDicta(state.form).then(res => {
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
