/* eslint-disable no-inner-declarations */
/* eslint-disable no-undef */
var mapContainer, mapOption, map, manager, options, ps, placeResultsInfoWinow;
var commentIndex = 0;
var startX, startY, startOverlayPoint;
var overlays = [];
var placeResults = [];
var info = {},
    cusInfo = {};
var commentCondition = 'both';

const createMap = () => {
    var mapContainer = document.getElementById('map'); //지도를 담을 영역의 DOM 레퍼런스
    var mapOption = {
        //지도를 생성할 때 필요한 기본 옵션
        center: new kakao.maps.LatLng(33.450701, 126.570667), //지도의 중심좌표.
        level: 3, //지도의 레벨(확대, 축소 정도)
    };

    map = new kakao.maps.Map(mapContainer, mapOption); //지도 생성 및 객체 리턴
    ps = new kakao.maps.services.Places();
    placeResultsInfoWinow = new kakao.maps.InfoWindow({
        zIndex: 1,
    });
    var zoomControl = new kakao.maps.ZoomControl();
    map.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT);

    var strokeColor = '#39f',
        fillColor = '#cce6ff',
        fillOpacity = 0.5,
        hintStrokeStyle = 'dash';

    options = {
        // Drawing Manager를 생성할 때 사용할 옵션입니다
        map: map, // Drawing Manager로 그리기 요소를 그릴 map 객체입니다
        drawingMode: [
            kakao.maps.Drawing.OverlayType.MARKER,
            kakao.maps.Drawing.OverlayType.ARROW,
            kakao.maps.Drawing.OverlayType.POLYLINE,
            kakao.maps.Drawing.OverlayType.RECTANGLE,
            kakao.maps.Drawing.OverlayType.CIRCLE,
            kakao.maps.Drawing.OverlayType.ELLIPSE,
            kakao.maps.Drawing.OverlayType.POLYGON,
        ],
        // 사용자에게 제공할 그리기 가이드 툴팁입니다
        // 사용자에게 도형을 그릴때, 드래그할때, 수정할때 가이드 툴팁을 표시하도록 설정합니다
        guideTooltip: ['draw', 'drag', 'edit'],
        markerOptions: {
            draggable: true,
            removable: true,
        },
        arrowOptions: {
            draggable: true,
            removable: true,
            strokeColor: strokeColor,
            hintStrokeStyle: hintStrokeStyle,
        },
        polylineOptions: {
            draggable: true,
            removable: true,
            strokeColor: strokeColor,
            hintStrokeStyle: hintStrokeStyle,
        },
        rectangleOptions: {
            draggable: true,
            removable: true,
            strokeColor: strokeColor,
            fillColor: fillColor,
            fillOpacity: fillOpacity,
        },
        circleOptions: {
            draggable: true,
            removable: true,
            strokeColor: strokeColor,
            fillColor: fillColor,
            fillOpacity: fillOpacity,
        },
        ellipseOptions: {
            draggable: true,
            removable: true,
            strokeColor: strokeColor,
            fillColor: fillColor,
            fillOpacity: fillOpacity,
        },
        polygonOptions: {
            draggable: true,
            removable: true,
            strokeColor: strokeColor,
            fillColor: fillColor,
            fillOpacity: fillOpacity,
        },
    };

    // 위에 작성한 옵션으로 Drawing Manager를 생성합니다
    manager = new kakao.maps.Drawing.DrawingManager(options);
    manager.addListener('drawend', function(mouseEvent) {
        if (commentCondition === 'both') {
            if (mouseEvent.overlayType === 'marker') {
                info['comment-' + (commentIndex - 1)].open(map, mouseEvent.target);
            } else if (mouseEvent.overlayType === 'circle' || mouseEvent.overlayType === 'ellipse') {
                cusInfo['comment-' + (commentIndex - 1)].setPosition(mouseEvent.target.getPosition());
                cusInfo['comment-' + (commentIndex - 1)].setMap(map);
            } else if (mouseEvent.overlayType === 'arrow' || mouseEvent.overlayType === 'polyline' || mouseEvent.overlayType === 'polygon') {
                cusInfo['comment-' + (commentIndex - 1)].setPosition(mouseEvent.target.getPath()[Math.floor(mouseEvent.target.getPath().length / 2)]);
                cusInfo['comment-' + (commentIndex - 1)].setMap(map);
            } else if (mouseEvent.overlayType === 'rectangle') {
                let pos_11 = mouseEvent.target
                    .getBounds()
                    .getSouthWest()
                    .getLat();
                let pos_12 = mouseEvent.target
                    .getBounds()
                    .getSouthWest()
                    .getLng();
                let pos_21 = mouseEvent.target
                    .getBounds()
                    .getNorthEast()
                    .getLat();
                let pos_22 = mouseEvent.target
                    .getBounds()
                    .getNorthEast()
                    .getLng();
                let a = pos_11 + Math.abs(pos_11 - pos_21) / 2;
                let b = pos_12 + Math.abs(pos_12 - pos_22) / 2;
                cusInfo['comment-' + (commentIndex - 1)].setPosition(new kakao.maps.LatLng(a, b));
                cusInfo['comment-' + (commentIndex - 1)].setMap(map);
            }
        }
    });
    kakao.maps.event.addListener(map, 'click', function() {
        for (var i = 0; i < placeResults.length; i++) {
            placeResults[i].setMap(null);
        }
        document.getElementById('menu-search-area').classList.add('hide');
        document.getElementById('hide-info').classList.remove('hide');
        document.getElementById('menu-wrap').classList.add('close-menu');
    });
    var undoBtn = document.getElementById('undo');
    var redoBtn = document.getElementById('redo');

    // Drawing Manager 객체에 state_changed 이벤트를 등록합니다
    // state_changed 이벤트는 그리기 요소의 생성/수정/이동/삭제 동작
    // 또는 Drawing Manager의 undo, redo 메소드가 실행됐을 때 발생합니다
    manager.addListener('state_changed', function() {
        // 되돌릴 수 있다면 undo 버튼을 활성화 시킵니다
        if (manager.undoable()) {
            undoBtn.disabled = false;
            undoBtn.classList.remove('disabled');
        } else {
            // 아니면 undo 버튼을 비활성화 시킵니다
            undoBtn.disabled = true;
            undoBtn.classList.add('disabled');
        }

        // 취소할 수 있다면 redo 버튼을 활성화 시킵니다
        if (manager.redoable()) {
            redoBtn.disabled = false;
            redoBtn.classList.remove('disabled');
        } else {
            // 아니면 redo 버튼을 비활성화 시킵니다
            redoBtn.disabled = true;
            redoBtn.classList.add('disabled');
        }
    });
};
const selectOverlay = (condition, type, title, content) => {
    // 그리기 중이면 그리기를 취소합니다
    manager.cancel();
    if (condition === 'both') {
        commentCondition = condition;
        insertComment(type, title, content);
        // 클릭한 그리기 요소 타입을 선택합니다
        manager.select(kakao.maps.Drawing.OverlayType[type]);
    } else if (condition === 'comment') {
        commentCondition = condition;
        insertComment(type, title, content);
    } else if (condition === 'drawtool') {
        commentCondition = condition;
        manager.select(kakao.maps.Drawing.OverlayType[type]);
    }
};

