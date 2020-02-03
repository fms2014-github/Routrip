import axios from 'axios';

export default axios.create({
    baseURL: 'http://192.168.100.62:9999',
    headers: {
        'Content-type': 'application/json',
    },
});
