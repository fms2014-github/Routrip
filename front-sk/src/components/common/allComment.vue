<template>
    <div>
        {{title}} 글에 {{comment}} 을 남겼습니다.
    </div>
</template>

<script>
import common from "../../assets/css/common.scss"


export default {
    props: {
        title: String,
        comment:String,
    }
}
</script>