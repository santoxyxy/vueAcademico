<template>
  <el-dialog
    v-model="visible"
    :title="state.mapaObj.codmat ? 'Editar Asignación' : 'Nueva Asignación'"
    width="500px"
    @close="handleClose"
  >
    <el-form :model="state.mapaObj" label-width="120px">
      <el-form-item label="Materia" required>
        <el-select 
          v-model="state.mapaObj.codmat" 
          placeholder="Seleccione materia"
          style="width: 100%"
          :disabled="!!state.mapaObj.codmat"
        >
          <el-option
            v-for="item in state.materiaOptions"
            :key="item.codmat"
            :label="`${item.codmat} - ${item.nombre}`"
            :value="item.codmat"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="Paralelo" required>
        <el-select 
          v-model="state.mapaObj.codpar" 
          placeholder="Seleccione paralelo"
          style="width: 100%"
          :disabled="!!state.mapaObj.codpar"
        >
          <el-option
            v-for="item in state.paraleloOptions"
            :key="item.codp"
            :label="item.nombre"
            :value="item.codp"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="Gestión" required>
        <el-input-number 
          v-model="state.mapaObj.gestion" 
          :min="2020" 
          :max="2030"
          :disabled="!!state.mapaObj.gestion"
          style="width: 100%"
        />
      </el-form-item>

      <el-form-item label="Estado">
        <el-select v-model="state.mapaObj.estado" style="width: 100%">
          <el-option label="Activo" :value="1" />
          <el-option label="Inactivo" :value="0" />
        </el-select>
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="handleClose">Cancelar</el-button>
      <el-button type="primary" @click="handleSubmit">Guardar</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { editMapa } from "../../api/mapa/mapa";
import { getMateriaList } from "../../api/materia/materia"; // Asume que existe
import { getParaleloList } from "../../api/paralelo/paralelo"; // Asume que existe
import { errorMsg, successMsg } from "../../utils/message";
import { onMounted, reactive, computed } from "vue";

const props = defineProps({
  dialogVisible: Boolean,
  mapaObj: Object,
  gestion: Number
});

const emit = defineEmits(['update:dialogVisible', 'getList']);

const state = reactive({
  mapaObj: {},
  materiaOptions: [],
  paraleloOptions: []
});

const visible = computed({
  get: () => props.dialogVisible,
  set: (val) => emit('update:dialogVisible', val)
});

onMounted(() => {
  getMateriaListFun();
  getParaleloListFun();
});

// Cargar opciones
const getMateriaListFun = () => {
  getMateriaList({ blurry: '' }).then(res => {
    if (res.success) {
      state.materiaOptions = res.data;
    }
  });
};

const getParaleloListFun = () => {
  getParaleloList({ blurry: '' }).then(res => {
    if (res.success) {
      state.paraleloOptions = res.data;
    }
  });
};

// Actualizar objeto
const updateObj = () => {
  state.mapaObj = props.mapaObj.codmat 
    ? { ...props.mapaObj } 
    : { codmat: null, codpar: null, gestion: props.gestion, estado: 1 };
};

import { watch } from 'vue';
watch(() => props.dialogVisible, (val) => {
  if (val) updateObj();
});

// Guardar
const handleSubmit = () => {
  if (!state.mapaObj.codmat || !state.mapaObj.codpar || !state.mapaObj.gestion) {
    errorMsg('Todos los campos son obligatorios');
    return;
  }

  editMapa(state.mapaObj).then(res => {
    if (res.success) {
      successMsg(res.data);
      handleClose();
      emit('getList');
    } else {
      errorMsg(res.msg);
    }
  });
};

// Cerrar
const handleClose = () => {
  visible.value = false;
};
</script>