// undo 버튼 클릭시 호출되는 함수입니다.
const undo = () => {
    // 그리기 요소를 이전 상태로 되돌립니다
    manager.undo();
};

// redo 버튼 클릭시 호출되는 함수입니다.
const redo = () => {
    // 이전 상태로 되돌린 상태를 취소합니다
    manager.redo();
};

const searchPlace = place => {
    if (!place.replace(/^\s+|\s+$/g, '')) {
        alert('찾을 장소를 입력해 주세요.');
    } else {
        ps.keywordSearch(place.replace(/^\s+|\s+$/g, ''), placesSearchCB);
    }
    // 장소검색이 완료됐을 때 호출되는 콜백함수 입니다
    function placesSearchCB(data, status, pagination) {
        if (status === kakao.maps.services.Status.OK) {
            // 정상적으로 검색이 완료됐으면
            // 검색 목록과 마커를 표출합니다
            displayPlaces(data);

            // 페이지 번호를 표출합니다
            displayPagination(pagination);
        } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
            alert('검색 결과가 존재하지 않습니다.');
            return;
        } else if (status === kakao.maps.services.Status.ERROR) {
            alert('검색 결과 중 오류가 발생했습니다.');
            return;
        }
    }

    // 검색 결과 목록과 마커를 표출하는 함수입니다
    function displayPlaces(places) {
        var listEl = document.getElementById('placesList'),
            menuEl = document.getElementById('menu-wrap'),
            fragment = document.createDocumentFragment(),
            bounds = new kakao.maps.LatLngBounds(),
            listStr = '';

        // 검색 결과 목록에 추가된 항목들을 제거합니다
        removeAllChildNods(listEl);

        // 지도에 표시되고 있는 마커를 제거합니다
        removeMarker();

        for (var i = 0; i < places.length; i++) {
            // 마커를 생성하고 지도에 표시합니다
            var placePosition = new kakao.maps.LatLng(places[i].y, places[i].x),
                marker = addMarker(placePosition, i),
                itemEl = getListItem(i, places[i]); // 검색 결과 항목 Element를 생성합니다

            // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
            // LatLngBounds 객체에 좌표를 추가합니다
            bounds.extend(placePosition);

            // 마커와 검색결과 항목에 mouseover 했을때
            // 해당 장소에 인포윈도우에 장소명을 표시합니다
            // mouseout 했을 때는 인포윈도우를 닫습니다
            (function(marker, title) {
                kakao.maps.event.addListener(marker, 'mouseover', function() {
                    displayInfowindow(marker, title);
                });

                kakao.maps.event.addListener(marker, 'mouseout', function() {
                    placeResultsInfoWinow.close();
                });

                itemEl.onmouseover = function() {
                    displayInfowindow(marker, title);
                };

                itemEl.onmouseout = function() {
                    placeResultsInfoWinow.close();
                };
            })(marker, places[i].place_name);

            fragment.appendChild(itemEl);
        }

        // 검색결과 항목들을 검색결과 목록 Elemnet에 추가합니다
        listEl.appendChild(fragment);
        menuEl.scrollTop = 0;

        // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
        map.setBounds(bounds);
    }

    // 검색결과 항목을 Element로 반환하는 함수입니다
    function getListItem(index, places) {
        var el = document.createElement('li'),
            itemStr = '<span class="markerbg marker_' + (index + 1) + '"></span>' + '<div class="info">' + '   <h5>' + places.place_name + '</h5>';

        if (places.road_address_name) {
            itemStr += '    <span>' + places.road_address_name + '</span>' + '   <span class="jibun gray">' + places.address_name + '</span>';
        } else {
            itemStr += '    <span>' + places.address_name + '</span>';
        }

        itemStr += '  <span class="tel">' + places.phone + '</span>' + '</div>';

        el.innerHTML = itemStr;
        el.className = 'item';

        return el;
    }

    // 마커를 생성하고 지도 위에 마커를 표시하는 함수입니다
    function addMarker(position, idx, title) {
        var imageSrc = 'http://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png', // 마커 이미지 url, 스프라이트 이미지를 씁니다
            imageSize = new kakao.maps.Size(36, 37), // 마커 이미지의 크기
            imgOptions = {
                spriteSize: new kakao.maps.Size(36, 691), // 스프라이트 이미지의 크기
                spriteOrigin: new kakao.maps.Point(0, idx * 46 + 10), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
                offset: new kakao.maps.Point(13, 37), // 마커 좌표에 일치시킬 이미지 내에서의 좌표
            },
            markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions),
            marker = new kakao.maps.Marker({
                position: position, // 마커의 위치
                image: markerImage,
            });

        marker.setMap(map); // 지도 위에 마커를 표출합니다
        placeResults.push(marker); // 배열에 생성된 마커를 추가합니다

        return marker;
    }

    // 지도 위에 표시되고 있는 마커를 모두 제거합니다
    function removeMarker() {
        for (var i = 0; i < placeResults.length; i++) {
            placeResults[i].setMap(null);
        }
        placeResults = [];
    }

    // 검색결과 목록 하단에 페이지번호를 표시는 함수입니다
    function displayPagination(pagination) {
        var paginationEl = document.getElementById('pagination'),
            fragment = document.createDocumentFragment(),
            i;

        // 기존에 추가된 페이지번호를 삭제합니다
        while (paginationEl.hasChildNodes()) {
            paginationEl.removeChild(paginationEl.lastChild);
        }

        for (i = 1; i <= pagination.last; i++) {
            var el = document.createElement('a');
            el.href = '#';
            el.innerHTML = i;

            if (i === pagination.current) {
                el.className = 'on';
            } else {
                el.onclick = (function(i) {
                    return function() {
                        pagination.gotoPage(i);
                    };
                })(i);
            }

            fragment.appendChild(el);
        }
        paginationEl.appendChild(fragment);
    }

    // 검색결과 목록 또는 마커를 클릭했을 때 호출되는 함수입니다
    // 인포윈도우에 장소명을 표시합니다
    function displayInfowindow(marker, title) {
        var content = '<div style="padding:5px;z-index:1;">' + title + '</div>';

        placeResultsInfoWinow.setContent(content);
        placeResultsInfoWinow.open(map, marker);
    }

    // 검색결과 목록의 자식 Element를 제거하는 함수입니다
    function removeAllChildNods(el) {
        while (el.hasChildNodes()) {
            el.removeChild(el.lastChild);
        }
    }
};

