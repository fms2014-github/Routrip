<template>
    <div>
        {{ title }} 글에 좋아요를 눌렀습니다.

    </div>
</template>

<script>
import common from "../../assets/css/common.scss"


export default {
    props: {
        title: String,
        
    }
}
</script>