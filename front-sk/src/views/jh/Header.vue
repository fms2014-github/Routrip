<template>
    <div id="header" :class="{ headerHeight: headerHeight }">
        <div class="hambuger">
            <i class="fas fa-bars"></i>
        </div>
        <div class="home">
            <router-link v-bind:to="{ name: 'Main' }" class="btn--text">
                <img class="logo" src="../../assets/images/routrip_logo.png" />
            </router-link>
            <span class="title" :class="{ scrollDown: scrollDown }">루트립</span>
        </div>
        <div class="search-ipad" :class="{ scrollDown: !scrollDown }">
            <form action class="search-form">
                <input type="text" placeholder="검색..." />
            </form>
        </div>
        <div class="menu">
            <span class="menu-icon">
                <i class="far fa-plus-square"></i>
            </span>
            <span class="menu-icon">
                <i class="far fa-comment-alt"></i>
            </span>
            <a href="#">
                <img class="profile-img" src="../../assets/images/user.png" />
            </a>
            <button @click="logout">logout</button>
        </div>
    </div>
</template>

<script>
import '../../assets/css/main/header.scss';
import { createNamespacedHelpers } from 'vuex';

const userMapActions = createNamespacedHelpers('User').mapActions;

export default {
    created() {
        window.addEventListener('scroll', this.scrollY);
    },
    destroyed() {
        window.removeEventListener('scroll', this.scrollY);
    },
    methods: {
        ...userMapActions(['logout']),
        scrollY(event) {
            if (window.scrollY > 40) {
                this.scrollDown = true;
                this.headerHeight = true;
            } else {
                this.scrollDown = false;
                this.headerHeight = false;
            }
        },
    },
    data: () => {
        return {
            scrollDown: false,
            headerHeight: false,
        };
    },
};
</script>
