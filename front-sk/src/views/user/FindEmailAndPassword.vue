<template>
    <div id="find-email-password">
        <div class="toggle-title">
            <h1 id="find-title">아이디 및 비밀번호 찾기</h1>
            <button class="close" @click="close"><img class="close-img" src="../../assets/images/close.png" /></button>
            <h1 id="find-email-tab" @click="showEmailFunc">Email 찾기</h1>
            <h1 id="find-password-tab" @click="showPasswordFunc">비밀번호 찾기</h1>
        </div>
        <div id="find-email" :class="{ showEmail: !showEmail }">
            <div id="find-email-input" v-if="!findSuccess">
                <div class="input-with-label">
                    <input v-model="FindName"
                        v-bind:class="{ error: error.FindName, complete: !error.FindName && FindName.length !== 0 }"
                        @keyup.enter="FindName" id="FindName" placeholder="이름을 입력해 주세요." type="text" />
                    <label for="FindName">이름</label>
                    <div class="error-text" v-if="error.FindName">
                        {{ error.FindName }}
                    </div>
                </div>
                <div class="input-with-label">
                    <input v-model="birth"
                        v-bind:class="{ error: error.birth, complete: !error.birth && birth.length !== 0 }"
                        @keyup.enter="birth" id="birth" placeholder="930904" type="date" />
                    <label for="birth">생년월일</label>
                    <div class="error-text" v-if="error.birth">
                        {{ error.birth }}
                    </div>
                </div>
                <div class="input-with-label">
                    <input v-model="phone"
                        v-bind:class="{ error: error.phone, complete: !error.phone && phone.length !== 0 }"
                        @keyup.enter="phone" id="phone" placeholder="01012345678" maxlength="11" type="text" />
                    <label for="phone">전화번호</label>
                    <div class="error-text" v-if="error.phone">
                        {{ error.phone }}
                    </div>
                </div>
                <button class="btn btn--back btn-find-email" @click="findEmail" :disabled="!findEmailBtn"
                    :class="{ disabled: !findEmailBtn }">
                    이메일 찾기
                </button>
            </div>
            <div id="find-email-result" v-if="findSuccess">
                <h1>가입하신 이메일 주소는</h1>
                <h3 v-for="value in resultEmail" :key="value.id">{{ value }}</h3>
            </div>
        </div>
        <div id="find-password" :class="{ showPassword: !showPassword }">
            <div class="input-with-label">
                <input v-model="FindEmail"
                    v-bind:class="{ error: error.FindEmail, complete: !error.FindEmail && FindEmail.length !== 0 }"
                    @keyup.enter="checkEmail" id="FindEmail" placeholder="이메일을 입력해 주세요." type="text" />
                <label for="FindEmail">이메일</label>
                <div class="error-text" v-if="error.FindEmail">
                    {{ error.FindEmail }}
                </div>
            </div>
            <div class="input-with-label" :class="{ certNumBox: !certNumBox }">
                <input v-model="certNum"
                    v-bind:class="{ error: error.certNum, complete: !error.certNum && certNum.length !== 0 }"
                    @keyup.enter="checkCertNumFunc" id="certNum" placeholder="인증번호를 입력해 주세요." type="text" />
                <label for="certNum">인증번호</label>
                <div class="error-text" v-if="error.certNum">
                    {{ error.certNum }}
                </div>
            </div>
            <button class="btn btn--back btn-send-certNum" @click="sendCertNumFunc" :disabled="!sendCertNum"
                :class="{ disabled: !sendCertNum, sendCertNumBtn: !sendCertNumBtn }">
                인증번호 전송
            </button>
            <button class="btn btn--back btn-check-certNum" @click="compareCertNum" :disabled="!checkCertNum"
                :class="{ disabled: !checkCertNum, checkCertNumBtn: !checkCertNumBtn }">
                인증번호 확인
            </button>
        </div>
        <div id="button-wrap">
            <div @click="toLogin">로그인 하러 가기</div>
            <div @click="toJoin">가입하기</div>
        </div>
    </div>
</template>

