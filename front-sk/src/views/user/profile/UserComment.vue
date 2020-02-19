<template>
   <div>
        <div style="text-align:center; font-size:2.1vw; @include wrap; margin-bottom:7vw;">
            총 {{ commentList.length }}개의 글에 댓글을 남겼습니다. 
        </div>
        <table style="width:100%;">
            <thead>
                <th>
                    <td style="width:5vw;">번호</td><td style="width:58vw;">내용</td><td style="width:10vw; text-align:center; padding-right:10vw;">날짜</td><td></td>
                </th>
            </thead>
            <tbody v-for="(comment,index) in commentList" v-bind:key="comment.boardid">
                <router-link :to="{ name: 'Detail', params: { boardid: comment.boardid } }">        
                    <tr>
                        <td style="width:5vw;">{{ doMath(index) }}</td><td style="width:58vw;">{{ comment.contents }}</td>
                        <td style="padding-right:7vw;">{{ comment.writedate.split(':')[0] + ":" + comment.writedate.split(':')[1] }}</td>
                        
                    </tr>
                </router-link>
                    <div style="float:right; margin:3px 2.2vw 0 0;">
                        <button class="del" @click="delComment(id=comment.commentid,index)">댓글 삭제</button>
                    </div>
            </tbody>
        </table>
    </div>
</template>

<script>
import likes from "../../../assets/css/likes.scss"

import Axios from 'axios'
import Swal from 'sweetalert2'


export default {
     mounted(){
        this.reqComments();
    },
    methods: {
        reqComments() {
        console.log("hihi comment")
        const jwt = localStorage.getItem('routrip_JWT');
        
        Axios.post('http://192.168.100.70:8083/page/searchComment' , {jwt : jwt})
            .then(res => {
                console.log(res)
                this.commentList=res.data
            });
        },
        doMath(index){
            return index+1
        },
         delComment(id,index){
            const swalWithBootstrapButtons = Swal.mixin({
            customClass: {
                confirmButton: 'btn btn-success',
                cancelButton: 'btn btn-danger'
            },
            buttonsStyling: false
            })

            Swal.fire({
            title: '댓글을 삭제할까요?',
            text: "삭제된 댓글은 복구되지 않습니다.",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: '예',
            cancelButtonText: '아니오',
            }).then((result) => {
            if (result.value) {
                console.log(id)
                Axios.delete('http://192.168.100.70:8083/page/comment', 
                { data: id })
                    .then(res=>{
                        Swal.fire({
                        title:'삭제되었습니다.',
                        icon:'success'
                    }).then( (dismiss)=>{
                        if (dismiss){
                            this.reqComments();
                                }
                            }
                        )
                    })
                }
            })
        }
    },
  
    data(){
        return{
            commentList:[]
        };
    }

}


</script>
