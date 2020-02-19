<template>
    <div id="content-upload-form">
        <Header></Header>
        <div id="map-wrapper">
            <div id="map"></div>
            <div id="edit">
                <button class="draw-tools" @click="selectDrawTools('MARKER', $event)">마커</button>
                <button class="draw-tools" @click="selectDrawTools('POLYLINE', $event)">선</button>
                <button class="draw-tools" @click="selectDrawTools('ARROW', $event)">화살표선</button>
                <button class="draw-tools" @click="selectDrawTools('RECTANGLE', $event)">사각형</button>
                <button class="draw-tools" @click="selectDrawTools('CIRCLE', $event)">원</button>
                <button class="draw-tools" @click="selectDrawTools('ELLIPSE', $event)">타원</button>
                <button class="draw-tools" @click="selectDrawTools('POLYGON', $event)">다각형</button>
                <div id="work">
                    <button id="undo" class="draw-tools disabled" @click="undo()" disabled>실행취소</button>
                    <button id="redo" class="draw-tools disabled" @click="redo()" disabled>원래대로</button>
                </div>
                <div id="drop-down-wrap" class="drop-up">
                    <div id="insert-comment-wrap">
                        <span id="insert-comment-wrap-title">설명 넣기</span>
                        <label for="comment-title-insert">
                            제목
                            <input
                                id="comment-title-insert"
                                type="text"
                                v-model="commentTitle"
                                maxlength="24"
                                placeholder="24자 까지 작성 가능합니다."
                            />
                        </label>
                        <label for="comment-content-insert">
                            내용
                            <textarea
                                id="comment-content-insert"
                                type="text"
                                v-model="commentContent"
                                maxlength="80"
                                placeholder="70자 까지 작성 가능합니다."
                            />
                        </label>
                        <button id="comment-submit" @click="createDraw">생성</button>
                    </div>
                    <div id="create-condition">
                        <span>생성 조건</span>
                        <label for="both">
                            <input
                                v-model="createCondition"
                                id="both"
                                name="condition"
                                type="radio"
                                value="both"
                            />둘 다 생성
                        </label>
                        <label for="only-comment">
                            <input
                                v-model="createCondition"
                                id="only-comment"
                                name="condition"
                                type="radio"
                                value="comment"
                            />주석만 생성
                        </label>
                        <label for="only-drawTool">
                            <input
                                v-model="createCondition"
                                id="only-drawTool"
                                name="condition"
                                type="radio"
                                value="drawtool"
                            />그리기 도구만
                            생성
                        </label>
                    </div>
                </div>
            </div>
            <div id="menu-wrap" class="close-menu">
                <div class="option">
                    <div id="menu-search-area" class="hide">
                        <label>
                            키워드 :
                            <input v-model="placeSearch" type="text" id="keyword" size="15" />
                        </label>
                        <button @click="onPlaceSearch">검색하기</button>
                        <span>(지도를 클릭하면 줄어듭니다.)</span>
                    </div>
                    <div id="hide-info" @click="menuToggle">검색 화면 펼치기</div>
                </div>
                <hr />
                <ul id="placesList"></ul>
                <div id="pagination"></div>
            </div>
        </div>
        <div id="content-wrapper">
            <div class="input-with-label">
                <input v-model="title" placeholder="제목을 입력하세요" type="text" />
                <label for="inputValue">제목</label>
            </div>
            <div class="select-form">
                <span class="select-form-title">키워드</span>
                <div class="select-tag">
                    <div class="hashtag-form" v-for="(keyword, index) in keywords" :key="index.id">
                        {{ keyword }}
                        <button class="hashtag-cansle" @click="removeHash(index)">×</button>
                    </div>
                </div>
            </div>
            <div class="search">
                <input
                    v-model="keywordTag"
                    class="search-bar"
                    type="text"
                    @keyup.enter="createHash()"
                />
            </div>
            <div id="write-content">
                <div id="summernote"></div>
            </div>
            <div id="inserted-image-list">
                <span>
                    삽입한 이미지
                    <span v-if="updataPost">(대표 이미지만 수정 가능 합니다.)</span>
                </span>
                <hr />
                <div class="image-list-wrap" v-for="(image, index) in imageArr" :key="index.id">
                    <img
                        class="inserted-image"
                        @click="setRepresentative(image)"
                        :src="image"
                        :class="{representative: getRepresentative(image)}"
                    />
                    <button
                        @click="deleteImage(image)"
                        class="delete-image"
                        :class="{ 'representative-button': getRepresentative(image)}"
                    >×</button>
                    <span v-if="getRepresentative(image)" class="representative-image">대표이미지</span>
                </div>
            </div>
            <div id="calendar-wrapper">
                <span>여행 기간</span>
                <label for="calendar-nights">
                    <input
                        id="calendar-nights"
                        name="calendar-nights"
                        class="calendar"
                        type="number"
                        v-model="night"
                    />
                    <span>박</span>
                </label>
                <label for="calendar-days">
                    <input
                        id="calendar-days"
                        name="calendar-days"
                        class="calendar"
                        type="number"
                        v-model="day"
                    />
                    <span>일</span>
                </label>
            </div>
            <button id="content-submit" @click="submit">제출</button>
        </div>
    </div>
