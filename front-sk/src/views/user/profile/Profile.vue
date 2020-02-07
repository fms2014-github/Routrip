<template>

    <div class="profile-page">
        <div class="wrapD">
            <h2>My Profile</h2>
            <button v-if="show" @click="logout">logout</button>
            <div class="profile-wrap">
                <UserPicture :userPicture="true" />
                <div class="user-info">
                    <HeaderComponent :headerTitle="userinfo.email" :mailIcon="true" />
                    <HeaderComponent :headerTitle="userinfo.nickname" rightText="수정"/>
               
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
            </div>

            <div class="profile-tab-page">
                <router-view name="profile"></router-view>
            </div>
        </div>
    </div>
</template>

<script>
import UserApi from '../../../apis/UserApi';
import HeaderComponent from '../../../components/common/Header';
import UserPicture from '../../../components/common/UserPicture';
import TabComponent from '../../../components/common/Tab';
import '../../../assets/css/profile.scss';
import '../../../assets/css/style.scss';
import Axios from 'axios';


export default {
    
    components: {
        HeaderComponent,
        UserPicture,
        TabComponent,
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
        
    },
    methods: {
        tokener(e) {
            console.log("gihihihifgigfdig",e)          
            Axios.get('http://192.168.100.70:8083/account/decode'+e)
                .then(res=>{
                    console.log(res.data)
                    this.userinfo.email=res.data.email
                    this.userinfo.nickname=res.data.nickname
                }).catch(error=>{
                    console.error(error);
                })
        },
        popupToggle() {
            this.popup = true;
        },
        getInfo() {
            this.userinfo.email = localStorage.getItem('loginedEmail');
            this.userinfo.nickname = localStorage.getItem('nickName');
        },
        updated() {
            if (localStorage.getItem('popup') !== null) {
                this.popup = !Boolean(localStorage.getItem('popup'));
                localStorage.removeItem('popup');
            }
        },
        logout() {
            localStorage.removeItem('loginedEmail');
            localStorage.removeItem('nickName');
            this.$router.push({ name: 'Login' });
        },
        checkLogin() {
            if (localStorage.getItem('loginedEmail') !== null) {
                this.show = true;
            } else {
                this.show = false;
            }
        },

        async changeNick(){
            const ipAPI = 'usernick'
            const inputValue = fetch(ipAPI)
                .then(response => response.json())
                .then(data => data.ip)
            const { value: ipAddress } = await Swal.fire({
                title: 'Enter your IP address',
                input: 'text',
                inputValue: inputValue,
                showCancelButton: true,
                inputValidator: (value) => {
                    if (!value) {
                        return 'You need to write something!'
                    }
                }
            })
            if (ipAddress) {
                Swal.fire(`Your IP address is ${ipAddress}`)
            }
        }
    },
    data() {
        return {
            popup: 'false',
            userinfo: {
                token:'',
                email: '',
                nickname: '',
            },
            show: false,
        };
    },
};
</script>
