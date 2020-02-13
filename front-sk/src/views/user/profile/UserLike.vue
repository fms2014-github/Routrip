<template>
    <div>
        <h1>안녕 여긴 좋아요</h1>
        
        <li v-for="like in likeList" v-bind:key="like.boardid">
            {{ like.title }} 글에 좋아요를 눌렀습니다. ( {{ like.writedate }} )
        </li>
    </div>  
</template>

<script>
// import allLike from "../../../components/common/allLike"
import Axios from 'axios';


export default {


    mounted(){
        this.reqlikes();
    },
    methods:{
        reqlikes() {
        console.log("hihi")
        const jwt = localStorage.getItem('routrip_JWT');
        Axios.post('http://192.168.100.70:8083/page/favoriteBoard/' , {jwt : jwt})
            .then(res => {
                console.log(res.data)
                this.likeList=res.data
                
            });
        }
    },
    data(){
        return{
            
            likeList:[]

        };
    },

};
</script>
