<!--
    가입하기는 기본적인 폼만 제공됩니다
    기능명세에 따라 개발을 진행하세요.
    Sub PJT I에서는 UX, 디자인 등을 포함하여 백엔드를 제외하여 개발합니다.
 -->
<template>
    <div id="find-email-password" class="wrapC">
        <div class="toggle-title">
            <h1 @click="showEmailFunc">Email 찾기</h1>
            <h1 @click="showPasswordFunc">비밀번호 찾기</h1>
        </div>
        <div id="find-email" :class="{ showEmail: !showEmail }">
            <div class="input-with-label">
                <input
                    v-model="name"
                    v-bind:class="{ error: error.name, complete: !error.name && name.length !== 0 }"
                    @keyup.enter="name"
                    id="name"
                    placeholder="이름을 입력해 주세요."
                    type="text"
                />
                <label for="name">이름</label>
                <div class="error-text" v-if="error.name">
                    {{ error.name }}
                </div>
            </div>
            <div class="input-with-label">
                <input
                    v-model="birth"
                    v-bind:class="{ error: error.birth, complete: !error.birth && birth.length !== 0 }"
                    @keyup.enter="birth"
                    id="birth"
                    placeholder="930904"
                    type="text"
                />
                <label for="birth">생년월일</label>
                <div class="error-text" v-if="error.birth">
                    {{ error.birth }}
                </div>
            </div>
            <div class="input-with-label">
                <input
                    v-model="phone"
                    v-bind:class="{ error: error.phone, complete: !error.phone && phone.length !== 0 }"
                    @keyup.enter="phone"
                    id="phone"
                    placeholder="01012345678"
                    type="text"
                />
                <label for="phone">전화번호</label>
                <div class="error-text" v-if="error.phone">
                    {{ error.phone }}
                </div>
            </div>
            <button class="btn btn--back btn-find-email" @click="findEmail" :disabled="!findEmailBtn" :class="{ disabled: !findEmailBtn }">
                이메일 찾기
            </button>
        </div>
        <div id="find-password" :class="{ showPassword: !showPassword }">
            <div class="input-with-label">
                <input
                    v-model="email"
                    v-bind:class="{ error: error.email, complete: !error.email && email.length !== 0 }"
                    @keyup.enter="checkEmail"
                    id="email"
                    placeholder="이메일을 입력해 주세요."
                    type="text"
                />
                <label for="email">이메일</label>
                <div class="error-text" v-if="error.email">
                    {{ error.email }}
                </div>
            </div>
            <div class="input-with-label" :class="{ certNumBox: !certNumBox }">
                <input
                    v-model="certNum"
                    v-bind:class="{ error: error.certNum, complete: !error.certNum && certNum.length !== 0 }"
                    @keyup.enter="checkCertNumFunc"
                    id="certNum"
                    placeholder="인증번호를 입력해 주세요."
                    type="text"
                />
                <label for="certNum">인증번호</label>
                <div class="error-text" v-if="error.certNum">
                    {{ error.certNum }}
                </div>
            </div>
            <button
                class="btn btn--back btn-send-certNum"
                @click="sendCertNumFunc"
                :disabled="!sendCertNum"
                :class="{ disabled: !sendCertNum, sendCertNumBtn: !sendCertNumBtn }"
            >
                인증번호 전송
            </button>
            <button
                class="btn btn--back btn-check-certNum"
                @click="compareCertNum"
                :disabled="!checkCertNum"
                :class="{ disabled: !checkCertNum, checkCertNumBtn: !checkCertNumBtn }"
            >
                인증번호 확인
            </button>
        </div>
        <div @click="toLogin">로그인하기</div>
        <div @click="toJoin">가입하기</div>
    </div>
</template>

<script>
import '../../assets/css/user.scss';
import '../../assets/css/style.scss';
import UserApi from '../../apis/UserApi';
import * as EmailValidator from 'email-validator';

export default {
    watch: {
        certNum: function(v) {
            this.checkCertNumFunc();
        },
        email: function(v) {
            this.checkEmail();
        },
        name: function(v) {
            this.checkName();
        },
        phone: function(v) {
            this.checkPhone();
        },
        birth: function(v) {
            this.checkBirth();
        },
    },
    methods: {
        checkEmail() {
            if (this.email.length >= 0 && !EmailValidator.validate(this.email)) {
                this.error.email = '이메일 형식이 아닙니다.';
                this.sendCertNum = false;
            } else {
                this.error.email = false;
                this.sendCertNum = true;
            }
        },
        checkCertNumFunc() {
            if (this.certNum.length != 6) {
                this.error.certNum = '인증번호는 6자리 입니다.';
                this.checkCertNum = false;
            } else {
                this.error.certNum = false;
                this.checkCertNum = true;
            }
        },
        checkName() {
            /*
                if (this.name.length >= 0 && this.name.length <= 2){
                    this.error.name = "두글자 이상 입력해 주세요."+ this.name.length;
                }
                else this.error.name = false;
                */
        },
        checkPhone() {
            if (this.phone.length < 10 || this.phone.length > 11) {
                this.error.phone = '10~11자리 숫자로 입력해주세요.';
            } else this.error.phone = false;

            this.findEmailBtn = !this.error.phone && !this.error.birth;
        },
        checkBirth() {
            if (this.birth.length != 6) {
                this.error.birth = '6자리 숫자로 입력해주세요.';
            } else this.error.birth = false;

            this.findEmailBtn = !this.error.phone && !this.error.birth;
        },
        showEmailFunc() {
            this.showEmail = true;
            this.showPassword = false;
            this.init();
        },
        showPasswordFunc() {
            this.showEmail = false;
            this.showPassword = true;
            this.init();
        },
        init() {
            this.email = '';
            this.name = '';
            this.phone = '';
            this.birth = '';
            this.certNum = '';
            this.error.email = false;
            this.error.name = false;
            this.error.phone = false;
            this.error.birth = false;
            this.error.certNum = false;
            this.sendCertNum = false;
            this.certNumBox = false;
            this.sendCertNumBtn = true;
            this.checkCertNum = false;
            this.checkCertNumBtn = false;
            this.findEmailBtn = false;
        },
        sendCertNumFunc() {
            alert('위 이메일로 인증번호를 보냈습니다.');
            this.certNumBox = true;
            this.sendCertNumBtn = false;
            this.checkCertNumBtn = true;
        },
        compareCertNum() {
            alert('인증되었습니다!');
            this.$router.push('/user/ChangePassword');
        },
        findEmail() {
            alert('이메일은 qkr******@naver.com 입니다.');
        },
        toLogin() {
            let flag = confirm('로그인 화면으로 돌아가시겠습니까?');
            if (flag) this.$router.push('/');
        },
        toJoin() {
            let flag = confirm('회원가입 페이지로 이동하시겠습니까?');
            if (flag) this.$router.push('/user/join');
        },
    },
    data: () => {
        return {
            email: '',
            certNum: '',
            name: '',
            phone: '',
            birth: '',
            error: {
                email: false,
                certNum: false,
                name: false,
                phone: false,
                birth: false,
            },
            showEmail: true,
            showPassword: false,
            sendCertNum: false,
            certNumBox: false,
            sendCertNumBtn: true,
            checkCertNum: false,
            checkCertNumBtn: false,
            findEmailBtn: false,
        };
    },
};
</script>
