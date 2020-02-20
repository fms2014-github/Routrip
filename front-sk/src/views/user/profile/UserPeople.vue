<template>
    <div style="width: 100%;">
        <div style="float: left; width: 45%;">
            <div style="margin-bottom:2vw;">
                <h2 style="ab">following</h2>
                <h3>총 {{ followingList.length }}명을 팔로우하고 있습니다.</h3>
            </div>
            <div>
                <div class="follow-picture" v-for="following in followingList" v-bind:key="following.uid">
                    <img :src="following.profileImg" alt="userPic"> 
                    <span style="font-size:20px;"> {{ following.nickname }} </span>
                </div>                
            </div>
        </div>
        <div style="float: left; width: 45%;">
            <div style="margin-bottom:2vw;">
                <h2 style="ab">follower</h2>
                <h3>총 {{ followerList.length }}명이 당신을 팔로우하고 있습니다.</h3>
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
            Axios.post('http://52.78.8.59:8083/account/follower' , {jwt : jwt})
                .then(res => { 
                    // console.log("follower")
                    // console.log(res)
                    this.followerList=res.data
                });
            // console.log("hihi following")                
            Axios.post('http://52.78.8.59:8083/account/following' , {jwt : jwt})
                .then(res => {
                    // console.log("following")
                    // console.log(res)
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
