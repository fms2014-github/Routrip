<template>
    <div class="row">
        <li v-for="post in postList" v-bind:key="post.boardid">
            {{ post.title }}
        </li>




        <!-- <allPost title="제주도 3박4일" keywords="#저가여행 #맛집위주" picture="trip1.jpg"> </allPost>

        <allPost title="강릉 2박3일" keywords="#자연경관 #커플여행" picture="trip2.jpg"> </allPost>

        <allPost title="제주도 3박4일" keywords="#저가여행 #맛집위주" picture="trip5.jpg"> </allPost>

        <allPost title="제주도 3박4일" keywords="#저가여행 #맛집위주"> </allPost>

        <allPost title="제주도 3박4일" keywords="#저가여행 #맛집위주"> </allPost>

        <allPost title="제주도 3박4일" keywords="#저가여행 #맛집위주"> </allPost>

        <allPost title="으아아"> </allPost> -->

        
    </div>
</template>

<script>
// import allPost from '../../../components/common/allPost';
import Axios from 'axios'

export default {
    // components: {
    //     allPost,
    // },
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
    data(){
        return{
            postList:[]
        };
    }
};
</script>
