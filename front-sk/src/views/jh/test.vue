<template>
    <hooper>
        <slide>slide 1</slide>
        <slide>slide 2</slide>
        <slide>slide 3</slide>
        <slide>slide 4</slide>
        <slide>slide 5</slide>
        <slide>slide 6</slide>

        <hooper-pagination slot="hooper-addons"></hooper-pagination>
    </hooper>
</template>

<script>
import { Hooper, Slide, Pagination as HooperPagination } from 'hooper';

export default {
    components: {
        Hooper,
        Slide,
        HooperPagination,
    },
};
</script>