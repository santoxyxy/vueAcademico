<template>
  <div class="app-container">
    <div class="search-and-controls">
      <!-- Búsqueda (reemplaza v-slot:top-right de q-table) -->
      <el-input
        v-model="state.blurry"
        placeholder="Buscar por Nombre de Compañía u otros campos..."
        clearable
        style="width: 300px;"
        @keyup.enter="getModulo1TableFun()"
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
      
      <!-- Botón de consulta (opcional, si no usas keyup.enter) -->
      <el-button type="primary" :icon="Search" @click="getModulo1TableFun()">
        Consultar
      </el-button>

      <!-- Aquí irían botones adicionales (e.g., Agregar Nuevo Registro) -->
    </div>

    <!-- El-Table (reemplaza q-table) -->
    <el-table
      :data="state.tableData"
      v-loading="loading"
      row-key="idpedido"
      border
      stripe
      style="width: 100%; margin-top: 20px;"
    >
      <!-- Usaremos la prop `prop` para mapear los campos y el slot para el formato de fecha -->

      <!-- ID Pedido -->
      <el-table-column prop="idpedido" label="Mi Id de Pedido" width="120" align="center" sortable />

      <!-- Fecha de Pedido (Formato manual) -->
      <el-table-column prop="fechapedido" label="Fecha de pedido" width="160" align="left" sortable>
        <template #default="{ row }">
          {{ formatDate(row.fechapedido) }}
        </template>
      </el-table-column>

      <!-- Fecha de Envío (Formato manual) -->
      <el-table-column prop="fechaenvio" label="Fecha de envío" width="160" align="left" sortable>
        <template #default="{ row }">
          {{ formatDate(row.fechaenvio) }}
        </template>
      </el-table-column>
      
      <!-- Nombre de la Compañía -->
      <el-table-column prop="nombrecompania" label="Nombre de la Compañia" min-width="250" align="left" sortable show-overflow-tooltip />
      
      <!-- Importe de Ventas -->
      <el-table-column prop="importeventas" label="Importe de Ventas" width="150" align="right" sortable />

      <!-- Acciones (reemplaza v-slot:body-cell-actions) -->
      <el-table-column label="Acciones" width="150" align="center" fixed="right">
        <template #default="{ row }">
          <!-- Botón Editar (similar a fab-mini dense square outline icon="mode_edit" color="primary") -->
          <el-button 
            :icon="Edit" 
            type="primary" 
            size="small" 
            circle 
            plain 
            @click="onEdit(row)" 
            class="q-mr-sm" 
            aria-label="Editar"
          />
          
          <!-- Botón Eliminar (similar a fab-mini dense square outline icon="delete" color="negative") -->
          <el-button 
            :icon="Delete" 
            type="danger" 
            size="small" 
            circle 
            plain 
            @click="onDelete(row)" 
            aria-label="Eliminar"
          />
        </template>
      </el-table-column>
    </el-table>

    <!-- Paginación (Usando tu componente Pagination.vue) -->
    <!-- Nota: El componente de paginación debe emitir un evento con { page, limit } -->
    <Pagination 
      :total="state.total" 
      :page="pagination.page" 
      :limit="pagination.rowsPerPage" 
      @pagination="handlePaginationChange"
      style="margin-top: 20px;"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { getModulo1Table } from '../../api/modulo1/modulo1';
import { errorMsg, infoMsg } from '../../utils/message';
import Pagination from '../../components/Pagination.vue';
import { Search, Edit, Delete } from '@element-plus/icons-vue';
import { ElMessageBox } from 'element-plus';

// Estado global para la tabla y filtros
const state = reactive({
  blurry: '',
  tableData: [],
  total: 0
});

// Referencias para el estado de la UI
const loading = ref(false);
const pagination = ref({
  sortBy: 'nombre', // No se usa directamente en la tabla, pero se mantiene para la lógica
  descending: false,
  page: 1, // Página actual
  rowsPerPage: 10, // Tamaño de la página
});

// Función para formatear fechas (reemplaza date.formatDate de Quasar)
const formatDate = (dateValue) => {
  if (!dateValue) return '';
  // Asume que dateValue es una cadena ISO o timestamp
  return new Intl.DateTimeFormat('es-ES', { 
    day: '2-digit', 
    month: '2-digit', 
    year: 'numeric' 
  }).format(new Date(dateValue));
};

// Función principal para obtener datos de la tabla
const getModulo1TableFun = () => {
  loading.value = true;
  
  const params = {
    blurry: state.blurry,
    size: pagination.value.rowsPerPage,
    currentPage: pagination.value.page 
  };

  getModulo1Table(params)
    .then((res) => {
      loading.value = false;
      if (res.success) {
        state.tableData = res.data.records || [];
        state.total = res.data.total || 0;
      } else {
        errorMsg(res.msg);
      }
    })
    .catch(() => {
      loading.value = false;
      errorMsg('Error al cargar los datos de Modulo 1');
    });
};

// Maneja el cambio de página o límite desde el componente de paginación
const handlePaginationChange = ({ page, limit }) => {
  pagination.value.page = page;
  pagination.value.rowsPerPage = limit;
  getModulo1TableFun();
};

const onEdit = (row) => {
  console.log('Editar pedido con ID:', row.idpedido);
  // Aquí abrirías un el-dialog con los datos de `row`
}

const onDelete = (row) => {
  ElMessageBox.confirm(`¿Está seguro de que desea eliminar el pedido con ID ${row.idpedido}?`, 'Confirmación de Eliminación', {
    confirmButtonText: 'Sí, Eliminar',
    cancelButtonText: 'Cancelar',
    type: 'warning',
  })
    .then(() => {
      console.log('Solicitud de eliminación para ID:', row.idpedido);
      // Aquí llamarías a la API de eliminación (e.g., delModulo1Api)
      // Si la eliminación es exitosa:
      // successMsg('Pedido eliminado exitosamente');
      // getModulo1TableFun();
    })
    .catch(() => {
      infoMsg('Operación cancelada');
    });
}

// Llama a la función de carga inicial al montar el componente
onMounted(() => {
  getModulo1TableFun();
});
</script>

<style scoped>
.app-container {
  padding: 20px;
}
.search-and-controls {
  display: flex;
  gap: 10px;
  align-items: center;
  margin-bottom: 20px;
}
/* Estilo para que el botón circular y outline (plain) se vean más como Quasar */
:deep(.el-button.is-circle.is-plain) {
  padding: 6px;
  min-height: 28px;
  min-width: 28px;
}
</style>