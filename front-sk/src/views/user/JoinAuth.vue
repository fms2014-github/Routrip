<template>
    <div id="JoinAuth" class="wrapC">
        <div id="JoinAuth-form">
            <div>
                <h1>인증번호 확인</h1>
                <button class="close" @click="close"><img class="close-img" src="../../assets/images/close.png" /></button>
                <h3>작성한 이메일 주소로 인증번호가 발송되었습니다.</h3>
            </div>
            <div class="input-with-label">
                <input
                    v-model="userkey"
                    v-bind:class="{
                        error: error.userkey,
                        complete: !error.userkey && userkey.length !== 0,
                        disabled: !userkey,
                    }"
                    @keyup.enter="submit"
                    id="auth"
                    placeholder="인증번호를 입력하세요."
                    type="text"
                    maxlength="6"
                />
                <label for="auth">인증번호</label>
                <div class="error-text" v-if="error.userkey">
                    {{ error.userkey }}
                </div>
            </div>
        </div>
        <div>
            <button class="btn btn--back btn--login" @click="submit" :disabled="!isSubmit" :class="{ disabled: !isSubmit }">
                <span>인증번호 확인</span>
            </button>
            <div class="back" @click="back">뒤로 가기</div>
        </div>
    </div>
</template>

<script>
import '../../assets/css/user.scss';
import '../../assets/css/style.scss';
import UserApi from '../../apis/UserApi';
import * as EmailValidator from 'email-validator';

export default {
    data: () => {
        return {
            email: '',
            userkey: '',
            isEmail: false,
            isLoading: false,
            error: {
                userkey: false,
            },
            isSubmit: false,
        };
    },
    created() {
        this.component = this;
        this.email = sessionStorage.getItem('tempEmail')
    },
    watch: {
        userkey: function(v) {
            this.checkForm();
        }
    },
    methods: {
        checkForm() {
            if (this.userkey.length != 6) this.error.userkey = '인증번호를 입력하세요.';
            else {
                this.error.userkey = false;
                this.isSubmit = true;
            }
        },
        submit() {
            if (this.isSubmit && !this.isEmail) {
                let { email, userkey } = this;
                let data = {
                    email,
                    userkey
                };

                console.log(data);

                UserApi.requestAuthUserKey(data,res =>{
                    console.log(res)
                    alert("가입이 완료되었습니다.")
                    this.$emit('successAuth');
                }, error=>{
                    console.log(error)
                    alert("인증번호가 틀렸습니다.")
                })
            }
        },
        back() {
            this.$router.back();
        },
        close() {
            // this.$router.push('/');
        },
    },
};
</script>
