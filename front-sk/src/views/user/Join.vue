<template>
    <div>
        <div id="sign-up">
            <div>
                <span v-if="snscheck === 0">
                    <h1 id="Join-title">가입하기</h1>
                </span>
                <span v-if="snscheck !== 0">
                    <h1 id="Join-title">SNS 가입하기</h1>
                </span>
                <button class="close" @click="close"><img class="close-img" src="../../assets/images/close.png" /></button>
            </div>
            <div id="term-form">
                <div v-if="snscheck === 0">
                    <div class="input-with-label">
                        <input
                            id="email"
                            v-model="email"
                            :class="{ error: error.email, complete: !error.email && email.length !== 0 }"
                            placeholder="이메일을 입력하세요."
                            type="text"
                            @keyup.enter="submit"
                        />
                        <label for="email">이메일</label>
                        <div v-if="error.email" class="error-text">
                            {{ error.email }}
                        </div>
                    </div>
                    <div class="input-with-label">
                        <input
                            id="password"
                            v-model="password"
                            type="password"
                            :class="{ error: error.password, complete: !error.password && password.length !== 0 }"
                            placeholder="비밀번호를 입력하세요."
                            @keyup.enter="submit"
                        />
                        <label for="password">비밀번호</label>
                        <div v-if="error.password" class="error-text">
                            {{ error.password }}
                        </div>
                    </div>
                    <div class="input-with-label">
                        <input
                            id="passwordConfirm"
                            v-model="passwordConfirm"
                            type="password"
                            :class="{ error: error.passwordConfirm, complete: !error.passwordConfirm && passwordConfirm.length !== 0 }"
                            placeholder="비밀번호를 한번 더 입력하세요."
                            @keyup.enter="submit"
                        />
                        <label for="passwordConfirm">비밀번호 확인</label>
                        <div v-if="error.passwordConfirm" class="error-text">
                            {{ error.passwordConfirm }}
                        </div>
                    </div>
                </div>
                <div class="input-with-label">
                    <input
                        id="name"
                        v-model="name"
                        :class="{ error: error.name, complete: name.length >= 2 }"
                        placeholder="이름을 입력하세요."
                        type="text"
                        @keyup.enter="submit"
                    />
                    <label for="name">이름</label>
                    <div v-if="error.name" class="error-text">
                        {{ error.name }}
                    </div>
                </div>
                <div class="input-with-label">
                    <input
                        id="nickname"
                        v-model="nickname"
                        :class="{ error: error.nickname, complete: nickname.length >= 2 }"
                        placeholder="닉네임을 입력하세요."
                        type="text"
                        @keyup.enter="submit"
                    />
                    <label for="nickname">닉네임</label>
                    <div v-if="error.nickname" class="error-text">
                        {{ error.nickname }}
                    </div>
                </div>
                <div class="input-with-label">
                    <input
                        id="birth"
                        v-model="birth"
                        :class="{ error: error.birth, complete: birth.length !== 0 }"
                        placeholder="닉네임을 입력하세요."
                        type="date"
                        @keyup.enter="submit"
                    />
                    <label for="birth">생년월일</label>
                    <div v-if="error.birth" class="error-text">
                        {{ error.birth }}
                    </div>
                </div>
                <div class="input-with-label">
                    <input
                        id="phone-number"
                        v-model="phone"
                        type="text"
                        :class="{ error: error.phone, complete: !error.phone && phone.length >= 10 }"
                        placeholder="휴대폰 번호를 입력하세요."
                        @keyup.enter="submit"
                        @keyup="phone_regx"
                        maxlength="11"
                    />
                    <label for="phone">휴대폰 번호</label>
                    <div v-if="error.phone" class="error-text">
                        {{ error.phone }}
                    </div>
                </div>
            </div>
            <div id="term-doc">
                <label>
                    <input id="term" v-model="isTerm" type="checkbox" />
                    <span>약관을 동의합니다.</span>
                </label>

                <div class="term-view">
                    <span @click="termPopup = true">약관보기</span>
                </div>
                <div v-if="error.term" class="error-text red">
                    {{ error.term }}
                </div>
            </div>
            <button class="btn btn--back btn--login" :disabled="!isSubmit" :class="{ disabled: !isSubmit }" @click="submit">
                가입하기
            </button>
        </div>
        <div v-if="termPopup" class="terms-doc-dialog">
            <div class="terms">
                <h1>개인정보 이용 동의서</h1>
                <div class="terms-content">
                    Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam rutrum justo diam, sed tincidunt ex sodales molestie. Nulla interdum
                    convallis odio, porttitor semper enim hendrerit sit amet. Phasellus et est et nunc varius aliquam. Fusce in urna sollicitudin,
                    pellentesque velit sed, iaculis enim. Duis non risus vel nunc elementum faucibus ac rhoncus arcu. Interdum et malesuada fames ac
                    ante ipsum primis in faucibus. Cras efficitur nulla lacus, non pretium mi dapibus convallis. Curabitur quis est sed justo pulvinar
                    tincidunt. Vestibulum molestie libero vitae velit consequat suscipit. Mauris elementum facilisis felis in tincidunt. Donec
                    vulputate tincidunt ex non elementum. Integer eget aliquet nisl. Nullam lectus turpis, dapibus a orci a, molestie accumsan nisl.
                    Donec volutpat lacus lacus, eget sodales enim luctus a. Pellentesque tincidunt ligula ut ligula rhoncus luctus. Suspendisse in
                    augue id velit pretium varius.
                </div>
                <button @click="termPopup = false">
                    닫기
                </button>
            </div>
        </div>
    </div>
