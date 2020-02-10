<template>
    <img :src="img" />
</template>

<script>
export default {
    data() {
        return {
            img: this.$route.query.url,
        };
    },
};
</script>
