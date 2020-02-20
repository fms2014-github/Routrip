import Axios from 'axios';
import Swal from 'sweetalert2';
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
    const jwt = localStorage.getItem('routrip_JWT');
    //vuex에 user정보 비우기
    Axios.post('http://192.168.100.70:8083/account/logout/', {
      jwt: jwt
    }).then(() => {
      commit('setUser', null);
      Swal.fire({
        icon: 'warning',
        title: '로그아웃',
        text: '로그아웃 되었습니다!',
      }).then(() => {
        router.push("/");
      })

      //로컬스토리지 비우기
      localStorage.clear();
    }).then(() => {
      router.push('/');
    }).catch(error => {
      console.log(error.response.status);
      if (error.response.status === 406) {
        Swal.fire({
          icon: 'error',
          title: '세션 만료',
          text: '로그인을 다시 해주세요!',
          confirmButtonText: 'Login',
        }).then(() => {
          router.push("/");
        })
        console.log("logout 406 ERROR");
      }
    });
  },
  async reqUserInfo({
    commit
  }) {
    const jwt = localStorage.getItem('routrip_JWT');
    await Axios.post('http://192.168.100.70:8083/account/decode/', {
      jwt: jwt
    }).then(res => {
      commit('setUser', res);
    }).catch(error => {
      if (error.response.status === 406) {
        console.log("reqUserInfo 406 ERROR");
      }
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
  mutations
};