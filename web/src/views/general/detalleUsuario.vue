<template>
  <q-dialog v-model="dialogVisible" persistent maximized>
    <q-card style="min-width: 500px; max-width: 700px">
      <q-card-section class="text-h6">
        Detalles de {{ usuarioData?.username }}
      </q-card-section>

      <q-card-section>
        <!-- Información principal -->
        <q-table
          :rows="detalleRows"
          :columns="columns"
          flat
          bordered
          class="q-mb-md"
          row-key="label"
        >
          <template v-slot:body-cell-value="props">
            <span v-if="props.row.type === 'tag'">
              <q-badge
                :color="props.row.color"
                :label="props.row.value"
                align="center"
              />
            </span>
            <span v-else>{{ props.row.value }}</span>
          </template>
        </q-table>

        <!-- Historial de gestiones -->
        <q-separator spaced />
        <div class="row items-center q-mb-sm">
          <q-icon name="event" class="q-mr-sm" />
          <span class="text-subtitle2">Historial de Gestiones</span>
        </div>

        <q-timeline dense class="scroll-area" style="max-height: 300px;">
          <q-timeline-entry
            v-for="gestion in gestiones"
            :key="gestion"
            :title="`Gestión ${gestion}`"
            side="top"
          >
            <q-card flat bordered class="q-pa-sm">
              <div class="text-subtitle2">Año Académico {{ gestion }}</div>
              <q-btn
                label="Ver Detalles"
                color="primary"
                size="sm"
                class="q-mt-sm"
                @click="verDetalleGestion(gestion)"
              />
            </q-card>
          </q-timeline-entry>
        </q-timeline>
      </q-card-section>

      <q-card-actions align="right">
        <q-btn flat label="Cerrar" color="primary" @click="handleClose" />
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { Notify } from 'quasar'
import { getGestionesByUser, getGeneralByUserAndGestion } from '@/api/general/general'

const props = defineProps({
  modelValue: Boolean,
  usuarioData: Object
})

const emit = defineEmits(['update:modelValue'])

// Estado
const gestiones = ref([])
const dialogVisible = computed({
  get: () => props.modelValue,
  set: val => emit('update:modelValue', val)
})

// Tabla de detalles principal
const detalleRows = computed(() => [
  { label: 'Usuario', value: props.usuarioData?.username || '-', type: 'tag', color: 'primary' },
  { label: 'Nombre', value: props.usuarioData?.nickName || '-' },
  { label: 'Nombre Completo', value: props.usuarioData?.nombreCompleto || 'No disponible' },
  { label: 'Email', value: props.usuarioData?.email || 'No disponible' },
  { label: 'Tipo', value: props.usuarioData?.tipo || 'N/A', type: 'tag', color: props.usuarioData?.tipo === 'DOCENTE' ? 'green' : 'blue' },
  { label: 'Teléfono', value: props.usuarioData?.telf || 'No disponible' },
  { label: 'Gestión Actual', value: props.usuarioData?.gestion || '-', type: 'tag', color: 'orange' },
  { label: 'Estado', value: props.usuarioData?.enabled ? 'Activo' : 'Inactivo', type: 'tag', color: props.usuarioData?.enabled ? 'green' : 'red' },
  { label: 'Total Materias', value: props.usuarioData?.totalMaterias || 0, type: 'tag', color: 'green' },
  { label: 'Total Notas', value: props.usuarioData?.totalNotas || 0, type: 'tag', color: 'blue' }
])

const columns = [
  { name: 'label', label: 'Campo', field: 'label', align: 'left' },
  { name: 'value', label: 'Valor', field: 'value', align: 'left' }
]

// Cargar gestiones del usuario
const cargarGestiones = async () => {
  if (!props.usuarioData?.ids) return

  try {
    const { data } = await getGestionesByUser(props.usuarioData.ids)
    if (data.success) gestiones.value = data.data
  } catch (error) {
    Notify.create({ type: 'negative', message: 'Error al cargar gestiones: ' + error.message })
  }
}

// Ver detalle de una gestión
const verDetalleGestion = async (gestion) => {
  try {
    const { data } = await getGeneralByUserAndGestion(props.usuarioData.ids, gestion)
    if (data.success) {
      Notify.create({ type: 'positive', message: `Gestión ${gestion}: ${data.data.totalMaterias} materias, ${data.data.totalNotas} notas` })
    }
  } catch (error) {
    Notify.create({ type: 'negative', message: 'Error al obtener detalles: ' + error.message })
  }
}

// Cerrar dialog
const handleClose = () => {
  dialogVisible.value = false
}

// Cargar gestiones al abrir
watch(() => props.usuarioData, (newData) => {
  if (newData && props.modelValue) cargarGestiones()
}, { immediate: true })
</script>

<style scoped>
.scroll-area {
  padding-left: 10px;
  overflow-y: auto;
}
</style>
