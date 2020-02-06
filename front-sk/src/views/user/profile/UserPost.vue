<template>

    <div class="row">
        <allBoard title = '제주도 3박4일'
		    keywords = "#저가여행 #맛집위주"
            picture=trip2.jpg>
        </allBoard>

        <allBoard title = '강릉 2박3일'
		    keywords = "#자연경관 #커플여행 ">
        </allBoard>
        
        <allBoard title = '제주도 3박4일'
		    keywords = "#저가여행 #맛집위주"
            picture=trip4.jpg>
        </allBoard>

        <allBoard title = '제주도 3박4일'
		    keywords = "#저가여행 #맛집위주">
        </allBoard>

        <allBoard title = '제주도 3박4일'
		    keywords = "#저가여행 #맛집위주">
        </allBoard>

        <allBoard title = '제주도 3박4일'
		    keywords = "#저가여행 #맛집위주">
        </allBoard>
        
    </div>
   
</template>

<script>
import allBoard from "../../../components/common/allBoard"

export default {
    
    components:{
        allBoard
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
