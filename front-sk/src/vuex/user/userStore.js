import Axios from 'axios';
import {
    router
} from "../../routes";

// 상태를 선언하는 부분입니다.
// 상태? 전역 변수라고 생각하시면 편합니다.
// state
const state = {
    user: {},
};

// 해당 vuex에서 사용하는
// 메소드를 정의하는 부분입니다.
// dispatch
const actions = {
    logout({
        commit
    }) {
        // console.log("LOGOUT!!!");
        const jwt = localStorage.getItem('routrip_JWT');
        //vuex에 user정보 비우기
        Axios.post('http://192.168.100.70:8083/account/logout/', {
            jwt: jwt
        }).then(() => {
            commit('setUser', null);
            //로컬스토리지 비우기
            localStorage.clear();
        }).then(() => {
            router.push('/');
        });
    },
    async reqUserInfo({
        commit
    }) {
        // console.log('스토어 액션 호출!');
        const jwt = localStorage.getItem('routrip_JWT');
        // console.log(jwt);
        await Axios.post('http://192.168.100.70:8083/account/decode/', {
            jwt: jwt
        }).then(res => {
            commit('setUser', res);
            // console.log(res.data)
            // console.log(res)
            // console.log(res.data.nickname);
            // console.log(res.data.profileImg);
            // console.log(profile)
        });
    },

};

// 위의 state 값을 가져오는 메소드를
// 만드는 부분입니다.
// getters
const getters = {
    getUser: state => state.user,
};

// 위의 state 값을 저장하는 메소드를
// 만드는 부분입니다.
// commit
const mutations = {
    setUser: (state, user) => (state.user = user),
};

// 위의 값들을 내보내기 하는 부분입니다.
export default {
    namespaced: true,
    state,
    actions,
    getters,
    mutations,
    // data: () => {
    //     return {
    //         jwt,
    //     };
    // },
};