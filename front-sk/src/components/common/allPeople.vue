<template>
    <div>
        {{picture}}{{nickName}} 
    </div>
</template>

<script>
// import common from "../../assets/css/common.scss"


export default {
    props:{
        picture:String,
        nickName:String,
    }
}
</script>