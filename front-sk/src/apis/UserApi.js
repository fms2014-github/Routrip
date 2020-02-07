/*
User API 예시
 */
import http from '../http-common';

const requestLogin = (data, callback, errorCallback) => {
    //백앤드와 로그인 통신하는 부분
    http.post('http://192.168.100.70:8083/account/login', data)
        .then(res => {
            callback(res);
        })
        .catch(error => {
            errorCallback(error);
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
const findEmail = (data, callback, errorCallback) => {
    //백앤드와 로그인 통신하는 부분
    http.post('/account/email', data)
        .then(res => {
            console.log(res);
            callback(res);
        })
        .catch(error => {
            console.log(error);
            errorCallback(false);
        });
};

//GET /account/password/{email}
const findPassword = (data, callback, errorCallback) => {
    //백앤드와 로그인 통신하는 부분
    http.get('/account/password/' + data)
        .then(res => {
            console.log(res);
            callback(res);
        })
        .catch(error => {
            console.log(error);
            errorCallback(false);
        });
};

//GET /account/password/{email}
const passwordReset = (data, callback, errorCallback) => {
    //백앤드와 로그인 통신하는 부분
    http.put('/account/password', data)
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
    findEmail: (data, callback, errorCallback) => findEmail(data, callback, errorCallback),
    findPassword: (data, callback, errorCallback) => findPassword(data, callback, errorCallback),
    passwordReset: (data, callback, errorCallback) => passwordReset(data, callback, errorCallback),
};

export default UserApi;
