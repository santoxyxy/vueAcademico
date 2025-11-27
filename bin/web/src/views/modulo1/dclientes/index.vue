<templatexxxx>
<q-layout view="hHh lpR fFf">
    <q-page-container>
      <div class="q-pa-md">
        <q-table
          color="primary"
          card-class="bg-amber-5 text-brown"
          table-class="text-grey-8"
          table-header-class="text-brown"
          flat
          bordered
          ref="tableRef"
          title="Detalle de Clientessssss"
          :rows="state.tableData"
          :columns="columns"
          row-key="idcliente"
          v-model:pagination="pagination"
          :loading="loading"
          :filter="state.blurry"
          binary-state-sort
          :visible-columns="visibleColumns"
        >
          <template v-slot:top>
            <--<q-btn icon="add" @click="onEdit(0)">QUE ES</q-btn> !-->
            <q-space />
          </template>
          <template v-slot:top-right>
            <q-input borderless dense debounce="300" v-model="state.blurry" placeholder="Buscar">
              <template v-slot:append>
                <q-icon name="search" @click="getModulo1DClientesTableFun" />
              </template>
            </q-input>
          </template>
          <template v-slot:body-cell-actions="props">
            <q-td :props="props">
              <q-btn
                @click="onEdit(props.row)"
                fab-mini
                dense
                square
                outline
                icon="mode_edit"
                color="primary"
                aria-label="Editar"
                class="q-mr-sm"
              />
              <q-btn
                @click="onPedidos(props.row)"
                fab-mini
                dense
                square
                outline
                icon="table_view"
                color="negative"
                aria-label="Detalle"
              />
            </q-td>
          </template>
        </q-table>
        <q-pagination
          v-model="pagination.page"
          :max="Math.ceil(state.total / pagination.rowsPerPage)"
          @update:model-value="getModulo1DClientesTableFun"
        />
      </div>

      <!-- Componente de pedidos -->
      <pedidoDclientes
        v-model="pedidoDialogVisible"
        :cliente-obj="state.selectedCliente"
        @close="pedidoDialogVisible = false"
      />
      
      <!-- Componente de edición -->
      <editDClientes
        v-model="clienteDialogVisible"
        :cliente-obj="state.selectedCliente"
        @close="clienteDialogVisible = false"
      />
    </q-page-container>
  </q-layout>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { getModulo1DClientesTable, delModulo1 } from '../../../api/modulo1/modulo1';
import { errorMsg } from '../../../utils/message';
import { useRouter } from 'vue-router'; // Agregado para usar rutas dinámicas
import pedidoDclientes from './pedidoDclientes.vue';
import editDClientes from './editDClientes.vue';

const router = useRouter(); // Inicializar el router

const state = reactive({
  blurry: '',
  tableData: [],
  total: 0,
  selectedCliente: null,
});

const columns = [
  { name: 'idcliente', label: 'ID Cliente', align: 'center', field: 'idcliente', sortable: true },
  { name: 'nombre', label: 'Nombre', align: 'left', field: 'nombre', sortable: true },
  { name: 'actions', label: 'Acciones', align: 'center', field: 'actions', sortable: false },
];

const visibleColumns = ref(['idcliente', 'nombre', 'actions']);
const loading = ref(false);
const pagination = ref({ sortBy: 'nombre', descending: false, page: 1, rowsPerPage: 10 });

const pedidoDialogVisible = ref(false);
const clienteDialogVisible = ref(false);

const getModulo1DClientesTableFun = () => {
  loading.value = true;
  const params = {
    blurry: state.blurry,
    currentPage: pagination.value.page,
    size: pagination.value.rowsPerPage,
  };
  getModulo1DClientesTable(params)
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
      errorMsg('Error al cargar los datos');
    });
};

const onPedidos = (row) => {
  if (!row || !row.idcliente) return;
  state.selectedCliente = { ...row };
  pedidoDialogVisible.value = true;
};

const onEdit = (row) => {
  if (!row || !row.idcliente) {
    console.error('Fila inválida o ID de cliente no encontrado:', row);
    return;
  }
  state.selectedCliente = { ...row };
  clienteDialogVisible.value = true;

  // Alternativamente, usar rutas dinámicas:
   router.push({ name: 'EditCliente', params: { idcliente: row.idcliente } });
};

onMounted(() => {
  getModulo1DClientesTableFun();
});
</script>