<script>
    import '../../assets/css/user.scss';
    import '../../assets/css/style.scss';
    import UserApi from '../../apis/UserApi';
    import * as EmailValidator from 'email-validator';

    export default {
        watch: {
            certNum: function (v) {
                this.checkCertNumFunc();
            },
            FindEmail: function (v) {
                this.checkEmail();
            },
            FindName: function (v) {
                this.checkForm();
            },
            phone: function (v) {
                this.checkForm();
            },
            birth: function (v) {
                this.checkForm();
            },
        },
        methods: {
            checkEmail() {
                if (this.FindEmail.length >= 0 && !EmailValidator.validate(this.FindEmail)) {
                    this.error.FindEmail = '이메일 형식이 아닙니다.';
                    this.sendCertNum = false;
                } else {
                    this.error.FindEmail = false;
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
                if (this.FindName.length >= 0 && this.FindName.length <= 2) {
                    this.error.FindName = '두글자 이상 입력해 주세요.';
                } else this.error.FindName = false;
            },
            checkPhone() {
                if (this.phone.length < 10 || this.phone.length > 11) {
                    this.error.phone = '10~11자리 숫자로 입력해주세요.';
                } else this.error.phone = false;
            },
            checkBirth() {
                if (this.birth.length != 10) {
                    this.error.birth = '6자리 숫자로 입력해주세요.';
                } else this.error.birth = false;
            },
            checkForm() {
                this.checkName() 
                this.checkPhone() 
                this.checkBirth()
                let findEmailBtn = true;
                Object.values(this.error).map(v => {
                    if (v) findEmailBtn = false;
                });
                this.findEmailBtn = findEmailBtn;
            },
            showEmailFunc() {
                this.showEmail = true;
                this.showPassword = false;
                this.findSuccess = false;
                this.init();
            },
            showPasswordFunc() {
                this.showEmail = false;
                this.showPassword = true;
                this.init();
            },
            init() {
                this.FindEmail = '';
                this.FindName = '';
                this.phone = '';
                this.birth = '';
                this.certNum = '';
                this.error.FindEmail = false;
                this.error.FindName = false;
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
                if (this.sendCertNum) {
                    UserApi.findPassword(
                        this.FindEmail,
                        res => {
                            alert('위 이메일로 인증번호를 보냈습니다.');
                            localStorage.setItem('authEmail', this.FindEmail);
                            // console.log(res.data);
                            this.authCode = res.data;
                        },
                        error => {
                            // console.log(error);
                        },
                    );
                }
                this.certNumBox = true;
                this.sendCertNumBtn = false;
                this.checkCertNumBtn = true;
            },
            compareCertNum() {
                // console.log('this.certNum', this.certNum);
                // console.log('this.authCode', this.authCode);
                if (this.certNum === String(this.authCode)) {
                    alert('인증되었습니다.');
                    this.$emit('nextStep')
                } else {
                    alert('인증번호가 틀렸습니다.');
                }
            },
            findEmail() {
                if (this.findEmailBtn) {
                    var {
                        birth,
                        phone
                    } = this;
                    var data = {
                        name: this.FindName,
                        birth,
                        phone
                    };
                    UserApi.findEmail(
                        data,
                        res => {
                            // console.log(res.data);
                            this.resultEmail = res.data
                            this.findSuccess = true
                        },
                        error => {
                            // console.log(error);
                            alert('가입하신 메일 주소가 없습니다.')
                        },
                    );
                }
            },
            toLogin() {
                let flag = confirm('로그인 화면으로 돌아가시겠습니까?');
                if (flag) {
                    this.init()
                    this.$emit('popupToggle')
                }
            },
            toJoin() {
                let flag = confirm('회원가입 페이지로 이동하시겠습니까?');
                if (flag) {
                    this.init()
                    this.$emit('routeJoinPage')
                }
            },
            close() {
                this.$emit('popupToggle')
                this.init()
            },
        },
        data: () => {
            return {
                FindEmail: '',
                certNum: '',
                FindName: '',
                phone: '',
                birth: '',
                resultEmail: '',
                error: {
                    FindEmail: false,
                    certNum: false,
                    FindName: false,
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
                findSuccess: false,
                authCode: '',
            };
        },
    };
</script>