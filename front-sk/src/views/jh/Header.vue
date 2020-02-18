<template>
    <div id="header" :class="{ headerHeight: headerHeight }">
        <div class="hambuger">
            <i class="fas fa-bars"></i>
        </div>
        <div class="home">
            <router-link :to="{ name: 'Main' }" class="btn--text">
                <img class="logo" src="../../assets/images/routrip_logo.png" />
            </router-link>
            <span class="title" :class="{ scrollDown: scrollDown }">루 : 트립</span>
        </div>
        <div class="search-box">
            <form>
                <input v-model="searchWord" />
                <button @click="searchAll">
                    <i class="fa fa-search"></i>
                </button>
            </form>
        </div>
        <div class="menu">
            <span class="menu-icon">
                <i class="far fa-plus-square"></i>
            </span>
            <span class="menu-icon">
                <i class="far fa-comment-alt"></i>
            </span>
            <a href="/profile">
                <img class="profile-img" src="../../assets/images/user.png" />
            </a>
            <button @click="logout">logout</button>
        </div>
    </div>
</template>

<script>
import '../../assets/css/main/header.scss';
import { createNamespacedHelpers } from 'vuex';
import Axios from 'axios';

import Notification from '../user/Notification'

const userMapActions = createNamespacedHelpers('User').mapActions;
const URI = 'http://192.168.100.70:8083/';
export default {
  data: () => {
    return {
      scrollDown: false,
      headerHeight: false,
      searchWord: '',
      isSearchPage: false,
    };
  },
  created() {
    window.addEventListener('scroll', this.scrollY);
  },
  destroyed() {
    window.removeEventListener('scroll', this.scrollY);
  },
  methods: {
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
  },
};
</script>
