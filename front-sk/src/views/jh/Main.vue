<template>
    <div id="main">
        <Header></Header>
        <hr />
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
};
</script>

<style></style>
