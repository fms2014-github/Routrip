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
            <div class="post-imgs">
                <img src="../../assets/images/trip_img/trip7.jpg" alt />
            </div>
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
                너무너무 즐거웠어요를레이히~~~~~요를레이히~~~~
                요를레이히~~~~~요를레이히~~~~요를레이히~~~~~요를레이히~~~~
                요를레이히~~~~~요를레이히~~~~요를레이히~~~~~요를레이히~~~~
                요를레이히~~~~~요를레이히~~~~요를레이히~~~~~요를레이히~~~~
                요를레이히~~~~~요를레이히~~~~요를레이히~~~~~요를레이히~~~~
                요를레이히~~~~~요를레이히~~~~요를레이히~~~~~요를레이히~~~~
                요를레이히~~~~~요를레이히~~~~요를레이히~~~~~요를레이히~~~~
                요를레이히~~~~~요를레이히~~~~요를레이히~~~~~요를레이히~~~~
                요를레이히~~~~~요를레이히~~~~요를레이히~~~~~요를레이히~~~~
                요를레이히~~~~~요를레이히~~~~요를레이히~~~~~요를레이히~~~~
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
export default {
    data: () => {
        return {
            comment: '',
        };
    },
    methods: {
        resize() {
            console.log(this.comment);
        },
    },
};
</script>

<style>
</style>