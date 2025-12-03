<template>
  <el-dialog
    :model-value="dialogVisible"
    width="80vw"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    @close="emit('close')"
  >
    <template #title>
      Pedidos del Cliente
    </template>

    <!-- BUSCADOR -->
    <div class="flex justify-end mb-3">
      <el-input
        v-model="state.blurry"
        placeholder="Buscar"
        clearable
        @keyup.enter="getModulo1DClientesPedidosTableFun"
        style="width: 250px"
      >
        <template #append>
          <el-button
            icon="Search"
            @click="getModulo1DClientesPedidosTableFun"
          />
        </template>
      </el-input>
    </div>

    <!-- TABLA -->
    <el-table
      :data="state.tableData"
      border
      style="width: 100%;"
      v-loading="loading"
    >
      <el-table-column
        prop="iddepedido"
        label="ID Pedido"
        align="center"
        width="120"
      />

      <el-table-column
        prop="fechadepedido"
        label="Fecha de pedido"
        align="center"
      >
        <template #default="scope">
          {{ formatDate(scope.row.fechadepedido) }}
        </template>
      </el-table-column>

      <el-table-column
        prop="fechadeenvio"
        label="Fecha de envío"
        align="center"
      >
        <template #default="scope">
          {{ formatDate(scope.row.fechadeenvio) }}
        </template>
      </el-table-column>

      <el-table-column
        prop="subtotal"
        label="Sub Total"
        align="center"
      />

      <el-table-column
        prop="totalpedido"
        label="Total Pedido"
        align="center"
      />

      <el-table-column
        prop="gastosdeenvio"
        label="Gastos de Envío"
        align="center"
      />
    </el-table>

    <!-- PAGINACIÓN -->
    <div class="flex justify-center mt-3">
      <el-pagination
        background
        layout="prev, pager, next"
        :page-size="pagination.rowsPerPage"
        :total="state.total"
        v-model:current-page="pagination.page"
        @current-change="getModulo1DClientesPedidosTableFun"
      />
    </div>

    <!-- BOTONES -->
    <template #footer>
      <el-button type="danger" @click="emit('close')">Cerrar</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, watch } from "vue";
import { getModulo1DClientesPedidosTable } from "../../../api/modulo1/modulo1";
import { errorMsg } from "../../../utils/message";

const props = defineProps({
  clienteObj: { type: Object, default: () => ({}) },
  modelValue: { type: Boolean, default: false },
});

const emit = defineEmits(["close", "update:modelValue"]);

// Control del diálogo
const dialogVisible = ref(props.modelValue);
watch(() => props.modelValue, (val) => (dialogVisible.value = val));
watch(dialogVisible, (val) => emit("update:modelValue", val));

// Estado
const state = reactive({
  tableData: [],
  blurry: "",
  total: 0,
});

const pagination = ref({
  page: 1,
  rowsPerPage: 10,
});

const loading = ref(false);

// Formateo de fecha estilo Quasar pero sin Quasar
const formatDate = (val) => {
  if (!val) return "";
  const d = new Date(val);
  return d.toLocaleDateString("es-BO");
};

// Cargar pedidos
const getModulo1DClientesPedidosTableFun = () => {
  if (!props.clienteObj.idcliente) return;

  loading.value = true;

  const params = {
    id: props.clienteObj.idcliente,
    blurry: state.blurry,
    currentPage: pagination.value.page,
    size: pagination.value.rowsPerPage,
  };

  getModulo1DClientesPedidosTable(params)
    .then((res) => {
      loading.value = false;

      if (res.success) {
        state.tableData = res.data.records || [];
        state.total = res.data.total || 0;
        dialogVisible.value = true;
      } else {
        errorMsg(res.msg);
      }
    })
    .catch(() => {
      loading.value = false;
      errorMsg("Error al cargar los pedidos");
    });
};

// Cargar automáticamente cuando cambia el cliente
watch(
  () => props.clienteObj,
  (newVal) => {
    if (newVal && newVal.idcliente) {
      pagination.value.page = 1; // reset página
      getModulo1DClientesPedidosTableFun();
    }
  },
  { immediate: true }
);
</script>

<style scoped>
.flex {
  display: flex;
}
.justify-end {
  justify-content: flex-end;
}
.justify-center {
  justify-content: center;
}
.mb-3 {
  margin-bottom: 1rem;
}
.mt-3 {
  margin-top: 1rem;
}
</style>
