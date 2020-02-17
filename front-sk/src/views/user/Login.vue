<template>
    <div class="user" id="login">
        <div class="wrapC left-view">
            <div class="login-img">
                <img class="main-logo" :src="getImageUrl()" />
                <h1>루 : 트립</h1>
                <h2 class="mention">
                    Let's share Route, ROUTRIP!
                </h2>
                <button @click="gogo">알람 페이지 가보쟈</button>
            </div>
        </div>

        <div class="wrapC right-view">
            <div class="input-with-label">
                <input v-model="LoginEmail" v-bind:class="{
                        error: error.LoginEmail,
                        complete: !error.LoginEmail && LoginEmail.length !== 0,
                    }" @keyup.enter="login" id="LoginEmail" placeholder="이메일을 입력하세요." type="text" />
                <label for="LoginEmail">이메일</label>
                <div class="error-text" v-if="error.LoginEmail">
                    {{ error.LoginEmail }}
                </div>
            </div>

            <div class="input-with-label">
                <input v-model="password" type="password" v-bind:class="{
                        error: error.password,
                        complete: !error.password && password.length !== 0,
                    }" id="password" @keyup.enter="login" placeholder="비밀번호를 입력하세요." />
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
            <button class="btn btn--back btn--login" @click="login" :disabled="!isSubmit"
                :class="{ disabled: !isSubmit }">
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
                    <NaverLogin :component="component" />
                </div>
            </div>
            <div class="add-option">
                <div class="text">
                    <div class="bar"></div>
                </div>
                <div class="wrap btn--text" @click="popupFindToggle">
                    이메일/비밀번호 찾기
                </div>
                <div class="wrap btn--text" @click="popupJoinToggle">
                    가입하기
                </div>
            </div>
            <div id="popup-join" :class="{ hideJoin: !popupJoin }">
                <join v-if="!this.joinNextStep" @nextStep="joinNextStepToggle" @popupToggle="popupJoinToggle"
                    :snscheck="loginApi" @snsToggle="snsToggle" />
                <joinAuth v-if="this.joinNextStep" @successAuth="popupJoinToggle" />
            </div>
            <div id="popup-find" :class="{ hideJoin: !popupFind }">
                <FindEmailAndPassword v-if="!this.findNextStep" @nextStep="findNextStepToggle"
                    @popupToggle="popupFindToggle" @routeJoinPage="routeJoinPage" />
                <ChangePassword v-if="this.findNextStep" @resetPassword="popupFindToggle"/>
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
    import joinAuth from './JoinAuth';
    import FindEmailAndPassword from './FindEmailAndPassword';
    import ChangePassword from './ChangePassword'

    // store
    // 뷰엑스를 쓰는 방법 중 하나를 가져옴
    import {
        createNamespacedHelpers
    } from 'vuex';
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
            joinAuth,
            FindEmailAndPassword,
            ChangePassword,
        },
        created() {

            // console.log("GET LOADING: ", this.$store.getters.getLoading);
            // console.log("STATE LOADING", this.$store.state.flag);

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
            password: function (v) {
                this.checkForm();
            },
            LoginEmail: function (v) {
                this.checkForm();
            },
            emailSaveCheck: function (v) {
                if (v) {
                    localStorage.setItem('emailSaveCheck', this.emailSaveCheck);
                } else {
                    localStorage.removeItem('emailSaveCheck');
                }
            },
        },
        computed: {
            ...userHelper.mapState(['user']),
            ...userHelper.mapGetters(['getUser']),
        },
        methods: {
            routeJoinPage() {
                this.popupJoinToggle()
                this.popupFindToggle()
            },
            gogo() {
                this.$router.push('/alarm')
            },
            joinNextStepToggle() {
                this.joinNextStep = !this.joinNextStep;
            },
            findNextStepToggle() {
                this.findNextStep = !this.findNextStep;
            },
            ...userHelper.mapActions(['reqUserInfo']),
            ...userHelper.mapMutations(['setUser']),
            getImageUrl() {
                return require('../../assets/images/routrip_logo.png');
            },
            checkForm() {
                if (this.LoginEmail.length >= 0 && !EmailValidator.validate(this.LoginEmail)) this.error.LoginEmail = '이메일 형식이 아닙니다.';
                else {
                    this.error.LoginEmail = false;
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
                this.$store.commit('setLoading')
                console.log("GET LOADING: ", this.$store.getters.getLoading);
                if (this.isSubmit) {
                    let {
                        password
                    } = this;
                    let data = {
                        email: this.LoginEmail,
                        password,
                    };
                    this.isSubmit = false;
                    UserApi.requestLogin(
                        data,
                        res => {
                            //통신을 통해 전달받은 값 콘솔에 출력
                            // console.log(res.data);

                            // getters로 가져오는 법
                            // console.log(this.getUser);

                            // mutations 쓰는 법
                            // 전역사용
                            // 1. this.$store.commit('User/setUser', res.data);
                            // 2. helpers 이용
                            this.setUser(res.data);
                            // console.log('뷰엑스!!!!!');
                            localStorage.setItem('routrip_JWT', res.data);
                            this.reqUserInfo();
                            // console.log(this.getUser);
                            if(this.emailSaveCheck){
                                localStorage.setItem('LoginEmail', this.LoginEmail);
                            }else{
                                localStorage.removeItem('LoginEmail');
                            }
                            // console.log(this.getUser);
                            //요청이 끝나면 버튼 활성화
                            this.isSubmit = true;
                            
                            this.$store.commit('setLoading')
                            this.$router.push({
                                name: 'Main'
                            });
                            
                        },
                        error => {
                            //요청이 끝나면 버튼 활성화
                            this.isSubmit = true;
                            localStorage.setItem('tempInput', this.LoginEmail);

                            this.error.loginFail = '이메일 주소나 비밀번호가 틀렸습니다.';
                        },
                    );
                    console.log("GET LOADING: ", this.$store.getters.getLoading);
                }
            },
            popupJoinToggle() {
                if(this.popupJoin) this.init()
                this.popupJoin = !this.popupJoin;
            },
            popupFindToggle() {
                if(this.popupFind) this.init()
                this.popupFind = !this.popupFind;
            },
            snsToggle() {
                this.loginApi = 0;
                this.popupJoinToggle()
            },
            loginOrJoin(loginApi) {
                this.loginApi = loginApi;
                console.log('loginOrJoin(loginApi)')
                Kakao.API.request({
                    url: '/v1/user/me',
                    success: res => {
                        this.setUser(res);
                        console.log('this.setUser(res)', res)
                        this.userSnsId = res.id;
                        console.log('this.userSnsId = res.id', res.id)
                        sessionStorage.setItem('snsId', res.id);
                        Axios.post('http://192.168.100.70:8083/account/snslogin', {
                                loginApi: loginApi,
                                userid: res.id,
                            })
                            .then(res2 => {
                                console.log('login')
                                localStorage.setItem('routrip_JWT', res2.data);
                                if (res2.data !== '') {
                                    console.log('login')
                                    this.reqUserInfo();
                                    this.$router.push('/main');
                                    // console.log(this.getUser);
                                }
                            })
                            .then(() => {
                                console.log('Join')
                                this.popupJoinToggle()
                            });
                    },
                    fail: error => {
                        console.log('error', error)
                    }
                });
            },
            init() {
                this.joinNextStep = false
                this.findNextStep = false
            },
        },
        data: () => {
            return {
                joinNextStep: false,
                findNextStep: false,
                LoginEmail: '',
                password: '',
                passwordSchema: new PV(),
                emailSaveCheck: false,
                error: {
                    LoginEmail: false,
                    passowrd: false,
                    loginFail: false,
                },
                isSubmit: false,
                component: this,
                autoLogin: false,
                popupJoin: false,
                popupFind: false,
                userSnsId: '',
                loginApi: 0,
            };
        },
        mounted() {
            if (localStorage.getItem('tempInput') !== null) {
                this.LoginEmail = localStorage.getItem('tempInput');
                localStorage.removeItem('tempInput');
            }
            if (localStorage.getItem('LoginEmail') !== null) {
                this.LoginEmail = localStorage.getItem('LoginEmail');
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
                this.popup = Boolean(localStorage.getItem('popup'));
                this.popup = !this.popup;
                localStorage.removeItem('popup');
            }
        },
    };
</script>