<template>
    <div id="main">
        <Header></Header>
        <div class="body">
            <div class="posting-box">
                <div class="postings">
                    <div class="posting-component" v-for="(data, dataIdx) in datas" :key="dataIdx">
                        <div class="postings-posting">
                            <div class="post-info">
                                <div class="profile-img">
                                    <img :src="data.user.profileImg" />
                                </div>
                                <div class="name-time">
                                    <strong>{{ data.title }}</strong>
                                    <span>{{ data.writeday }}</span>
                                    <br />
                                    <span>{{ data.user.nickname }}</span>
                                </div>
                                <div class="else" @click="showElseBtn(data)">
                                    <span>
                                        <i class="fas fa-ellipsis-h"></i>
                                    </span>
                                </div>
                            </div>
                        </div>

                        <div class="post-imgs-box">
                            <hooper class="post-img-box">
                                <slide v-for="(img, imgIdx) in data.imgs" :key="imgIdx">
                                    <router-link
                                        :to="{ name: 'Detail', params: { boardid: data.boardid } }"
                                    >
                                        <img :src="img.src" alt />
                                    </router-link>
                                </slide>
                                <hooper-pagination slot="hooper-addons"></hooper-pagination>
                            </hooper>
                        </div>

                        <div class="sns-tag-box">
                            <div class="sns-btn">
                                <div class="like">
                                    <button @click="toggleLikeBtn(data.boardid)">
                                        <div :class="{ likeToggle: likeShow[dataIdx].like }">
                                            <i class="far fa-heart"></i>
                                        </div>
                                        <div :class="{ likeToggle: !likeShow[dataIdx].like }">
                                            <i class="fas fa-heart" style="color:red;"></i>
                                        </div>
                                    </button>
                                </div>
                                <div class="scrap">
                                    <button @click="toggleScrapBtn(data.boardid)">
                                        <div :class="{ scrapToggle: scrapShow[dataIdx].scrap }">
                                            <i class="far fa-bookmark"></i>
                                        </div>
                                        <div :class="{ scrapToggle: !scrapShow[dataIdx].scrap }">
                                            <i class="fas fa-bookmark" style="color:blue;"></i>
                                        </div>
                                    </button>
                                </div>
                                <div class="state" v-if="data.favoriteNum == 1">
                                    <strong>{{ whoLiked[dataIdx] }}</strong>님이 게시글을 좋아합니다.
                                </div>
                                <div class="state" v-if="data.favoriteNum > 1">
                                    <strong>{{ whoLiked[dataIdx] }}</strong>
                                    님 외 {{ data.favoriteNum - 1 }}명이 이 게시글을 좋아합니다.
                                </div>
                            </div>

                            <div class="keywords">
                                <div
                                    @click="search(keyword)"
                                    class="keyword"
                                    v-for="(keyword, keywordIdx) in data.keywords"
                                    :key="keywordIdx"
                                >
                                    <span>#{{ keyword }}</span>
                                </div>
                            </div>
                        </div>

                        <div class="comment-box">
                            <div class="comments">
                                <div
                                    class="comment"
                                    v-for="(comment, commentIdx) in data.comments"
                                    :key="commentIdx"
                                >
                                    <div class="writer-img">
                                        <img :src="comment.user.profileImg" alt />
                                    </div>
                                    <div class="comment-info">
                                        <div class="comment-info-box">
                                            <div class="writer">
                                                <div class="writer-info">
                                                    <strong>{{ comment.user.nickname }}</strong>
                                                    <span>{{ comment.writeday }}</span>
                                                </div>
                                                <div class="writer-reply">
                                                    <span>댓글달기</span>
                                                </div>
                                            </div>
                                            <div class="writer-text">
                                                <span>{{ comment.contents }}</span>
                                            </div>
                                        </div>
                                        <div
                                            class="comment-delete"
                                            v-if="comment.uid == getUser.data.uid"
                                        >
                                            <button @click="deleteComment(comment)">삭제</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="write-comment">
                                <form action class="comment-form">
                                    <textarea
                                        class="comment"
                                        placeholder="댓글 달기..."
                                        autocomplete="off"
                                        wrap="soft"
                                        v-model="comment"
                                    ></textarea>
                                </form>
                                <div class="comment-btn">
                                    <button @click="addComment(data)">
                                        <strong>등록</strong>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="else-modal" :class="{ elseModalBackground: !elseModalBackground }">
            <div class="modal-box">
                <div class="box-content">
                    <button class="else-btn first">게시물로 이동</button>
                    <button
                        :class="{ followBtn: !followBtn }"
                        class="else-btn middle"
                        @click="follow"
                    >팔로우</button>
                    <button
                        :class="{ unfollowBtn: !unfollowBtn }"
                        class="else-btn middle"
                        @click="follow"
                    >팔로우 취소</button>
                    <button :class="{ myPosting: !myPosting }" class="else-btn middle">내글 수정</button>
                    <button :class="{ myPosting: !myPosting }" class="else-btn middle">내글 삭제</button>
                    <button class="else-btn last" @click="noShowElseBtn">X</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
