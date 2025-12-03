<template>
  <q-page padding>
    <div class="searchDiv q-mb-md">
      <q-input
        dense
        outlined
        v-model="state.blurry"
        placeholder="Por favor ingrese un nombre de ítem"
        clearable
        @keyup.enter="getItemsListFun"
        class="searchInput"
      />
      <q-btn color="primary" @click="getItemsListFun" class="q-ml-sm">
        Consulta
      </q-btn>
      <q-btn
        color="positive"
        v-if="hasPer('items:add')"
        @click="editItemsFun"
        class="q-ml-auto"
      >
        Nuevo
      </q-btn>
    </div>

    <q-table
      :rows="state.tableData"
      row-key="codi"
      flat
      bordered
      :loading="loading"
      class="shadow-1"
    >
      <template v-slot:top-right>
        <!-- Puedes añadir filtros o botones extra aquí si quieres -->
      </template>

      <q-th auto-width>#</q-th>
      <q-tr v-for="(row, index) in state.tableData" :key="row.codi">
        <q-td auto-width>{{ index + 1 }}</q-td>
        <q-td>{{ row.codi }}</q-td>
        <q-td>{{ row.nombre }}</q-td>
        <q-td class="text-center">
          <q-badge
            :color="row.estado === 1 ? 'positive' : 'negative'"
            :label="row.estado === 1 ? 'Activo' : 'Inactivo'"
          />
        </q-td>
        <q-td class="text-center">
          <q-btn
            v-if="hasPer('items:edit')"
            dense
            flat
            color="primary"
            label="Editar"
            @click="editItemsFun({ ...row })"
          />
          <q-btn
            v-if="hasPer('items:del')"
            dense
            flat
            color="negative"
            label="Borrar"
            @click="delItemsFun(row.codi, row.nombre)"
            class="q-ml-sm"
          />
        </q-td>
      </q-tr>
    </q-table>

    <!-- Modal de edición -->
    <edit-items
      v-model:dialog-visible="dialogVisible"
      :items-obj="state.itemsObj"
      @get-list="getItemsListFun"
    />
  </q-page>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { queryItemsTable, delItems } from '@/api/items/items'
import { errorMsg, infoMsg, successMsg } from '@/utils/message'
import { hasPer } from '@/utils/common'
import EditItems from './editItems'
import { Dialog } from 'quasar'

const dialogVisible = ref(false)
const loading = ref(false)

const state = reactive({
  blurry: '',
  itemsObj: {},
  tableData: []
})

const getItemsListFun = () => {
  loading.value = true
  queryItemsTable({ blurry: state.blurry })
    .then(res => {
      if (res.success) {
        state.tableData = res.data.records
      } else {
        errorMsg(res.msg)
      }
    })
    .finally(() => {
      loading.value = false
    })
}

const editItemsFun = (row) => {
  dialogVisible.value = true
  state.itemsObj = row?.codi ? row : {}
}

const delItemsFun = (id, name) => {
  Dialog.create({
    title: 'Confirmar',
    message: `Confirmar para eliminar ítem 【${name}】？`,
    cancel: true,
    persistent: true
  }).onOk(() => {
    delItems(id).then(res => {
      if (res.success) {
        successMsg(res.data)
        getItemsListFun()
      } else {
        errorMsg(res.msg)
      }
    })
  }).onCancel(() => {
    infoMsg('Operación Cancelada')
  })
}

onMounted(() => {
  getItemsListFun()
})
</script>

<style scoped>
.searchDiv {
  display: flex;
  align-items: center;
  gap: 12px;
}
.searchInput {
  width: 300px;
}
</style>
