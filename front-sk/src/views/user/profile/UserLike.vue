<template>
    <div>
        <div style="text-align:center; font-size:2.1vw; @include wrap; margin-bottom:7vw;">
            총 {{ likeList.length }}개의 글에 좋아요를 눌렀네요. 
        </div>
        <table>
            <thead>
                <th style="width:100%">
                    <td style="width:5vw;">번호</td><td style="width:73vw;">내용</td><td style="width:10vw; text-align:center;">날짜</td>
                </th>
            </thead>
            
            <tbody v-for="(like,index) in likeList" v-bind:key="like.boardid">
                <router-link :to="{ name: 'Detail', params: { boardid: like.boardid } }">   
                    <tr>
                        <td style="width:5vw;">{{ doMath(index) }}</td><td style="width:73vw;">{{ like.title }}</td>
                        <td style="padding-left:0px;">{{ like.writedate.split(':')[0] + ":" + like.writedate.split(':')[1] }}</td>
                    </tr>
                </router-link>  
                
            </tbody>
        </table>
    </div>
</template>
 
<script>
import likes from "../../../assets/css/likes.scss"

import Axios from 'axios';


export default {


    mounted(){
        this.reqlikes();
    },
    methods:{
        reqlikes() {
            // console.log("hihi")
            const jwt = localStorage.getItem('routrip_JWT');
            Axios.post('http://192.168.100.70:8083/page/favoriteBoard/' , {jwt : jwt})
                .then(res => {
                // console.log(res.data)
                this.likeList=res.data
            });
        },
        doMath(index){
            return index+1
        }
    },
    data(){
        return{
        
            likeList:[
            ]

        };
    },

};
</script>

<style scoped>



</style>