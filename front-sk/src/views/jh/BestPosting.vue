<template>
    <hooper :infiniteScroll="true">
        <slide>
            <img src="../../assets/images/trip_img/trip6.jpg" />
        </slide>
        <slide>
            <img src="../../assets/images/trip_img/trip7.jpg" />
        </slide>
        <slide>
            <img src="../../assets/images/trip_img/trip8.jpg" />
        </slide>
        <slide>
            <img src="../../assets/images/trip_img/trip6.jpg" />
        </slide>
        <slide>
            <img src="../../assets/images/trip_img/trip7.jpg" />
        </slide>
        <slide>
            <img src="../../assets/images/trip_img/trip5.jpg" />
        </slide>
    </hooper>
</template>

<script>
import '../../assets/css/main/bestPosting.scss';
import { Hooper, Slide } from 'hooper';

export default {
    components: {
        Hooper,
        Slide,
    },
};
</script>

<style></style>
