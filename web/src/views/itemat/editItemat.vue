<template>
  <div>
    <el-dialog :title="state.title" v-model="dialogVisible" width="600px" @close="closeFun">
      <el-form :model="state.form" :rules="state.rules" ref="formRef" label-width="150px">
        <el-form-item label="Código Materia" prop="codmat">
          <el-input v-model="state.form.codmat" placeholder="Ej: MAT101" :disabled="!!state.form.codmat && isEdit"></el-input>
        </el-form-item>

        <el-form-item label="Ítem" prop="codi">
          <el-select v-model="state.form.codi" placeholder="Seleccione un ítem" style="width: 100%;" @change="handleItemChange">
            <el-option
              v-for="item in state.itemsList"
              :key="item.codi"
              :label="item.nombre"
              :value="item.codi">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="Gestión" prop="gestion">
          <el-input-number v-model="state.form.gestion" :min="2020" :max="2050" style="width: 100%;"></el-input-number>
        </el-form-item>

        <el-form-item label="Ponderación (%)" prop="ponderacion">
          <el-input-number v-model="state.form.ponderacion" :min="0" :max="100" style="width: 100%;"></el-input-number>
          <el-alert
            v-if="state.totalPonderacion > 0"
            :title="`Total configurado: ${state.totalPonderacion}%`"
            :type="state.totalPonderacion === 100 ? 'success' : 'warning'"
            :closable="false"
            style="margin-top: 10px;">
          </el-alert>
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
import { editItemat, getItemsByMateria } from "../../api/itemat/itemat";
import { getItemsList } from "../../api/items/items";
import { errorMsg, successMsg, warningMsg } from "../../utils/message";
import { computed, reactive, ref, watch } from "vue";

const props = defineProps({
  dialogVisible: Boolean,
  itematObj: Object
})

const emit = defineEmits(['update:dialogVisible', 'getList'])

const dialogVisible = computed({
  get: () => props.dialogVisible,
  set: (val) => emit('update:dialogVisible', val)
})

const formRef = ref(null)
const isEdit = computed(() => !!(state.form.codmat && state.form.codi && state.form.gestion))

const state = reactive({
  title: 'Nueva Configuración',
  form: {
    codmat: '',
    codi: null,
    gestion: new Date().getFullYear(),
    ponderacion: 0,
    estado: 1
  },
  rules: {
    codmat: [
      { required: true, message: 'Por favor ingrese código de materia', trigger: 'blur' }
    ],
    codi: [
      { required: true, message: 'Por favor seleccione un ítem', trigger: 'change' }
    ],
    gestion: [
      { required: true, message: 'Por favor ingrese gestión', trigger: 'change' }
    ],
    ponderacion: [
      { required: true, message: 'Por favor ingrese ponderación', trigger: 'blur' },
      { type: 'number', min: 0, max: 100, message: 'Debe estar entre 0 y 100', trigger: 'change' }
    ],
    estado: [
      { required: true, message: 'Por favor seleccione estado', trigger: 'change' }
    ]
  },
  itemsList: [],
  totalPonderacion: 0
})

// Cargar lista de ítems disponibles
const loadItemsList = () => {
  getItemsList('').then(res => {
    if (res.success) {
      state.itemsList = res.data
    }
  })
}

// Calcular total de ponderaciones cuando cambie la materia o gestión
const calculateTotal = () => {
  if (state.form.codmat && state.form.gestion) {
    getItemsByMateria(state.form.codmat, state.form.gestion).then(res => {
      if (res.success && res.data) {
        const currentPonderacion = state.form.ponderacion || 0
        const existingTotal = res.data
          .filter(item => item.codi !== state.form.codi)
          .reduce((sum, item) => sum + (item.ponderacion || 0), 0)
        
        state.totalPonderacion = existingTotal + currentPonderacion
      }
    })
  }
}

watch(() => props.itematObj, (newVal) => {
  loadItemsList()
  
  if (newVal && newVal.codmat && newVal.codi) {
    state.title = 'Editar Configuración'
    state.form = { ...newVal }
  } else {
    state.title = 'Nueva Configuración'
    state.form = {
      codmat: newVal?.codmat || '',
      codi: null,
      gestion: newVal?.gestion || new Date().getFullYear(),
      ponderacion: 0,
      estado: 1
    }
  }
  
  calculateTotal()
}, { deep: true, immediate: true })

watch(() => [state.form.codmat, state.form.gestion, state.form.ponderacion], () => {
  calculateTotal()
})

const handleItemChange = () => {
  calculateTotal()
}

// Cerrar Dialog
const closeFun = () => {
  formRef.value?.resetFields()
  state.totalPonderacion = 0
  dialogVisible.value = false
}

// Guardar
const submitFun = () => {
  formRef.value?.validate((valid) => {
    if (valid) {
      // Advertencia si no suma 100%
      if (state.totalPonderacion !== 100) {
        warningMsg(`Las ponderaciones suman ${state.totalPonderacion}%. Se recomienda que sumen 100%`)
      }
      
      editItemat(state.form).then(res => {
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