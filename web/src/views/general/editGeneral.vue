<template>
  <el-dialog
    v-model="dialogVisible"
    :title="isEdit ? 'Editar Registro' : 'Registrar Usuario en Gestión'"
    width="600px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      label-width="120px"
    >
      <el-form-item label="Usuario" prop="ids">
        <el-select
          v-model="formData.ids"
          filterable
          placeholder="Seleccione usuario"
          :disabled="isEdit"
          style="width: 100%"
          @change="handleUserChange"
        >
          <el-option
            v-for="user in userList"
            :key="user.id"
            :label="`${user.username} - ${user.nickName}`"
            :value="user.id"
          >
            <span style="float: left">{{ user.username }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px">
              {{ user.nickName }}
            </span>
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="Gestión" prop="gestion">
        <el-select
          v-model="formData.gestion"
          placeholder="Seleccione gestión"
          :disabled="isEdit"
          style="width: 100%"
        >
          <el-option
            v-for="year in gestionesDisponibles"
            :key="year"
            :label="year"
            :value="year"
          />
        </el-select>
      </el-form-item>

      <!-- Información del usuario seleccionado -->
      <el-alert
        v-if="selectedUserInfo"
        :title="`Usuario: ${selectedUserInfo.username}`"
        type="info"
        :closable="false"
        style="margin-bottom: 20px"
      >
        <template #default>
          <p><strong>Nombre:</strong> {{ selectedUserInfo.nickName }}</p>
          <p><strong>Email:</strong> {{ selectedUserInfo.email }}</p>
        </template>
      </el-alert>
    </el-form>

    <template #footer>
      <el-button @click="handleClose">Cancelar</el-button>
      <el-button type="primary" :loading="submitLoading" @click="handleSubmit">
        {{ isEdit ? 'Actualizar' : 'Registrar' }}
      </el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, computed, watch, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
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

// Reglas de validación
const rules = {
  ids: [
    { required: true, message: 'Por favor seleccione un usuario', trigger: 'change' }
  ],
  gestion: [
    { required: true, message: 'Por favor seleccione una gestión', trigger: 'change' }
  ]
}

// Obtener lista de usuarios
const fetchUserList = async () => {
  try {
    const { data } = await getUserList({
      blurry: '',
      currentPage: 1,
      size: 1000
    })
    
    if (data.success) {
      userList.value = data.data.records
    }
  } catch (error) {
    ElMessage.error('Error al cargar usuarios: ' + error.message)
  }
}

// Cambio de usuario
const handleUserChange = (userId) => {
  selectedUserInfo.value = userList.value.find(u => u.id === userId)
}

// Enviar formulario
const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (!valid) return

    submitLoading.value = true
    try {
      const apiMethod = isEdit.value ? editGeneral : registrarEnGestion
      const { data } = await apiMethod(formData)

      if (data.success) {
        ElMessage.success(data.data)
        emit('refresh')
        handleClose()
      } else {
        ElMessage.error(data.msg)
      }
    } catch (error) {
      ElMessage.error('Error al guardar: ' + error.message)
    } finally {
      submitLoading.value = false
    }
  })
}

// Cerrar dialog
const handleClose = () => {
  formRef.value?.resetFields()
  selectedUserInfo.value = null
  dialogVisible.value = false
}

// Inicializar datos de edición
watch(() => props.editData, (newData) => {
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
}, { immediate: true })

onMounted(() => {
  fetchUserList()
})
</script>