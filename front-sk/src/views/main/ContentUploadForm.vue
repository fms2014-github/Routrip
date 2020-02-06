<template>
    <div id="content-upload-form">
        <InputForm id="content-title" :enterInput="test1" :errorText="error.title" placeholder="제목을 입력해 주세요." label="제목" />
        <div class="select-form">
            <span class="select-form-title">장소</span>
            <div class="selected">
                <div class="hashtag-form" v-for="area in areas" :key="area">{{ area }}<button class="hashtag-cansle">×</button></div>
            </div>
        </div>
        <div class="search">
            <input class="search-bar" type="text" />
            <div class="search-result-dropdown">
                <div class="dropdown-list" v-for="resultArea in resultAreas" :key="resultArea">{{ resultArea }}</div>
            </div>
        </div>
        <div class="select-form">
            <span class="select-form-title">키워드</span>
            <div class="selected">
                <div class="hashtag-form" v-for="keyword in keywords" :key="keyword">{{ keyword }}<button class="hashtag-cansle">×</button></div>
            </div>
        </div>
        <div class="search">
            <input class="search-bar" type="text" />
        </div>
        <div id="map-wrapper">
            <div id="map"></div>
            <div id="insert-comment-wrap">
                <span id="insert-comment-wrap-title">설명 넣기</span>
                <label for="comment-title-insert">제목 <input id="comment-title-insert" type="text" v-model="commentTitle"/></label>
                <label for="comment-content-insert">내용<textarea id="comment-content-insert" type="text" v-model="commentContent"/></label>
                <button id="comment-submit" @click="insertComment">주석 생성</button>
            </div>

            <p class="edit">
                <button id="undo" class="disabled" onclick="undo()" disabled>실행취소</button>
                <button id="redo" class="disabled" onclick="redo()" disabled>원래대로</button>
            </p>
            <br />
            <button onclick="extractionImg()">이미지 생성</button>
        </div>
        <div id="map-image"></div>
        <div id="image-upload">
            <label id="upload-button" for="image-file">사진 업로드</label>
            <input id="image-file" type="file" multiple @change="imageFiles" />
            <div id="file-list">
                <div class="upload-image-preview" v-for="(image, idx) in imageArr" :key="image.id">
                    <img class="image-size" :src="imageArrBase64[idx]" />
                    <button class="deleteImg" @click="deleteImg(image, idx)">×</button>
                </div>
            </div>
        </div>
        <textarea id="text-area"></textarea>
        <div id="calendar-wrapper">
            <label for="calendar-nights"><input id="calendar-nights" name="calendar-nights" class="calendar" type="number" /><span>박</span></label>
            <label for="calendar-days"><input id="calendar-days" name="calendar-days" class="calendar" type="number" /><span>일</span></label>
        </div>
        <button @click="submit">제출</button>
        <button @click="getMapData">test</button>
    </div>
</template>

<script>
import '../../assets/css/contentUpload.scss';
import InputForm from '../../components/common/Input';

export default {
    components: {
        InputForm,
    },
    data() {
        return {
            areas: ['장소 #1', '장소 #2', '장소 #3', '장소 #4', '장소 #5', '장소 #6'],
            keywords: ['키워드 #1', '키워드 #2', '키워드 #3', '키워드 #4', '키워드 #5'],
            resultAreas: ['결과 #1', '결과 #2', '결과 #3', '결과 #4', '결과 #5'],
            imageArr: [],
            imageArrBase64: [],
            commentTitle: '',
            commentContent: '',
            error: {
                title: '',
            },
        };
    },
    methods: {
        test1($event, type) {
            console.log($event);
        },
        submit() {},
        imageFiles(e) {
            var fileList = document.querySelector('#file-list');
            var get_file = e.target.files;
            for (var i = 0; i < get_file.length; i++) {
                this.imageArr.push(get_file[i]);
            }

            // for (var i = 0; i < get_file.length; i++) {
            //     var reader = new FileReader();

            //     reader.onload = (function(imgab) {
            //         return function(e) {
            //             imgab.push(e.target.result);
            //         };
            //     })(this.imageArrBase64);
            //     if (get_file) {
            //         reader.readAsDataURL(get_file[i]);
            //     }
            // }
        },
        deleteImg(id, idx) {
            this.imageArr.splice(idx, 1);
            this.imageArrBase64.splice(idx, 1);
        },
        getMapData() {
            console.log(getOverlays());
        },
        insertComment() {
            insertComment(this.commentTitle, this.commentContent);
            console.log('주석 달기 성공');
            this.commentTitle = this.commentContent = '';
        },
    },
};
</script>
