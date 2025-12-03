<template>
  <div>
    <q-dialog v-model="dialogVisible" persistent>
      <q-card style="min-width: 500px; max-width: 600px;">
        <q-card-section>
          <div class="text-h6">{{ state.title }}</div>
        </q-card-section>

        <q-card-section>
          <q-form ref="formRef" @submit.prevent="submitFun">
            <q-input
              dense
              outlined
              label="Código Materia"
              v-model="state.form.codmat"
              :rules="[val => !!val || 'Requerido']"
              placeholder="Ej: MAT101"
              class="q-mb-sm"
            />

            <q-input
              dense
              outlined
              type="number"
              label="ID Usuario"
              v-model.number="state.form.idusuario"
              :rules="[val => val !== null || 'Requerido']"
              class="q-mb-sm"
            />

            <q-input
              dense
              outlined
              type="number"
              label="Código Ítem"
              v-model.number="state.form.codi"
              :rules="[val => val !== null || 'Requerido']"
              class="q-mb-sm"
            />

            <q-input
              dense
              outlined
              type="number"
              label="Código Paralelo"
              v-model.number="state.form.coda"
              :rules="[val => val !== null || 'Requerido']"
              class="q-mb-sm"
            />

            <q-input
              dense
              outlined
              type="number"
              label="Código Modalidad"
              v-model.number="state.form.codp"
              :rules="[val => val !== null || 'Requerido']"
              class="q-mb-sm"
            />

            <q-input
              dense
              outlined
              type="number"
              label="Gestión"
              v-model.number="state.form.gestion"
              :rules="[val => val !== null || 'Requerido']"
              :min="2020"
              :max="2050"
              class="q-mb-sm"
            />

            <q-input
              dense
              outlined
              type="number"
              label="Nota"
              v-model.number="state.form.nota"
              :rules="[val => val !== null || 'Requerido', val => val >= 0 && val <= 100 || 'Entre 0 y 100']"
              class="q-mb-sm"
            />

            <q-input
              dense
              outlined
              type="number"
              label="Código Modalidad"
              v-model.number="state.form.coddm"
              class="q-mb-sm"
            />
          </q-form>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat label="Cancelar" color="secondary" @click="closeFun" />
          <q-btn flat label="Guardar" color="primary" @click="submitFun" />
        </q-card-actions>
      </q-card>
    </q-dialog>
  </div>
</template>

<script setup>
import { reactive, ref, computed, watch } from 'vue'
import { editNota } from '@/api/notas/notas'
import { successMsg, errorMsg } from '@/utils/message'

const props = defineProps({
  dialogVisible: Boolean,
  notaObj: Object
})

const emit = defineEmits(['update:dialogVisible', 'getList'])

const dialogVisible = computed({
  get: () => props.dialogVisible,
  set: val => emit('update:dialogVisible', val)
})

const formRef = ref(null)

const state = reactive({
  title: 'Nueva Nota',
  form: {
    codmat: '',
    codi: null,
    coda: null,
    codp: null,
    gestion: new Date().getFullYear(),
    idusuario: null,
    nota: 0,
    coddm: null
  }
})

watch(
  () => props.notaObj,
  newVal => {
    if (newVal && newVal.codmat) {
      state.title = 'Editar Nota'
      state.form = { ...newVal }
    } else {
      state.title = 'Nueva Nota'
      state.form = {
        codmat: '',
        codi: null,
        coda: null,
        codp: null,
        gestion: newVal?.gestion || new Date().getFullYear(),
        idusuario: null,
        nota: 0,
        coddm: null
      }
    }
  },
  { deep: true, immediate: true }
)

const closeFun = () => {
  formRef.value?.resetValidation()
  dialogVisible.value = false
}

const submitFun = () => {
  formRef.value?.validate().then(valid => {
    if (valid) {
      editNota(state.form).then(res => {
        if (res.success) {
          successMsg(res.data)
          emit('getList')
          closeFun()
        } else errorMsg(res.msg)
      })
    }
  })
}
</script>

<style scoped>
.q-mb-sm {
  margin-bottom: 8px;
}
</style>
