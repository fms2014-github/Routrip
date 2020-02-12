<template>
    <div id="app">
        <router-view></router-view>
    </div>
</template>
<script>
import { createNamespacedHelpers } from 'vuex';

const userMapActions = createNamespacedHelpers('User').mapActions;

export default {
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
