import Vue from 'vue';
import App from './App.vue';
import Router from 'vue-router';
import routes from './routes';
import store from './vuex/store';
import Swal from 'sweetalert2';

Vue.config.productionTip = false;

Vue.use(Router);
Vue.prototype.$Swal = Swal;

const router = new Router({
    routes,
    mode: 'history',
});

new Vue({
    router,
    store,
    render: h => h(App),
}).$mount('#app');
