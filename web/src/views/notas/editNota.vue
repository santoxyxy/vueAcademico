<template>
  <div>
    <el-dialog :title="state.title" v-model="dialogVisible" width="600px" @close="closeFun">
      <el-form :model="state.form" :rules="state.rules" ref="formRef" label-width="150px">
        <el-form-item label="Código Materia" prop="codmat">
          <el-input v-model="state.form.codmat" placeholder="Ej: MAT101"></el-input>
        </el-form-item>

        <el-form-item label="ID Usuario" prop="idusuario">
          <el-input-number v-model="state.form.idusuario" :min="1" style="width: 100%;"></el-input-number>
        </el-form-item>

        <el-form-item label="Código Ítem" prop="codi">
          <el-input-number v-model="state.form.codi" :min="1" style="width: 100%;"></el-input-number>
        </el-form-item>

        <el-form-item label="Código Paralelo" prop="coda">
          <el-input-number v-model="state.form.coda" :min="1" style="width: 100%;"></el-input-number>
        </el-form-item>

        <el-form-item label="Código Modalidad" prop="codp">
          <el-input-number v-model="state.form.codp" :min="1" style="width: 100%;"></el-input-number>
        </el-form-item>

        <el-form-item label="Gestión" prop="gestion">
          <el-input-number v-model="state.form.gestion" :min="2020" :max="2050" style="width: 100%;"></el-input-number>
        </el-form-item>

        <el-form-item label="Nota" prop="nota">
          <el-input-number v-model="state.form.nota" :min="0" :max="100" style="width: 100%;"></el-input-number>
        </el-form-item>

        <el-form-item label="Código Modalidad" prop="coddm">
          <el-input-number v-model="state.form.coddm" :min="1" style="width: 100%;"></el-input-number>
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
import { editNota } from "@/api/notas/notas";
import { errorMsg, successMsg } from "@/utils/message";
import { computed, reactive, ref, watch } from "vue";

const props = defineProps({
  dialogVisible: Boolean,
  notaObj: Object
})

const emit = defineEmits(['update:dialogVisible', 'getList'])

const dialogVisible = computed({
  get: () => props.dialogVisible,
  set: (val) => emit('update:dialogVisible', val)
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
  },
  rules: {
    codmat: [{ required: true, message: 'Requerido', trigger: 'blur' }],
    idusuario: [{ required: true, message: 'Requerido', trigger: 'change' }],
    codi: [{ required: true, message: 'Requerido', trigger: 'change' }],
    coda: [{ required: true, message: 'Requerido', trigger: 'change' }],
    codp: [{ required: true, message: 'Requerido', trigger: 'change' }],
    gestion: [{ required: true, message: 'Requerido', trigger: 'change' }],
    nota: [
      { required: true, message: 'Requerido', trigger: 'change' },
      { type: 'number', min: 0, max: 100, message: 'Entre 0 y 100', trigger: 'change' }
    ]
  }
})

watch(() => props.notaObj, (newVal) => {
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
}, { deep: true, immediate: true })

const closeFun = () => {
  formRef.value?.resetFields()
  dialogVisible.value = false
}

const submitFun = () => {
  formRef.value?.validate((valid) => {
    if (valid) {
      editNota(state.form).then(res => {
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