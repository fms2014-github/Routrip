<template>

    <div class="post">
        <h1>안녕 여긴 글</h1>
        <UserProfile :description="hi" >

        </UserProfile>
        
    </div>
   
</template>

<script>
import UserProfile from "../../../components/common/UserProfile"

export default {
    
    components:{
        UserProfile
    }

    // data() {
    //     return {
    //         dummy: [
    //             {
    //                 id: 1,
    //                 title: '제목',
    //                 content: '내용',
    //             },
    //             {
    //                 id: 2,
    //                 title: '제목1',
    //                 content: '내용1',
    //             },
    //         ],
    //         post: null,
    //     };
    // },
    // created() {
    //     this.fetchData();
    // },
    // watch: {
    //     $route: 'fetchData',
    // },
    // methods: {
    //     fetchData() {
    //         this.error = this.post = null;
    //         this.loading = true;
    //     },
    //     plus() {
    //         const data = {
    //             id: 3,
    //             title: '제목 new',
    //             content: '내용 new',
    //         };
    //         this.dummy.push(data);
    //     },
    // },
};
</script>
