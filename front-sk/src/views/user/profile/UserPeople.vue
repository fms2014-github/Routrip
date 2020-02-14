<template>
    <div style="width: 100%;">
        <div style="float: left; width: 45%;">
            <div style="margin: 0px;">
                <h2 style="ab">following</h2>
                <h3>{{ followingList.length }}</h3>
            </div>
            <div>
                <div class="follow-picture" v-for="following in followingList" v-bind:key="following.uid">
                    <img :src="following.profileImg" alt="userPic"> 
                    <span style="font-size:20px;"> {{ following.nickname }} </span>
                </div>                
            </div>
        </div>
        <div style="float: left; width: 45%;">
            <div style="margin: 0px;">
                <h2 style="ab">follower</h2>
                <h3>{{ followerList.length }}</h3>
            </div>
            <div>
                <div class="follow-picture" v-for="follower in followerList" v-bind:key="follower.uid">
                    <img :src="follower.profileImg" alt="userPic"> 
                    <span class="follow" style="font-size:20px;"> {{ follower.nickname }} </span>
                </div>                
            </div>
        </div>
    </div>
</template>

<script>
import Swal from 'sweetalert2';
import Axios from 'axios';


export default {
   
    mounted(){
        this.reqfollow();
    },
    methods:{
        
        reqfollow() {
            const jwt = localStorage.getItem('routrip_JWT');
            Axios.post('http://192.168.100.70:8083/account/follower' , {jwt : jwt})
                .then(res => { 
                    console.log("follower")
                    console.log(res)
                    this.followerList=res.data
                });
            console.log("hihi following")                
            Axios.post('http://192.168.100.70:8083/account/following' , {jwt : jwt})
                .then(res => {
                    console.log("following")
                    console.log(res)
                    this.followingList=res.data
                });
        }
    },
    data(){
        return{
            followerList:[],
            followingList:[]
        };
    }
};
</script>
