<template>
    
    <button @click="handleModal" v-if="userPicture">
        <div class="user-picture"> <img :src="pic" > </div>
    </button>

</template>

<script>
import Swal from 'sweetalert2';
import $ from 'jquery';
import Axios from 'axios';


var apiUrl='https://api.imgur.com/3/image';
var apiKey = 'ddbe62505149f6d';
var settings={
    processData:false,
    async:false,
    crossDomain: true,
    contentType: false,
    type:'POST',
    url:apiUrl,
    headers:{
        Authorization: 'Client-ID ' + apiKey,
        Accept: 'application/json'
    },
    mimeType: 'multipart/form-data'
}


export default {
    name: 'userpicture',
    props: ['userPicture','pic'],
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
            if (file) {
                
                var formData=new FormData();
                formData.append("image", file);
                settings.data=formData

                console.log('ready')
                $.ajax(settings).done(response => {
                    this.pic = JSON.parse(response).data.link
                    const jwt = localStorage.getItem('routrip_JWT');
                    Axios.put('http://192.168.100.70:8083/account/user/',
                        {
                            profileImg : JSON.parse(response).data.link,
                            jwt : jwt
                        }
                    )
                    .then(res => {
                        console.log(res.data,"안녕나는jwt");
                        localStorage.setItem('routrip_JWT', res.data);
                        
                    });

                });

                const reader = new FileReader()
                reader.onload = (e) => {
                    Swal.fire({
                        title: '성공적으로 변경되었습니다!',
                        imageUrl: e.target.result,
                        icon:'success',
                        imageAlt: 'The uploaded picture'
                    })
                }
                reader.readAsDataURL(file)
            }
        },
    },
    data() {
        return {

        };
    },
};
</script>