</template>

<script>
import Header from '../jh/Header.vue';
import '../../assets/css/WriteForm.scss';
import ImageUpload from '../../apis/ImgurAPI.js';
import kakaoMap from '../../apis/kakaoMapAPI.js';
import Axios from 'axios';
const URI = 'http://192.168.100.70:8083/';
var asdf;
export default {
    components: {
        Header,
    },
    created() {
        this.jwt = localStorage.getItem('routrip_JWT');
        if (this.$route.params.boardid !== undefined) {
            this.boardid = this.$route.params.boardid;
            this.updataPost = true;
            Axios.post(`${URI}/page/boardDetail`, {
                jwt: this.jwt,
                boardid: this.boardid,
            }).then(res => {
                this.title = res.data.title;
                this.keywords = res.data.keywords;
                this.content = res.data.content;
                this.night = res.data.tripterm.split(' ')[0];
                this.day = res.data.tripterm.split(' ')[1];
                for (let i in res.data.imgs) {
                    this.imageArr.push(res.data.imgs[i].src);
                    this.representativeImage.push(res.data.imgs[i].src);
                }
                console.log('update', res.data);
                kakaoMap.getMpaData(res.data, this.updataPost);
            });
        }
        console.log(this.boardid);
    },
    mounted() {
        //CKEditor.createCKEditor();
        kakaoMap.createMap();
        var imageArr = this.imageArr;
        var representativeImage = this.representativeImage;
        // eslint-disable-next-line no-undef
        $(() => {
            // eslint-disable-next-line no-undef
            $('#summernote').summernote({
                height: 400, // set editor height
                callbacks: {
                    onImageUpload: function(files) {
                        // upload image to server and create imgNode...
                        console.log('file :', files);
                        for (var i = 0; i < files.length; i++) {
                            if (files) {
                                ImageUpload.uploadImage(
                                    files[i],
                                    res => {
                                        console.log(res.data.data.link);
                                        imageArr.push(res.data.data.link);
                                        representativeImage.push(res.data.data.link);
                                        if (representativeImage.length > 10) {
                                            const idx = representativeImage.indexOf(res.data.data.link);
                                            if (idx > -1) representativeImage.splice(idx, 1);
                                        }
                                        // eslint-disable-next-line no-undef
                                        $('#summernote').summernote('insertImage', res.data.data.link, res.data.data.id);
                                    },
                                    error => {
                                        console.log(error);
                                    },
                                );
                            }
                        }
                    },
                },
            });
            setTimeout(() => {
                let clear = document.getElementsByClassName('note-editable')[0].querySelector('p');
                clear.parentNode.removeChild(clear);
                // eslint-disable-next-line no-undef
                $('#summernote').summernote('pasteHTML', this.content);
            }, 1000);
        });
    },
    data() {
        return {
            updataPost: false,
            boardid: 0,
            jwt: '',
            title: '',
            keywordTag: '',
            selectDraw: '',
            imageArr: [],
            representativeImage: [],
            placeSearch: '이태원 맛집',
            keywords: [],
            resultAreas: [],
            commentTitle: '',
            commentContent: '',
            content: '',
            error: {
                title: '',
            },
            night: '',
            day: '',
            createCondition: 'both',
        };
    },
    watch: {},
    methods: {
        setRepresentative(image) {
            let idx = this.representativeImage.indexOf(image);
            let len = this.representativeImage.length;
            if (idx > -1) {
                this.representativeImage.splice(idx, 1);
            } else if (len === 10) {
                this.representativeImage.splice(len - 1, 1);
                this.representativeImage.push(image);
            } else {
                this.representativeImage.push(image);
            }
        },
        getRepresentative(image) {
            let idx = this.representativeImage.indexOf(image);
            if (idx > -1) {
                return true;
            } else {
                return false;
            }
        },
        menuToggle() {
            document.getElementById('menu-search-area').classList.remove('hide');
            document.getElementById('hide-info').classList.add('hide');
            document.getElementById('menu-wrap').classList.remove('close-menu');
            kakaoMap.reloadPlace();
        },
        onPlaceSearch() {
            kakaoMap.searchPlace(this.placeSearch);
        },
        createDraw() {
            kakaoMap.selectOverlay(this.createCondition, this.selectDraw, this.commentTitle, this.commentContent);
            this.commentTitle = this.commentContent = '';
        },
        selectDrawTools(s, e) {
            var len = document.getElementsByClassName('draw-tools').length;
            if (e.target && !e.target.classList.contains('selected')) {
                for (var i = 0; i < len; i++) {
                    document.getElementsByClassName('draw-tools')[i].classList.remove('selected');
                }
                e.target.classList.add('selected');
                document.getElementById('drop-down-wrap').classList.add('drop-down');
                document.getElementById('drop-down-wrap').classList.remove('drop-up');
            } else if (e.target && e.target.classList.contains('selected')) {
                e.target.classList.remove('selected');
                document.getElementById('drop-down-wrap').classList.remove('drop-down');
                document.getElementById('drop-down-wrap').classList.add('drop-up');
            }
            this.selectDraw = s;
        },
        removeHash(a) {
            this.keywords.splice(a, 1);
        },
        createHash() {
            // eslint-disable-next-line no-useless-escape
            let regExp = /[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"\s]/gi;
            // eslint-disable-next-line no-useless-escape
            if (this.keywordTag.replace(regExp, '') !== '') {
                // eslint-disable-next-line no-useless-escape
                this.keywords.push(this.keywordTag.replace(regExp, '').trim());
                this.keywordTag = '';
            } else {
                alert('태그에 공백을 포함한 특수문자는 들어갈 수 없습니다.');
                this.keywordTag = '';
            }
        },
        undo() {
            kakaoMap.undo();
        },
        redo() {
            kakaoMap.redo();
        },
        deleteImage(image) {
            let selectedImg = document.querySelector('.note-editable img[src="' + image + '"]');
            let imageArrIdx = this.imageArr.indexOf(image);
            let representativeIdx;

            if (imageArrIdx > -1) {
                this.imageArr.splice(imageArrIdx, 1);
                selectedImg.parentNode.removeChild(selectedImg);
            }
            if (this.getRepresentative(image)) {
                representativeIdx = this.representativeImage.indexOf(image);
                this.representativeImage.splice(representativeIdx, 1);
            }
        },
        submit() {
            // eslint-disable-next-line no-undef
            var markupStr = $('#summernote').summernote('code');
            var mapData = kakaoMap.submitData();
            var tempKeyword = '';
            console.log('data', kakaoMap.submitData());
            // console.log('areasTag', Object.keys(this.areas));
            // console.log('keywords', Object.keys(this.keywords));
            // console.log('title', this.title);
            // console.log('content', markupStr);
            for (var i in this.keywords) {
                tempKeyword += this.keywords[i] + ' ';
            }
            var sendData = {
                boardid: this.boardid,
                jwt: this.jwt.trim(),
                title: this.title,
                keywords: tempKeyword,
                content: markupStr,
                info: mapData.infoData,
                cusInfo: mapData.cusInfoData,
                marker: mapData.marker,
                polyline: mapData.polyline,
                rectangle: mapData.rectangle,
                circle: mapData.circle,
                polygon: mapData.polygon,
                arrow: mapData.arrow,
                ellipse: mapData.ellipse,
                night: this.night,
                day: this.day,
                image: this.representativeImage,
            };
            console.log('jwt', sendData);
            if (!this.updataPost) {
                Axios.post(`${URI}/page/board/`, sendData)
                    .then(res => {
                        console.log(res);
                        kakaoMap.init();
                        this.$router.push('/main');
                    })
                    .catch(error => {
                        console.log(error);
                    });
            } else {
                Axios.put(`${URI}/page/board/`, sendData)
                    .then(res => {
                        console.log('put', res);
                        kakaoMap.init();
                        this.$router.push('/main');
                    })
                    .catch(error => {
                        console.log('put', error);
                    });
            }
        },
    },
};
</script>