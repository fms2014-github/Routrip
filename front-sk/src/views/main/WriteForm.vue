<template>
    <div id="content-upload-form">
        <InputForm id="content-title" :enterInput="test1" :errorText="error.title" placeholder="제목을 입력해 주세요." label="제목" />
        <div class="select-form">
            <span class="select-form-title">장소</span>
            <div class="select-tag">
                <div class="hashtag-form" v-for="area in areas" :key="area">
                    {{ area }}<button class="hashtag-cansle" @click="removeHash(area, 'area')">×</button>
                </div>
            </div>
        </div>
        <div class="search">
            <input v-model="areaTag" class="search-bar" type="text" @keyup.enter="createHash('area')" />
            <div class="search-result-dropdown">
                <div class="dropdown-list" v-for="resultArea in resultAreas" :key="resultArea">{{ resultArea }}</div>
            </div>
        </div>
        <div class="select-form">
            <span class="select-form-title">키워드</span>
            <div class="select-tag">
                <div class="hashtag-form" v-for="keyword in keywords" :key="keyword">
                    {{ keyword }}<button class="hashtag-cansle" @click="removeHash(keyword, 'keyword')">×</button>
                </div>
            </div>
        </div>
        <div class="search">
            <input v-model="keywordTag" class="search-bar" type="text" @keyup.enter="createHash('keyword')" />
        </div>
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
                    <button id="undo" class="draw-tools disabled" onclick="undo()" disabled>실행취소</button>
                    <button id="redo" class="draw-tools disabled" onclick="redo()" disabled>원래대로</button>
                </div>
                <div id="drop-down-wrap" class="drop-up">
                    <div id="insert-comment-wrap">
                        <span id="insert-comment-wrap-title">설명 넣기</span>
                        <label for="comment-title-insert">제목 <input id="comment-title-insert" type="text" v-model="commentTitle"/></label>
                        <label for="comment-content-insert">내용<textarea id="comment-content-insert" type="text" v-model="commentContent"/></label>
                        <button id="comment-submit" @click="createDraw">생성</button>
                    </div>
                    <div id="create-condition">
                        <span>생성 조건</span>
                        <label for="both"><input v-model="createCondition" id="both" name="condition" type="radio" value="both" />둘 다 생성</label>
                        <label for="only-comment"
                            ><input v-model="createCondition" id="only-comment" name="condition" type="radio" value="comment" />주석만 생성</label
                        >
                        <label for="only-drawTool"
                            ><input v-model="createCondition" id="only-drawTool" name="condition" type="radio" value="drawtool" />그리기 도구만
                            생성</label
                        >
                    </div>
                </div>
            </div>
            <br />
        </div>
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
    </div>
</template>

<script>
import '../../assets/css/WriteForm.scss';
import InputForm from '../../components/common/Input';

export default {
    components: {
        InputForm,
    },
    data() {
        return {
            areaTag: '',
            keywordTag: '',
            selectDraw: '',
            areaData: [
                '장소 #1',
                '장소 #2',
                '장소 #3',
                '장소 #4',
                '장소 #5',
                '장소 #6',
                '가',
                '가나',
                '가나다',
                '가나다라',
                '가나다라마',
                '가나다라마바',
                '가나다라마바사',
            ],
            areas: [],
            keywords: ['키워드 #1', '키워드 #2', '키워드 #3', '키워드 #4', '키워드 #5'],
            resultAreas: [],
            imageArr: [],
            imageArrBase64: [],
            commentTitle: '',
            commentContent: '',
            error: {
                title: '',
            },
            createCondition: 'both',
        };
    },
    watch: {
        areaword: function(v) {
            this.findArea();
        },
        hashTag: function() {},
    },
    methods: {
        createDraw() {
            selectOverlay(this.createCondition, this.selectDraw, this.commentTitle, this.commentContent);
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
        removeHash(a, s) {
            if (s === 'area') {
                this.areas.splice(this.areas.indexOf(a), 1);
            } else if (s === 'keyword') {
                this.keywords.splice(this.keywords.indexOf(a), 1);
            }
        },
        createHash(s) {
            if (s === 'area' && this.areaTag !== '') {
                if (this.areas.length === 20) {
                    alert('태그는 20개 이상 생성할 수 없습니다.');
                    return;
                }
                this.areas.push(this.areaTag);
                this.areaTag = '';
            } else if (s === 'keyword' && this.keywordTag !== '') {
                if (this.keywords.length === 10) {
                    alert('태그는 20개 이상 생성할 수 없습니다.');
                    return;
                }
                this.keywords.push(this.keywordTag);
                this.keywordTag = '';
            }
        },
        findArea() {
            this.resultAreas = [];
            for (var i in this.areaData) {
                if (this.areaData[i].indexOf(this.areaTag) >= 0) {
                    this.resultAreas.push(this.areaData[i]);
                }
            }
            if (this.areaTag === '') {
                this.resultAreas = [];
            }
        },
        test1() {
            console.log('');
        },
        submit() {},
        imageFiles(e) {
            var fileList = document.querySelector('#file-list');
            var get_file = e.target.files;
            for (var i = 0; i < get_file.length; i++) {
                this.imageArr.push(get_file[i]);
            }

            for (var i = 0; i < get_file.length; i++) {
                var reader = new FileReader();

                reader.onload = (function(imgab) {
                    return function(e) {
                        imgab.push(e.target.result);
                    };
                })(this.imageArrBase64);
                if (get_file) {
                    reader.readAsDataURL(get_file[i]);
                }
            }
        },
        deleteImg(id, idx) {
            this.imageArr.splice(idx, 1);
            this.imageArrBase64.splice(idx, 1);
        },
    },
};
</script>
