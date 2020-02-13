<template>
    <div>
        <li v-for="comment in commentList" v-bind:key="comment.commentid">
            {{ comment.contents }} {{comment.writedate}}
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
