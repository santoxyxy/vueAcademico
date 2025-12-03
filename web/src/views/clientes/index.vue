<template>
  <div class="app-container">

    <!-- 🔍 Barra de búsqueda -->
    <div class="toolbar">
      <q-input
        v-model="state.blurry"
        outlined
        label="Ingrese nombre o ID de cliente"
        clearable
        class="search-input"
        @keyup.enter="getClientesListFun"
      />

      <q-btn
        color="primary"
        label="Consultar"
        @click="getClientesListFun"
      />

      <q-btn
        v-if="hasPer('clientes:add')"
        color="primary"
        label="Nuevo Cliente"
        class="q-ml-auto"
        @click="editClienteFun"
      />
    </div>

    <!-- 🧾 Tabla -->
    <q-table
      title="Clientes"
      :rows="state.tableData"
      row-key="id"
      :loading="loading"
      flat
      bordered
      dense
    >
      <template v-slot:body-cell-Compañia="props">
        {{ props.row.Compañia }}
      </template>
      <template v-slot:body-cell-Apellidos="props">
        {{ props.row.Apellidos }}
      </template>
      <template v-slot:body-cell-Nombre="props">
        {{ props.row.Nombre }}
      </template>
      <template v-slot:body-cell-Direccion="props">
        {{ props.row.Direccion }}
      </template>
      <template v-slot:body-cell-Ciudad="props">
        {{ props.row.Ciudad }}
      </template>

      <!-- ACCIONES -->
      <template v-slot:body-cell-acciones="props">
        <q-btn
          size="sm"
          color="primary"
          label="Editar"
          class="q-mr-sm"
          @click="editClienteFun(props.row)"
        />
        <q-btn
          size="sm"
          color="negative"
          label="Eliminar"
          @click="delClienteFun(props.row.id, props.row.Nombre)"
        />
      </template>
    </q-table>

    <!-- 📄 Paginación -->
    <q-pagination
      v-model="state.current"
      :max="Math.ceil(state.total / state.size)"
      max-pages="7"
      boundary-numbers
      class="q-mt-md"
      @update:model-value="getClientesListFun"
    />

    <!-- ✏ Modal de edición -->
    <edit-cliente
      v-model:dialog-visible="dialogVisible"
      :cliente-obj="state.clienteObj"
      @get-list="getClientesListFun"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from "vue";
import { getClientesList, delCliente } from "../../api/clientes/clientes";
import { errorMsg, infoMsg, successMsg } from "../../utils/message";
import { hasPer } from "../../utils/common";
import { Dialog } from "quasar";
import editCliente from "./editCliente.vue";

const state = reactive({
  blurry: "",
  tableData: [],
  clienteObj: {},
  current: 1,
  size: 10,
  total: 0,
});

const dialogVisible = ref(false);
const loading = ref(false);

// 🔹 Obtener lista de clientes
const getClientesListFun = () => {
  loading.value = true;

  const params = {
    blurry: state.blurry,
    size: state.size,
    currentPage: state.current,
  };

  getClientesList(params)
    .then((res) => {
      loading.value = false;
      if (res.success) {
        state.tableData = res.data.records || res.data;
        state.total = res.data.total || 0;
      } else {
        errorMsg(res.msg);
      }
    })
    .catch(() => {
      loading.value = false;
    });
};

// 🔹 Abrir modal editar / nuevo
const editClienteFun = (row = null) => {
  state.clienteObj = row ? { ...row } : {};
  dialogVisible.value = true;
};

// 🔹 Eliminar cliente
const delClienteFun = (id, name) => {
  Dialog.create({
    title: "Confirmación",
    message: `¿Estás seguro de eliminar el cliente ${name}?`,
    persistent: true,
    ok: { label: "Eliminar", color: "negative" },
    cancel: { label: "Cancelar" },
  }).onOk(() => {
    delCliente({ id }).then((res) => {
      if (res.success) {
        successMsg(res.data);
        getClientesListFun();
      } else {
        errorMsg(res.msg);
      }
    });
  }).onCancel(() => {
    infoMsg("Operación cancelada");
  });
};

onMounted(() => {
  getClientesListFun();
});
</script>

<style scoped>
.app-container {
  padding: 20px;
}

.toolbar {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
  align-items: center;
}

.search-input {
  width: 300px;
}
</style>
