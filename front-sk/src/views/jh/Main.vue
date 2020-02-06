<template>
    <div id="main">
        <Header></Header>
        <div class="body">
            <div class="search-desktop">
                <form action class="search-form">
                    <input type="text" placeholder="검색..." />
                </form>
            </div>
            <div class="best-posting">
                <BestPosting></BestPosting>
            </div>
            <div class="posting-box">
                <div class="postings">
                    <Posting></Posting>
                </div>
            </div>
        </div>
        <Footer></Footer>
    </div>
</template>

<script>
import Header from './Header.vue';
import Footer from './Footer.vue';
import Posting from './Posting.vue';
import BestPosting from './BestPosting.vue';
import Kakao from '../../components/user/snsLogin/kakao';
import '../../assets/css/main/main.scss';
// 뷰엑스를 가져옴
import { createNamespacedHelpers } from 'vuex';
// load user store 필요한 부분만 가져오기
const userMapState = createNamespacedHelpers('User').mapState;
const userMapMutations = createNamespacedHelpers('User').mapMutations;
const userMapGetters = createNamespacedHelpers('User').mapGetters;

export default {
    components: {
        Header,
        Footer,
        Posting,
        BestPosting,
    },
    computed: {
        ...userMapState(['User']),
        ...userMapGetters(['getUser']),
    },
    methods: {
        ...userMapMutations(['setUser']),
        kakao() {
            const at = localStorage.getItem('kakao_access_token');
            const rt = localStorage.getItem('kakao_refresh_token');
            console.log(at);
            console.log(rt);
            Kakao.init('cffc768e4739655aab323adbd9eb2633');
            console.log(Kakao.isInitialized());
            Kakao.API.request({
                url: '/v1/user/me',
                success: res => {
                    this.setUser(res);
                    console.log(res);
                    // console.log(res.properties.nickname);
                    // console.log(res.properties.profile_image);
                    console.log(this.getUser);
                },
            });
        },
    },
};
</script>

<style></style>
