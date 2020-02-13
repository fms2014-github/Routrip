<template>
    <div class="header-default">
        <button class="menu" v-if="!isBack"></button>

        <button class="menu" v-if="profileIcon"></button>

        <button class="mail" v-if="mailIcon"></button>


        <h4 class="title">
            {{ headerTitle }}
        </h4>

        <button v-if="rightText" class="right-text" :class="{ disabled: isDisabled }" :disabled="isDisabled" @click="test">
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
    props: ['profileIcon', 'mailIcon', 'headerTitle' , 'rightText', 'isDisabled','changeNick'],
    mounted() {
        console.log(this.headerTitle);
    },
    methods: {
       test() {
           
           this.$emit('changeNick');
       }
    }
};
</script>
