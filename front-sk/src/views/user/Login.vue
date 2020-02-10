<template>
    <div class="user" id="login">
        <div class="wrapC left-view">
            <div class="login-img">
                <img class="main-logo" :src="getImageUrl()" />
                <h1>루 : 트립</h1>
                <h2 class="mention">
                    Let's share Route, ROUTRIP!
                </h2>
            </div>
        </div>

        <div class="wrapC right-view">
            <div class="input-with-label">
                <input
                    v-model="email"
                    v-bind:class="{
                        error: error.email,
                        complete: !error.email && email.length !== 0,
                    }"
                    @keyup.enter="login"
                    id="email"
                    placeholder="이메일을 입력하세요."
                    type="text"
                />
                <label for="email">이메일</label>
                <div class="error-text" v-if="error.email">
                    {{ error.email }}
                </div>
            </div>

            <div class="input-with-label">
                <input
                    v-model="password"
                    type="password"
                    v-bind:class="{
                        error: error.password,
                        complete: !error.password && password.length !== 0,
                    }"
                    id="password"
                    @keyup.enter="login"
                    placeholder="비밀번호를 입력하세요."
                />
                <label for="password">비밀번호</label>
                <div class="error-text" v-if="error.password">
                    {{ error.password }}
                </div>
            </div>

            <div class="checkOption">
                <div class="emailSaveCheck">
                    <input id="emailSaveCheck" name="emailSaveCheck" type="checkbox" v-model="emailSaveCheck" />
                    <label for="emailSaveCheck">이메일 저장</label>
                </div>
                <div class="autoLogin">
                    <input id="autoLogin" name="autoLogin" type="checkbox" v-model="autoLogin" />
                    <label for="autoLogin">자동 로그인</label>
                </div>
            </div>
            <button class="btn btn--back btn--login" @click="login" :disabled="!isSubmit" :class="{ disabled: !isSubmit }">
                로그인
            </button>
            <div class="error-text red" v-if="error.loginFail">
                {{ error.loginFail }}
            </div>

            <div class="sns-login">
                <div class="text">
                    <p>SNS 간편 로그인</p>
                    <div class="bar"></div>
                </div>
                <div class="logos">
                    <kakaoLogin @loginOrJoin="loginOrJoin" :component="component" v-on:checkLogin="loginOrJoin" />
                    <GoogleLogin :component="component" />
                    <button @click="reqUserInfo">요청하기</button>
                    <NaverLogin :component="component" />
                </div>
            </div>
            <div class="add-option">
                <div class="text">
                    <div class="bar"></div>
                </div>
                <div class="wrap">
                    <router-link
                        v-bind:to="{
                            name: 'FindEmailAndPassword',
                        }"
                        class="btn--text"
                        >이메일/비밀번호 찾기</router-link
                    >
                </div>
                <div class="wrap btn--text" @click="popupToggle">
                    가입하기
                </div>
            </div>
            <div id="popup-join" :class="{ hideJoin: !popup }">
                <join @popupToggle="popupToggle" />
            </div>
        </div>
    </div>
</template>

<script>
import '../../assets/css/style.scss';
import '../../assets/css/user.scss';
import PV from 'password-validator';
import * as EmailValidator from 'email-validator';
import Kakao from '../../components/user/snsLogin/kakao.js';
import KakaoLogin from '../../components/user/snsLogin/Kakao.vue';
import GoogleLogin from '../../components/user/snsLogin/Google.vue';
import UserApi from '../../apis/UserApi';
import NaverLogin from '../../components/user/snsLogin/Naver.vue';
import join from './Join';

// store
// 뷰엑스를 쓰는 방법 중 하나를 가져옴
import { createNamespacedHelpers } from 'vuex';
import Axios from 'axios';

// load user store
// const userMapState = createNamespacedHelpers('User').mapState;
// const userMapGetters = createNamespacedHelpers('User').mapGetters;
// const userMapMutations = createNamespacedHelpers('User').mapMutations;

// 전체를 가져온다
const userHelper = createNamespacedHelpers('User');

