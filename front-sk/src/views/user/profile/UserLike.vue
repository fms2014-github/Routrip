<template>
    <div>
        <h2 style="text-align:center; font-size:2vw; @include wrap; margin-bottom:30px;">총 {{ likeList.length }}개의 글에 좋아요를 눌렀네요. </h2>
        <li v-for="like in likeList" v-bind:key="like.boardid">
            <h4 style="font-size:1.2vw;">
                <router-link :to="{ name: 'Detail', params: { boardid: like.boardid } }">
                {{ like.title }} 글에 좋아요를 눌렀습니다. ( {{ like.writedate }} )
                </router-link>
            </h4>
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
