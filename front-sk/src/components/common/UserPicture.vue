

<template>
    
    <button @click="handleModal" v-if="userPicture">
        <div> <img src="../../assets/images/user.png" style="float:left; width:150px; height:150px; margin:10px; margin-bottom:80px;"> </div>
    </button>

</template>

<script>
import Swal from 'sweetalert2';

export default {
    name: 'userpicture',
    props: ['userPicture'],
    methods: {
        async handleModal() {
            const { value: file } = await Swal.fire({
                title: '프로필 이미지 변경',
                input: 'file',
                showCancelButton: true,
                inputAttributes: {
                    accept: 'image/*',
                    'aria-label': 'Upload your profile picture'
                }   
            })
            console.log(file)
            if (file) {
                const reader = new FileReader()
                reader.onload = (e) => {
                    Swal.fire({
                        title: '성공적으로 변경되었습니다!',
                        imageUrl: e.target.result,
                        icon:'success',
                        imageAlt: 'The uploaded picture'
                    })
                    // console.log(imageUrl)
                }
                // watch로 imag 값 변경될때마다 함수 실행
                // 유저 image 컬럼 업데이트, css상의 이미지 변경
                reader.readAsDataURL(file)
            }
        },
    },
    data() {
        return {
            // userPic:유저한테서 받아온 이미지파일
        };
    },
    watch:{
            
    },
};
</script>