const reloadPlace = () => {
    for (var i = 0; i < placeResults.length; i++) {
        placeResults[i].setMap(map);
    }
};
const getTest = () => {
    console.log('info', info);
    console.log('cusInfo', cusInfo);
    console.log('commentIndex', commentIndex);
    console.log(manager.getData());
};

const KakaoMap = {
    createMap: () => createMap(),
    selectOverlay: (condition, type, title, content) => selectOverlay(condition, type, title, content),
    undo: () => undo(),
    redo: () => redo(),
    getTest: () => getTest(),
    searchPlace: place => searchPlace(place),
    reloadPlace: () => reloadPlace(),
};
export default KakaoMap;

function insertComment(type, title, content) {
    let commentWrap = document.createElement('div');
    var commentTitle = document.createElement('div');
    var commentClose = document.createElement('button');
    var commentHr = document.createElement('hr');
    var commentContent = document.createElement('div');
    var titleTextNoded = document.createTextNode(title);
    var contentTextNoded = document.createTextNode(content);
    commentWrap.setAttribute('id', 'comment-' + commentIndex++);
    commentWrap.setAttribute('class', 'comment-wrap');
    commentTitle.setAttribute('class', 'comment-title');
    commentClose.setAttribute('class', 'close');
    commentContent.setAttribute('class', 'comment-content');
    commentWrap.appendChild(commentTitle);
    commentWrap.appendChild(commentHr);
    commentWrap.appendChild(commentContent);
    commentTitle.appendChild(titleTextNoded);
    commentContent.appendChild(contentTextNoded);
    commentTitle.after(commentClose);
    if (type === 'MARKER' && commentCondition === 'both') {
        var infowindow = new kakao.maps.InfoWindow({
            content: commentWrap,
        });
        info[infowindow.getContent().id] = infowindow;
        commentClose.onclick = function closeinfo() {
            delete info[infowindow.getContent().id];
            infowindow.close();
        };
        // 커스텀 오버레이에 mousedown이벤트를 등록합니다
        addEventHandle(commentWrap, 'mousedown', onMouseDown);

        // mouseup 이벤트가 일어났을때 mousemove 이벤트를 제거하기 위해
        // document에 mouseup 이벤트를 등록합니다
        addEventHandle(document, 'mouseup', onMouseUp);

        // 커스텀 오버레이에 mousedown 했을 때 호출되는 핸들러 입니다
        function onMouseDown(e) {
            // 커스텀 오버레이를 드래그 할 때, 내부 텍스트가 영역 선택되는 현상을 막아줍니다.
            if (e.preventDefault) {
                e.preventDefault();
            } else {
                e.returnValue = false;
            }

            var proj = map.getProjection(), // 지도 객체로 부터 화면픽셀좌표, 지도좌표간 변환을 위한 MapProjection 객체를 얻어옵니다
                overlayPos = infowindow.getPosition(); // 커스텀 오버레이의 현재 위치를 가져옵니다

            // 커스텀오버레이에서 마우스 관련 이벤트가 발생해도 지도가 움직이지 않도록 합니다
            kakao.maps.event.preventMap();

            // mousedown된 좌표를 설정합니다
            startX = e.clientX;
            startY = e.clientY;

            // mousedown됐을 때의 커스텀 오버레이의 좌표를
            // 지도 컨테이너내 픽셀 좌표로 변환합니다
            startOverlayPoint = proj.containerPointFromCoords(overlayPos);

            // document에 mousemove 이벤트를 등록합니다
            addEventHandle(document, 'mousemove', onMouseMove);
        }

        // 커스텀 오버레이에 mousedown 한 상태에서
        // mousemove 하면 호출되는 핸들러 입니다
        function onMouseMove(e) {
            // 커스텀 오버레이를 드래그 할 때, 내부 텍스트가 영역 선택되는 현상을 막아줍니다.
            if (e.preventDefault) {
                e.preventDefault();
            } else {
                e.returnValue = false;
            }

            var proj = map.getProjection(), // 지도 객체로 부터 화면픽셀좌표, 지도좌표간 변환을 위한 MapProjection 객체를 얻어옵니다
                deltaX = startX - e.clientX, // mousedown한 픽셀좌표에서 mousemove한 좌표를 빼서 실제로 마우스가 이동된 픽셀좌표를 구합니다
                deltaY = startY - e.clientY,
                // mousedown됐을 때의 커스텀 오버레이의 좌표에 실제로 마우스가 이동된 픽셀좌표를 반영합니다
                newPoint = new kakao.maps.Point(startOverlayPoint.x - deltaX, startOverlayPoint.y - deltaY),
                // 계산된 픽셀 좌표를 지도 컨테이너에 해당하는 지도 좌표로 변경합니다
                newPos = proj.coordsFromContainerPoint(newPoint);

            // 커스텀 오버레이의 좌표를 설정합니다
            infowindow.setPosition(newPos);
        }

        // mouseup 했을 때 호출되는 핸들러 입니다
        function onMouseUp(e) {
            // 등록된 mousemove 이벤트 핸들러를 제거합니다
            removeEventHandle(document, 'mousemove', onMouseMove);
        }

        // target node에 이벤트 핸들러를 등록하는 함수힙니다
        function addEventHandle(target, type, callback) {
            if (target.addEventListener) {
                target.addEventListener(type, callback);
            } else {
                target.attachEvent('on' + type, callback);
            }
        }

        // target node에 등록된 이벤트 핸들러를 제거하는 함수힙니다
        function removeEventHandle(target, type, callback) {
            if (target.removeEventListener) {
                target.removeEventListener(type, callback);
            } else {
                target.detachEvent('on' + type, callback);
            }
        }
    } else {
        var customOverlay = new kakao.maps.CustomOverlay({
            position: map.getCenter(),
            content: commentWrap,
            yAnchor: 1.3,
        });
        cusInfo[customOverlay.getContent().id] = customOverlay;
        commentClose.onclick = function closeOverlay() {
            delete cusInfo[customOverlay.getContent().id];
            customOverlay.setMap(null);
        };
        if (commentCondition === 'comment') {
            customOverlay.setMap(map);
        }
        // 커스텀 오버레이에 mousedown이벤트를 등록합니다
        addEventHandle(commentWrap, 'mousedown', onMouseDown);

        // mouseup 이벤트가 일어났을때 mousemove 이벤트를 제거하기 위해
        // document에 mouseup 이벤트를 등록합니다
        addEventHandle(document, 'mouseup', onMouseUp);

        // 커스텀 오버레이에 mousedown 했을 때 호출되는 핸들러 입니다
        function onMouseDown(e) {
            // 커스텀 오버레이를 드래그 할 때, 내부 텍스트가 영역 선택되는 현상을 막아줍니다.
            if (e.preventDefault) {
                e.preventDefault();
            } else {
                e.returnValue = false;
            }

            var proj = map.getProjection(), // 지도 객체로 부터 화면픽셀좌표, 지도좌표간 변환을 위한 MapProjection 객체를 얻어옵니다
                overlayPos = customOverlay.getPosition(); // 커스텀 오버레이의 현재 위치를 가져옵니다

            // 커스텀오버레이에서 마우스 관련 이벤트가 발생해도 지도가 움직이지 않도록 합니다
            kakao.maps.event.preventMap();

            // mousedown된 좌표를 설정합니다
            startX = e.clientX;
            startY = e.clientY;

            // mousedown됐을 때의 커스텀 오버레이의 좌표를
            // 지도 컨테이너내 픽셀 좌표로 변환합니다
            startOverlayPoint = proj.containerPointFromCoords(overlayPos);

            // document에 mousemove 이벤트를 등록합니다
            addEventHandle(document, 'mousemove', onMouseMove);
        }

        // 커스텀 오버레이에 mousedown 한 상태에서
        // mousemove 하면 호출되는 핸들러 입니다
        function onMouseMove(e) {
            // 커스텀 오버레이를 드래그 할 때, 내부 텍스트가 영역 선택되는 현상을 막아줍니다.
            if (e.preventDefault) {
                e.preventDefault();
            } else {
                e.returnValue = false;
            }

            var proj = map.getProjection(), // 지도 객체로 부터 화면픽셀좌표, 지도좌표간 변환을 위한 MapProjection 객체를 얻어옵니다
                deltaX = startX - e.clientX, // mousedown한 픽셀좌표에서 mousemove한 좌표를 빼서 실제로 마우스가 이동된 픽셀좌표를 구합니다
                deltaY = startY - e.clientY,
                // mousedown됐을 때의 커스텀 오버레이의 좌표에 실제로 마우스가 이동된 픽셀좌표를 반영합니다
                newPoint = new kakao.maps.Point(startOverlayPoint.x - deltaX, startOverlayPoint.y - deltaY),
                // 계산된 픽셀 좌표를 지도 컨테이너에 해당하는 지도 좌표로 변경합니다
                newPos = proj.coordsFromContainerPoint(newPoint);

            // 커스텀 오버레이의 좌표를 설정합니다
            customOverlay.setPosition(newPos);
        }

        // mouseup 했을 때 호출되는 핸들러 입니다
        function onMouseUp(e) {
            // 등록된 mousemove 이벤트 핸들러를 제거합니다
            removeEventHandle(document, 'mousemove', onMouseMove);
        }

        // target node에 이벤트 핸들러를 등록하는 함수힙니다
        function addEventHandle(target, type, callback) {
            if (target.addEventListener) {
                target.addEventListener(type, callback);
            } else {
                target.attachEvent('on' + type, callback);
            }
        }

        // target node에 등록된 이벤트 핸들러를 제거하는 함수힙니다
        function removeEventHandle(target, type, callback) {
            if (target.removeEventListener) {
                target.removeEventListener(type, callback);
            } else {
                target.detachEvent('on' + type, callback);
            }
        }
    }
}

