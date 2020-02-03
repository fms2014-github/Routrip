<template>
    <div id="error">
        <h1>통신 중 에러가 발생했습니다.</h1>
        <div class="history-back" @click="historyBack">이전 페이지로</div>
    </div>
</template>

<script>
import '../../assets/css/error.scss';
export default {
    methods: {
        historyBack() {
            this.$router.back();
        },
    },
};
</script>
