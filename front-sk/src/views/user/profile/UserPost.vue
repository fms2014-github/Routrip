<template>
    <div class="post">
        <h1>안녕 여긴 게시글</h1>

        <button @click="plus">리스트에 추가</button>

        <div v-for="(dum, index) in dummy" :key="index" class="content">
            <h2>{{ dum.title }}</h2>
            <p>{{ dum.content }}</p>
        </div>
    </div>
</template>

<script>
import UserApi from '../../../apis/UserApi';

export default {
    data() {
        return {
            dummy: [
                {
                    id: 1,
                    title: '제목',
                    content: '내용',
                },
                {
                    id: 2,
                    title: '제목1',
                    content: '내용1',
                },
            ],
            post: null,
        };
    },
    created() {
        this.fetchData();
    },
    watch: {
        $route: 'fetchData',
    },
    methods: {
        fetchData() {
            this.error = this.post = null;
            this.loading = true;
        },
        plus() {
            const data = {
                id: 3,
                title: '제목 new',
                content: '내용 new',
            };
            this.dummy.push(data);
        },
    },
};
</script>
