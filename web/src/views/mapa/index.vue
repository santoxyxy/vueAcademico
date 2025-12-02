<template>
  <div>
    <div class="searchDiv">
      <el-input 
        class="searchInput" 
        v-model="state.blurry" 
        placeholder="Buscar por materia o paralelo" 
        clearable
      />
      <el-select 
        v-model="state.gestion" 
        placeholder="Gestión" 
        style="width: 150px"
        @change="getMapaListFun"
      >
        <el-option
          v-for="year in state.gestionOptions"
          :key="year"
          :label="year"
          :value="year"
        />
      </el-select>
      <el-button type="primary" @click="getMapaListFun">Consulta</el-button>
      <el-button 
        v-if="hasPer('mapa:add')" 
        @click="editMapaFun" 
        style="float: right;"
      >
        Nuevo
      </el-button>
    </div>

    <el-table 
      :data="state.tableData" 
      border 
      height="calc(100vh - 180px)"
    >
      <el-table-column label="Código Materia" prop="codmat" width="120" />
      <el-table-column label="Materia" prop="nombreMateria" />
      <el-table-column label="Paralelo" prop="nombreParalelo" width="100" />
      <el-table-column label="Nivel" prop="nombreNivel" width="100" />
      <el-table-column label="Gestión" prop="gestion" width="100" align="center" />
      <el-table-column label="Estado" prop="estado" width="100" align="center">
        <template #default="scope">
          <el-tag :type="scope.row.estado === 1 ? 'success' : 'danger'">
            {{ scope.row.estado === 1 ? 'Activo' : 'Inactivo' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="Funcionar" width="180" align="center">
        <template #default="scope">
          <el-button 
            v-if="hasPer('mapa:edit')" 
            type="primary" 
            @click="editMapaFun(JSON.parse(JSON.stringify(scope.row)))"
          >
            Editar
          </el-button>
          <el-button 
            v-if="hasPer('mapa:del')" 
            type="danger" 
            @click="delMapaFun(scope.row)"
          >
            Borrar
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- Modal de edición -->
    <edit-mapa 
      v-model:dialog-visible="dialogVisible" 
      :mapa-obj="state.mapaObj" 
      :gestion="state.gestion"
      @get-list="getMapaListFun"
    />
  </div>
</template>

<script setup>
import { getMapaTable, delMapa } from "../../api/mapa/mapa";
import { errorMsg, infoMsg, successMsg } from "../../utils/message";
import { hasPer } from "../../utils/common";
import editMapa from "./editMapa";
import { onMounted, reactive, ref } from "vue";
import { ElMessageBox } from "element-plus";

const state = reactive({
  blurry: '',
  gestion: new Date().getFullYear(),
  tableData: [],
  mapaObj: {},
  gestionOptions: []
});

const dialogVisible = ref(false);

onMounted(() => {
  generateGestionOptions();
  getMapaListFun();
});

// Generar opciones de gestión
const generateGestionOptions = () => {
  const currentYear = new Date().getFullYear();
  for (let i = -2; i <= 2; i++) {
    state.gestionOptions.push(currentYear + i);
  }
};

// Obtener lista
const getMapaListFun = () => {
  getMapaTable({ 
    blurry: state.blurry,
    gestion: state.gestion 
  }).then(res => {
    if (res.success) {
      state.tableData = res.data.records || res.data;
    } else {
      errorMsg(res.msg);
    }
  });
};

// Editar
const editMapaFun = (row) => {
  dialogVisible.value = true;
  state.mapaObj = row ? row : {};
};

// Eliminar
const delMapaFun = (row) => {
  ElMessageBox.confirm(
    `Confirmar eliminar asignación ${row.nombreMateria} - ${row.nombreParalelo}？`, 
    'Pista', 
    {
      confirmButtonText: 'Seguro',
      cancelButtonText: 'Cancelar',
      type: 'warning'
    }
  ).then(() => {
    delMapa(row.codmat, row.codpar, row.gestion).then(res => {
      if (res.success) {
        successMsg(res.data);
        getMapaListFun();
      } else {
        errorMsg(res.msg);
      }
    });
  }).catch(() => {
    infoMsg('Operacion Cancelada');
  });
};
</script>

<style scoped>
.searchDiv {
  display: flex;
  gap: 16px;
  margin-bottom: 16px;
}

.searchInput {
  width: 400px;
}
</style>