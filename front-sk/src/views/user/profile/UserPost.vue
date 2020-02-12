<template>
    <div class="row">
        <allPost title="제주도 3박4일" keywords="#저가여행 #맛집위주" picture="trip1.jpg"> </allPost>

        <allPost title="강릉 2박3일" keywords="#자연경관 #커플여행" picture="trip2.jpg"> </allPost>

        <allPost title="제주도 3박4일" keywords="#저가여행 #맛집위주" picture="trip5.jpg"> </allPost>

        <allPost title="제주도 3박4일" keywords="#저가여행 #맛집위주"> </allPost>

        <allPost title="제주도 3박4일" keywords="#저가여행 #맛집위주"> </allPost>

        <allPost title="제주도 3박4일" keywords="#저가여행 #맛집위주"> </allPost>

        <allPost title="으아아"> </allPost>

        {{ axios }}
    </div>
</template>

<script>
import allPost from '../../../components/common/allPost';

export default {
    components: {
        allPost,
    },
    methods: {},
};
</script>
