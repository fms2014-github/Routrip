<template>
     <div>
        <div class="row">
            <h5 style="text-align:center; font-size:2.1vw; @include wrap; margin-bottom:10vw;">총 {{scrapList.length}} 개의 스크랩</h5>
            <div v-for="scrap in scrapList" :key="scrap.boardid">
                <div class="gallery">
                    <router-link :to="{ name: 'Detail', params: { boardid: scrap.boardid } }">
                    <div class="one">
                        <h1 class = 'card-header-title' style="margin-bottom:10px;" >
                           
                                {{ scrap.title }}
                            
                        </h1>
                        
                            <div class= 'card'>
                                <img v-if="scrap.imgs[0]!== undefined" :src="scrap.imgs[0].src" class='card-image'>
                                <img v-if="scrap.imgs[0]== undefined" src="../../../assets/images/noImage.png" class='card-image'>
                                <div class ='card-info'>
                                    <ul>
                                        <li class="card-likes"><span class="visually-hidden">좋아요 :  </span><i class="fas fa-heart" aria-hidden="true"></i> {{scrap.favorite.length}}</li>
                                        <li class="card-comments"><span class="visually-hidden">댓글 수 : </span><i class="fas fa-comment" aria-hidden="true"></i> {{ scrap.commentNum }}</li>
                                    </ul>
                                </div> 	
                            </div>
                        <div class="keywords">
                            <div class = 'card-footer' >
                                <div class="keyword" v-for="keyword in scrap.keywords" v-bind:key="keyword">
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

import Axios from 'axios';


export default {

    created(){
        this.reqScrap();
    },
    methods:{
        async reqScrap() {
        console.log("hihi scrap")
        const jwt = localStorage.getItem('routrip_JWT');
        await Axios.post('http://192.168.100.70:8083/page/scrapBoard/' , {jwt : jwt})
            .then(res => {
                console.log("???", res.data)
                this.scrapList=res.data
                
            });
        }
    },
    data(){
        return{
            scrapList:[{
                title: 'none'
            }]
        };
    },

};
</script>
