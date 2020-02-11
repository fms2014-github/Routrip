<template>
    <div class="profile-page">
        <div class="wrapD">
            <h2>My Profile</h2>
            <button @click="logout">logout</button>
            <div class="profile-wrap">
                <UserPicture :userPicture="true" />
                <div class="user-info">
<<<<<<< HEAD
                    <HeaderComponent :headerTitle="userinfo.nickname" rightText="수정" @changeNick="changeNick" />
        
                <button @click="reqUserInfo">테스트</button>
                <button @click="test">프린터</button>

                <div class="none-border">
                    <button class="button-text">회원탈퇴</button>
                </div>
=======
                    <HeaderComponent :headerTitle="userinfo.email" :mailIcon="true" />
                    <HeaderComponent
                        :headerTitle="userinfo.nickname"
                        rightText="수정"
                        :changeNick="changeNick"
                    />

                    <button @click="reqUserInfo">테스트</button>

                    <div class="none-border">
                        <button class="button-text">회원탈퇴</button>
                    </div>
>>>>>>> 6a1fc191ef2a978dcd8c12b5c70bf3f3ea85fae9
                </div>
            </div>
            
            <div class="wrap">
                <router-link v-bind:to="{ name: 'UserPost' }">
                    <TabComponent tabTitle="글" :isActive="true" />
                </router-link>
                <router-link v-bind:to="{ name: 'UserComment' }">
                    <TabComponent tabTitle="댓글" :isActive="true" />
                </router-link>
                <router-link v-bind:to="{ name: 'UserLike' }">
                    <TabComponent tabTitle="좋아요" :isActive="true" />
                </router-link>
                <router-link v-bind:to="{ name: 'UserPeople' }">
                    <TabComponent tabTitle="사람들" :isActive="true" />
                </router-link>
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

<<<<<<< HEAD
// import UserApi from '../../../apis/UserApi';
import HeaderComponent from '../../../components/common/Header';
import UserPicture from '../../../components/common/UserPicture';
import TabComponent from '../../../components/common/Tab';


import '../../../assets/css/profile.scss';
import '../../../assets/css/style.scss';


=======
>>>>>>> 6a1fc191ef2a978dcd8c12b5c70bf3f3ea85fae9
const userMapActions = createNamespacedHelpers('User').mapActions;
const userMapGetters = createNamespacedHelpers('User').mapGetters;

export default {
    components: {
        HeaderComponent,
        UserPicture,
        TabComponent,
    },
    computed: {
        ...userMapGetters(['getUser']),
    },

    // created: function(){
    //     Axios.post('http://192.168.100.70:8083/account/test/')
    //         .then(res => {
    //             console.log(res.data)
    //             this.userinfo.token=res.data;

    //         }).catch(error=>{
    //             console.error(error);
    //         })
    // },

    mounted() {
        this.getInfo();
        this.checkLogin();

        this.reqInfo();
    },
    methods: {
        ...userMapActions(['reqUserInfo']),
<<<<<<< HEAD
        ...userMapActions(['logout']),

        async reqInfo() {
            
            await this.reqUserInfo();
            console.log("?", this.getUser);
            this.userinfo.nickname=this.getUser.data.nickname;
        },
        getAllPost(){
            
        },

        test() {
            this.userinfo.nickname=this.getUser.data.nickname
        },
        logout() {
            this.logout().then(() => {
                this.$router.push('/');
            })
        },
      
=======
>>>>>>> 6a1fc191ef2a978dcd8c12b5c70bf3f3ea85fae9

        // tokener(e) {
        //     console.log("gihihihifgigfdig",e)
        //     Axios.get('http://192.168.100.70:8083/account/decode'+e)
        //         .then(res=>{
        //             console.log(res.data)
        //             this.userinfo.email=res.data.email
        //             this.userinfo.nickname=res.data.nickname
        //         }).catch(error=>{
        //             console.error(error);
        //         })
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

<<<<<<< HEAD
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
                    Axios.put('http://192.168.100.70:8083/account/user/', { 
                            nickname: value,
                            jwt : jwt 
                        }
                    )
                    // .then(res => {
                    //     this.reqUserInfo();
                    //     const jwt = localStorage.getItem('routrip_JWT');
                    // });
                }
            }})},
=======
        async changeNick() {
            await Swal.fire({
                title: '바꿀 닉네임을 입력해주세요.',
                input: 'text',
                inputValue: '테스트',
                showCancelButton: true,
                inputValidator: value => {
                    if (!value) {
                        return 'You need to write something!';
                    }
                },
            });
        },
>>>>>>> 6a1fc191ef2a978dcd8c12b5c70bf3f3ea85fae9
    },
    data() {
        
        return {
            hi:'',
            userinfo: {
                token: '',
                email: '',
                nickname: '',
                posts:[],
            },
            show: false,
        };
    },
};
</script>