function getMapDrawData() {
    // Drawing Manager에서 그려진 데이터 정보를 가져옵니다
    var data = manager.getData();

    // 지도에 가져온 데이터로 도형들을 그립니다
    drawMarker(data[kakao.maps.drawing.OverlayType.MARKER]);
    drawPolyline(data[kakao.maps.drawing.OverlayType.POLYLINE]);
    drawRectangle(data[kakao.maps.drawing.OverlayType.RECTANGLE]);
    drawCircle(data[kakao.maps.drawing.OverlayType.CIRCLE]);
    drawPolygon(data[kakao.maps.drawing.OverlayType.POLYGON]);
    drawArrow(data[kakao.maps.Drawing.OverlayType.ARROW]);
    drawEllipse(data[kakao.maps.Drawing.OverlayType.ELLIPSE]);
}

// Drawing Manager에서 가져온 데이터 중 마커를 아래 지도에 표시하는 함수입니다
function drawMarker(markers) {
    var len = markers.length,
        i = 0;

    for (; i < len; i++) {
        var marker = new kakao.maps.Marker({
            position: new kakao.maps.LatLng(markers[i].y, markers[i].x),
            zIndex: markers[i].zIndex,
        });

        overlays.push(marker);
    }
}

// Drawing Manager에서 가져온 데이터 중 선을 아래 지도에 표시하는 함수입니다
function drawPolyline(lines) {
    var len = lines.length,
        i = 0;

    for (; i < len; i++) {
        var path = pointsToPath(lines[i].points);
        var style = lines[i].options;
        var polyline = new kakao.maps.Polyline({
            path: path,
            strokeColor: style.strokeColor,
            strokeOpacity: style.strokeOpacity,
            strokeStyle: style.strokeStyle,
            strokeWeight: style.strokeWeight,
        });

        overlays.push(polyline);
    }
}

