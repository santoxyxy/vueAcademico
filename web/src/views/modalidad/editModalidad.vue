<template>
  <el-dialog 
    :title="state.title" 
    v-model="visible" 
    width="500px"
    :close-on-click-modal="false" 
    @opened="openFun"
  >
    <el-form 
      :model="state.form" 
      :rules="state.rules" 
      ref="formRef" 
      label-width="140px"
    >
      <el-form-item label="Nombre" prop="nombre">
        <el-input 
          v-model="state.form.nombre" 
          placeholder="Ingrese el nombre de la modalidad"
          maxlength="100"
          show-word-limit
        />
      </el-form-item>

      <el-form-item label="Estado" prop="estado">
        <el-radio-group v-model="state.form.estado">
          <el-radio :value="1">
            <el-icon style="vertical-align: middle; margin-right: 4px;"><CircleCheck /></el-icon>
            Activo
          </el-radio>
          <el-radio :value="0">
            <el-icon style="vertical-align: middle; margin-right: 4px;"><CircleClose /></el-icon>
            Inactivo
          </el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="resetForm(formRef)">Reiniciar</el-button>
      <el-button type="primary" :loading="isLoading" @click="submitForm">
        Guardar
      </el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { computed, reactive, ref } from 'vue'
import { CircleCheck, CircleClose } from '@element-plus/icons-vue'
import { editModalidad } from '@/api/modalidad/modalidad'
import { errorMsg, successMsg } from '@/utils/message'
import { resetForm } from '@/utils/common'

const props = defineProps({
  dialogVisible: { type: Boolean, required: true, default: false },
  modalidadObj: { type: Object, default: () => ({}) }
})

const emit = defineEmits(['update:dialogVisible', 'get-list'])

const visible = computed({
  get: () => props.dialogVisible,
  set: (val) => emit('update:dialogVisible', val)
})

const isLoading = ref(false)
const formRef = ref()

const state = reactive({
  title: 'Nueva Modalidad',
  form: {
    codm: null,
    nombre: '',
    estado: 1
  },
  rules: {
    nombre: [
      { required: true, message: 'El nombre es obligatorio', trigger: 'blur' },
      { min: 3, max: 100, message: 'Longitud entre 3 y 100 caracteres', trigger: 'blur' }
    ],
    estado: [{ required: true, message: 'El estado es obligatorio', trigger: 'change' }]
  }
})

const openFun = () => {
  resetForm(formRef.value)
  state.title = 'Nueva Modalidad'
  isLoading.value = false

  if (props.modalidadObj.codm) {
    state.title = 'Editar Modalidad'
    state.form = { ...props.modalidadObj }
  } else {
    state.form = { codm: null, nombre: '', estado: 1 }
  }
}

const submitForm = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      isLoading.value = true
      editModalidad(state.form).then((res) => {
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