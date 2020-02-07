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
    beforeCreate() {
        this.reqUserInfo();
    },
    mounted() {
        console.log('haha');
    },
    methods: {
        ...userMapActions(['reqUserInfo']),
    },
};
</script>
<style></style>
