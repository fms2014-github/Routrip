<template>
    <div id="app">
        <Loading 
            :active.sync="$store.state.flag"
            color="#41A8AC"
            :can-cancel="true" 
            :is-full-page="true"
        />
        <router-view></router-view>
    </div>
</template>

<script>
// Import Loading Animations
import Loading from 'vue-loading-overlay';
// Import stylesheet
import 'vue-loading-overlay/dist/vue-loading.css';

import { createNamespacedHelpers } from 'vuex';
// import Loading from '../src/components/common/Loading'

const userMapActions = createNamespacedHelpers('User').mapActions;

export default {
    components: {
        Loading
    },
    name: 'app',
    created() {
        if (localStorage.getItem('routrip_JWT') !== null && 
        localStorage.getItem('routrip_JWT') !== undefined && 
        localStorage.getItem('routrip_JWT') !== ""){
        this.reqUserInfo();
        }
    },
    methods: {
        ...userMapActions(['reqUserInfo']),
    },
};
</script>
<style></style>