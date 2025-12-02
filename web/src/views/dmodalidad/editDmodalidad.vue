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
      <el-form-item label="Nombre Detalle" prop="nombre">
        <el-input 
          v-model="state.form.nombre" 
          placeholder="Ingrese el nombre del detalle"
        />
      </el-form-item>

      <el-form-item label="Modalidad Padre" prop="codm">
        <el-select 
          v-model="state.form.codm" 
          placeholder="Seleccione modalidad"
          filterable
          style="width: 100%"
        >
          <el-option 
            v-for="item in state.modalidadList" 
            :key="item.codm" 
            :label="item.nombre" 
            :value="item.codm"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="Estado" prop="estado">
        <el-radio-group v-model="state.form.estado">
          <el-radio :value="1">Activo</el-radio>
          <el-radio :value="0">Inactivo</el-radio>
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
import { editDmodalidad } from '@/api/dmodalidad/dmodalidad'
import { errorMsg, successMsg } from '@/utils/message'
import { resetForm } from '@/utils/common'

const props = defineProps({
  dialogVisible: { type: Boolean, required: true, default: false },
  dmodalidadObj: { type: Object, default: () => ({}) }
})

const emit = defineEmits(['update:dialogVisible', 'get-list'])

const visible = computed({
  get: () => props.dialogVisible,
  set: (val) => emit('update:dialogVisible', val)
})

const isLoading = ref(false)
const formRef = ref()

const state = reactive({
  title: 'Nuevo Detalle',
  form: {
    coddm: null,
    nombre: '',
    codm: null,
    estado: 1
  },
  rules: {
    nombre: [{ required: true, message: 'El nombre es obligatorio', trigger: 'blur' }],
    codm: [{ required: true, message: 'Debe seleccionar una modalidad', trigger: 'change' }],
    estado: [{ required: true, message: 'El estado es obligatorio', trigger: 'change' }]
  },
  modalidadList: [] // Deberías llenar esto con una llamada API a /sys/modalidad/list
})

const openFun = () => {
  resetForm(formRef.value)
  state.title = 'Nuevo Detalle'
  isLoading.value = false
  
  // Cargar modalidades padre (necesitarás crear ese endpoint)
  getModalidadList()

  if (props.dmodalidadObj.coddm) {
    state.title = 'Editar Detalle'
    state.form = { ...props.dmodalidadObj }
  } else {
    state.form = { coddm: null, nombre: '', codm: null, estado: 1 }
  }
}

// TODO: Implementar llamada real a API de modalidades
const getModalidadList = () => {
  // Ejemplo temporal - reemplazar con llamada real
  state.modalidadList = [
    { codm: 1, nombre: 'Presencial' },
    { codm: 2, nombre: 'Virtual' },
    { codm: 3, nombre: 'Híbrida' }
  ]
}

const submitForm = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      isLoading.value = true
      editDmodalidad(state.form).then((res) => {
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