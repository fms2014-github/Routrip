<template>
    <div class="profile-page">
        <Header></Header>
        <div class="wrapD">
            <h2>My Profile</h2>
            <div class="profile-wrap">
                <UserPicture :userPicture="true" :pic="userinfo.pic" />
                <div class="user-info">
                    <HeaderComponent :headerTitle="userinfo.email" :profileIcon="false" :mailIcon="true" />
                    <HeaderComponent :headerTitle="userinfo.nickname" :profileIcon="true" rightText="수정" @changeNick="changeNick" />
                <div class="none-border">
                    <button class="button-text" @click="delUser">회원탈퇴</button>
                </div>
                </div>
            </div>
            <div class="wrap">
                <router-link v-bind:to="{ name: 'UserPost' }"><TabComponent tabTitle="내 글" :isActive="true"/></router-link>
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
        Header,
        
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
            this.userinfo.nickname=this.getUser.data.nickname;
            this.userinfo.pic=this.getUser.data.profileImg;
            this.userinfo.email=this.getUser.data.email;
            if(!this.getUser.data.email){
                this.userinfo.email='SNS유저'
            }
        },

        async delUser(){
            await Swal.fire({
            title: '계정이 영구 삭제됩니다.',
            text: "비밀번호를 입력해 주세요.(일반 회원만)",
            input: 'password',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#d33',
            inputPlaceholder: 'SNS유저는 입력하지 않아도 됩니다.',
            inputValue: '',
            inputValidator:(value)=>{
            if (!value) {
              return '비밀번호를 입력해 주세요.'
            }
            else{
                const jwt = localStorage.getItem('routrip_JWT');
                Axios.delete('http://192.168.100.70:8083/account/user/',
                        {   data:{
                            jwt: jwt,
                            password: value}
                        }
                    ).then(res => {
                        console.log(res.headers);
                        console.log(jwt)
                        console.log('탈퇴',res)
                            Swal.fire({
                            icon:"success",
                            title:'모든 정보가 삭제되었습니다. \n 다음에 다시 만나요!'
                        }).then( (dismiss)=>{
                            if (dismiss){
                                
                                this.$router.push('/');
                            }
                        }
                    )       
                    }).catch(error=>{
                       Swal.fire({
                           title:'비밀번호가 틀렸거나, 값이 유효하지 않습니다.',
                           icon:'error'
                       })
                    });
            }}})
                        
        },

    getInfo() {
      this.userinfo.email = localStorage.getItem('loginedEmail');
      this.userinfo.nickname = localStorage.getItem('nickName');
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
        inputValidator: value => {
          if (!value) {
            return '뭐라도 써보세요!';
          } else {
            const jwt = localStorage.getItem('routrip_JWT');
            Axios.put('http://192.168.100.70:8083/account/user/', {
              nickname: value,
              jwt: jwt,
            }).then(res => {
              console.log(res.data);
              localStorage.setItem('routrip_JWT', res.data);
              this.reqInfo();
            });
          }
        },
      });
    },
  },
  data() {
        return {
            hi:'',
            userinfo: {
                email: '',
                nickname: '',
                pic:'',
            },
            show: false,
        };
    },
};
</script>