function drawArrow(arrows) {
    var len = arrows.length,
        i = 0;

    for (; i < len; i++) {
        var path = pointsToPath(arrows[i].points);
        var style = arrows[i].options;
        var arrow = new kakao.maps.Polyline({
            endArrow: true,
            path: path,
            strokeColor: style.strokeColor,
            strokeOpacity: style.strokeOpacity,
            strokeStyle: style.strokeStyle,
            strokeWeight: style.strokeWeight,
        });

        overlays.push(arrow);
    }
}
// Drawing Manager에서 가져온 데이터 중 사각형을 아래 지도에 표시하는 함수입니다
function drawRectangle(rects) {
    var len = rects.length,
        i = 0;

    for (; i < len; i++) {
        var style = rects[i].options;
        var rect = new kakao.maps.Rectangle({
            bounds: new kakao.maps.LatLngBounds(
                new kakao.maps.LatLng(rects[i].sPoint.y, rects[i].sPoint.x),
                new kakao.maps.LatLng(rects[i].ePoint.y, rects[i].ePoint.x),
            ),
            strokeColor: style.strokeColor,
            strokeOpacity: style.strokeOpacity,
            strokeStyle: style.strokeStyle,
            strokeWeight: style.strokeWeight,
            fillColor: style.fillColor,
            fillOpacity: style.fillOpacity,
        });

        overlays.push(rect);
    }
}

