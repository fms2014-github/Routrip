<template>
    <div class="postings-posting">
        <hooper :infiniteScroll="true" :itemsToShow="3" :progress="true" :autoPlay="true" :playSpeed="2000">
            <slide>
                <img src="../../assets/images/trip_img/trip1.jpg" />
            </slide>
            <slide>
                <img src="../../assets/images/trip_img/trip2.jpg" />
            </slide>
            <slide>
                <img src="../../assets/images/trip_img/trip3.jpg" />
            </slide>
            <slide>
                <img src="../../assets/images/trip_img/trip4.jpg" />
            </slide>
            <slide>
                <img src="../../assets/images/trip_img/trip5.jpg" />
            </slide>
            <slide>
                <img src="../../assets/images/trip_img/trip6.jpg" />
            </slide>
            <slide>
                <img src="../../assets/images/trip_img/trip7.jpg" />
            </slide>
            <slide>
                <img src="../../assets/images/trip_img/trip8.jpg" />
            </slide>
            <hooper-navigation slot="hooper-addons"></hooper-navigation>
        </hooper>
    </div>
</template>

<script>
import '../../assets/css/main/bestPosting.scss';
import { Hooper, Slide, Navigation as HooperNavigation } from 'hooper';

export default {
    components: {
        Hooper,
        Slide,
        HooperNavigation,
    },
};
</script>

<style></style>
