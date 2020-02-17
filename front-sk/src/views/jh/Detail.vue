<template>
    <div class="detail">
        <Header></Header>
        <div class="body">
            <div class="posting-box">
                <div class="img-box">
                    <div id="detaila"></div>
                </div>
                <div class="comment-box">
                    <div class="writer">
                        <div class="profile-img">
                            <img :src="data.user.profileImg" />
                        </div>
                        <div class="name-time-follow-box">
                            <div class="name-time">
                                <strong>{{ data.title }}</strong>
                                <span>{{ data.writeday }}</span>
                                <br />
                                <span>{{ data.user.nickname }}</span>
                            </div>
                            <div class="follow" @click="toggleFollow">
                                <span :class="{ followShow: !follow }">팔로우</span>
                                <span :class="{ unfollowShow: !unfollow }">팔로우 취소</span>
                            </div>
                        </div>
                    </div>
                    <div class="sns-tag-box">
                        <div class="sns-btn">
                            <div class="like">
                                <button @click="toggleLikeBtn">
                                    <div :class="{ likeit: likeit }">
                                        <i class="far fa-heart"></i>
                                    </div>
                                    <div :class="{ likeit: !likeit }">
                                        <i class="fas fa-heart" style="color:red;"></i>
                                    </div>
                                </button>
                            </div>
                            <div class="scrap">
                                <button @click="toggleScrapBtn">
                                    <div :class="{ scrapit: scrapit }">
                                        <i class="far fa-bookmark"></i>
                                    </div>
                                    <div :class="{ scrapit: !scrapit }">
                                        <i class="fas fa-bookmark" style="color:blue;"></i>
                                    </div>
                                </button>
                            </div>
                            <div class="state" v-if="data.favoriteNum == 1">
                                <strong>{{ whoLiked }}</strong>님이 게시글을 좋아합니다.
                            </div>
                            <div class="state" v-if="data.favoriteNum > 1">
                                <strong>{{ whoLiked }}</strong>
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

                    <div class="comments">
                        <div class="show-comment">
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
                                            <strong>{{ comment.user.nickname }}</strong>
                                            <span>{{ comment.writeday }}</span>
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
                                <button @click="addComment">
                                    <strong>등록</strong>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import '../../assets/css/main/detail.scss';

import Header from './Header.vue';
//axios
import Axios from 'axios';

import kakaoMap from '../../apis/kakaoMapAPI.js'

//component
import { Hooper, Slide, Pagination as HooperPagination, Navigation as HooperNavigation } from 'hooper';

// 뷰엑스를 가져옴
import { createNamespacedHelpers } from 'vuex';
// load user store 필요한 부분만 가져오기
const userMapState = createNamespacedHelpers('User').mapState;
const userMapMutations = createNamespacedHelpers('User').mapMutations;
const userMapGetters = createNamespacedHelpers('User').mapGetters;
const userMapActions = createNamespacedHelpers('User').mapActions;

