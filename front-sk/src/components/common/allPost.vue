<template>
    <div class="gallery">
         <div class = 'card-header'>
            <h1 class = 'card-header-title'>{{title}}</h1>       
        </div>
        <div class= 'card'>
            <img :src="picture | getUrl" class = 'card-image'>
            
            <div class ='card-info'>
                <ul>
                    <li class="card-likes"><span class="visually-hidden">좋아요 : </span><i class="fas fa-heart" aria-hidden="true"></i> 56</li>
                    <li class="card-comments"><span class="visually-hidden">댓글 수 : </span><i class="fas fa-comment" aria-hidden="true"></i> 2</li>
                </ul>
            </div> 	
        </div>
        <div class = 'card-footer'>
            {{keywords}}
        </div>
    </div>
</template>

<script>
import myPost from "../../assets/css/myPost.scss"


export default {
    props: {
        title: String,
        picture: {
            type: String,
            default: "trip1.jpg"
        },
        keywords:String,
    },
    filters: {
        getUrl(url) {
            console.log(url);
            return require(`../../assets/images/trip_img/${url}`);
        }
    }
}   
</script>
