<template>
  <div>
    <el-dialog :title="state.title" v-model="dialogVisible" width="500px" @close="closeFun">
      <el-form :model="state.form" :rules="state.rules" ref="formRef" label-width="120px">
        <el-form-item label="Nombre" prop="nombre">
          <el-input v-model="state.form.nombre" placeholder="Ingrese nombre del paralelo"></el-input>
        </el-form-item>
        <el-form-item label="Estado" prop="estado">
          <el-radio-group v-model="state.form.estado">
            <el-radio :label="1">Activo</el-radio>
            <el-radio :label="0">Inactivo</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="closeFun">Cancelar</el-button>
          <el-button type="primary" @click="submitFun">Guardar</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { editParalelo } from "../../api/paralelo/paralelo";
import { errorMsg, successMsg } from "../../utils/message";
import { computed, reactive, ref, watch } from "vue";

const props = defineProps({
  dialogVisible: Boolean,
  paraleloObj: Object
})

const emit = defineEmits(['update:dialogVisible', 'getList'])

const dialogVisible = computed({
  get: () => props.dialogVisible,
  set: (val) => emit('update:dialogVisible', val)
})

const formRef = ref(null)

const state = reactive({
  title: '新增平行班',
  form: {
    codp: null,
    nombre: '',
    estado: 1
  },
  rules: {
    nombre: [
      { required: true, message: 'Por favor ingrese nombre', trigger: 'blur' }
    ],
    estado: [
      { required: true, message: 'Por favor seleccione estado', trigger: 'change' }
    ]
  }
})

watch(() => props.paraleloObj, (newVal) => {
  if (newVal && newVal.codp) {
    state.title = 'Editar Paralelo'
    state.form = { ...newVal }
  } else {
    state.title = 'Nuevo Paralelo'
    state.form = { codp: null, nombre: '', estado: 1 }
  }
}, { deep: true, immediate: true })

// Cerrar Dialog
const closeFun = () => {
  formRef.value?.resetFields()
  dialogVisible.value = false
}

// Guardar
const submitFun = () => {
  formRef.value?.validate((valid) => {
    if (valid) {
      editParalelo(state.form).then(res => {
        if (res.success) {
          successMsg(res.data)
          emit('getList')
          closeFun()
        } else {
          errorMsg(res.msg)
        }
      })
    }
  })
}
</script>

<style scoped>
</style>