const URI = 'http://192.168.100.70:8083/';
export default {
    components: {
        Header,
    },
    data: () => {
        return {
            data: '',
            boardid: '',
            likeit: false,
            whoLiked: '',
            scrapit: false,
            followList: [],
            jwt: '',
            follow: false,
            unfollow: false,
            comment: '',
        };
    },
    created() {
        this.jwt = localStorage.getItem('routrip_JWT');
        this.boardid = this.$route.params.boardid;
        this.showAll();
    },
    mounted() {
        if (this.getUser.user === undefined) {
            this.req();
        } else {
            this.getUser;
        }
        kakaoMap.viewMap();
        
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
            Axios.post(`${URI}/page/boardDetail`, { jwt: this.jwt, boardid: this.boardid })
                .then(res => {
                    // console.log(res.data);
                    this.data = res.data;

                    Axios.post(`${URI}/page/scrapBoard`, { jwt: this.jwt })
                        .then(res => {
                            // console.log(res.data);
                            this.scrapit = false;
                            for (var i = 0; i < res.data.length; ++i) {
                                if (res.data[i].boardid == this.data.boardid) {
                                    this.scrapit = true;
                                    break;
                                }
                            }
                            Axios.post(`${URI}/page/favoriteBoard`, { jwt: this.jwt })
                                .then(res => {
                                    // console.log(res.data);
                                    this.likeit = false;
                                    for (var i = 0; i < res.data.length; ++i) {
                                        if (res.data[i].boardid == this.data.boardid) {
                                            this.likeit = true;
                                            break;
                                        }
                                    }

                                    var uid = this.getUser.data.uid;
                                    // console.log('uid : ' + uid);
                                    Axios.post(`${URI}/account/following`, { uid: uid })
                                        .then(res => {
                                            // console.log(res.data);
                                            // console.log(this.boardData);
                                            this.follow = false;
                                            this.unfollow = false;
                                            if (this.data.uid != uid) {
                                                this.follow = true;
                                                for (var i = 0; i < res.data.length; ++i) {
                                                    if (res.data[i].uid == this.data.uid) {
                                                        this.follow = false;
                                                        this.unfollow = true;
                                                        break;
                                                    }
                                                }
                                            }
                                        })
                                        .catch(res => {
                                            console.log('팔로우 정보 조회 실패');
                                        });
                                    if (this.data.favoriteNum > 0) {
                                        this.whoLiked = this.data.favorite[0].nickname;
                                        // console.log(this.whoLiked);
                                    }
                                })
                                .catch(res => {
                                    console.log('좋아요 게시글 조회 실패');
                                });
                        })
                        .catch(res => {
                            console.log('스크랩 게시글 조회 실패');
                        });
                })
                .catch(res => {
                    console.log('상세 게시물 조회 실패');
                });
        },
        toggleLikeBtn() {
            var boardid = this.data.boardid;
            Axios.post(`${URI}/page/favorite`, { jwt: this.jwt, boardid: boardid })
                .then(res => {
                    this.showAll();
                })
                .catch(res => {
                    console.log('좋아요 버튼 에러');
                });
        },
        toggleScrapBtn() {
            var boardid = this.data.boardid;
            Axios.post(`${URI}/page/scrap`, { jwt: this.jwt, boardid: boardid })
                .then(res => {
                    this.showAll();
                })
                .catch(res => {
                    console.log('스크랩 버튼 에러');
                });
        },
        toggleFollow() {
            Axios.post(`${URI}/account/follow`, { jwt: this.jwt, uid: this.data.uid })
                .then(res => {
                    if (this.follow) {
                        alert('팔로우 되었습니다.');
                    } else {
                        alert('팔로우가 취소 되었습니다.');
                    }
                    this.showAll();
                })
                .catch(res => {
                    console.log('팔로우 등록 및 취소 실패');
                });
        },
        addComment() {
            // console.log(this.comment);
            var commentObject = new Object();
            commentObject.jwt = this.jwt;
            commentObject.boardid = this.data.boardid;
            commentObject.contents = this.comment;
            commentObject.uid = this.data.uid;
            if (this.comment == '') {
                alert('댓글을 입력해주세요');
            } else {
                Axios.post(`${URI}/page/comment`, commentObject)
                    .then(res => {
                        // console.log('댓글 달기 성공');
                        this.comment = '';
                        this.showAll();
                    })
                    .catch(res => {
                        console.log('댓글 달기 실패');
                    });
                this.getAlldata();
            }
        },
        deleteComment(comment) {
            if (confirm('댓글을 삭제하시겠습니까?')) {
                Axios.delete(`${URI}/page/comment`, {
                    data: comment.commentid,
                })
                    .then(res => {
                        // console.log('댓글 삭제 성공');
                        this.showAll();
                    })
                    .catch(res => {
                        console.log('댓글 삭제 실패');
                    });
                this.getAlldata();
            }
        },
        search(keyword) {
            // console.log(keyword);
            this.$router.push({ name: 'Search', params: { keyword: keyword } });
        },
    },
};
</script>

<style></style>
