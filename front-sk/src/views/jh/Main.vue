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
import '../../assets/css/main/main.scss';
export default {
    components: {
        Header,
        Footer,
        Posting,
        BestPosting,
    },
};
</script>

<style></style>
