<template>
  <div>
    <el-dialog title="Registro Masivo de Notas" v-model="dialogVisible" width="800px" @close="closeFun">
      <el-alert title="Ingrese las notas para todos los estudiantes del paralelo" type="info" :closable="false" style="margin-bottom: 15px;"></el-alert>
      
      <el-form :model="state.filters" label-width="150px" style="margin-bottom: 20px;">
        <el-row :gutter="10">
          <el-col :span="12">
            <el-form-item label="Código Materia">
              <el-input v-model="state.filters.codmat" placeholder="MAT101"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Código Ítem">
              <el-input-number v-model="state.filters.codi" :min="1" style="width: 100%;"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="12">
            <el-form-item label="Código Paralelo">
              <el-input-number v-model="state.filters.coda" :min="1" style="width: 100%;"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Gestión">
              <el-input-number v-model="state.filters.gestion" :min="2020" :max="2050" style="width: 100%;"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-button type="primary" @click="addRow">+ Agregar Estudiante</el-button>
      </el-form>

      <el-table :data="state.notasList" border max-height="400px">
        <el-table-column label="ID Usuario" width="120">
          <template #default="scope">
            <el-input-number v-model="scope.row.idusuario" :min="1" size="small" style="width: 100%;"></el-input-number>
          </template>
        </el-table-column>
        <el-table-column label="Nota (0-100)" width="120">
          <template #default="scope">
            <el-input-number v-model="scope.row.nota" :min="0" :max="100" size="small" style="width: 100%;"></el-input-number>
          </template>
        </el-table-column>
        <el-table-column label="Código Modalidad" width="150">
          <template #default="scope">
            <el-input-number v-model="scope.row.coddm" :min="1" size="small" style="width: 100%;"></el-input-number>
          </template>
        </el-table-column>
        <el-table-column label="Código Programa" width="150">
          <template #default="scope">
            <el-input-number v-model="scope.row.codp" :min="1" size="small" style="width: 100%;"></el-input-number>
          </template>
        </el-table-column>
        <el-table-column label="Acciones" width="100" align="center">
          <template #default="scope">
            <el-button type="danger" size="small" @click="removeRow(scope.$index)">
              <el-icon><Delete /></el-icon>
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="closeFun">Cancelar</el-button>
          <el-button type="primary" @click="submitFun" :disabled="state.notasList.length === 0">
            Guardar Todo ({{ state.notasList.length }} notas)
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { registrarNotasMasivas } from "@/api/notas/notas";
import { errorMsg, successMsg } from "@/utils/message";
import { computed, reactive } from "vue";

const props = defineProps({
  dialogVisible: Boolean
})

const emit = defineEmits(['update:dialogVisible', 'getList'])

const dialogVisible = computed({
  get: () => props.dialogVisible,
  set: (val) => emit('update:dialogVisible', val)
})

const state = reactive({
  filters: {
    codmat: '',
    codi: null,
    coda: null,
    gestion: new Date().getFullYear()
  },
  notasList: []
})

const addRow = () => {
  if (!state.filters.codmat || !state.filters.codi || !state.filters.coda) {
    errorMsg('Complete los filtros primero')
    return
  }

  state.notasList.push({
    codmat: state.filters.codmat,
    codi: state.filters.codi,
    coda: state.filters.coda,
    gestion: state.filters.gestion,
    idusuario: null,
    nota: 0,
    coddm: null,
    codp: null
  })
}

const removeRow = (index) => {
  state.notasList.splice(index, 1)
}

const closeFun = () => {
  state.notasList = []
  dialogVisible.value = false
}

const submitFun = () => {
  // Validar que todos tengan idusuario
  const invalid = state.notasList.some(n => !n.idusuario || !n.coddm || !n.codp)
  if (invalid) {
    errorMsg('Complete todos los campos requeridos')
    return
  }

  registrarNotasMasivas(state.notasList).then(res => {
    if (res.success) {
      successMsg(`${state.notasList.length} notas registradas exitosamente`)
      emit('getList')
      closeFun()
    } else {
      errorMsg(res.msg)
    }
  })
}
</script>

<style scoped>
</style>