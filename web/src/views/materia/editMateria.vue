<template>
  <q-dialog v-model="dialogVisible" persistent>
    <q-card style="min-width: 500px; max-width: 600px;">
      <q-card-section>
        <div class="text-h6">{{ isEdit ? 'Editar Materia' : 'Nueva Materia' }}</div>
      </q-card-section>

      <q-card-section>
        <q-form ref="formRef" @submit.prevent="handleSubmit" class="q-gutter-md">
          <!-- Código -->
          <q-input
            v-model="formData.codmat"
            label="Código"
            :disable="isEdit"
            maxlength="15"
            :rules="[val => !!val || 'El código es obligatorio', val => (val && val.length >=3 && val.length <=15) || 'Debe tener entre 3 y 15 caracteres']"
          />
          <div class="text-caption text-grey-7">
            Máximo 15 caracteres. No se puede modificar una vez creado.
          </div>

          <!-- Nombre -->
          <q-input
            v-model="formData.nombre"
            label="Nombre"
            maxlength="30"
            :rules="[val => !!val || 'El nombre es obligatorio', val => (!val || val.length <=30) || 'Máximo 30 caracteres']"
          />

          <!-- Nivel -->
          <q-select
            v-model="formData.codn"
            label="Nivel"
            :options="nivelesList.map(n => ({ label: n.nombre, value: n.codn }))"
            option-label="label"
            option-value="value"
            emit-value
            map-options
            :rules="[val => !!val || 'Debe seleccionar un nivel']"
            use-chips
            clearable
          />

          <!-- Estado -->
          <q-toggle
            v-model="formData.estado"
            label="Estado"
            true-value="1"
            false-value="0"
            left-label
            color="primary"
          >
            <template v-slot:label>
              {{ formData.estado === 1 ? 'Activo' : 'Inactivo' }}
            </template>
          </q-toggle>
        </q-form>
      </q-card-section>

      <q-card-actions align="right">
        <q-btn flat label="Cancelar" color="grey" @click="handleClose" />
        <q-btn
          label="Guardar"
          color="primary"
          :loading="loading"
          @click="handleSubmit"
        />
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script setup>
import { ref, reactive, computed, watch } from 'vue'
import { editMateria } from '@/api/materia/materia'
import { getNivelesList } from '@/api/niveles/niveles'
import { errorMsg, successMsg } from '@/utils/message'

const props = defineProps({
  modelValue: Boolean,
  materiaData: Object
})
const emit = defineEmits(['update:modelValue', 'success'])

const dialogVisible = computed({
  get: () => props.modelValue,
  set: val => emit('update:modelValue', val)
})

const formRef = ref(null)
const loading = ref(false)
const nivelesList = ref([])

const formData = reactive({
  codmat: '',
  nombre: '',
  codn: null,
  estado: '1'
})

const isEdit = computed(() => !!props.materiaData?.codmat)

// Cargar niveles
const loadNiveles = async () => {
  try {
    const res = await getNivelesList()
    if (res.success) nivelesList.value = res.data
    else errorMsg(res.msg || 'Error al cargar niveles')
  } catch (err) {
    errorMsg(err.message || 'Error al cargar niveles')
  }
}

// Resetear formulario
const resetForm = () => {
  if (props.materiaData) {
    Object.assign(formData, { ...props.materiaData, estado: String(props.materiaData.estado) })
  } else {
    Object.assign(formData, { codmat: '', nombre: '', codn: null, estado: '1' })
  }
  formRef.value?.resetValidation()
}

// Enviar formulario
const handleSubmit = async () => {
  if (!formRef.value) return
  formRef.value.validate().then(async valid => {
    if (!valid) return
    loading.value = true
    try {
      const res = await editMateria({ ...formData, estado: Number(formData.estado) })
      if (res.success) {
        successMsg(res.msg || 'Operación exitosa')
        emit('success')
        handleClose()
      } else {
        errorMsg(res.msg || 'Error al guardar')
      }
    } catch (err) {
      errorMsg(err.message || 'Error en la operación')
    } finally {
      loading.value = false
    }
  })
}

// Cerrar diálogo
const handleClose = () => {
  resetForm()
  dialogVisible.value = false
}

// Watcher para abrir diálogo
watch(() => props.modelValue, (val) => {
  if (val) {
    loadNiveles()
    resetForm()
  }
}, { immediate: true })
</script>

<style scoped>
.text-caption {
  margin-top: -10px;
  margin-bottom: 10px;
}
</style>
