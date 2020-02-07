import Vue from 'vue';
import Vuex from 'vuex';

// 1. 사용할 vuex Javascript 파일을 import 한다.
import UserStore from './user/userStore';

Vue.use(Vuex);

export default new Vuex.Store({
    state: {},
    getters: {},
    mutations: {},
    actions: {},
    // 2. modules에 추가한다.
    modules: {
        User: UserStore,
    },
});
