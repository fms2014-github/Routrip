<template>
    <div id="header" :class="{ headerHeight: headerHeight }">
        <div class="hambuger">
            <i class="fas fa-bars"></i>
        </div>
        <div class="home">
            <router-link v-bind:to="{ name: 'Main' }" class="btn--text">
                <img class="logo" src="../../assets/images/routrip_logo.png" />
            </router-link>
            <span class="title" :class="{ scrollDown: scrollDown }">루 : 트립</span>
        </div>
        <div class="search-box">
            <form>
                <input v-model="searchWord" placeholder="Search..." />
                <button @click="searchAll">
                    <i class="fa fa-search"></i>
                </button>
            </form>
        </div>
        <div class="menu">
            <div class="menu-box">
                <span class="menu-icon">
                    <router-link v-bind:to="{ name: 'WriteForm' }" class="btn--text"><i class="far fa-plus-square"></i></router-link>
                </span>
            </div>
            <div class="menu-box">
                <span class="menu-icon">
                    <i class="far fa-bell"></i>
                </span>
            </div>
            <div class="menu-box mypage" @click="toggleDropBox">
                <span class="menu-icon">
                    <img class="profile-img" :src="userImg" />
                </span>
                <div class="drop-box" :class="{ dropBox: !dropBox }">
                    <!-- <div><router-link :to="{ name: 'Profile' }">마이페이지</router-link></div> -->
                    <div><a href="/profile">마이페이지</a></div>
                    <div @click="logout">로그아웃</div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import '../../assets/css/main/header.scss';
import { createNamespacedHelpers } from 'vuex';
import Axios from 'axios';

const userMapState = createNamespacedHelpers('User').mapState;
const userMapMutations = createNamespacedHelpers('User').mapMutations;
const userMapGetters = createNamespacedHelpers('User').mapGetters;
const userMapActions = createNamespacedHelpers('User').mapActions;
const URI = 'http://192.168.100.70:8083/';
export default {
    created() {
        this.jwt = localStorage.getItem('routrip_JWT');
        Axios.post(`${URI}/account/decode`, { jwt: this.jwt }).then(res => {
            this.userImg = res.data.profileImg;
            // console.log(res.data);
        });
        window.addEventListener('scroll', this.scrollY);
    },
    computed: {
        ...userMapState(['User']),
        ...userMapGetters(['getUser']),
    },
    destroyed() {
        window.removeEventListener('scroll', this.scrollY);
    },
    methods: {
        ...userMapMutations(['setUser']),
        ...userMapActions(['reqUserInfo']),
        ...userMapActions(['logout']),
        scrollY() {
            if (window.scrollY > 40) {
                this.scrollDown = true;
                this.headerHeight = true;
            } else {
                this.scrollDown = false;
                this.headerHeight = false;
            }
        },
        searchAll() {
            // console.log(this.searchWord);
            this.$router.push({ name: 'Middleware', params: { searchWord: this.searchWord } });
        },
        toggleDropBox() {
            this.dropBox = !this.dropBox;
        },
    },
    data: () => {
        return {
            scrollDown: false,
            headerHeight: false,
            searchWord: '',
            isSearchPage: false,
            userImg: '',
            dropBox: false,
        };
    },
};
</script>
