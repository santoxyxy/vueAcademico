<template>
  <el-dialog 
    :title="state.title" 
    v-model="visible" 
    :close-on-click-modal="false" 
    @opened="openFun"
    width="600px"
  >
    <el-form :model="state.prograForm" :rules="state.rules" ref="prograRef" label-width="120px">
      
      <el-form-item label="Gestión" prop="gestion">
        <el-date-picker
          v-model="state.prograForm.gestion"
          type="year"
          placeholder="Seleccione año académico"
          format="YYYY"
          value-format="YYYY"
          style="width: 100%"
          :disabled="state.isEdit"
        />
      </el-form-item>

      <el-form-item label="Materia" prop="codmat">
        <el-select
          v-model="state.prograForm.codmat"
          placeholder="Seleccione materia"
          filterable
          remote
          :remote-method="searchMaterias"
          :loading="state.loadingMaterias"
          style="width: 100%"
          :disabled="state.isEdit"
          @change="handleMateriaChange"
        >
          <el-option
            v-for="materia in state.materiasList"
            :key="materia.codmat"
            :label="`${materia.codmat} - ${materia.nombre}`"
            :value="materia.codmat"
          >
            <span style="float: left">{{ materia.codmat }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px">{{ materia.nombre }}</span>
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="Paralelo" prop="codpar">
        <el-select
          v-model="state.prograForm.codpar"
          placeholder="Seleccione paralelo"
          style="width: 100%"
          :disabled="state.isEdit || !state.prograForm.codmat"
        >
          <el-option
            v-for="paralelo in state.paralelosList"
            :key="paralelo.codp"
            :label="paralelo.nombre"
            :value="paralelo.codp"
          />
        </el-select>
        <span v-if="!state.prograForm.codmat" style="color: #909399; font-size: 12px">
          Primero seleccione una materia
        </span>
      </el-form-item>

      <el-form-item label="Periodo" prop="codp">
        <el-input-number
          v-model="state.prograForm.codp"
          :min="1"
          :max="2"
          style="width: 100%"
          disabled
        />
        <span style="color: #909399; font-size: 12px">
          (Siempre 1 según diseño del sistema)
        </span>
      </el-form-item>

      <el-form-item label="Docente" prop="ids">
        <el-select
          v-model="state.prograForm.ids"
          placeholder="Seleccione docente responsable"
          filterable
          remote
          :remote-method="searchDocentes"
          :loading="state.loadingDocentes"
          style="width: 100%"
        >
          <el-option
            v-for="docente in state.docentesList"
            :key="docente.id"
            :label="`${docente.nombre} ${docente.ap} ${docente.am || ''}`"
            :value="docente.id"
          >
            <div style="display: flex; justify-content: space-between;">
              <span>{{ docente.nombre }} {{ docente.ap }}</span>
              <span style="color: #8492a6; font-size: 13px">{{ docente.email }}</span>
            </div>
          </el-option>
        </el-select>
      </el-form-item>

      <el-alert
        v-if="state.isEdit"
        title="Nota: Los campos Gestión, Materia y Paralelo no se pueden modificar (son parte de la clave primaria)"
        type="info"
        :closable="false"
        show-icon
        style="margin-bottom: 15px"
      />

    </el-form>

    <template #footer>
      <span>
        <el-button @click="resetForm(prograRef)">Reiniciar</el-button>
        <el-button type="primary" :loading="isLoading" @click="submitProgra">Guardar</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { editProgra } from '../../api/progra/progra'
import { errorMsg, successMsg } from '../../utils/message'
import { resetForm } from '../../utils/common'
import { computed, reactive, ref } from 'vue'

const props = defineProps({
  dialogVisible: {
    type: Boolean,
    required: true,
    default: false
  },
  prograObj: Object
})

const emit = defineEmits(['update:dialogVisible', 'get-list'])

const visible = computed({
  get: () => props.dialogVisible,
  set: (val) => emit('update:dialogVisible', val)
})

const isLoading = ref(false)
const prograRef = ref()

const state = reactive({
  title: 'Nueva Clase',
  isEdit: false,
  prograForm: {
    codpar: null,
    codp: 1,
    codmat: '',
    gestion: null,
    ids: null
  },
  rules: {
    gestion: [{ required: true, message: 'La gestión es obligatoria', trigger: 'change' }],
    codmat: [{ required: true, message: 'La materia es obligatoria', trigger: 'change' }],
    codpar: [{ required: true, message: 'El paralelo es obligatorio', trigger: 'change' }],
    codp: [{ required: true, message: 'El periodo es obligatorio', trigger: 'change' }],
    ids: [{ required: true, message: 'El docente es obligatorio', trigger: 'change' }]
  },
  materiasList: [],
  paralelosList: [],
  docentesList: [],
  loadingMaterias: false,
  loadingDocentes: false
})

const openFun = () => {
  resetForm(prograRef.value)
  state.title = 'Nueva Clase'
  state.isEdit = false
  isLoading.value = false
  
  // Cargar listas iniciales
  searchMaterias('')
  searchDocentes('')
  
  if (props.prograObj.codmat) {
    state.title = 'Editar Clase'
    state.isEdit = true
    state.prograForm = { 
      ...props.prograObj,
      gestion: props.prograObj.gestion?.toString()
    }
    
    // Cargar paralelos de la materia seleccionada
    loadParalelosByMateria(props.prograObj.codmat, props.prograObj.gestion)
  } else {
    state.prograForm = {
      codpar: null,
      codp: 1,
      codmat: '',
      gestion: new Date().getFullYear().toString(),
      ids: null
    }
    state.paralelosList = []
  }
}

// Buscar materias (simulado - reemplazar con API real)
const searchMaterias = (query) => {
  state.loadingMaterias = true
  // TODO: Implementar llamada a API real
  // getMateriasList({ blurry: query }).then(res => { ... })
  
  // Datos de ejemplo - REEMPLAZAR con API real
  setTimeout(() => {
    state.materiasList = [
      { codmat: 'MAT101', nombre: 'Cálculo I', codn: 1 },
      { codmat: 'MAT102', nombre: 'Álgebra Lineal', codn: 1 },
      { codmat: 'FIS101', nombre: 'Física I', codn: 1 },
      { codmat: 'QUI101', nombre: 'Química General', codn: 1 },
      { codmat: 'INF101', nombre: 'Programación I', codn: 1 }
    ].filter(m => !query || m.codmat.includes(query.toUpperCase()) || m.nombre.toLowerCase().includes(query.toLowerCase()))
    state.loadingMaterias = false
  }, 300)
}

// Buscar docentes (simulado - reemplazar con API real)
const searchDocentes = (query) => {
  state.loadingDocentes = true
  // TODO: Implementar llamada a API real
  // getDocentesList({ blurry: query, tipo: 'DOCENTE' }).then(res => { ... })
  
  // Datos de ejemplo - REEMPLAZAR con API real
  setTimeout(() => {
    state.docentesList = [
      { id: 10, nombre: 'Juan', ap: 'Pérez', am: 'García', email: 'juan.perez@universidad.edu', tipo: 'DOCENTE' },
      { id: 11, nombre: 'María', ap: 'López', am: 'Martínez', email: 'maria.lopez@universidad.edu', tipo: 'DOCENTE' },
      { id: 12, nombre: 'Carlos', ap: 'Rodríguez', am: 'Sánchez', email: 'carlos.rodriguez@universidad.edu', tipo: 'DOCENTE' },
      { id: 13, nombre: 'Ana', ap: 'Fernández', am: 'González', email: 'ana.fernandez@universidad.edu', tipo: 'DOCENTE' }
    ].filter(d => !query || d.nombre.toLowerCase().includes(query.toLowerCase()) || d.ap.toLowerCase().includes(query.toLowerCase()))
    state.loadingDocentes = false
  }, 300)
}

// Cargar paralelos cuando se selecciona una materia
const handleMateriaChange = (codmat) => {
  if (codmat && state.prograForm.gestion) {
    loadParalelosByMateria(codmat, state.prograForm.gestion)
  } else {
    state.paralelosList = []
    state.prograForm.codpar = null
  }
}

// Cargar paralelos de una materia (simulado - reemplazar con API real)
const loadParalelosByMateria = (codmat, gestion) => {
  // TODO: Implementar llamada a API real
  // getMapaByMateria(codmat, gestion).then(res => { ... })
  
  // Datos de ejemplo - REEMPLAZAR con API real
  state.paralelosList = [
    { codp: 1, nombre: 'A' },
    { codp: 2, nombre: 'B' },
    { codp: 3, nombre: 'C' },
    { codp: 4, nombre: 'D' }
  ]
}

// Enviar formulario
const submitProgra = () => {
  prograRef.value.validate((valid) => {
    if (valid) {
      isLoading.value = true
      
      const formData = {
        ...state.prograForm,
        gestion: parseInt(state.prograForm.gestion)
      }
      
      editProgra(formData).then(res => {
        if (res.success) {
          successMsg(res.data)
          visible.value = false
          emit('get-list')
        } else {
          errorMsg(res.msg)
        }
        isLoading.value = false
      }).catch(() => {
        isLoading.value = false
      })
    }
  })
}
</script>

<style scoped>
:deep(.el-form-item__content) {
  line-height: 32px;
  font-size: 14px;
}

:deep(.el-select-dropdown__item) {
  height: auto;
  padding: 8px 20px;
}

.el-alert {
  font-size: 13px;
}
</style>