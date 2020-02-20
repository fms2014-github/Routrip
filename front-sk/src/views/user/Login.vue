<template>
    <div id="login" class="user">
        <div class="wrapC left-view">
            <Routrip></Routrip>
        </div>

        <div class="wrapC right-view">
            <div class="input-with-label">
                <input
                    id="LoginEmail"
                    v-model="LoginEmail"
                    :class="{
                        error: error.LoginEmail,
                        complete: !error.LoginEmail && LoginEmail.length !== 0,
                    }"
                    placeholder="이메일을 입력하세요."
                    type="text"
                    @keyup.enter="login"
                />
                <label for="LoginEmail">이메일</label>
                <div v-if="error.LoginEmail" class="error-text">
                    {{ error.LoginEmail }}
                </div>
            </div>

            <div class="input-with-label">
                <input
                    id="password"
                    v-model="password"
                    type="password"
                    :class="{
                        error: error.password,
                        complete: !error.password && password.length !== 0,
                    }"
                    placeholder="비밀번호를 입력하세요."
                    @keyup.enter="login"
                />
                <label for="password">비밀번호</label>
                <div v-if="error.password" class="error-text">
                    {{ error.password }}
                </div>
            </div>

            <div class="checkOption">
                <div class="emailSaveCheck">
                    <input id="emailSaveCheck" v-model="emailSaveCheck" name="emailSaveCheck" type="checkbox" />
                    <label for="emailSaveCheck">이메일 저장</label>
                </div>
                <div class="autoLogin">
                    <input id="autoLogin" v-model="autoLogin" name="autoLogin" type="checkbox" />
                    <label for="autoLogin">자동 로그인</label>
                </div>
            </div>
            <button class="btn btn--back btn--login" :disabled="!isSubmit" :class="{ disabled: !isSubmit }" @click="login">
                로그인
            </button>
            <div v-if="error.loginFail" class="error-text red">
                {{ error.loginFail }}
            </div>

            <div class="sns-login">
                <div class="text">
                    <p>SNS 간편 로그인</p>
                    <div class="bar"></div>
                </div>
                <div class="logos">
                    <kakaoLogin :component="component" @loginOrJoin="loginOrJoin" @checkLogin="loginOrJoin" />
                    <GoogleLogin :component="component" />
                    <NaverLogin :component="component" />
                </div>
            </div>
            <div class="add-option">
                <div class="text">
                    <div class="bar"></div>
                </div>
                <div class="wrap btn--text" @click="popupFindToggle">이메일/비밀번호 찾기</div>
                <div class="wrap btn--text" @click="popupToggle">
                    가입하기
                </div>
            </div>
            <!-- <div id="popup-join" :class="{ hideJoin: !popupJoin }">
                <join v-if="!this.joinNextStep" :snscheck="loginApi" @nextStep="joinNextStepToggle"
                    @popupToggle="popupJoinToggle" @snsToggle="snsToggle" />
                <joinAuth v-if="this.joinNextStep" @successAuth="popupJoinToggle" />
            </div> -->
            <div id="popup-join" :class="{ hideJoin: !popup }">
                <Join v-if="popup" :snscheck="loginApi" @popupToggle="popupToggle" @nextStep="nextStepToggle" />
                <!-- <JoinAuth v-if="!popup && nextStep" @nextStep="nextStepToggle" /> -->
            </div>

            <div id="popup-find" :class="{ hideJoin: !popupFind }">
                <FindEmailAndPassword
                    v-if="!this.findNextStep"
                    @nextStep="findNextStepToggle"
                    @popupToggle="popupFindToggle"
                    @routeJoinPage="routeJoinPage"
                />
                <ChangePassword v-if="this.findNextStep" @resetPassword="popupFindToggle" />
            </div>
        </div>
    </div>
</template>

