<template>
  <q-layout view="hHh lpR fFf">
    <q-page-container>
      <div class="q-pa-md">
        <!-- Barra de búsqueda -->
        <div class="searchDiv">
          <q-input 
            class="searchInput" 
            v-model="state.blurry" 
            placeholder="Buscar por código o nombre de materia" 
            clearable
            @keyup.enter="getMateriasListFun"
          />
          <q-btn color="primary" @click="getMateriasListFun" dense>
            <q-icon name="search" />
            Consultar
          </q-btn>
          <q-btn 
            color="positive" 
            @click="openAddDialog"
            dense
            v-if="hasPer('/sys/materia/edit')"
          >
            <q-icon name="add" />
            Nueva Materia
          </q-btn>
        </div>

        <!-- Filtro por estado -->
        <div class="q-mb-md">
          <q-btn-toggle
            v-model="state.estadoFiltro"
            toggle-color="primary"
            :options="[
              { label: 'Activos', value: 1 },
              { label: 'Inactivos', value: 0 },
              { label: 'Todos', value: null }
            ]"
            @update:model-value="getMateriasListFun"
            dense
          />
        </div>

        <!-- Tabla de materias -->
        <q-table
          flat
          bordered
          ref="tableRef"
          title="Gestión de Materias"
          :rows="state.tableData"
          :columns="columns"
          row-key="codmat"
          v-model:pagination="pagination"
          :loading="loading"
          binary-state-sort
          @request="onRequest"
        >
          <!-- Estado -->
          <template v-slot:body-cell-estado="props">
            <q-td :props="props">
              <q-badge 
                :color="props.row.estado === 1 ? 'positive' : 'negative'"
                :label="props.row.estado === 1 ? 'Activo' : 'Inactivo'"
              />
            </q-td>
          </template>

          <!-- Acciones -->
          <template v-slot:body-cell-actions="props">
            <q-td :props="props">
              <q-btn 
                flat 
                dense 
                color="primary" 
                icon="edit"
                @click="editMateria(props.row)"
                v-if="hasPer('/sys/materia/edit')"
              >
                <q-tooltip>Editar</q-tooltip>
              </q-btn>
              <q-btn 
                flat 
                dense 
                color="negative" 
                icon="delete"
                @click="deleteMateria(props.row)"
                v-if="hasPer('/sys/materia/del')"
              >
                <q-tooltip>Eliminar</q-tooltip>
              </q-btn>
            </q-td>
          </template>
        </q-table>

        <!-- Paginación -->
        <q-pagination
          v-model="pagination.page"
          :max="Math.ceil(state.total / pagination.rowsPerPage)"
          @update:model-value="getMateriasListFun"
          max-pages="7"
          color="primary"
          class="q-mt-md"
        />
      </div>

      <!-- Modal de edición -->
      <EditMateria
        v-model="dialogVisible"
        :materia-data="currentMateria"
        @success="getMateriasListFun"
      />
    </q-page-container>
  </q-layout>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { queryMateriaTable, delMateria } from '@/api/materia/materia';
import { errorMsg, successMsg } from '@/utils/message';
import { hasPer } from '@/utils/common';
import EditMateria from './editMateria.vue';
import { Dialog } from 'quasar';

// Estado reactivo
const state = reactive({
  blurry: '',
  tableData: [],
  current: 1,
  size: 10,
  total: 0,
  estadoFiltro: 1 // 1=Activos, 0=Inactivos, null=Todos
});

// Columnas
const columns = [
  { name: 'codmat', required: true, label: 'Código', align: 'left', field: 'codmat', sortable: true },
  { name: 'nombre', required: true, label: 'Nombre', align: 'left', field: 'nombre', sortable: true },
  { name: 'nombreNivel', label: 'Nivel', align: 'left', field: 'nombreNivel', sortable: true },
  { name: 'estado', label: 'Estado', align: 'center', field: 'estado' },
  { name: 'actions', label: 'Acciones', align: 'center' }
];

const loading = ref(false);
const pagination = ref({
  sortBy: 'codmat',
  descending: false,
  page: 1,
  rowsPerPage: 10
});

const dialogVisible = ref(false);
const currentMateria = ref(null);

// Obtener lista de materias
const getMateriasListFun = () => {
  loading.value = true;
  const params = {
    blurry: state.blurry,
    size: state.size,
    currentPage: pagination.value.page,
    estado: state.estadoFiltro
  };

  queryMateriaTable(params)
    .then((res) => {
      loading.value = false;
      if (res.success) {
        state.tableData = res.data.records;
        state.total = res.data.total;
      } else {
        errorMsg(res.msg);
      }
    })
    .catch(() => {
      loading.value = false;
    });
};

// Manejo de paginación
const onRequest = (props) => {
  const { page, rowsPerPage, sortBy, descending } = props.pagination;
  pagination.value.sortBy = sortBy || '';
  pagination.value.descending = descending;
  pagination.value.page = page;
  pagination.value.rowsPerPage = rowsPerPage;
  state.size = rowsPerPage;
  getMateriasListFun();
};

// Abrir diálogo para nueva materia
const openAddDialog = () => {
  currentMateria.value = null;
  dialogVisible.value = true;
};

// Editar materia
const editMateria = (row) => {
  currentMateria.value = { ...row };
  dialogVisible.value = true;
};

// Eliminar materia con Quasar Dialog
const deleteMateria = (row) => {
  Dialog.create({
  title: 'Confirmar eliminación',
  message: `¿Está seguro de eliminar la materia "${row.nombre}"?`,
  persistent: true,
  ok: { label: 'Sí, eliminar', color: 'negative' },
  cancel: { label: 'Cancelar', color: 'grey-5' }
}).onOk(() => {
  delMateria(row.codmat).then((res) => {
    if (res.success) {
      successMsg('Materia eliminada exitosamente');
      getMateriasListFun();
    } else {
      errorMsg(res.msg);
    }
  });
});
};

onMounted(() => {
  getMateriasListFun();
});
</script>

<style scoped>
.searchDiv {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.searchInput {
  flex: 1;
  max-width: 400px;
}
</style>
