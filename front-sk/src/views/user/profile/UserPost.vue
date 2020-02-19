<template>
    <div>
        <div class="row">
            <div style="text-align:center; font-size:2.1vw; @include wrap; margin-bottom:7vw;">
                총 {{postList.length}} 개의 글이 있습니다.</div>
            <div v-for="post in postList" v-bind:key="post.boardid">
                <div class="gallery">
                    <router-link :to="{ name: 'Detail', params: { boardid: post.boardid } }">
                    <div class="one">
                        <h1 class = 'card-header-title' style="margin-bottom:10px;" >
                            {{ post.title }}
                        </h1>
                            <div class= 'card'>
                                <img v-if="post.imgs[0]!== undefined" :src="post.imgs[0].src" class='card-image'>
                                <img v-if="post.imgs[0]== undefined" src="../../../assets/images/noImage.png" class='card-image'>
                                <div class ='card-info'>
                                    <ul>
                                        <li class="card-likes"><span class="visually-hidden">좋아요 :  </span><i class="fas fa-heart" aria-hidden="true"></i> {{post.favorite.length}}</li>
                                        <li class="card-comments"><span class="visually-hidden">댓글 수 : </span><i class="fas fa-comment" aria-hidden="true"></i> {{ post.commentNum }}</li>
                                    </ul>
                                </div> 	
                            </div>
                        <div class="keywords">
                            <div class = 'card-footer' >
                                <div class="keyword" v-for="keyword in post.keywords" v-bind:key="keyword">
                                    <span style="display:inline-block; white-space:nowrap;">#{{keyword}}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                    </router-link>
                </div>
            </div>
        </div>
     </div>

</template>

<script>
import myPost from "../../../assets/css/myPost.scss"

import Axios from 'axios'

export default {

    created(){
        this.reqPosts();
    },
    filters: {

        setHashTag(tag) {
            return "&#35" + tag;
        }
    },
    methods: {
        // sharper(keyword){
        //     return "#"+keyword
        // },
        reqPosts() {
        console.log("hihi Post")
        const jwt = localStorage.getItem('routrip_JWT');
        Axios.post('http://192.168.100.70:8083/page/searchBoard/' , {jwt : jwt})
            .then(res => {
                console.log(res.data[0].imgs[0].src)
                console.log(res.data)
                this.postList=res.data
                console.log(this.postList)
            });
        }
    },
    data(){
        return{
            postList:[]
        };
    },
};
</script>