export default {
    components: {
        KakaoLogin,
        GoogleLogin,
        NaverLogin,
        join,
    },
    created() {
        this.component = this;
        this.passwordSchema
            .is()
            .min(8)
            .is()
            .max(100)
            .has()
            .digits()
            .has()
            .letters();
    },
    watch: {
        password: function(v) {
            this.checkForm();
        },
        email: function(v) {
            this.checkForm();
        },
        emailSaveCheck: function(v) {
            if (v) {
                localStorage.setItem('emailSaveCheck', this.emailSaveCheck);
            } else {
                localStorage.removeItem('saveEmail');
                localStorage.removeItem('emailSaveCheck');
            }
        },
    },
    computed: {
        ...userHelper.mapState(['user']),
        ...userHelper.mapGetters(['getUser']),
    },
    methods: {
        ...userHelper.mapActions(['reqUserInfo']),
        ...userHelper.mapMutations(['setUser']),
        getImageUrl() {
            return require('../../assets/images/routrip_logo.png');
        },
        checkForm() {
            if (this.email.length >= 0 && !EmailValidator.validate(this.email)) this.error.email = '이메일 형식이 아닙니다.';
            else {
                this.error.email = false;
                this.error.loginFail = false;
            }

            if (this.password.length >= 0 && !this.passwordSchema.validate(this.password))
                this.error.password = '영문,숫자 포함 8 자리이상이어야 합니다.';
            else {
                this.error.password = false;
                this.error.loginFail = false;
            }

            let isSubmit = true;
            Object.values(this.error).map(v => {
                if (v) isSubmit = false;
            });
            this.isSubmit = isSubmit;
        },
        login() {
            if (this.isSubmit) {
                let { email, password } = this;
                let data = {
                    email,
                    password,
                };
                this.isSubmit = false;
                UserApi.requestLogin(
                    data,
                    res => {
                        //통신을 통해 전달받은 값 콘솔에 출력
                        console.log(res.data);

                        // getters로 가져오는 법
                        console.log(this.getUser);

                        // mutations 쓰는 법
                        // 전역사용
                        // 1. this.$store.commit('User/setUser', res.data);
                        // 2. helpers 이용
                        this.setUser(res.data);

                        console.log(this.getUser);
                        localStorage.setItem('loginedEmail', this.email);
                        //요청이 끝나면 버튼 활성화
                        this.isSubmit = true;
                        if (this.emailSaveCheck) {
                            localStorage.setItem('saveEmail', this.email);
                        }
                        // this.$router.push({ name: 'Main' });
                    },
                    error => {
                        //요청이 끝나면 버튼 활성화
                        this.isSubmit = true;
                        localStorage.setItem('tempInput', this.email);

                        this.error.loginFail = '이메일 주소나 비밀번호가 틀렸습니다.';
                    },
                );
            }
        },
        popupToggle() {
            this.popup = !this.popup;
        },
        loginOrJoin() {
            Kakao.API.request({
                url: '/v1/user/me',
                success: res => {
                    this.setUser(res);
                    this.userSnsId = res.id;
                    Axios.post('http://192.168.100.70:8083/account/snslogin', {
                        loginApi: 1,
                        userid: res.id,
                    })
                        .then(res2 => {
                            localStorage.setItem('routrip_JWT', res2.data);
                            if (res2.data !== '') {
                                this.reqUserInfo();
                                this.$router.push('/main');
                                console.log(this.getUser);
                            }
                        })
                        .then(() => this.popupToggle());
                },
            });
        },
    },
    data: () => {
        return {
            email: '',
            password: '',
            passwordSchema: new PV(),
            emailSaveCheck: false,
            error: {
                email: false,
                passowrd: false,
                loginFail: false,
            },
            isSubmit: false,
            component: this,
            autoLogin: false,
            popup: false,
            userSnsId: '',
        };
    },
    mounted() {
        if (localStorage.getItem('tempInput') !== null) {
            this.email = localStorage.getItem('tempInput');
            localStorage.removeItem('tempInput');
        }
        if (localStorage.getItem('saveEmail') !== null) {
            this.email = localStorage.getItem('saveEmail');
        }
        if (localStorage.getItem('emailSaveCheck') !== null) {
            this.emailSaveCheck = localStorage.getItem('emailSaveCheck');
        }
        if (localStorage.getItem('popup') !== null) {
            this.popup = Boolean(localStorage.getItem('popup'));
            localStorage.removeItem('popup');
        }
    },
    updated() {
        if (localStorage.getItem('popup') !== null) {
            this.popup = !Boolean(localStorage.getItem('popup'));
            localStorage.removeItem('popup');
        }
    },
};
</script>
