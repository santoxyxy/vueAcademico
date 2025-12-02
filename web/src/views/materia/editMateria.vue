<template>
  <el-dialog
    v-model="dialogVisible"
    :title="isEdit ? 'Editar Materia' : 'Nueva Materia'"
    width="600px"
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      label-width="120px"
      v-loading="loading"
    >
      <el-form-item label="Código" prop="codmat">
        <el-input
          v-model="formData.codmat"
          placeholder="Código de la materia (ej: MAT-101)"
          :disabled="isEdit"
          maxlength="15"
        />
        <el-text size="small" type="info">
          Máximo 15 caracteres. No se puede modificar una vez creado.
        </el-text>
      </el-form-item>

      <el-form-item label="Nombre" prop="nombre">
        <el-input
          v-model="formData.nombre"
          placeholder="Nombre de la materia"
          maxlength="30"
        />
      </el-form-item>

      <el-form-item label="Nivel" prop="codn">
        <el-select
          v-model="formData.codn"
          placeholder="Seleccione un nivel"
          style="width: 100%"
          filterable
        >
          <el-option
            v-for="nivel in nivelesList"
            :key="nivel.codn"
            :label="nivel.nombre"
            :value="nivel.codn"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="Estado" prop="estado">
        <el-switch
          v-model="formData.estado"
          :active-value="1"
          :inactive-value="0"
          active-text="Activo"
          inactive-text="Inactivo"
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="handleClose">Cancelar</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="loading">
        {{ isEdit ? 'Guardar Cambios' : 'Crear Materia' }}
      </el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, computed, watch } from 'vue';
import { ElMessage } from 'element-plus';
import { editMateria } from '@/api/materia/materia';
import { getNivelesList } from '@/api/niveles/niveles';

const props = defineProps({
  modelValue: Boolean,
  materiaData: Object
});

const emit = defineEmits(['update:modelValue', 'success']);

// Estado
const formRef = ref(null);
const loading = ref(false);
const nivelesList = ref([]);

const formData = reactive({
  codmat: '',
  nombre: '',
  codn: null,
  estado: 1
});

// Reglas de validación
const rules = {
  codmat: [
    { required: true, message: 'El código es obligatorio', trigger: 'blur' },
    { min: 3, max: 15, message: 'Debe tener entre 3 y 15 caracteres', trigger: 'blur' }
  ],
  nombre: [
    { required: true, message: 'El nombre es obligatorio', trigger: 'blur' },
    { max: 30, message: 'Máximo 30 caracteres', trigger: 'blur' }
  ],
  codn: [
    { required: true, message: 'Debe seleccionar un nivel', trigger: 'change' }
  ]
};

// Computed
const dialogVisible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
});

const isEdit = computed(() => !!props.materiaData?.codmat);

// Cargar niveles
const loadNiveles = async () => {
  try {
    const { data } = await getNivelesList();
    if (data.success) {
      nivelesList.value = data.data;
    }
  } catch (error) {
    ElMessage.error('Error al cargar niveles: ' + error.message);
  }
};

// Resetear formulario
const resetForm = () => {
  if (props.materiaData) {
    Object.assign(formData, props.materiaData);
  } else {
    Object.assign(formData, {
      codmat: '',
      nombre: '',
      codn: null,
      estado: 1
    });
  }
  formRef.value?.clearValidate();
};

// Enviar formulario
const handleSubmit = async () => {
  if (!formRef.value) return;

  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true;
      try {
        const { data } = await editMateria(formData);
        
        if (data.success) {
          ElMessage.success(data.msg || 'Operación exitosa');
          emit('success');
          handleClose();
        } else {
          ElMessage.error(data.msg || 'Error al guardar');
        }
      } catch (error) {
        ElMessage.error('Error: ' + error.message);
      } finally {
        loading.value = false;
      }
    }
  });
};

// Cerrar diálogo
const handleClose = () => {
  resetForm();
  dialogVisible.value = false;
};

// Watcher para cargar datos al abrir
watch(() => props.modelValue, (newVal) => {
  if (newVal) {
    loadNiveles();
    resetForm();
  }
}, { immediate: true });
</script>

<style scoped>
.el-text {
  display: block;
  margin-top: 5px;
}
</style>