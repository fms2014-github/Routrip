<template>
    <div class="detail">
        <Header></Header>
    </div>
</template>

<script>
import Header from './Header.vue';
//axios
import Axios from 'axios';

//component
import { Hooper, Slide, Pagination as HooperPagination, Navigation as HooperNavigation } from 'hooper';

// 뷰엑스를 가져옴
import { createNamespacedHelpers } from 'vuex';
// load user store 필요한 부분만 가져오기
const userMapState = createNamespacedHelpers('User').mapState;
const userMapMutations = createNamespacedHelpers('User').mapMutations;
const userMapGetters = createNamespacedHelpers('User').mapGetters;
const userMapActions = createNamespacedHelpers('User').mapActions;

const URI = 'http://192.168.100.70:8083/';
export default {
    components: {
        Header,
    },
};
</script>

<style></style>
