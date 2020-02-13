/*
User API 예시
 */
import http from '../http-common';

const requestLogin = (data, callback, errorCallback) => {
    http.post('/account/login', data)
        .then(res => {
            callback(res);
        })
        .catch(error => {
            errorCallback(error);
        });
};
const requestSignUp = (data, callback, errorCallback) => {
    http.post('/account/signup', data)
        .then(res => {
            callback(res);
        })
        .catch(error => {
            errorCallback(false);
        });
};
const requestAuthUserKey = (data, callback, errorCallback) => {
    http.put('/account/signup', data)
        .then(res => {
            callback(res);
        })
        .catch(error => {
            errorCallback(false);
        });
};
const requestSnsSignUp = (data, callback, errorCallback) => {
    http.post('/account/snssignup', data)
        .then(res => {
            callback(res);
        })
        .catch(error => {
            errorCallback(false);
        });
};
const findEmail = (data, callback, errorCallback) => {
    http.post('/account/email', data)
        .then(res => {
            callback(res);
        })
        .catch(error => {
            errorCallback(false);
        });
};

//GET /account/password/{email}
const findPassword = (data, callback, errorCallback) => {
    http.get('/account/password/' + data)
        .then(res => {
            callback(res);
        })
        .catch(error => {
            errorCallback(false);
        });
};

//GET /account/password/{email}
const passwordReset = (data, callback, errorCallback) => {
    http.put('/account/password', data)
        .then(res => {
            callback(res);
        })
        .catch(error => {
            errorCallback(false);
        });
};
const UserApi = {
    requestLogin: (data, callback, errorCallback) => requestLogin(data, callback, errorCallback),
    requestSignUp: (data, callback, errorCallback) => requestSignUp(data, callback, errorCallback),
    requestSnsSignUp: (data, callback, errorCallback) => requestSnsSignUp(data, callback, errorCallback),
    findEmail: (data, callback, errorCallback) => findEmail(data, callback, errorCallback),
    findPassword: (data, callback, errorCallback) => findPassword(data, callback, errorCallback),
    passwordReset: (data, callback, errorCallback) => passwordReset(data, callback, errorCallback),
    requestAuthUserKey: (data, callback, errorCallback) => requestAuthUserKey(data, callback, errorCallback),
};

export default UserApi;
