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
        this.reqUserInfo();
    },
    methods: {
        ...userMapActions(['reqUserInfo']),
    },
};
</script>
<style></style>
