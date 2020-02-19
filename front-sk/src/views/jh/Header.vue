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
                    <a href="/profile">
                        <div class="profile">
                            <div class="profile-img">
                                <img :src="user.profileImg" />
                            </div>
                            <div class="profile-info">
                                <div class="profile-nickname">{{ user.nickname }}</div>
                                <div class="profile-email">{{ user.email }}</div>
                            </div>
                        </div>
                    </a>

                    <div class="hambuger-menu">
                        <!-- <i class="far fa-bell"></i> -->
                        <img src="../../assets/images/bell.png" alt />
                        <span>알림</span>
                    </div>
                    <div class="hambuger-menu">
                        <router-link v-bind:to="{ name: 'WriteForm' }">
                            <!-- <i class="far fa-plus-square"></i> -->
                            <img src="../../assets/images/pencil.png" alt />
                            <span>글쓰기</span>
                        </router-link>
                    </div>
                    <div class="hambuger-menu">
                        <div @click="logout">
                            <!-- <i class="far fa-user"></i> -->
                            <img src="../../assets/images/exit.png" alt />
                            <span>로그아웃</span>
                        </div>
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
                    <router-link v-bind:to="{ name: 'WriteForm' }" class="btn--text">
                        <!-- <i class="far fa-plus-square"></i> -->
                        <img src="../../assets/images/pencil.png" alt />
                    </router-link>
                </span>
            </div>
            <div class="menu-box">
                <div class="wobble-hor-top">
                    <span class="menu-icon">
                        <img src="../../assets/images/bell.png" @click="dropAlarm" />
                    </span>
                </div>
                <div class="alarm-drop-box" :class="{dtoggle2:!dtoggle2}">
                    <div
                        class="alarm-box"
                        v-for="(alarm, alarmIdx) in alarms"
                        :key="alarmIdx"
                        :class="{isread : alarm.isread==1}"
                    >
                        <div class="profile-img">
                            <img :src="alarm.user.profileImg" alt />
                        </div>
                        <div class="alarm-info">
                            <div class="text-time">
                                <div class="alarm-text">{{alarm.text}}</div>
                                <div class="alarm-alarmtime">{{alarm.alarmtime}}</div>
                                <div class="delete-alarm" @click="deleteAlarm(alarm)">x</div>
                            </div>
                            <div
                                v-if="alarm.alarmtype>=2 && alarm.alarmtype<=4"
                            >" {{alarm.detail}} "</div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="menu-box mypage" @click="toggleDropBox">
                <div class="profile-img-box">
                    <span class="menu-icon">
                        <img class="profile-img" :src="user.profileImg" />
                    </span>
                </div>
                <div class="drop-box" :class="{ dropBox: !dropBox }">
                    <div class="drop-box-menu">
                        <a href="/profile">
                            <img src="../../assets/images/user2.png" alt />
                            <span>마이페이지</span>
                        </a>
                    </div>
                    <div class="drop-box-menu" @click="logout">
                        <img src="../../assets/images/exit.png" alt />
                        <span>로그아웃</span>
                    </div>
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
        });
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
        dropAlarm() {
            this.dtoggle2 = !this.dtoggle2;
            if (this.dtoggle2) {
                this.alarms = [];
                Axios.post(`${URI}/account/alarm`, { jwt: this.jwt })
                    .then(res => {
                        for (var i = 0; i < res.data.length; ++i) {
                            this.alarms.push(res.data[i]);
                        }
                    })
                    .catch(res => {});
            }
        },
        deleteAlarm(alarm) {
            console.log(alarm.alarmid);
            Axios.delete(`${URI}/account/alarm`, { data: { alarmid: alarm.alarmid } })
                .then(res => {
                    this.alarms = [];
                    Axios.post(`${URI}/account/alarm`, { jwt: this.jwt })
                        .then(res => {
                            for (var i = 0; i < res.data.length; ++i) {
                                this.alarms.push(res.data[i]);
                            }
                        })
                        .catch(res => {});
                })
                .catch(res => {
                    console.log('알람 삭제 실패');
                });
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
            dtoggle2: false,
            alarms: [],
        };
    },
};
</script>
