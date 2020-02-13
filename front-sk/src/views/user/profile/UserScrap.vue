<template>
    <div>
        <h1>안녕 여긴 스크랩</h1>
        
        <li v-for="scrap in scrapList" v-bind:key="scrap.boardid">
            {{ scrap.title }} 스크랩
        </li>
    </div>  
</template>

<script>

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
                console.log(res)
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
