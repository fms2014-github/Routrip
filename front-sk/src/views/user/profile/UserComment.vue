<template>
    <div>
        <h2 style="text-align:center; font-size:2vw; @include wrap;">총 {{ commentList.length }}개의 댓글이 있습니다. </h2>
        <li v-for="comment in commentList" v-bind:key="comment.commentid">
            <h4 style="font-size:1.2vw;">
            <router-link :to="{ name: 'Detail', params: { boardid: comment.boardid } }">
            {{ comment.contents }} {{comment.writedate}}
            </router-link>
            </h4>
        </li>

    </div>
</template>

<script>
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
        }
    },
    data(){
        return{
            commentList:[]
        };
    }

}




</script>
