<!--
    가입하기는 기본적인 폼만 제공됩니다
    기능명세에 따라 개발을 진행하세요.
    Sub PJT I에서는 UX, 디자인 등을 포함하여 백엔드를 제외하여 개발합니다.
 -->
<template>
    <div id="change-password" class="wrapC">
        <h1>비밀번호 재설정</h1>
        <h3>새로운 비밀번호를 입력해 주세요.</h3>

        <div class="input-password">
            <div class="input-with-label">
                <input
                    v-model="password"
                    v-bind:class="{ error: error.password, complete: !error.password && this.password.length !== 0 }"
                    @keyup.enter="password"
                    id="password1"
                    placeholder="비밀번호를 입력하세요."
                    type="password"
                />
                <label for="password1">비밀번호</label>
                <div class="error-text" v-if="error.password">
                    {{ error.password }}
                </div>
            </div>
            <div class="input-with-label">
                <input
                    v-model="password2"
                    v-bind:class="{ error: error.password2, complete: !error.password2 && this.password2.length !== 0 }"
                    @keyup.enter="password2"
                    id="password2"
                    placeholder="비밀번호를 입력하세요."
                    type="password"
                />
                <label for="password2">비밀번호 확인</label>
                <div class="error-text" v-if="error.password2">
                    {{ error.password2 }}
                </div>
            </div>
            <div class="password-error-msg" :class="{ samePassword: !samePassword }">
                <span style="color:red;">비밀번호가 동일하지 않습니다.</span>
            </div>
        </div>

        <button @click="register" class="btn btn--back" :disabled="!registerBtn" :class="{ disabled: !registerBtn }">
            비밀번호변경
        </button>
        <div class="back" @click="toLogin">로그인하기</div>
    </div>
</template>

<script>
import '../../assets/css/style.scss';
import '../../assets/css/user.scss';
import PV from 'password-validator';
import UserApi from '../../apis/UserApi';

export default {
    created() {
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
            this.checkPassword1();
        },
        password2: function(v) {
            this.checkPassword2();
        },
    },
    methods: {
        checkPassword1() {
            if (this.password.length >= 0 && !this.passwordSchema.validate(this.password))
                this.error.password1 = '영문,숫자 포함 8 자리이상이어야 합니다.';
            else this.error.password1 = false;
        },
        checkPassword2() {
            if (this.password2.length >= 0 && !this.passwordSchema.validate(this.password2)) {
                this.error.password2 = '영문,숫자 포함 8 자리이상이어야 합니다.';
                this.registerBtn = false;
            } else {
                this.error.password2 = false;

                if (this.password !== this.password2) {
                    this.samePassword = true;
                    this.registerBtn = false;
                } else {
                    this.samePassword = false;
                    this.registerBtn = true;
                }
            }
        },
        register() {
            if (this.registerBtn) {
                var { password } = this;
                var email = localStorage.getItem('authEmail');
                var data = { email, password };
                UserApi.passwordReset(
                    data,
                    res => {
                        alert('비밀번호가 번경되었습니다.');
                        this.$emit('resetPassword')
                    },
                    error => {
                        // console.log(error);
                        this.$router.push('/error');
                    },
                );
            }
        },
        toLogin() {
            let flag = confirm('로그인 화면으로 돌아가시겠습니까?');
            if (flag) this.$router.push('/');
        },
    },
    data: () => {
        return {
            passwordSchema: new PV(),
            password: '',
            password2: '',
            error: {
                password: false,
                password2: false,
            },
            isSubmit: false,
            samePassword: false,
            registerBtn: false,
        };
    },
};
</script>
