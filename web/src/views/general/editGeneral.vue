<template>
  <q-dialog v-model="dialogVisible" persistent maximized>
    <q-card style="min-width: 500px; max-width: 600px">
      <q-card-section class="text-h6">
        {{ isEdit ? 'Editar Registro' : 'Registrar Usuario en Gestión' }}
      </q-card-section>

      <q-card-section>
        <q-form ref="formRef" @submit.prevent="handleSubmit">
          <!-- Usuario -->
          <q-select
            filled
            label="Usuario"
            v-model="formData.ids"
            :options="userList.map(u => ({ label: `${u.username} - ${u.nickName}`, value: u.id }))"
            emit-value
            map-options
            use-input
            input-debounce="300"
            :disable="isEdit"
            @update:model-value="handleUserChange"
            class="q-mb-md"
            :rules="[val => !!val || 'Por favor seleccione un usuario']"
          />

          <!-- Gestión -->
          <q-select
            filled
            label="Gestión"
            v-model="formData.gestion"
            :options="gestionesDisponibles.map(y => ({ label: y, value: y }))"
            :disable="isEdit"
            class="q-mb-md"
            :rules="[val => !!val || 'Por favor seleccione una gestión']"
          />

          <!-- Información del usuario seleccionado -->
          <q-banner v-if="selectedUserInfo" class="q-mb-md" dense>
            <div><strong>Usuario:</strong> {{ selectedUserInfo.username }}</div>
            <div><strong>Nombre:</strong> {{ selectedUserInfo.nickName }}</div>
            <div><strong>Email:</strong> {{ selectedUserInfo.email }}</div>
          </q-banner>
        </q-form>
      </q-card-section>

      <q-card-actions align="right">
        <q-btn flat label="Cancelar" color="primary" @click="handleClose" />
        <q-btn
          label="Guardar"
          color="primary"
          :loading="submitLoading"
          @click="handleSubmit"
        >
          {{ isEdit ? 'Actualizar' : 'Registrar' }}
        </q-btn>
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script setup>
import { ref, reactive, computed, watch, onMounted } from 'vue'
import { Notify } from 'quasar'
import { editGeneral, registrarEnGestion } from '@/api/general/general'
import { getUserList } from '@/api/user/sysUser'

const props = defineProps({
  modelValue: Boolean,
  editData: Object
})

const emit = defineEmits(['update:modelValue', 'refresh'])

// Estado
const formRef = ref(null)
const submitLoading = ref(false)
const userList = ref([])
const selectedUserInfo = ref(null)

// Dialog visibility
const dialogVisible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})

// Es edición?
const isEdit = computed(() => !!props.editData)

// Formulario
const formData = reactive({
  ids: null,
  gestion: new Date().getFullYear()
})

// Gestiones disponibles
const gestionesDisponibles = computed(() => {
  const currentYear = new Date().getFullYear()
  return Array.from({ length: 5 }, (_, i) => currentYear - i + 1)
})

// Obtener lista de usuarios
const fetchUserList = async () => {
  try {
    const { data } = await getUserList({ blurry: '', currentPage: 1, size: 1000 })
    if (data.success) userList.value = data.data.records
  } catch (error) {
    Notify.create({ type: 'negative', message: 'Error al cargar usuarios: ' + error.message })
  }
}

// Cambio de usuario
const handleUserChange = (userId) => {
  selectedUserInfo.value = userList.value.find(u => u.id === userId)
}

// Enviar formulario
const handleSubmit = async () => {
  if (!formRef.value) return

  const valid = await formRef.value.validate()
  if (!valid) return

  submitLoading.value = true
  try {
    const apiMethod = isEdit.value ? editGeneral : registrarEnGestion
    const { data } = await apiMethod(formData)
    if (data.success) {
      Notify.create({ type: 'positive', message: data.data })
      emit('refresh')
      handleClose()
    } else {
      Notify.create({ type: 'negative', message: data.msg })
    }
  } catch (error) {
    Notify.create({ type: 'negative', message: 'Error al guardar: ' + error.message })
  } finally {
    submitLoading.value = false
  }
}

// Cerrar dialog
const handleClose = () => {
  formRef.value?.resetValidation()
  selectedUserInfo.value = null
  dialogVisible.value = false
}

// Inicializar datos de edición
watch(
  () => props.editData,
  (newData) => {
    if (newData) {
      formData.ids = newData.ids
      formData.gestion = newData.gestion
      if (newData.ids) {
        selectedUserInfo.value = {
          username: newData.username,
          nickName: newData.nickName,
          email: newData.email
        }
      }
    } else {
      formData.ids = null
      formData.gestion = new Date().getFullYear()
    }
  },
  { immediate: true }
)

onMounted(() => {
  fetchUserList()
})
</script>
