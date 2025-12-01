<template>
  <el-dialog
    v-model="dialogVisible"
    :title="`Detalles de ${usuarioData?.username}`"
    width="700px"
    @close="handleClose"
  >
    <el-descriptions :column="2" border>
      <el-descriptions-item label="Usuario">
        <el-tag>{{ usuarioData?.username }}</el-tag>
      </el-descriptions-item>
      
      <el-descriptions-item label="Nombre">
        {{ usuarioData?.nickName }}
      </el-descriptions-item>

      <el-descriptions-item label="Nombre Completo" :span="2">
        {{ usuarioData?.nombreCompleto || 'No disponible' }}
      </el-descriptions-item>

      <el-descriptions-item label="Email" :span="2">
        {{ usuarioData?.email }}
      </el-descriptions-item>

      <el-descriptions-item label="Tipo">
        <el-tag :type="usuarioData?.tipo === 'DOCENTE' ? 'success' : 'primary'">
          {{ usuarioData?.tipo || 'N/A' }}
        </el-tag>
      </el-descriptions-item>

      <el-descriptions-item label="Teléfono">
        {{ usuarioData?.telf || 'No disponible' }}
      </el-descriptions-item>

      <el-descriptions-item label="Gestión Actual">
        <el-tag type="warning">{{ usuarioData?.gestion }}</el-tag>
      </el-descriptions-item>

      <el-descriptions-item label="Estado">
        <el-tag :type="usuarioData?.enabled ? 'success' : 'danger'">
          {{ usuarioData?.enabled ? 'Activo' : 'Inactivo' }}
        </el-tag>
      </el-descriptions-item>

      <el-descriptions-item label="Total Materias">
        <el-badge :value="usuarioData?.totalMaterias" type="success" />
      </el-descriptions-item>

      <el-descriptions-item label="Total Notas">
        <el-badge :value="usuarioData?.totalNotas" type="primary" />
      </el-descriptions-item>
    </el-descriptions>

    <!-- Gestiones del usuario -->
    <el-divider content-position="left">
      <el-icon><Calendar /></el-icon>
      Historial de Gestiones
    </el-divider>

    <el-timeline v-loading="loadingGestiones">
      <el-timeline-item
        v-for="gestion in gestiones"
        :key="gestion"
        :timestamp="`Gestión ${gestion}`"
        placement="top"
      >
        <el-card>
          <h4>Año Académico {{ gestion }}</h4>
          <el-button
            type="primary"
            size="small"
            @click="verDetalleGestion(gestion)"
          >
            Ver Detalles
          </el-button>
        </el-card>
      </el-timeline-item>
    </el-timeline>

    <template #footer>
      <el-button type="primary" @click="handleClose">Cerrar</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { Calendar } from '@element-plus/icons-vue'
import { getGestionesByUser, getGeneralByUserAndGestion } from '@/api/general/general'

const props = defineProps({
  modelValue: Boolean,
  usuarioData: Object
})

const emit = defineEmits(['update:modelValue'])

// Estado
const gestiones = ref([])
const loadingGestiones = ref(false)

// Dialog visibility
const dialogVisible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})

// Cargar gestiones del usuario
const cargarGestiones = async () => {
  if (!props.usuarioData?.ids) return

  loadingGestiones.value = true
  try {
    const { data } = await getGestionesByUser(props.usuarioData.ids)
    
    if (data.success) {
      gestiones.value = data.data
    }
  } catch (error) {
    ElMessage.error('Error al cargar gestiones: ' + error.message)
  } finally {
    loadingGestiones.value = false
  }
}

// Ver detalle de una gestión
const verDetalleGestion = async (gestion) => {
  try {
    const { data } = await getGeneralByUserAndGestion(props.usuarioData.ids, gestion)
    
    if (data.success) {
      ElMessage.success(`Gestión ${gestion}: ${data.data.totalMaterias} materias, ${data.data.totalNotas} notas`)
    }
  } catch (error) {
    ElMessage.error('Error al obtener detalles: ' + error.message)
  }
}

// Cerrar dialog
const handleClose = () => {
  dialogVisible.value = false
}

// Cargar gestiones al abrir
watch(() => props.usuarioData, (newData) => {
  if (newData && props.modelValue) {
    cargarGestiones()
  }
}, { immediate: true })
</script>

<style scoped>
.el-timeline {
  padding-left: 10px;
  max-height: 300px;
  overflow-y: auto;
}
</style>