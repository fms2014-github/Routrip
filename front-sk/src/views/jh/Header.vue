<template>
    <div id="header" :class="{ headerHeight: headerHeight }">
        <div class="hambuger">
            <div class="hambuger-box" @click="toggle1" :class="{ dtoggle1: dtoggle1 }">
                <a class="menu-trigger" href="#">
                    <span></span>
                    <span></span>
                    <span></span>
                </a>
            </div>
            <div :class="{ dtoggle1: dtoggle1 }">
                <div class="hambuger-drop">
                    <div class="profile">
                        <div class="profile-img">
                            <img :src="user.profileImg" />
                        </div>
                        <div class="profile-info">
                            <div class="profile-nickname">{{ user.nickname }}</div>
                            <div class="profile-email">{{ user.email }}</div>
                        </div>
                    </div>
                    <div class="hambuger-menu"><i class="far fa-bell"></i><span>알림</span></div>
                    <div class="hambuger-menu">
                        <router-link v-bind:to="{ name: 'WriteForm' }"><i class="far fa-plus-square"></i><span>글쓰기</span></router-link>
                    </div>
                    <div class="hambuger-menu">
                        <a href="/profile"><i class="far fa-user"></i><span>마이페이지</span></a>
                    </div>
                </div>
            </div>
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
                    <img class="profile-img" :src="user.profileImg" />
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
            this.user = res.data;
            console.log(res.data);
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
        toggle1() {
            this.dtoggle1 = !this.dtoggle1;
        },
    },
    data: () => {
        return {
            scrollDown: false,
            headerHeight: false,
            searchWord: '',
            isSearchPage: false,
            user: '',
            dropBox: false,
            dtoggle1: false,
        };
    },
};
</script>
