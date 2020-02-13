<template>
    <div class="profile-page">
        <Header></Header>
        <div class="wrapD">
            <h2>My Profile</h2>
            <div class="profile-wrap">
                <UserPicture :userPicture="true" :pic="userinfo.pic" />
                <div class="user-info">
                    <HeaderComponent :headerTitle="userinfo.nickname" rightText="수정" @changeNick="changeNick" />
        
                <!-- <button @click="reqlikes">테스트</button> -->

                <div class="none-border">
                    <button class="button-text">회원탈퇴</button>
                </div>
                </div>
            </div>
            
            <div class="wrap">
                <router-link v-bind:to="{ name: 'UserPost' }"><TabComponent tabTitle="글" :isActive="true"/></router-link>
                <router-link v-bind:to="{ name: 'UserComment' }"><TabComponent tabTitle="댓글" :isActive="true"/></router-link>
                <router-link v-bind:to="{ name: 'UserLike' }"><TabComponent tabTitle="좋아요" :isActive="true"/></router-link>
                <router-link v-bind:to="{ name: 'UserPeople' }"><TabComponent tabTitle="사람들" :isActive="true"/></router-link>
                <router-link v-bind:to="{ name: 'UserScrap' }"><TabComponent tabTitle="스크랩" :isActive="true"/></router-link>
            </div>

            <div class="profile-tab-page">
                <router-view name="profile"></router-view>
            </div>
        </div>
    </div>
</template>

<script>

import Axios from 'axios';
import Swal from 'sweetalert2';
import { createNamespacedHelpers } from 'vuex';

// import UserApi from '../../../apis/UserApi';
import HeaderComponent from '../../../components/common/Header';
import UserPicture from '../../../components/common/UserPicture';
import TabComponent from '../../../components/common/Tab';
import Header from '../../jh/Header';


import '../../../assets/css/profile.scss';
import '../../../assets/css/style.scss';


const userMapActions = createNamespacedHelpers('User').mapActions; //
const userMapGetters = createNamespacedHelpers('User').mapGetters; //

export default {
    components: {
        HeaderComponent,
        UserPicture,
        TabComponent,
        Header
    },
    computed: {
        ...userMapGetters(['getUser']),
    },


    mounted() {
        this.getInfo();
        this.checkLogin();
        this.reqInfo();

    },
    methods: {
        ...userMapActions(['reqUserInfo']),
        ...userMapActions(['logout']),


        async reqInfo() {
            await this.reqUserInfo();
            
            console.log("?", this.getUser);
            this.userinfo.nickname=this.getUser.data.nickname;
            this.userinfo.pic=this.getUser.data.profileImg;
        },

        // logoutClick() {
        //     this.logout().then(() => {
        //         this.$router.push('/');
        //     })
        // },
        
        popupToggle() {
            this.popup = true;
        },
        getInfo() {
            this.userinfo.email = localStorage.getItem('loginedEmail');
            this.userinfo.nickname = localStorage.getItem('nickName');
        },
        updated() {
            if (localStorage.getItem('popup') !== null) {
                // this.popup = !Boolean(localStorage.getItem('popup'));
                localStorage.removeItem('popup');
            }
        },
        
        checkLogin() {
            if (localStorage.getItem('loginedEmail') !== null) {
                this.show = true;
            } else {
                this.show = false;
            }
        },

        async changeNick() { 
            await Swal.fire({
            title: '바꿀 닉네임을 입력해주세요.',
            input: 'text',
            inputValue: this.userinfo.nickname,
            showCancelButton: true,
            inputValidator: (value) => {
                if (!value) {
                    return '뭐라도 써보세요!'
                }
                else{
                    const jwt = localStorage.getItem('routrip_JWT');
                    Axios.put('http://192.168.100.70:8083/account/user/',
                        {
                            nickname: value,
                            jwt : jwt
                        }
                    )
                    .then(res => {
                        console.log(res.data);
                        localStorage.setItem('routrip_JWT', res.data);
                        this.reqInfo();

                    });
                }
            }})},
    },
    data() {
        
        return {
            hi:'',
            userinfo: {
                token: '',
                email: '',
                nickname: '',
                pic:'',
            },
            show: false,
        };
    },
};
</script>