// Drawing Manager에서 가져온 데이터 중 원을 아래 지도에 표시하는 함수입니다
function drawCircle(circles) {
    var len = circles.length,
        i = 0;

    for (; i < len; i++) {
        var style = circles[i].options;
        var circle = new kakao.maps.Circle({
            center: new kakao.maps.LatLng(circles[i].center.y, circles[i].center.x),
            radius: circles[i].radius,
            strokeColor: style.strokeColor,
            strokeOpacity: style.strokeOpacity,
            strokeStyle: style.strokeStyle,
            strokeWeight: style.strokeWeight,
            fillColor: style.fillColor,
            fillOpacity: style.fillOpacity,
        });

        overlays.push(circle);
    }
}

function drawEllipse(ellipses) {
    var len = ellipses.length,
        i = 0;

    for (; i < len; i++) {
        var style = ellipses[i].options;
        var circle = new kakao.maps.Circle({
            rx: ellipses[i].rx,
            ry: ellipses[i].ry,
            center: new kakao.maps.LatLng(ellipses[i].center.y, ellipses[i].center.x),
            strokeColor: style.strokeColor,
            strokeOpacity: style.strokeOpacity,
            strokeStyle: style.strokeStyle,
            strokeWeight: style.strokeWeight,
            fillColor: style.fillColor,
            fillOpacity: style.fillOpacity,
        });

        overlays.push(circle);
    }
}

// Drawing Manager에서 가져온 데이터 중 다각형을 아래 지도에 표시하는 함수입니다
function drawPolygon(polygons) {
    var len = polygons.length,
        i = 0;

    for (; i < len; i++) {
        var path = pointsToPath(polygons[i].points);
        var style = polygons[i].options;
        var polygon = new kakao.maps.Polygon({
            map: map,
            path: path,
            strokeColor: style.strokeColor,
            strokeOpacity: style.strokeOpacity,
            strokeStyle: style.strokeStyle,
            strokeWeight: style.strokeWeight,
            fillColor: style.fillColor,
            fillOpacity: style.fillOpacity,
        });

        overlays.push(polygon);
    }
}

// Drawing Manager에서 가져온 데이터 중
// 선과 다각형의 꼭지점 정보를 kakao.maps.LatLng객체로 생성하고 배열로 반환하는 함수입니다
function pointsToPath(points) {
    var len = points.length,
        path = [],
        i = 0;

    for (; i < len; i++) {
        var latlng = new kakao.maps.LatLng(points[i].y, points[i].x);
        path.push(latlng);
    }

    return path;
}
