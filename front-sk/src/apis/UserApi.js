/*
User API 예시
 */
import http from '../http-common';

const email = 'ssafy@gmail.com';
const password = '1234qwer';

const requestLogin = (data, callback, errorCallback) => {
    //백앤드와 로그인 통신하는 부분
    if (data.email !== email || data.password !== password) {
        errorCallback(false);
        return;
    }
    http.post('/account/login', data)
        .then(res => {
            console.log(res);
            callback(res);
        })
        .catch(error => {
            console.log(error);
            // errorCallback(false);
            errorCallback(true);
        });
};
const requestSignUp = (data, callback, errorCallback) => {
    //백앤드와 로그인 통신하는 부분
    http.post('/account/signup', data)
        .then(res => {
            console.log(res);
            callback(res);
        })
        .catch(error => {
            console.log(error);
            errorCallback(false);
        });
};
const UserApi = {
    requestLogin: (data, callback, errorCallback) => requestLogin(data, callback, errorCallback),
    requestSignUp: (data, callback, errorCallback) => requestSignUp(data, callback, errorCallback),
};

export default UserApi;
