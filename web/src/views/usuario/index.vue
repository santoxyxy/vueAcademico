<template>
<q-layout view="hHh lpR fFf">
    <q-page-container>
        <q-table
        title="Listar Usuarios"
        :columns="columns"
        :rows="state.tableData"
        row-key="idusuario"
        :visible-columns="visibleColumns"
        >
        <template v-slot:top-left >
        <q-btn
        color="primary"
        icon="add"
        label ="+ usuario"
        @click = "onEdit()"
        />
    </template>
        </q-table>
    </q-page-container>
</q-layout>
</template>
<script setup>
import {ref, reactive, onMounted} from 'vue'
import {queryUsuarioTable} from '../../api/usuario/usuario'
import {errorMsg} from '../../utils/message'
import Pagination from '../../components/Pagination.vue'
// columnas llamadas de backend (controller)
const state = reactive ({
blurry:'',
tableData:[],
userObj:[],
current:1,
size:10,
total: 0,
})
const columns = [
    {name:'idusuario',label:'Id Usuario',align:'center',field:'idusuario',sortable:true},
    {name:'nombre',label:'Nombre',align:'center',field:'nombre',sortable:true},
    {name:'ap',label:'Apellido Paterno',align:'left',field:'ap',sortable:true},
    {name:'am',label:'Apellido Materno',align:'center',field:'am',sortable:true},
]
const visibleColumns = ref(['idusuario','nombre'])

// funcion 
const queryUsuarioTableFun=()=> {
    const params = {
        blurry: state.blurry,
        size:state.size,
        currentPage: state.current
    }
    queryUsuarioTable(params)
    .then((res)=> {
            if (res.success) {
                state.tableData = res.data.records
                state.total = res.data.total
            }  else {
                errorMsg(res.mgs)
            }
        }
    ) .catch(()=>{
        errorMsg('Error al cargar los datos')
    } )
}

const onEdit = () => {
    console.log('NUEVO USUARIO')
}
onMounted(() => {
queryUsuarioTableFun()
})
</script>
<style lang="sass">
</style>