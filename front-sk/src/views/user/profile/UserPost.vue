<template>
    <div>
        <div class="row">
            <h2 style="text-align:center; font-size: 2.1vw; @include wrap;">
                총 {{postList.length}} 개의 글이 있습니다.</h2>
            <div v-for="post in postList" v-bind:key="post.boardid">
                <div class="gallery">
                    <div class="one">
                        <h1 class = 'card-header-title'  >
                            <router-link :to="{ name: 'Detail', params: { boardid: post.boardid } }">
                                {{ post.title }}
                            </router-link>
                        </h1>
                        <h4 style="text-align:right;">{{ post.writedate.split(':')[0] + ":" + post.writedate.split(':')[1] }}</h4>    
                        <router-link :to="{ name: 'Detail', params: { boardid: post.boardid } }">
                            <div class= 'card'>
                                <img :src="post.imgs[0].src+control" class = 'card-image'>
                                
                                <div class ='card-info'>
                                    <ul>
                                        <li class="card-likes"><span class="visually-hidden">좋아요 :  </span><i class="fas fa-heart" aria-hidden="true"></i> {{post.favorite.length}}</li>
                                        <li class="card-comments"><span class="visually-hidden">댓글 수 : </span><i class="fas fa-comment" aria-hidden="true"></i> {{ post.commentNum }}</li>
                                    </ul>
                                </div> 	
                            </div>
                        </router-link>
                        <div class="keywords">
                            <div class = 'card-footer' >
                                <div class="keyword" v-for="keyword in post.keywords" v-bind:key="keyword">
                                    <span>#{{keyword}}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
     </div>

</template>

<script>
import myPost from "../../../assets/css/myPost.scss"

import Axios from 'axios'

export default {

    mounted(){
        this.reqPosts();
    },
    methods: {
        reqPosts() {
        console.log("hihi Post")
        const jwt = localStorage.getItem('routrip_JWT');
        Axios.post('http://192.168.100.70:8083/page/searchBoard/' , {jwt : jwt})
            .then(res => {
                console.log(res)
                this.postList=res.data
            });
        }
    },
    
    go(){

    },

    data(){
        return{
            
            postList:[]
        };
    }
};
</script>