<script>
import '../../assets/css/style.scss';
import '../../assets/css/user.scss';
import PV from 'password-validator';
import Routrip from '../main/RoutripLogo';
import * as EmailValidator from 'email-validator';
import Kakao from '../../components/user/snsLogin/kakao.js';
import KakaoLogin from '../../components/user/snsLogin/Kakao.vue';
import GoogleLogin from '../../components/user/snsLogin/Google.vue';
import UserApi from '../../apis/UserApi';
import NaverLogin from '../../components/user/snsLogin/Naver.vue';
import Join from './Join';
import joinAuth from './JoinAuth';
import FindEmailAndPassword from './FindEmailAndPassword';
import ChangePassword from './ChangePassword';

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
        Join,
        // joinAuth,
        FindEmailAndPassword,
        ChangePassword,
        Routrip,
    },
    watch: {
        password: function(v) {
            this.checkForm();
        },
        LoginEmail: function(v) {
            this.checkForm();
        },
        emailSaveCheck: function(v) {
            if (v) {
                localStorage.setItem('emailSaveCheck', this.emailSaveCheck);
            } else {
                localStorage.removeItem('emailSaveCheck');
            }
        },
    },
    created() {
        // // console.log("GET LOADING: ", this.$store.getters.getLoading);
        // // console.log("STATE LOADING", this.$store.state.flag);

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
    computed: {
        ...userHelper.mapState(['user']),
        ...userHelper.mapGetters(['getUser']),
    },
    methods: {
        routeJoinPage() {
            this.popupJoinToggle();
            this.popupFindToggle();
        },
        popupToggle() {
            this.loginApi = 0;
            this.popup = !this.popup;
            // console.log('POPUP: ', this.popup);
        },
        nextStepToggle() {
            this.popup = false;
            this.nextStep = !this.nextStep;
        },

        joinNextStepToggle() {
            this.joinNextStep = !this.joinNextStep;
        },
        findNextStepToggle() {
            this.findNextStep = !this.findNextStep;
        },
        ...userHelper.mapActions(['reqUserInfo']),
        ...userHelper.mapMutations(['setUser']),

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
            this.$store.commit('setLoading');
            // console.log('GET LOADING: ', this.$store.getters.getLoading);
            if (this.isSubmit) {
                let { password } = this;
                let data = {
                    email: this.LoginEmail,
                    password,
                };
                this.isSubmit = false;
                UserApi.requestLogin(
                    data,
                    res => {
                        //통신을 통해 전달받은 값 콘솔에 출력
                        // // console.log(res.data);

                        // getters로 가져오는 법
                        // // console.log(this.getUser);

                        // mutations 쓰는 법
                        // 전역사용
                        // 1. this.$store.commit('User/setUser', res.data);
                        // 2. helpers 이용
                        this.setUser(res.data);
                        // // console.log('뷰엑스!!!!!');
                        localStorage.setItem('routrip_JWT', res.data);
                        this.reqUserInfo();
                        // // console.log(this.getUser);
                        if (this.emailSaveCheck) {
                            localStorage.setItem('LoginEmail', this.LoginEmail);
                        } else {
                            localStorage.removeItem('LoginEmail');
                        }
                        // // console.log(this.getUser);
                        //요청이 끝나면 버튼 활성화
                        this.isSubmit = true;

                        this.$store.commit('setLoading');
                        this.$router.push({
                            name: 'Main',
                        });
                    },
                    error => {
                        //요청이 끝나면 버튼 활성화
                        this.isSubmit = true;
                        localStorage.setItem('tempInput', this.LoginEmail);

                        this.error.loginFail = '이메일 주소나 비밀번호가 틀렸습니다.';
                    },
                );
                // console.log('GET LOADING: ', this.$store.getters.getLoading);
            }
        },
        popupJoinToggle() {
            if (this.popupJoin) this.init();
            this.popupJoin = !this.popupJoin;
        },
        popupFindToggle() {
            if (this.popupFind) this.init();
            this.popupFind = !this.popupFind;
        },
        snsToggle() {
            this.loginApi = 0;
            this.popupJoinToggle();
        },
        loginOrJoin(loginApi) {
            this.loginApi = loginApi;

            // console.log('???', this.popup);

            Kakao.API.request({
                url: '/v2/user/me',
                success: res => {
                    this.setUser(res);
                    this.userSnsId = res.id;
                    sessionStorage.setItem('snsId', res.id);
                    // console.log(res);
                    Axios.post('http://localhost:8083/account/snslogin', {
                        loginApi: loginApi,
                        userid: res.id,
                    })
                        .then(res2 => {
                            localStorage.setItem('routrip_JWT', res2.data);
                            if (res2.data !== '') {
                                this.reqUserInfo();
                                this.$router.push('/main');
                            }
                        })
                        .then(() => {
                            this.popup = !this.popup;
                        });
                },
                fail: error => {
                    // console.log(error);
                },
            });
        },

        init() {
            this.joinNextStep = false;
            this.findNextStep = false;
        },
    },
    data: () => {
        return {
            nextStep: false,
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
            popup: false,
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