</template>

<script>
import '../../assets/css/user.scss';
import '../../assets/css/style.scss';
import UserApi from '../../apis/UserApi';
import PV from 'password-validator';
import * as EmailValidator from 'email-validator';

export default {
    props: {
        snscheck: { type: Number, default: 0 },
        snsToggle: { type: Function },
    },

    data: () => {
        return {
            email: '',
            password: '',
            passwordConfirm: '',
            passwordSchema: new PV(),
            nickname: '',
            name: '',
            birth: '',
            phone: '',
            isTerm: false,
            isLoading: false,
            error: {
                phone: false,
                name: false,
                email: false,
                password: false,
                nickname: false,
                passwordConfirm: false,
                term: false,
            },
            isSubmit: false,
            passwordType: 'password',
            passwordConfirmType: 'password',
            termPopup: false,
        };
    },
    watch: {
        name: function(v) {
            this.checkForm();
        },
        nickname: function(v) {
            this.checkForm();
        },
        email: function(v) {
            this.checkForm();
        },
        password: function(v) {
            this.checkForm();
        },
        passwordConfirm: function(v) {
            this.checkForm();
        },
        phone: function(v) {
            this.checkForm();
        },
        birth: function(v) {
            this.checkForm();
        },
        isTerm: function(v) {
            this.checkForm();
        },
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
    mounted() {
        // console.log('SNSCHECK: ', this.snscheck);

        if (localStorage.getItem('nickname') !== null) {
            this.nickname = localStorage.getItem('nickname');
            localStorage.removeItem('nickname');
        }
        if (localStorage.getItem('email') !== null) {
            this.email = localStorage.getItem('email');
            localStorage.removeItem('email');
        }
    },
    destroyed() {
        // console.log('디스트로이');
    },
    methods: {
        phone_regx() {
            if (this.phone != this.phone.replace(/\D/gi, '')) {
                this.phone = this.phone.replace(/\D/gi, '');
                alert('숫자만 입력해 주세요.');
            }
        },
        checkForm() {
            if (this.snscheck === 0) {
                if (this.email.length >= 0 && !EmailValidator.validate(this.email)) {
                    this.error.email = '이메일 형식이 아닙니다.';
                } else {
                    this.error.email = false;
                }

                if (this.password.length >= 0 && !this.passwordSchema.validate(this.password)) {
                    this.error.password = '영문,숫자 포함 8 자리이상이어야 합니다.';
                } else {
                    this.error.password = false;
                }

                if (this.password != this.passwordConfirm) this.error.passwordConfirm = '비밀번호가 일치하지 않습니다.';
                else {
                    this.error.passwordConfirm = false;
                }
            }
            if (this.name.length < 2) this.error.name = '2자 이상 입력해 주세요.';
            else {
                this.error.name = false;
            }

            if (this.nickname.length < 2 || this.nickname.length > 10) this.error.nickname = '2자 이상 10자 이하로 입력해주세요.';
            else {
                this.error.nickname = false;
            }

            if (this.phone.length < 11) this.error.phone = '휴대폰 번호를 입력해 주세요.';
            else {
                this.error.phone = false;
            }

            if (this.birth === '') this.error.birth = '생년월일을 입력해 주세요.';
            else {
                this.error.birth = false;
            }

            if (!this.isTerm) this.error.term = '약관 동의에 체크해주세요';
            else {
                this.error.term = false;
            }
            let isSubmit = true;

            Object.values(this.error).map((v, i) => {
                if (v) {
                    isSubmit = false;
                }
            });
            this.isSubmit = isSubmit;
        },
        submit() {
            if (this.isSubmit) {
                let { email, password, nickname, name, birth, phone } = this;
                let data = {
                    email,
                    password,
                    nickname,
                    name,
                    birth,
                    phone,
                };
                //요청 후에는 버튼 비활성화
                this.isSubmit = false;

                UserApi.requestSignUp(
                    data,
                    res => {
                        //통신을 통해 전달받은 값 콘솔에 출력
                        // console.log(res);
                        //요청이 끝나면 버튼 활성화
                        this.isSubmit = true;
                        if (this.snscheck === 0) {
                            this.$emit('nextStep');
                        }
                    },
                    error => {
                        //요청이 끝나면 버튼 활성화
                        this.isSubmit = true;
                        localStorage.setItem('popup', 'false');
                        localStorage.setItem('nickname', this.nickname);
                        localStorage.setItem('email', this.email);
                        this.$router.push({ name: 'ErrorPage' });
                    },
                );
            }
        },
        close() {
            this.email = '';
            this.password = '';
            this.passwordConfirm = '';
            this.passwordSchema = new PV();
            this.nickname = '';
            this.name = '';
            this.birth = '';
            this.phone = '';
            this.isTerm = false;

            this.error.phone = false;
            this.error.name = false;
            this.error.email = false;
            this.error.password = false;
            this.error.nickname = false;
            this.error.passwordConfirm = false;
            this.error.term = false;

            console.log('클로즈 불렀냐?');
            console.log(this.snscheck);
            if (this.snscheck == 0) this.$emit('popupToggle');
            else {
                this.$emit('snsToggle');
            }
        },
    },
};
</script>
