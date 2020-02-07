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
                <div class="postings-posting">
                    <hooper
                        :infiniteScroll="true"
                        :itemsToShow="3"
                        :progress="true"
                        :autoPlay="true"
                        :playSpeed="2000"
                    >
                        <slide v-for="(data, dataIdx) in datas" :key="dataIdx">
                            <img :src="'http://192.168.100.70:8083/' + data.imgs[0].src" alt />
                        </slide>
                        <hooper-navigation slot="hooper-addons"></hooper-navigation>
                    </hooper>
                </div>
            </div>
            <div class="posting-box">
                <div class="postings">
                    <div class="posting-component" v-for="(data, dataIdx) in datas" :key="dataIdx">
                        <div class="postings-posting">
                            <div class="post-info">
                                <div class="profile-img">
                                    <img
                                        :src="'http://192.168.100.70:8083/' + data.user.profileImg"
                                    />
                                </div>
                                <div class="name-time">
                                    <strong>{{ data.title }}</strong>
                                    <span>{{ data.writeday }}</span>
                                    <br />
                                    <span>{{ data.user.nickname }}</span>
                                </div>
                                <div class="else">
                                    <span>
                                        <i class="fas fa-ellipsis-h"></i>
                                    </span>
                                </div>
                            </div>
                        </div>
                        <div class="post-imgs-box">
                            <hooper class="post-img-box">
                                <slide v-for="(img, imgIdx) in data.imgs" :key="imgIdx">
                                    <img :src="'http://192.168.100.70:8083/' + img.src" alt />
                                </slide>
                                <hooper-pagination slot="hooper-addons"></hooper-pagination>
                            </hooper>
                        </div>
                        <div class="sns-btn">
                            <div class="like">
                                <button>🧡</button>
                            </div>
                            <div class="follow">
                                <button>🏹</button>
                            </div>
                            <div class="state">{{ data.favoriteNum }}명이 이 게시글을 좋아합니다.</div>
                        </div>

                        <div class="text">
                            <span>{{ data.keyword }}</span>
                        </div>

                        <div class="comment-box">
                            <div class="comments">
                                <div
                                    class="comment"
                                    v-for="(comment, commentIdx) in data.comments"
                                    :key="commentIdx"
                                >
                                    <div class="writer-img">
                                        <img
                                            :src="'http://192.168.100.70:8083/' + comment.user.profileImg"
                                            alt
                                        />
                                    </div>
                                    <div class="comment-info">
                                        <div class="writer">
                                            <strong>{{ comment.user.nickname }}</strong>
                                            <span>{{ comment.writeday }}</span>
                                        </div>
                                        <div class="writer-text">
                                            <span>{{ comment.contents }}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="write-comment">
                                <form action class="comment-form">
                                    <textarea
                                        class="comment"
                                        @keydown="resize(this)"
                                        placeholder="댓글 달기..."
                                        autocomplete="off"
                                        wrap="soft"
                                    ></textarea>
                                </form>
                                <div class="comment-btn">
                                    <button>
                                        <strong>등록</strong>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <Footer></Footer>
    </div>
</template>

<script>
//vue
import Header from './Header.vue';
import Footer from './Footer.vue';

//js
import Kakao from '../../components/user/snsLogin/kakao';

// scss
import '../../assets/css/main/posting.scss';
import '../../assets/css/main/main.scss';
import '../../assets/css/main/bestPosting.scss';

//axios
import Axios from 'axios';

//component
import { Hooper, Slide, Pagination as HooperPagination, Navigation as HooperNavigation } from 'hooper';

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
        // Posting,
        Hooper,
        Slide,
        HooperPagination,
        HooperNavigation,
    },
    data: () => {
        return {
            datas: '',
        };
    },
    created: function() {
        // using JSONPlaceholder
        const URI = 'http://192.168.100.70:8083/';
        Axios.get(`${URI}/page/boardList`)
            .then(res => {
                console.log(res.data);
                this.datas = res.data;
            })
            .catch(res => {
                // console.log(res);
            });
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
