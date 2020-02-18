<template>
   <div>
        <div style="text-align:center; font-size:2.1vw; @include wrap; margin-bottom:7vw;">
            총 {{ commentList.length }}개의 글에 댓글을 남겼습니다. 
        </div>
        <table>
            <thead>
                <th>
                    <td style="width:5vw;">번호</td><td style="width:72vw;">내용</td><td>날짜</td>
                </th>
            </thead>
            <tbody v-for="(comment,index) in commentList" v-bind:key="comment.boardid">
                <!-- <div class="route"> -->
                <router-link :to="{ name: 'Detail', params: { boardid: comment.boardid } }">        
                    <tr>
                        <td style="width:5vw;">{{ doMath(index) }}</td><td style="width:68 vw;">{{ comment.contents }}</td>
                        <td>{{ comment.writedate.split(':')[0] + ":" + comment.writedate.split(':')[1] }}</td>
                    </tr>
                </router-link>
                <!-- <div> -->
            </tbody>
        </table>
    </div>
</template>

<script>
import likes from "../../../assets/css/likes.scss"

import Axios from 'axios'




export default {
     mounted(){
        this.reqComments();
    },
    methods: {
        reqComments() {
        console.log("hihi comment")
        const jwt = localStorage.getItem('routrip_JWT');
        Axios.post('http://192.168.100.70:8083/page/searchComment' , {jwt : jwt})
            .then(res => {
                console.log(res)
                this.commentList=res.data
            });
        },
        doMath(index){
            return index+1
        }
    },
  
    data(){
        return{
            commentList:[]
        };
    }

}




</script>