//vue
import Header from './Header.vue';
import Swal from 'sweetalert2';
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
const userMapActions = createNamespacedHelpers('User').mapActions;

const URI = 'http://localhost:8083/';
export default {
    components: {
        Header,
        // Posting,
        Hooper,
        Slide,
        HooperPagination,
    },
    data: () => {
        return {
            datas: '',
            comment: '',
            likeList: [],
            likeShow: [],
            whoLiked: '',
            scrapList: [],
            scrapShow: [],
            followList: [],
            elseModalBackground: false,
            boardData: '',
            jwt: '',
            followBtn: false,
            unfollowBtn: false,
            myPosting: false,
            keyword: '',
        };
    },
    mounted() {
        if (this.getUser.user === undefined) {
            this.req();
        } else {
            this.getUser;
        }
    },
    created: function() {
        this.jwt = localStorage.getItem('routrip_JWT');
        this.keyword = this.$route.params.searchWord;
        this.showAll();
    },
    computed: {
        ...userMapState(['User']),
        ...userMapGetters(['getUser']),
    },
    methods: {
        ...userMapMutations(['setUser']),
        ...userMapActions(['reqUserInfo']),
        async req() {
            await this.reqUserInfo();
            this.getUser;
        },
        showAll() {
            Axios.post(`${URI}/page/favoriteBoard`, { jwt: this.jwt })
                .then(res => {
                    // // console.log(res.data);
                    this.likeList = [];
                    for (var i = 0; i < res.data.length; ++i) {
                        this.likeList.push(res.data[i].boardid);
                    }

                    Axios.post(`${URI}/page/scrapBoard`, { jwt: this.jwt })
                        .then(res => {
                            // // console.log(res.data);
                            this.scrapList = [];
                            for (var i = 0; i < res.data.length; ++i) {
                                this.scrapList.push(res.data[i].boardid);
                            }

                            Axios.get(`${URI}/page/searchBoard/${this.keyword}`)
                                .then(res => {
                                    // // console.log(res.data);
                                    this.likeShow = [];
                                    this.scrapShow = [];
                                    this.whoLiked = [];
                                    this.datas = res.data;
                                    // // console.log(this.datas);
                                    // // console.log(this.getUser.data.uid);
                                    for (var i = 0; i < this.datas.length; ++i) {
                                        if (res.data[i].favoriteNum > 0) {
                                            this.whoLiked.push(res.data[i].favorite[0].nickname);
                                        } else {
                                            this.whoLiked.push('');
                                        }

                                        //좋아요
                                        if (this.likeList.includes(this.datas[i].boardid)) this.likeShow.push({ like: true });
                                        else this.likeShow.push({ like: false });
                                        //스크랩

                                        if (this.scrapList.includes(this.datas[i].boardid)) this.scrapShow.push({ scrap: true });
                                        else this.scrapShow.push({ scrap: false });
                                    }
                                })
                                .catch(res => {
                                    // console.log('키워드 검색 실패');
                                });
                        })
                        .catch(res => {
                            // console.log('스크랩 게시글 조회 실패');
                        });
                })
                .catch(res => {
                    // console.log('좋아요 게시글 조회 실패');
                });
        },
        getAlldata() {
            Axios.get(`${URI}/page/boardList`)
                .then(res => {
                    this.datas = res.data;
                })
                .catch(res => {
                    // // console.log(res);
                });
        },
        showElseBtn(data) {
            // // console.log(data);
            this.boardData = data;
            this.elseModalBackground = true;
            var uid = this.getUser.data.uid;
            // // console.log(uid);
            Axios.post(`${URI}/account/following`, { uid: uid })
                .then(res => {
                    // // console.log(res.data);
                    // // console.log(this.boardData);
                    if (this.boardData.uid == uid) {
                        //선택한 게시글이 내 게시글인경우
                        this.myPosting = true;
                    } else {
                        this.followBtn = true;
                        for (var i = 0; i < res.data.length; ++i) {
                            if (res.data[i].uid != this.boardData.uid) continue;
                            this.followBtn = false;
                            this.unfollowBtn = true;
                            break;
                        }
                    }
                })
                .catch(res => {
                    // console.log('팔로우 정보 조회 실패');
                });
        },
        noShowElseBtn() {
            this.elseModalBackground = false;
            this.followBtn = false;
            this.unfollowBtn = false;
            this.myPosting = false;
        },
        follow() {
            // console.log(this.boardData);
            Axios.post(`${URI}/account/follow`, { jwt: this.jwt, uid: this.boardData.uid })
                .then(res => {
                    if (this.followBtn) {
                        Swal.fire({
                            icon: 'success',
                            title: '"' + this.boardData.user.nickname + '" 님을 팔로우 했습니다.',
                        });
                    } else {
                        Swal.fire({
                            icon: 'warning',
                            title: '"' + this.boardData.user.nickname + '" 님의 팔로우를 취소했습니다.',
                        });
                    }
                    this.noShowElseBtn();
                })
                .catch(res => {
                    // console.log('팔로우 등록 및 취소 실패');
                });
        },
        addComment(info) {
            // // console.log(this.comment);
            var commentObject = new Object();
            commentObject.jwt = this.jwt;
            commentObject.boardid = info.boardid;
            commentObject.contents = this.comment;
            commentObject.uid = info.uid;
            if (this.comment == '') {
                Swal.fire({
                    icon: 'warning',
                    title: '댓글 오류',
                    text: '댓글을 입력해 주세요!',
                });
            } else {
                Axios.post(`${URI}/page/comment`, commentObject)
                    .then(res => {
                        // // console.log('댓글 달기 성공');
                        this.comment = '';
                    })
                    .catch(res => {
                        // console.log('댓글 달기 실패');
                    });
                this.getAlldata();
            }
        },
        deleteComment(info) {
            if (confirm('댓글을 삭제하시겠습니까?')) {
                Axios.delete(`${URI}/page/comment`, {
                    data: info.commentid,
                })
                    .then(res => {
                        // // console.log('댓글 삭제 성공');
                    })
                    .catch(res => {
                        // console.log('댓글 삭제 실패');
                    });
                this.getAlldata();
            }
        },
        toggleLikeBtn(boardid) {
            Axios.post(`${URI}/page/favorite`, { jwt: this.jwt, boardid: boardid })
                .then(res => {
                    this.showAll();
                })
                .catch(res => {
                    // console.log(res);
                });
        },
        toggleScrapBtn(boardid) {
            Axios.post(`${URI}/page/scrap`, { jwt: this.jwt, boardid: boardid })
                .then(res => {
                    this.showAll();
                })
                .catch(res => {
                    // console.log(res);
                });
        },
        toDetailPage(data) {
            // console.log(data);
        },
        search(keyword) {
            // // console.log(keyword);
            this.$router.push({ name: 'Search', params: { keyword: keyword } });
        },
    },
};
</script>

<style></style>
