import axios from 'axios';

export default axios.create({
    baseURL: 'http://192.168.100.70:8083',
    headers: {
        'Content-type': 'application/json',
    },
});
