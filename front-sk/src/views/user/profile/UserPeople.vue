<template>
    <div>

        <div style="display: inline-block; width:45vw; ">
            <h2>following</h2>
            <div class="follow-picture" style="margin-bottom:100px; margin-right:0px;" v-for="following in followingList" v-bind:key="following.uid">
                <img :src="following.profileImg" alt="userPic"> 
                <h3>{{ following.nickname }}</h3>
            </div>
        </div>
        <div style="display: inline-block; width:45vw; ">
            <h2>follower</h2>
            <li class="follow-picture" style="margin-bottom:100px; margin-right:0px;" v-for="follower in followerList" v-bind:key="follower.uid">
                <img :src="follower.profileImg" alt="userPic"> 
                <h3>{{ follower.nickname }}</h3>
            </li>
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
            console.log("hihi follower")
            const jwt = localStorage.getItem('routrip_JWT');
            Axios.post('http://192.168.100.70:8083/account/follower' , {jwt : jwt})
                .then(res => { 
                    console.log(res)
                    this.followerList=res.data
                });
            console.log("hihi following")                
            Axios.post('http://192.168.100.70:8083/account/following' , {jwt : jwt})
                .then(res => {
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
