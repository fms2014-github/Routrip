<template>
    <div id="header">
        <div class="hambuger">
            <i class="fas fa-bars"></i>
        </div>
        <div class="home">
            <img class="logo" src="../../assets/images/routrip_logo.png" />
            <span class="title">루트립</span>
        </div>
        <div class="search-ipad" :class="{showSearch : !showSearch}">
            <form action class="search-form">
                <input type="text" placeholder="검색..." />
                <button>검색</button>
            </form>
        </div>
        <div class="menu">
            <span class="menu-icon">
                <i class="far fa-plus-square"></i>
            </span>
            <span class="menu-icon">
                <i class="far fa-comment-alt"></i>
            </span>
            <a href="#">
                <img class="profile-img" src="../../assets/images/user.png" />
            </a>
        </div>
    </div>
</template>

<script>
import '../../assets/css/main/header.scss';
export default {
    created() {
        window.addEventListener('scroll', this.scrollY);
    },
    destroyed() {
        window.removeEventListener('scroll', this.scrollY);
    },
    methods: {
        scrollY(event) {
            console.log(window.scrollY);
            if (window.scrollY > 40) {
                this.showSearch = true;
            } else {
                this.showSearch = false;
            }
        },
    },
    data: () => {
        return {
            showSearch: false,
        };
    },
};
</script>
