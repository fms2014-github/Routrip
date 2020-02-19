<template>
    <div>
        <div class="row">
            <h2 style="text-align:center; font-size:2.1vw; @include wrap;">총 {{scrapList.length}} 개의 스크랩</h2>
            <div v-for="scrap in scrapList" :key="scrap.boardid">
                <div class="gallery">
                    <h1 class = 'card-header-title'>
                        <router-link :to="{ name: 'Detail', params: { boardid: scrap.boardid } }">
                            {{ scrap.title }}
                        </router-link>
                    </h1>
                <h4 style="text-align:right;">{{ scrap.scrapdate.split(':')[0]+":"+scrap.scrapdate.split(':')[1] }}</h4>
                    <router-link :to="{ name: 'Detail', params: { boardid: scrap.boardid } }">
                        <div class= 'card'>
                            <img :src="scrap.imgs[0].src" class = 'card-image'>
                            <div class ='card-info'>
                                <ul>
                                    <li class="card-likes"><span class="visually-hidden">좋아요 :  </span><i class="fas fa-heart" aria-hidden="true"></i> {{scrap.favorite.length}}</li>
                                    <li class="card-comments"><span class="visually-hidden">댓글 수 : </span><i class="fas fa-comment" aria-hidden="true"></i> {{ scrap.commentNum }}</li>
                                </ul>
                            </div> 	
                        </div>
                    </router-link>
                       
                    <div class="keywords">
                         <div class = 'card-footer' >
                            <div v-for="keyword in scrap.keywords" :key="keyword" class="keyword">
                                <span>#{{keyword}}</span>
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
import Axios from 'axios';


export default {
  data(){
    return{
      scrapList:[]
    };
  },
  mounted(){
    this.reqScrap();
  },
  methods:{
    reqScrap() { 
      console.log("hihi scrap")
      const jwt = localStorage.getItem('routrip_JWT');
      Axios.post('http://192.168.100.70:8083/page/scrapBoard/' , {jwt : jwt})
        .then(res => {
          console.log(res.data)
          this.scrapList=res.data
                
        });
    }
  },

};
</script>
