package com.web.curation.controller.page;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONStringer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.curation.model.BasicResponse;
import com.web.curation.model.board.Board;
import com.web.curation.model.board.Comment;
import com.web.curation.model.board.Img;
import com.web.curation.model.board.Marker;
import com.web.curation.model.user.Alarm;
import com.web.curation.model.user.User;
import com.web.curation.service.BoardService;
import com.web.curation.service.UserService;

import io.jsonwebtoken.Jwts;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
		@ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
		@ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
		@ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RequestMapping("/page")
@RestController
public class PageController {

	@Autowired
	private BoardService boardService;

	@Autowired
	private UserService userService;

	@PostMapping("/favorite")
	@ApiOperation(value = "좋아요 등록")
	public Object addFavorite(@RequestBody Map<String, String> map) throws Exception {
		String jwt = map.get("jwt");
		if (jwt == null)
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		if (isOkJwt(jwt)) {
			int boardid = Integer.parseInt(map.get("boardid"));
			int uid = (int) Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid");
			int ok = 0;
			List<Integer> usersid = boardService.getFavoriteByBoard(boardid);
			boolean flag = true;
			for (int i : usersid) {
				if (i == uid) {
					flag = false;
					Map<String, String> map1 = new HashMap<String, String>();
					map1.put("jwt", jwt);
					map1.put("boardid", String.valueOf(boardid));
					deleteFavorite(map1);
					return new ResponseEntity<>(HttpStatus.OK);
				}
			}
			if (flag)
				ok = boardService.addFavorite(uid, boardid);
			if (ok > 0)
				return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/favoriteBoard")
	@ApiOperation(value = "좋아요 누른 게시글")
	public Object FavoriteListByUser(@RequestBody Map<String, String> map) throws Exception {
		String jwt = map.get("jwt");
		if (jwt == null)
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		if (isOkJwt(jwt)) {
			List<Integer> boardsid = boardService
					.getFavoriteByUser((int) Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid"));
			List<Board> boards = new ArrayList<Board>();
			for (int i : boardsid) {
				Board b = boardService.findBoardByBoardId(i);
				List<Img> imgs = boardService.findBoardImg(b.getBoardid());
				List<Img> repimg = new ArrayList<Img>();
				for (Img img : imgs) {
					if (img.getRep() == 1)
						repimg.add(img);
				}
				b.setImgs(repimg);
				List<Comment> comments = boardService.findComment(b.getBoardid());
				for (Comment c : comments) {
					c.setUser(userService.findUserSimple(c.getUid()));
				}
				b.setCommentNum(comments.size());
				int favoriteNum = boardService.getFavoriteNum(b.getBoardid());
				boardService.updateFavoriteNum(b.getBoardid(), favoriteNum);
				b.setFavoriteNum(favoriteNum);
				b.setMarkers(boardService.findMarker(b.getBoardid()));
				b.setComments(comments);
				b.setUser(userService.findUserSimple(b.getUid()));
				List<Integer> usersid = boardService.getFavoriteByBoard(b.getBoardid());
				List<User> users = new ArrayList<User>();
				for (int ui : usersid)
					users.add(userService.findUserSimple(ui));
				b.setFavorite(users);
				b.setKeywords(b.getKeyword());
				boards.add(b);
			}
			return new ResponseEntity<>(boards, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// @DeleteMapping("/favorite")
	// @ApiOperation(value = "좋아요 해제")
	public Object deleteFavorite(Map<String, String> map) throws Exception {
		String jwt = map.get("jwt");
		if (jwt == null)
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		if (isOkJwt(jwt)) {
			int boardid = Integer.parseInt(map.get("boardid"));
			int ok = boardService.deleteFavorite((int) Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid"), boardid);
			if (ok > 0)
				return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/scrap")
	@ApiOperation(value = "스크랩 추가")
	public Object addScrap(@RequestBody Map<String, String> map) throws Exception {
		String jwt = map.get("jwt");
		if (jwt == null)
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		if (isOkJwt(jwt)) {
			int boardid = Integer.parseInt(map.get("boardid"));
			int uid = (int) Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid");
			int ok = 0;
			List<Integer> boardsid = boardService.getScrap(uid);
			boolean flag = true;
			for (int i : boardsid) {
				if (i == boardid) {
					flag = false;
					Map<String, String> map1 = new HashMap<String, String>();
					map1.put("jwt", jwt);
					map1.put("boardid", String.valueOf(boardid));
					deleteScrap(map1);
					return new ResponseEntity<>(HttpStatus.OK);
				}
			}
			if (flag)
				ok = boardService.addScrap(uid, boardid);
			if (ok > 0)
				return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/scrapBoard")
	@ApiOperation(value = "스크랩 게시글")
	public Object scrapBoard(@RequestBody Map<String, String> map) throws Exception {
		String jwt = map.get("jwt");
		if (jwt == null)
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		if (isOkJwt(jwt)) {
			List<Integer> boardsid = boardService
					.getScrap((int) Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid"));
			List<Board> boards = new ArrayList<Board>();
			for (int i : boardsid) {
				Board b = boardService.findBoardByBoardId(i);
				List<Img> imgs = boardService.findBoardImg(b.getBoardid());
				List<Img> repimg = new ArrayList<Img>();
				for (Img img : imgs) {
					if (img.getRep() == 1)
						repimg.add(img);
				}
				b.setImgs(repimg);
				List<Comment> comments = boardService.findComment(b.getBoardid());
				for (Comment c : comments) {
					c.setUser(userService.findUserSimple(c.getUid()));
				}
				b.setCommentNum(comments.size());
				int favoriteNum = boardService.getFavoriteNum(b.getBoardid());
				boardService.updateFavoriteNum(b.getBoardid(), favoriteNum);
				b.setFavoriteNum(favoriteNum);
				b.setMarkers(boardService.findMarker(b.getBoardid()));
				b.setComments(comments);
				b.setUser(userService.findUserSimple(b.getUid()));
				List<Integer> usersid = boardService.getFavoriteByBoard(b.getBoardid());
				List<User> users = new ArrayList<User>();
				for (int ui : usersid)
					users.add(userService.findUserSimple(ui));
				b.setFavorite(users);
				b.setKeywords(b.getKeyword());
				boards.add(b);
			}
			return new ResponseEntity<>(boards, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	public Object deleteScrap(Map<String, String> map) throws Exception {
		String jwt = map.get("jwt");
		if (jwt == null)
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		if (isOkJwt(jwt)) {
			int boardid = Integer.parseInt(map.get("boardid"));
			int ok = boardService.deleteScrap((int) Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid"), boardid);
			if (ok > 0)
				return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/followBoard")
	@ApiOperation(value = "팔로우 게시글")
	public Object followboard(@RequestBody Map<String, String> map) throws Exception {
		String jwt = map.get("jwt");
		if (isOkJwt(jwt)) {
			List<Board> boards = boardService
					.findBoardByFollow((int) Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid"));
			for (Board b : boards) {
				List<Img> imgs = boardService.findBoardImg(b.getBoardid());
				List<Img> repimg = new ArrayList<Img>();
				for (Img img : imgs) {
					if (img.getRep() == 1)
						repimg.add(img);
				}
				b.setImgs(repimg);
				List<Comment> comments = boardService.findComment(b.getBoardid());
				for (Comment c : comments) {
					c.setUser(userService.findUserSimple(c.getUid()));
				}
				b.setCommentNum(comments.size());
				int favoriteNum = boardService.getFavoriteNum(b.getBoardid());
				boardService.updateFavoriteNum(b.getBoardid(), favoriteNum);
				b.setFavoriteNum(favoriteNum);
				b.setMarkers(boardService.findMarker(b.getBoardid()));
				b.setComments(comments);
				List<Integer> usersid = boardService.getFavoriteByBoard(b.getBoardid());
				List<User> users = new ArrayList<User>();
				for (int ui : usersid)
					users.add(userService.findUserSimple(ui));
				b.setFavorite(users);
				b.setUser(userService.findUserSimple(b.getUid()));
				b.setKeywords(b.getKeyword());
			}
			return new ResponseEntity<>(boards, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/boardDetail")
	@ApiOperation(value = "게시글 상세 정보")
	public Object getBoardDetail(@RequestBody Map<String, String> map) throws Exception {
		String jwt = map.get("jwt");
		if (jwt == null)
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		if (isOkJwt(jwt)) {
			int boardid = Integer.parseInt(map.get("boardid"));
			int uid = (int) Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid");
			Board board = boardService.findBoardByBoardId(boardid);
			if (board.getUnveiled() == 0 && uid != board.getUid()) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			board.setImgs(boardService.findBoardImg(board.getBoardid()));
			List<Comment> comments = boardService.findComment(board.getBoardid());
			for (Comment c : comments) {
				c.setUser(userService.findUserSimple(c.getUid()));
			}
			board.setCommentNum(comments.size());
			board.setComments(comments);
			int favoriteNum = boardService.getFavoriteNum(board.getBoardid());
			boardService.updateFavoriteNum(board.getBoardid(), favoriteNum);
			board.setFavoriteNum(favoriteNum);
			board.setMarkers(boardService.findMarker(board.getBoardid()));
			List<Integer> usersid = boardService.getFavoriteByBoard(board.getBoardid());
			List<User> users = new ArrayList<User>();
			for (int ui : usersid)
				users.add(userService.findUserSimple(ui));
			board.setFavorite(users);
			board.setUser(userService.findUserSimple(board.getUid()));
			board.setKeywords(board.getKeyword());
			return new ResponseEntity<>(board, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/board")
	@ApiOperation(value = "게시글 작성 수정 버전(아마 이 방식이 맞다고 봄)")
	public Object addBoard2(@RequestBody Map<String, Object> map) throws Exception {
		System.out.println("게시글 작성 시작");
		// 이방식 성공하면 수정도 이런식으로 변경
		String jwt = (String) map.get("jwt");
		if (jwt == null)
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

		for (String key : map.keySet()) { // 어떤식으로 넘어오는지 확인용
			System.out.println(key + " : " + JSONStringer.valueToString(map.get(key)));
		}
		//return new ResponseEntity<>(HttpStatus.OK);

		Board board = new Board();
		board.setUid((int) Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid"));
		board.setTitle((String) map.get("title"));
		board.setTripterm((String) map.get("night") + " " + (String) map.get("day"));
		board.setKeyword((String) map.get("keyword"));
		board.setContent((String) map.get("content"));
		board.setInfo(JSONStringer.valueToString(map.get("info")));
		board.setCusInfo((String) map.get(JSONStringer.valueToString(map.get("cusInfo"))));
		board.setUnveiled(1);
		int ok = boardService.addBoard(board);
		if (ok > 0) {
			int repnum = 0;
			
			for (String key : map.keySet()) {
				if (key.equals("jwt") || key.equals("title") || key.equals("night") || key.equals("day")
						|| key.equals("keyword") || key.equals("content") || key.equals("info")
						|| key.equals("cusInfo"))
					continue;
				if(key.equals("marker")) {
					String marker = JSONStringer.valueToString(map.get("marker"));
					JSONArray array = (JSONArray) new JSONParser().parse(marker);
					for (int i = 0; i < array.size(); i++) {
						Marker m = new Marker();
						m.setBoardid(board.getBoardid());
						m.setLatitude((String)((JSONObject) array.get(i)).get("lat"));
						m.setLongitude((String)((JSONObject) array.get(i)).get("lng"));
						m.setOverlaytype("marker");
						boardService.addMarker(m);
					}
				}else if(key.equals("polyline")) {
					String marker = JSONStringer.valueToString(map.get("polyline"));
					JSONArray array = (JSONArray) new JSONParser().parse(marker);
					for (int i = 0; i < array.size(); i++) {
						Marker m = new Marker();
						String latitude = "";
						String longitude = "";
							JSONArray array2 = (JSONArray) array.get(i);
							for (int j = 0; j < array2.size(); j++) {
								latitude += ((JSONObject) array2.get(j)).get("lat")+ " ";
								longitude += ((JSONObject) array2.get(j)).get("lng")+ " ";
							}
						m.setLatitude(latitude);
						m.setLongitude(longitude);
						m.setOverlaytype("polyline");
						boardService.addMarker(m);
					}
				}else if(key.equals("rectangle")) {
					String marker = JSONStringer.valueToString(map.get("rectangle"));
					JSONArray array = (JSONArray) new JSONParser().parse(marker);
					for (int i = 0; i < array.size(); i++) {
						Marker m = new Marker();
						m.setBoardid(board.getBoardid());
						JSONObject sPoint = (JSONObject)((JSONObject) array.get(i)).get("sPoint");
						JSONObject ePoint = (JSONObject)((JSONObject) array.get(i)).get("ePoint");
						m.setLatitude((String)sPoint.get("lat")+" "+(String)ePoint.get("lat"));
						m.setLongitude((String)sPoint.get("lng")+" "+(String)ePoint.get("lng"));
						m.setOverlaytype("rectangle");
						boardService.addMarker(m);
					}
				}else if(key.equals("circle")) {
					String marker = JSONStringer.valueToString(map.get("circle"));
					JSONArray array = (JSONArray) new JSONParser().parse(marker);
					for (int i = 0; i < array.size(); i++) {
						Marker m = new Marker();
						m.setBoardid(board.getBoardid());
						m.setLatitude((String)((JSONObject) array.get(i)).get("lat"));
						m.setLongitude((String)((JSONObject) array.get(i)).get("lng"));
						m.setRadius((double)((JSONObject) array.get(i)).get("radius"));
						m.setOverlaytype("circle");
						boardService.addMarker(m);
					}
				}else if(key.equals("polygon")) {
					String marker = JSONStringer.valueToString(map.get("polygon"));
					JSONArray array = (JSONArray) new JSONParser().parse(marker);
					for (int i = 0; i < array.size(); i++) {
						Marker m = new Marker();
						String latitude = "";
						String longitude = "";
							JSONArray array2 = (JSONArray) array.get(i);
							for (int j = 0; j < array2.size(); j++) {
								latitude += ((JSONObject) array2.get(j)).get("lat")+ " ";
								longitude += ((JSONObject) array2.get(j)).get("lng")+ " ";
							}
						m.setLatitude(latitude);
						m.setLongitude(longitude);
						m.setOverlaytype("polygon");
						boardService.addMarker(m);
					}
				}else if(key.equals("arrow")) {
					String marker = JSONStringer.valueToString(map.get("arrow"));
					JSONArray array = (JSONArray) new JSONParser().parse(marker);
					for (int i = 0; i < array.size(); i++) {
						Marker m = new Marker();
						String latitude = "";
						String longitude = "";
							JSONArray array2 = (JSONArray) array.get(i);
							for (int j = 0; j < array2.size(); j++) {
								latitude += ((JSONObject) array2.get(j)).get("lat")+ " ";
								longitude += ((JSONObject) array2.get(j)).get("lng")+ " ";
							}
						m.setLatitude(latitude);
						m.setLongitude(longitude);
						m.setOverlaytype("arrow");
						boardService.addMarker(m);
					}
				}else if(key.equals("ellipse")) {
					String marker = JSONStringer.valueToString(map.get("ellipse"));
					JSONArray array = (JSONArray) new JSONParser().parse(marker);
					for (int i = 0; i < array.size(); i++) {
						Marker m = new Marker();
						m.setBoardid(board.getBoardid());
						m.setLatitude((String)((JSONObject) array.get(i)).get("lat"));
						m.setLongitude((String)((JSONObject) array.get(i)).get("lng"));
						m.setRx((double)((JSONObject) array.get(i)).get("rx"));
						m.setRy((double)((JSONObject) array.get(i)).get("ry"));
						m.setOverlaytype("ellipse");
						boardService.addMarker(m);
					}
				}
				else if(key.equals("image")) {
					String marker = JSONStringer.valueToString(map.get("image"));
					JSONArray array = (JSONArray) new JSONParser().parse(marker);
					for (int i = 0; i < array.size(); i++) {
						Img img = new Img();
						img.setBoardid(board.getBoardid());
						img.setSrc((String)array.get(i));
						img.setRep(1);
						if (img.getRep() == 1)
							repnum++;
						boardService.addImg(img);
					}
				}
			}

			if (repnum == 0) {// 대표 이미지가 하나도 없으면 게시글 등록 불가능
				boardService.deleteBoard(board.getBoardid());
			} else {
				List<Integer> follower = userService.getFollower(board.getUid());
				for (int i : follower) {
					Alarm alarm = new Alarm();
					alarm.setUid(i);
					alarm.setBoardid(board.getBoardid());
					alarm.setAlarmtype(4);
					alarm.setNickname(board.getUser().getNickname());
					userService.addAlarm(alarm);
				}
				return new ResponseEntity<>(HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/board0")
	@ApiOperation(value = "게시글 등록")
	public Object addBoard(@RequestBody Board board) throws Exception {
		// 삭제 예정
		int ok = boardService.addBoard(board);
		if (ok > 0) {
			int repcnt = 0;
			for (Img i : board.getImgs()) {
				i.setBoardid(board.getBoardid());
				if (i.getRep() == 1)
					repcnt++;
			}
			if (repcnt == 0 || board.getMarkers().size() == 0) {
				boardService.deleteBoard(board.getBoardid());
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			for (Img i : board.getImgs()) {
				boardService.addImg(i);
			}
			for (Marker m : board.getMarkers()) {
				m.setBoardid(board.getBoardid());
				boardService.addMarker(m);
			}

			List<Integer> follower = userService.getFollower(board.getUid());
			for (int i : follower) {
				Alarm alarm = new Alarm();
				alarm.setUid(i);
				alarm.setBoardid(board.getBoardid());
				alarm.setAlarmtype(4);
				alarm.setNickname(board.getUser().getNickname());
				userService.addAlarm(alarm);
			}
			// System.out.println("게시글 등록되었습니다.");
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PutMapping("/board")
	@ApiOperation(value = "게시글 수정")
	public Object updateBoard(@RequestBody Board board) throws Exception {
		// 나중에 게시물 작성을 보고 따라 바꿀 예정
		Board b = boardService.findBoardByBoardId(board.getBoardid());
		if (board.getTitle() != null)
			b.setTitle(board.getTitle());
		if (board.getTripterm() != null)
			b.setTripterm(board.getTripterm());
		if (board.getKeyword() != null)
			b.setKeyword(board.getKeyword());
		if (board.getUnveiled() != b.getUnveiled())
			b.setUnveiled(1);

		boolean change = false;
		int repcnt = 0;
		List<Img> imgs = boardService.findBoardImg(board.getBoardid());

		if (imgs.size() != board.getImgs().size())
			change = true;

		if (!change) {
			List<Img> newimgs = board.getImgs();
			for (int i = 0; i < imgs.size(); i++) {
				if (imgs.get(i).getRep() != newimgs.get(i).getRep()
						|| !imgs.get(i).getSrc().equals(newimgs.get(i).getSrc())) {
					change = true;
				}
				if (newimgs.get(i).getRep() == 1)
					repcnt++;
			}
		}

		if (repcnt == 0)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		if (change) {
			for (Img i : imgs) {
				boardService.deleteImg(i.getImgid());
			}
			for (Img i : board.getImgs()) {
				i.setBoardid(board.getBoardid());
				boardService.addImg(i);
				if (i.getRep() == 1)
					repcnt++;
			}
		}

		for (Marker m : board.getMarkers()) {
			if (m.getMarkerid() > 0) {// 있던 마커면 수정
				boardService.updateMarker(m);
			} else {// 없던 마커면 새로 등록
				m.setBoardid(board.getBoardid());
				boardService.addMarker(m);
			}
		}

		int ok = boardService.updateBoard(b);
		if (ok > 0)
			return new ResponseEntity<>(HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/boardList")
	@ApiOperation(value = "게시글 전체보기")
	public Object getBoardList() throws Exception {
		List<Board> boards = boardService.getBoardList();
		for (Board b : boards) {
			List<Img> imgs = boardService.findBoardImg(b.getBoardid());
			List<Img> repimg = new ArrayList<Img>();
			for (Img i : imgs) {
				if (i.getRep() == 1)
					repimg.add(i);
			}
			b.setImgs(repimg);
			List<Comment> comments = boardService.findComment(b.getBoardid());
			for (Comment c : comments) {
				c.setUser(userService.findUserSimple(c.getUid()));
			}
			b.setCommentNum(comments.size());
			int favoriteNum = boardService.getFavoriteNum(b.getBoardid());
			boardService.updateFavoriteNum(b.getBoardid(), favoriteNum);
			b.setFavoriteNum(favoriteNum);
			b.setMarkers(boardService.findMarker(b.getBoardid()));
			b.setComments(comments);
			List<Integer> usersid = boardService.getFavoriteByBoard(b.getBoardid());
			List<User> users = new ArrayList<User>();
			for (int ui : usersid)
				users.add(userService.findUserSimple(ui));
			b.setFavorite(users);
			b.setUser(userService.findUserSimple(b.getUid()));
			b.setKeywords(b.getKeyword());
		}
		return new ResponseEntity<>(boards, HttpStatus.OK);
	}

	@GetMapping("/searchBoard/{str}")
	@ApiOperation(value = "게시글 검색")
	public Object search(@PathVariable String str) throws Exception {
		List<Board> boards = boardService.getBoardList();
		List<Board> board = new ArrayList<Board>();

		for (Board b : boards) {
			String[] keywords = b.getKeyword().split(" ");
			boolean flag = false;
			if (str.contains(b.getTitle()) || b.getTitle().contains(str)) {// 제목과 검색어 어느쪽이 완전히 포함할 경우
				flag = true;
			} else {
				for (String k : keywords) {// 검색어가 키워드를 포함할 경우
					if (str.contains(k)) {
						flag = true;
						break;
					}
				}
				if (!flag) {
					String[] titles = b.getTitle().split(" ");// 검색어가 제목의 일부를 포함할 경우
					for (String t : titles) {
						if (str.contains(t)) {
							flag = true;
							break;
						}
					}
				}
				if (!flag) {
					String[] strs = str.split(" ");// 제목이 검색어의 일부를 포함할 경우
					for (String s : strs) {
						if (b.getTitle().contains(s)) {
							flag = true;
							break;
						}
					}
				}
			}
			if (flag) {// 보드 정보를 다 모아서 목록에 넣는다
				List<Img> imgs = boardService.findBoardImg(b.getBoardid());
				List<Img> repimg = new ArrayList<Img>();
				for (Img i : imgs) {
					if (i.getRep() == 1)
						repimg.add(i);
				}
				b.setImgs(repimg);
				List<Comment> comments = boardService.findComment(b.getBoardid());
				for (Comment c : comments) {
					c.setUser(userService.findUserSimple(c.getUid()));
				}
				b.setCommentNum(comments.size());
				int favoriteNum = boardService.getFavoriteNum(b.getBoardid());
				boardService.updateFavoriteNum(b.getBoardid(), favoriteNum);
				b.setFavoriteNum(favoriteNum);
				b.setMarkers(boardService.findMarker(b.getBoardid()));
				b.setComments(comments);
				List<Integer> usersid = boardService.getFavoriteByBoard(b.getBoardid());
				List<User> users = new ArrayList<User>();
				for (int ui : usersid)
					users.add(userService.findUserSimple(ui));
				b.setFavorite(users);
				b.setUser(userService.findUserSimple(b.getUid()));
				b.setKeywords(b.getKeyword());
				board.add(b);
			}
		}
		return new ResponseEntity<>(board, HttpStatus.OK);
	}

	@PostMapping("/searchBoard")
	@ApiOperation(value = "작성한 게시글")
	public Object writedBoard(@RequestBody Map<String, String> map) throws Exception {
		if (map.get("jwt") == null)
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		int uid = (int) Jwts.parser().parseClaimsJwt(map.get("jwt")).getBody().get("uid");
		List<Board> boards = boardService.findBoardListByUid(uid);
		for (Board b : boards) {
			List<Img> imgs = boardService.findBoardImg(b.getBoardid());
			List<Img> repimg = new ArrayList<Img>();
			for (Img i : imgs) {
				if (i.getRep() == 1)
					repimg.add(i);
			}
			b.setImgs(repimg);
			List<Comment> comments = boardService.findComment(b.getBoardid());
			for (Comment c : comments) {
				c.setUser(userService.findUserSimple(c.getUid()));
			}
			b.setCommentNum(comments.size());
			int favoriteNum = boardService.getFavoriteNum(b.getBoardid());
			boardService.updateFavoriteNum(b.getBoardid(), favoriteNum);
			b.setFavoriteNum(favoriteNum);
			b.setMarkers(boardService.findMarker(b.getBoardid()));
			b.setComments(comments);
			List<Integer> usersid = boardService.getFavoriteByBoard(b.getBoardid());
			List<User> users = new ArrayList<User>();
			for (int ui : usersid)
				users.add(userService.findUserSimple(ui));
			b.setFavorite(users);
			b.setUser(userService.findUserSimple(b.getUid()));
			b.setKeywords(b.getKeyword());
		}
		return new ResponseEntity<>(boards, HttpStatus.OK);
	}

	@PostMapping("/searchComment")
	@ApiOperation(value = "작성한 댓글")
	public Object writedComment(@RequestBody Map<String, String> map) throws Exception {
		if (map.get("jwt") == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		int uid = (int) Jwts.parser().parseClaimsJwt(map.get("jwt")).getBody().get("uid");
		List<Comment> comment = boardService.findCommentByUid(uid);
		List<Comment> comments = new ArrayList<Comment>();
		for (Comment c : comment) {
			if (c.getListener() == 0) {
				addListener(comments, c, c.getBoardid());
			}
		}
		return new ResponseEntity<>(comments, HttpStatus.OK);
	}

	public void addListener(List<Comment> comments, Comment c, int boardid) throws Exception {
		List<Comment> comment = boardService.findCommentByListener(c.getCommentid());
		if (comment == null)
			return;
		comments.add(c);
		for (Comment co : comment) {
			addListener(comments, co, boardid);
		}
	}

	@PostMapping("/comment")
	@ApiOperation(value = "댓글 등록")
	public Object addComment(@RequestBody Map<String, String> map) throws Exception {
		if (map.get("jwt") == null)
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		Comment comment = new Comment();
		comment.setUid((int) Jwts.parser().parseClaimsJwt(map.get("jwt")).getBody().get("uid"));
		comment.setBoardid(Integer.parseInt(map.get("boardid")));
		comment.setContents(map.get("contents"));
		if (map.get("listener") != null)
			comment.setListener(Integer.parseInt(map.get("listenter")));
		int ok = boardService.addComment(comment);
		if (ok > 0) {
			Alarm alarm = new Alarm();
			alarm.setUid(comment.getListener() == 0 ? boardService.findBoardByBoardId(comment.getBoardid()).getUid()
					: boardService.findCommentByCommentid(comment.getListener()).getUid());
			if (comment.getListener() > 0)
				comment.getCommentid();
			alarm.setBoardid(comment.getBoardid());
			alarm.setAlarmtype(comment.getListener() == 0 ? 2 : 3);
			alarm.setNickname(userService.findUserByUid(comment.getUid()).getNickname());
			userService.addAlarm(alarm);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@DeleteMapping("/comment")
	@ApiOperation(value = "댓글 삭제")
	public Object deleteComment(@RequestBody String commentid) throws Exception {
		int ok = boardService.deleteComment(Integer.parseInt(commentid));
		if (ok > 0)
			return new ResponseEntity<>(HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@DeleteMapping("/board")
	@ApiOperation(value = "게시글 삭제")
	public Object deleteBoard(@RequestBody String boardid) throws Exception {
		int ok = boardService.deleteBoard(Integer.parseInt(boardid));
		if (ok > 0)
			return new ResponseEntity<>(HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/board/{keyword}")
	@ApiOperation(value = "키워드 게시글")
	public Object keywordBoard(@PathVariable String keyword) throws Exception {
		List<Board> boards = boardService.findBoardListByKeyword(keyword);
		for (Board b : boards) {
			List<Img> imgs = boardService.findBoardImg(b.getBoardid());
			List<Img> repimg = new ArrayList<Img>();
			for (Img i : imgs) {
				if (i.getRep() == 1)
					repimg.add(i);
			}
			b.setImgs(repimg);
			List<Comment> comments = boardService.findComment(b.getBoardid());
			for (Comment c : comments) {
				c.setUser(userService.findUserSimple(c.getUid()));
			}
			b.setCommentNum(comments.size());
			int favoriteNum = boardService.getFavoriteNum(b.getBoardid());
			boardService.updateFavoriteNum(b.getBoardid(), favoriteNum);
			b.setFavoriteNum(favoriteNum);
			b.setMarkers(boardService.findMarker(b.getBoardid()));
			b.setComments(comments);
			List<Integer> usersid = boardService.getFavoriteByBoard(b.getBoardid());
			List<User> users = new ArrayList<User>();
			for (int ui : usersid)
				users.add(userService.findUserSimple(ui));
			b.setFavorite(users);
			b.setUser(userService.findUserSimple(b.getUid()));
			b.setKeywords(b.getKeyword());
		}
		return new ResponseEntity<>(boards, HttpStatus.OK);
	}

	public boolean isOkJwt(String jwt) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date exp = format.parse(format.format(Jwts.parser().parseClaimsJwt(jwt).getBody().getExpiration()));
		Date now = format.parse(format.format(new Date()));
		if (exp.getTime() < now.getTime())// 만료기간 지났으면 인증실패
			return false;
		List<String> exps = userService
				.findBlackListByUid((int) Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid"));// 최신 로그인 이전 시점 로그인
																									// 인증실패
		if (exps != null) {
			for (String e : exps) {
				if (exp.getTime() < format.parse(e).getTime()) {
					return false;
				}
			}
		}
		if (userService.findBlackList((int) Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid"),
				format.format(Jwts.parser().parseClaimsJwt(jwt).getBody().getExpiration())) > 0)
			return false;
		return true;
	}
}
