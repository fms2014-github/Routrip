// 상태를 선언하는 부분입니다.
// 상태? 전역 변수라고 생각하시면 편합니다.
const state = {
    user: {},
};

// 해당 vuex에서 사용하는
// 메소드를 정의하는 부분입니다.
const actions = {};

// 위의 state 값을 가져오는 메소드를
// 만드는 부분입니다.
const getters = {
    getUser: state => state.user,
};

// 위의 state 값을 저장하는 메소드를
// 만드는 부분입니다.
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
};
