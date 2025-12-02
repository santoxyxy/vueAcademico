<template>
  <el-dialog 
    :title="state.title" 
    v-model="visible" 
    width="600px"
    :close-on-click-modal="false" 
    @opened="openFun"
  >
    <el-form 
      :model="state.form" 
      :rules="state.rules" 
      ref="formRef" 
      label-width="130px"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="Gestión" prop="gestion">
            <el-input-number 
              v-model="state.form.gestion" 
              :min="2020"
              :max="2030"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="Periodo" prop="codp">
            <el-select 
              v-model="state.form.codp" 
              placeholder="Periodo"
              style="width: 100%"
            >
              <el-option label="1° Periodo" :value="1" />
              <el-option label="2° Periodo" :value="2" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="Materia" prop="codmat">
        <el-select 
          v-model="state.form.codmat" 
          placeholder="Seleccione materia"
          filterable
          style="width: 100%"
          @change="onMateriaChange"
        >
          <el-option 
            v-for="item in state.materiaList" 
            :key="item.codmat" 
            :label="`${item.codmat} - ${item.nombre}`" 
            :value="item.codmat"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="Paralelo" prop="codpar">
        <el-select 
          v-model="state.form.codpar" 
          placeholder="Seleccione paralelo"
          style="width: 100%"
        >
          <el-option 
            v-for="item in state.paraleloList" 
            :key="item.codpar" 
            :label="item.nombre" 
            :value="item.codpar"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="Docente" prop="ids">
        <el-select 
          v-model="state.form.ids" 
          placeholder="Seleccione docente"
          filterable
          style="width: 100%"
        >
          <el-option 
            v-for="item in state.docenteList" 
            :key="item.ids" 
            :label="`${item.nombre} ${item.ap || ''} ${item.am || ''} (${item.tipo || 'N/A'})`" 
            :value="item.ids"
          >
            <div style="display: flex; justify-content: space-between;">
              <span>{{ item.nombre }} {{ item.ap }} {{ item.am }}</span>
              <el-tag size="small" :type="getDocenteTagType(item.tipo)">
                {{ item.tipo }}
              </el-tag>
            </div>
          </el-option>
        </el-select>
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
  set: (val) => emit('update:dialogVisible', val)
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
  rules: {
    gestion: [{ required: true, message: 'La gestión es obligatoria', trigger: 'blur' }],
    codp: [{ required: true, message: 'El periodo es obligatorio', trigger: 'change' }],
    codmat: [{ required: true, message: 'Debe seleccionar una materia', trigger: 'change' }],
    codpar: [{ required: true, message: 'Debe seleccionar un paralelo', trigger: 'change' }],
    ids: [{ required: true, message: 'Debe seleccionar un docente', trigger: 'change' }]
  },
  materiaList: [],
  paraleloList: [],
  docenteList: []
})

const openFun = () => {
  resetForm(formRef.value)
  state.title = 'Nueva Asignación'
  isLoading.value = false
  
  // Cargar listas
  loadSelectOptions()

  if (props.dictaObj.codpar) {
    state.title = 'Editar Asignación'
    state.form = { ...props.dictaObj }
  } else {
    state.form = {
      codpar: null,
      codp: 1,
      codmat: '',
      gestion: props.dictaObj.gestion || new Date().getFullYear(),
      ids: null
    }
  }
}

const loadSelectOptions = () => {
  // TODO: Implementar llamadas reales
  // import { getMateriaList } from '@/api/materia/materia'
  // import { getParaleloList } from '@/api/paralelo/paralelo'
  // import { getPersonalList } from '@/api/personal/personal'
  
  // Datos de ejemplo
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

const onMateriaChange = (codmat) => {
  // Opcional: filtrar docentes según la materia seleccionada
  console.log('Materia seleccionada:', codmat)
}

const getDocenteTagType = (tipo) => {
  const types = {
    'Titular': 'success',
    'Invitado': 'warning',
    'Auxiliar': 'info'
  }
  return types[tipo] || ''
}

const submitForm = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      isLoading.value = true
      editDicta(state.form).then((res) => {
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

<style scoped>
:deep(.el-select-dropdown__item) {
  height: auto;
  padding: 8px;
}
</style>