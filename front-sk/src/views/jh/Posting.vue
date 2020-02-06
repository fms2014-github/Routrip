<template>
    <div class="posting-component">
        <div class="postings-posting">
            <div class="post-info">
                <div class="profile-img">
                    <img src="../../assets/images/user.png" alt />
                </div>
                <div class="name-time">
                    <strong>{{ nickname }}</strong>
                    <br />
                    <span>로마, 이탈리아</span>
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
                <slide>
                    <img src="../../assets/images/trip_img/trip6.jpg" alt />
                </slide>
                <slide>
                    <img src="../../assets/images/trip_img/trip7.jpg" alt />
                </slide>
                <slide>
                    <img src="../../assets/images/trip_img/trip8.jpg" alt />
                </slide>
                <slide>
                    <img src="../../assets/images/trip_img/trip6.jpg" alt />
                </slide>
                <slide>
                    <img src="../../assets/images/trip_img/trip7.jpg" alt />
                </slide>
                <slide>
                    <img src="../../assets/images/trip_img/trip8.jpg" alt />
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
        </div>
        <div class="text">
            <span>
                너무너무 즐거웠어요를레이히~~~~~요를레이히~~~~ 요를레이히~~~~~요를레이히~~~~요를레이히~~~~~요를레이히~~~~
                요를레이히~~~~~요를레이히~~~~요를레이히~~~~~요를레이히~~~~ 요를레이히~~~~~요를레이히~~~~요를레이히~~~~~요를레이히~~~~
                요를레이히~~~~~요를레이히~~~~요를레이히~~~~~요를레이히~~~~ 요를레이히~~~~~요를레이히~~~~요를레이히~~~~~요를레이히~~~~
                요를레이히~~~~~요를레이히~~~~요를레이히~~~~~요를레이히~~~~ 요를레이히~~~~~요를레이히~~~~요를레이히~~~~~요를레이히~~~~
                요를레이히~~~~~요를레이히~~~~요를레이히~~~~~요를레이히~~~~ 요를레이히~~~~~요를레이히~~~~요를레이히~~~~~요를레이히~~~~
                요를레이히~~~~~요를레이히~~~~요를레이히~~~~~요를레이히~~~~
            </span>
        </div>

        <div class="comment-box">
            <form action class="comment-form">
                <textarea
                    class="comment"
                    v-model="comment"
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
</template>

<script>
//scss import
import { Hooper, Slide, Pagination as HooperPagination } from 'hooper';
import '../../assets/css/main/posting.scss';
import Axios from 'axios';

export default {
    components: {
        Hooper,
        Slide,
        HooperPagination,
    },
    data: () => {
        return {
            comment: '',
            title: '',
            title: '',
            writedate: '',
            tripterm: '',
            keyword: '',
            latitude: '',
            longitude: '',
            level: '',
            comments: '',
            imgs: '',
            favoriteNum: '',
            commentNum: '',
            markers: '',
            nickname: '',
        };
    },
    created: function() {
        // using JSONPlaceholder
        const URI = 'http://192.168.100.70:8083/';
        Axios.get(`${URI}/page/boardList`)
            .then(res => {
                console.log(res.data);

                var length = Object.keys(res.data).length;
                for (var i = 0; i < length; ++i) {
                    var data = res.data[i];
                    this.title = data.title;
                    this.writedate = data.writedate;
                    this.tripterm = data.tripterm;
                    this.keyword = data.keyword;
                    this.latitude = data.latitude;
                    this.longitude = data.longitude;
                    this.level = data.level;
                    this.comments = data.comments;
                    this.imgs = data.imgs;
                    this.favoriteNum = data.favoriteNum;
                    this.commentNum = data.commentNum;
                    this.markers = data.markers;
                    this.nickname = data.nickname;
                    console.log(this.title);
                    console.log(this.nickname);
                }
            })
            .catch(res => {
                console.log(res);
            });
    },
    methods: {
        resize() {
            console.log(this.comment);
        },
    },
};
</script>
