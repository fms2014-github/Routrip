import axios from 'axios';

export default axios.create({
    baseURL: 'http://52.78.8.59:8083',
    headers: {
        'Content-type': 'application/json',
    },
});
