
<template>
    
    <button class="user-picture" @click="handleModal" v-if="userPicture">
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
                }
                // watch로 imag 값 변경될때마다 함수 실행
                // 유저 image 컬럼 업데이트, css상의 이미지 변경
                reader.readAsDataURL(file)
            }
        },
    },
};
</script>
