<template>
    <div>
        <button @click="reqUserFollow">테스트</button>
        <div style="display: inline-block; width:40vw; ">
            A영역
        </div>
        <div style="display: inline-block; width:40vw; ">
            B영역
        </div>

        <allPeople></allPeople>
    </div>
</template>

<script>
import allPeople from "../../../components/common/allPeople"
import Swal from 'sweetalert2';
import { createNamespacedHelpers } from 'vuex';



const userMapActions = createNamespacedHelpers('User').mapActions;


export default {
    components: {
        allPeople
    }
    ,
    methods:{
        ...userMapActions(['reqUserFollow']),

    }
};
</script>
