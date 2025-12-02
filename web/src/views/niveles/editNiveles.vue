<template>
  <div>
    <el-dialog :title="state.title" v-model="dialogVisible" width="500px" @close="closeFun">
      <el-form :model="state.form" :rules="state.rules" ref="formRef" label-width="120px">
        <el-form-item label="Nombre" prop="nombre">
          <el-input v-model="state.form.nombre" placeholder="Ingrese nombre del nivel (ej: Primaria, Secundaria)"></el-input>
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
import { editNiveles } from "@/api/niveles/niveles";
import { errorMsg, successMsg } from "@/utils/message";
import { computed, reactive, ref, watch } from "vue";

const props = defineProps({
  dialogVisible: Boolean,
  nivelesObj: Object
})

const emit = defineEmits(['update:dialogVisible', 'getList'])

const dialogVisible = computed({
  get: () => props.dialogVisible,
  set: (val) => emit('update:dialogVisible', val)
})

const formRef = ref(null)

const state = reactive({
  title: 'Nuevo Nivel',
  form: {
    codn: null,
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

watch(() => props.nivelesObj, (newVal) => {
  if (newVal && newVal.codn) {
    state.title = 'Editar Nivel'
    state.form = { ...newVal }
  } else {
    state.title = 'Nuevo Nivel'
    state.form = { codn: null, nombre: '', estado: 1 }
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
      editNiveles(state.form).then(res => {
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