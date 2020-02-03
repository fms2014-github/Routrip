<template>
    <div class="user" id="login">
        <div class="wrapC left-view">
            <div class="login-img">
                <img class="main-logo" :src="getImageUrl()" />
                <h1>루 : 트립</h1>
                <h2 class="mention">Let's share Route, ROUTRIP!</h2>
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
                <div class="error-text" v-if="error.email">{{ error.email }}</div>
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
                <div class="error-text" v-if="error.password">{{ error.password }}</div>
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
            <button class="btn btn--back btn--login" @click="login" :disabled="!isSubmit" :class="{ disabled: !isSubmit }">로그인</button>
            <div class="error-text red" v-if="error.loginFail">{{ error.loginFail }}</div>

            <div class="sns-login">
                <div class="text">
                    <p>SNS 간편 로그인</p>
                    <div class="bar"></div>
                </div>
                <div class="logos">
                    <kakaoLogin :component="component" />
                    <GoogleLogin :component="component" />
                    <NaverLogin :component="component" />
                </div>
            </div>
            <div class="add-option">
                <div class="text">
                    <div class="bar"></div>
                </div>
                <div class="wrap" @click="popupToggleFind">
                    <router-link v-bind:to="{ name: 'FindEmailAndPassword' }" class="btn--text">이메일/비밀번호 찾기</router-link>
                </div>
                <div class="wrap" @click="popupToggleJoin">
                    <router-link v-bind:to="{ name: 'Join' }" class="btn--text">가입하기</router-link>
                </div>
            </div>
            <div id="popup-join" :class="{ hideJoin: !popupJoin }">
                <router-view name="join" />
            </div>
            <div id="popup-find" :class="{ hideJoin: !popupFind }">
                <router-view name="find" />
            </div>
        </div>
    </div>
</template>

<script>
import '../../assets/css/style.scss';
import '../../assets/css/user.scss';
import PV from 'password-validator';
import * as EmailValidator from 'email-validator';
import KakaoLogin from '../../components/user/snsLogin/Kakao.vue';
import GoogleLogin from '../../components/user/snsLogin/Google.vue';
import UserApi from '../../apis/UserApi';
import NaverLogin from '../../components/user/snsLogin/Naver.vue';

export default {
    components: {
        KakaoLogin,
        GoogleLogin,
        NaverLogin,
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
            .letters()
            .not()
            .symbols();
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
    methods: {
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
                //요청 후에는 버튼 비활성화
                this.isSubmit = false;
                UserApi.requestLogin(
                    data,
                    res => {
                        //통신을 통해 전달받은 값 콘솔에 출력
                        console.log(res);
                        //요청이 끝나면 버튼 활성화
                        this.isSubmit = true;
                        if (this.emailSaveCheck) {
                            localStorage.setItem('saveEmail', this.email);
                        }
                        this.error.loginFail = '이메일 주소나 비밀번호가 틀렸습니다.';
                    },
                    error => {
                        if (error === true) {
                            this.$router.push({ name: 'Profile' });
                            localStorage.setItem('loginedEmail', this.email);
                            localStorage.setItem('nickName', '김코치');
                        } else {
                            //요청이 끝나면 버튼 활성화
                            this.isSubmit = true;
                            localStorage.setItem('tempInput', this.email);
                            this.$router.push({ name: 'ErrorPage' });
                        }
                    },
                );
            }
        },
        popupToggleJoin() {
            this.popupJoin = true;
            this.popupFind = false;
        },
        popupToggleFind() {
            this.popupFind = true;
            this.popupJoin = false;
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
            popupJoin: false,
            popupFind: false,
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
        if (localStorage.getItem('popupJoin') !== null) {
            this.popup = Boolean(localStorage.getItem('popupJoin'));
            localStorage.removeItem('popupJoin');
        }
        if (localStorage.getItem('popupFind') !== null) {
            this.popup = Boolean(localStorage.getItem('popupFind'));
            localStorage.removeItem('popupFind');
        }
    },
    updated() {
        if (localStorage.getItem('popupJoin') !== null) {
            this.popupJoin = !Boolean(localStorage.getItem('popupJoin'));
            localStorage.removeItem('popupJoin');
        }
        if (localStorage.getItem('popupFind') !== null) {
            this.popupFind = !Boolean(localStorage.getItem('popupFind'));
            localStorage.removeItem('popupFind');
        }
    },
};
</script>
