/*
User API 예시
 */
import axios from 'axios';

const uploadImage = (data1, callback, errorCallback) => {
    var temp = new FormData();
    temp.append('image', data1);
    axios
        .post('https://api.imgur.com/3/image', temp, {
            headers: {
                Authorization: 'Client-ID 4d07f11eccd122e',
            },
        })
        .then(res => {
            // console.log(res);
            callback(res);
        })
        .catch(error => {
            // console.log(error);
            errorCallback(false);
        });
};
const ImgurAPI = {
    uploadImage: (data1, callback, errorCallback) => uploadImage(data1, callback, errorCallback),
};

export default ImgurAPI;
