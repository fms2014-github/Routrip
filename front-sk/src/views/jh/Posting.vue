<template>
    <div class="posting-component">
        <div class="postings-posting">
            <div class="post-info">
                <div class="profile-img">
                    <img src="../../assets/images/user.png" alt />
                </div>
                <div class="name-time">
                    <strong>박정호</strong>
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
import { Hooper, Slide, Pagination as HooperPagination } from 'hooper';
import '../../assets/css/main/posting.scss';
export default {
    components: {
        Hooper,
        Slide,
        HooperPagination,
    },
    data: () => {
        return {
            comment: '',
            data: [
                '<div class="example-slide">Slide 1</div>',
                '<div class="example-slide">Slide 2</div>',
                '<div class="example-slide">Slide 3</div>',
            ],
        };
    },
    methods: {
        resize() {
            console.log(this.comment);
        },
    },
};
</script>
