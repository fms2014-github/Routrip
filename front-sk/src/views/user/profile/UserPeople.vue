<template>
    <div>

        <div style="display: inline-block; width:45vw; ">
            A영역
            <li v-for="following in followingList" v-bind:key="following.uid">
                {{ following.nickname }}
            </li>
        </div>
        <div style="display: inline-block; width:45vw; ">
            B영역
            <li v-for="follower in followerList" v-bind:key="follower.uid">
                {{ follower.nickname }}
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
