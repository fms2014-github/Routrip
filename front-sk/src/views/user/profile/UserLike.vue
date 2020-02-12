<template>
    <div>
        <h1>안녕 여긴 좋아요</h1>
        <allLike></allLike>
    </div>  
</template>

<script>
import allLike from "../../../components/common/allLike"
import Axios from 'axios';






export default {
    components:{
        allLike
    },

    mounted(){
        this.reqlikes();
    },
    methods:{
        reqlikes() {
        console.log("hihi")
        const jwt = localStorage.getItem('routrip_JWT');
        Axios.post('http://192.168.100.70:8083/page/favoriteBoard/' , {jwt : jwt})
            .then(res => {
                console.log(res)
            });
        }
    }

};
</script>
