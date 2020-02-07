<template>
    <div class="header-default">
        <button class="menu" v-if="!isBack"></button>

        <button class="menu" v-if="profileIcon"></button>

        <button class="mail" v-if="mailIcon"></button>

        <button v-if="isBack" class="back">
            <i class="fas fa-chevron-left"></i>
        </button>

        <h4 class="title">
            {{ headerTitle }}
        </h4>

        <button v-if="rightText" class="right-text" :class="{ disabled: isDisabled }" :disabled="isDisabled" @click="changeNick">
            {{ rightText }}
        </button>

        <button class="btn-search" v-if="isSearch">
            <i class="fas fa-search"></i>
        </button>
    </div>
</template>

<script>
export default {
    name: 'header',
    props: ['profileIcon', 'mailIcon', 'headerTitle', 'isBack', 'isSearch', 'rightText', 'isDisabled','changeNick'],
    mounted() {
        console.log(this.headerTitle);
    },
    methods: {
       
    }
};
</script>
