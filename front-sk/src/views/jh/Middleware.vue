<template>
    <div></div>
</template>

<script>
export default {
    created: function() {
        var searchWord = this.$route.params.searchWord;
        this.$router.push({ name: 'SearchAll', params: { searchWord: searchWord } });
    },
};
</script>