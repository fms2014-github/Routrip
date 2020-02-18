<template>
    <div>
        <h2>스크랩</h2><br><br><br><br>
        <div class="row">
            <div v-for="scrap in scrapList" v-bind:key="scrap.boardid">
                <div class="gallery">
                    <div class = 'card-header'>
                        <h1 class = 'card-header-title'>{{ scrap.title }}</h1> <h3>스크랩 날짜</h3>    
                    </div>
                    <div class= 'card'>
                        <img :src="scrap.imgs[0].src" class = 'card-image'>
                        
                        <div class ='card-info'>
                            <ul>
                                <li class="card-likes"><span class="visually-hidden">좋아요 :  </span><i class="fas fa-heart" aria-hidden="true"></i> {{scrap.favorite.length}}</li>
                                <li class="card-comments"><span class="visually-hidden">댓글 수 : </span><i class="fas fa-comment" aria-hidden="true"></i> {{ scrap.commentNum }}</li>
                            </ul>
                        </div> 	
                    </div>
                    <div class = 'card-footer'>
                        <!-- {{keywords}} -->
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
  data(){
    return{
            
      scrapList:[]

    };
  },

};
</script>
