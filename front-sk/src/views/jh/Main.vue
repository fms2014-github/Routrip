<template>
    <div id="main">
        <Header></Header>
        <hr />
        <div class="search-desktop">
            <form action class="search-form">
                <input type="text" placeholder="검색..." />
                <button>검색</button>
            </form>
        </div>
        <div class="best-posting">
            <BestPosting></BestPosting>
        </div>
        <div class="posting-box">
            <div class="postings">
                <Posting></Posting>
                <Posting></Posting>
                <Posting></Posting>
                <Posting></Posting>
                <Posting></Posting>
                <Posting></Posting>
            </div>
        </div>

        <Footer></Footer>
    </div>
</template>

<script>
import Header from './Header.vue';
import Footer from './Footer.vue';
import Posting from './Posting';
import BestPosting from './BestPosting.vue';
import '../../assets/css/main/main.scss';
export default {
    components: {
        Header,
        Footer,
        Posting,
        BestPosting,
    },
    data: () => {
        return {
            scrollIndex: 0,
        };
    },
};
</script>

<style></style>
