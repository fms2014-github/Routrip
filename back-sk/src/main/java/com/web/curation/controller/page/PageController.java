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

import com.web.curation.model.board.Board;
import com.web.curation.model.board.Comment;
import com.web.curation.model.board.Img;
import com.web.curation.model.board.Marker;
import com.web.curation.model.user.Alarm;
import com.web.curation.model.user.User;
import com.web.curation.service.BoardService;
import com.web.curation.service.UserService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.swagger.annotations.ApiOperation;

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
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
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
				b.setImgs(imgs);
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
				if (b.getKeyword() != null)
					b.setKeywords(b.getKeyword());
				boards.add(b);
			}
			return new ResponseEntity<>(boards, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
	}

	public Object deleteFavorite(Map<String, String> map) throws Exception {
		String jwt = map.get("jwt");
		if (jwt == null)
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		if (isOkJwt(jwt)) {
			int boardid = Integer.parseInt(map.get("boardid"));
			int ok = boardService.deleteFavorite((int) Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid"), boardid);
			if (ok > 0)
				return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
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
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
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
				b.setImgs(imgs);
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
				if (b.getKeyword() != null)
					b.setKeywords(b.getKeyword());
				b.setScrapdate(boardService.getScrapDate((int) Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid"),
						b.getBoardid()));
				boards.add(b);
			}
			return new ResponseEntity<>(boards, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
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
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
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
				b.setImgs(imgs);
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
				if (b.getKeyword() != null)
					b.setKeywords(b.getKeyword());
			}
			return new ResponseEntity<>(boards, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
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
			if (board.getKeyword() != null)
				board.setKeywords(board.getKeyword());
			return new ResponseEntity<>(board, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
	}

	@PostMapping("/board")
	@ApiOperation(value = "게시글 작성")
	public Object addBoard2(@RequestBody Map<String, Object> map) throws Exception {
		System.out.println("게시글 작성 시작");
		String jwt = (String) map.get("jwt");
		if (jwt == null || !isOkJwt(jwt))
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

		Board board = new Board();
		board.setUid((int) Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid"));
		board.setTitle((String) map.get("title"));
		board.setTripterm((String) map.get("night") + " " + (String) map.get("day"));
		board.setKeyword((String) map.get("keywords"));
		board.setContent((String) map.get("content"));
		board.setInfo(JSONStringer.valueToString(map.get("info")));
		board.setCusInfo(JSONStringer.valueToString(map.get("cusInfo")));
		board.setUnveiled(1);
		int ok = boardService.addBoard(board);
		if (ok > 0) {
			for (String key : map.keySet()) {
				if (key.equals("jwt") || key.equals("title") || key.equals("night") || key.equals("day")
						|| key.equals("keyword") || key.equals("content") || key.equals("info")
						|| key.equals("cusInfo"))
					continue;
				if (key.equals("marker")) {
					String marker = JSONStringer.valueToString(map.get("marker"));
					JSONArray array = (JSONArray) new JSONParser().parse(marker);
					for (int i = 0; i < array.size(); i++) {
						Marker m = new Marker();
						m.setBoardid(board.getBoardid());
						m.setLatitude(String.valueOf((double) ((JSONObject) array.get(i)).get("lat")));
						m.setLongitude(String.valueOf((double) ((JSONObject) array.get(i)).get("lng")));
						m.setOverlaytype("marker");
						boardService.addMarker(m);
					}
				} else if (key.equals("polyline")) {
					String marker = JSONStringer.valueToString(map.get("polyline"));
					JSONArray array = (JSONArray) new JSONParser().parse(marker);
					for (int i = 0; i < array.size(); i++) {
						Marker m = new Marker();
						m.setBoardid(board.getBoardid());
						String latitude = "";
						String longitude = "";
						JSONArray array2 = (JSONArray) array.get(i);
						for (int j = 0; j < array2.size(); j++) {
							latitude += ((JSONObject) array2.get(j)).get("lat") + " ";
							longitude += ((JSONObject) array2.get(j)).get("lng") + " ";
						}
						m.setLatitude(latitude);
						m.setLongitude(longitude);
						m.setOverlaytype("polyline");
						boardService.addMarker(m);
					}
				} else if (key.equals("rectangle")) {
					String marker = JSONStringer.valueToString(map.get("rectangle"));
					JSONArray array = (JSONArray) new JSONParser().parse(marker);
					for (int i = 0; i < array.size(); i++) {
						Marker m = new Marker();
						m.setBoardid(board.getBoardid());
						JSONObject sPoint = (JSONObject) ((JSONObject) array.get(i)).get("sPoint");
						JSONObject ePoint = (JSONObject) ((JSONObject) array.get(i)).get("ePoint");
						m.setLatitude(String.valueOf((double) sPoint.get("lat")) + " "
								+ String.valueOf((double) ePoint.get("lat")));
						m.setLongitude(String.valueOf((double) sPoint.get("lng")) + " "
								+ String.valueOf((double) ePoint.get("lng")));
						m.setOverlaytype("rectangle");
						boardService.addMarker(m);
					}
				} else if (key.equals("circle")) {
					String marker = JSONStringer.valueToString(map.get("circle"));
					JSONArray array = (JSONArray) new JSONParser().parse(marker);
					for (int i = 0; i < array.size(); i++) {
						Marker m = new Marker();
						m.setBoardid(board.getBoardid());
						m.setLatitude(String.valueOf((double) ((JSONObject) array.get(i)).get("lat")));
						m.setLongitude(String.valueOf((double) ((JSONObject) array.get(i)).get("lng")));
						m.setRadius((double) ((JSONObject) array.get(i)).get("radius"));
						m.setOverlaytype("circle");
						boardService.addMarker(m);
					}
				} else if (key.equals("polygon")) {
					String marker = JSONStringer.valueToString(map.get("polygon"));
					JSONArray array = (JSONArray) new JSONParser().parse(marker);
					for (int i = 0; i < array.size(); i++) {
						Marker m = new Marker();
						m.setBoardid(board.getBoardid());
						String latitude = "";
						String longitude = "";
						JSONArray array2 = (JSONArray) array.get(i);
						for (int j = 0; j < array2.size(); j++) {
							latitude += ((JSONObject) array2.get(j)).get("lat") + " ";
							longitude += ((JSONObject) array2.get(j)).get("lng") + " ";
						}
						m.setLatitude(latitude);
						m.setLongitude(longitude);
						m.setOverlaytype("polygon");
						boardService.addMarker(m);
					}
				} else if (key.equals("arrow")) {
					String marker = JSONStringer.valueToString(map.get("arrow"));
					JSONArray array = (JSONArray) new JSONParser().parse(marker);
					for (int i = 0; i < array.size(); i++) {
						Marker m = new Marker();
						m.setBoardid(board.getBoardid());
						String latitude = "";
						String longitude = "";
						JSONArray array2 = (JSONArray) array.get(i);
						for (int j = 0; j < array2.size(); j++) {
							latitude += ((JSONObject) array2.get(j)).get("lat") + " ";
							longitude += ((JSONObject) array2.get(j)).get("lng") + " ";
						}
						m.setLatitude(latitude);
						m.setLongitude(longitude);
						m.setOverlaytype("arrow");
						boardService.addMarker(m);
					}
				} else if (key.equals("ellipse")) {
					String marker = JSONStringer.valueToString(map.get("ellipse"));
					JSONArray array = (JSONArray) new JSONParser().parse(marker);
					for (int i = 0; i < array.size(); i++) {
						Marker m = new Marker();
						m.setBoardid(board.getBoardid());
						m.setLatitude(String.valueOf((double) ((JSONObject) array.get(i)).get("lat")));
						m.setLongitude(String.valueOf((double) ((JSONObject) array.get(i)).get("lng")));
						String rx = String.valueOf(((JSONObject) array.get(i)).get("rx"));
						String ry = String.valueOf(((JSONObject) array.get(i)).get("ry"));
						m.setRx(Double.valueOf(rx));
						m.setRy(Double.valueOf(ry));
						m.setOverlaytype("ellipse");
						boardService.addMarker(m);
					}
				} else if (key.equals("image")) {
					String marker = JSONStringer.valueToString(map.get("image"));
					JSONArray array = (JSONArray) new JSONParser().parse(marker);
					for (int i = 0; i < array.size(); i++) {
						Img img = new Img();
						img.setBoardid(board.getBoardid());
						img.setSrc((String) array.get(i));
						boardService.addImg(img);
					}
				}
			}

			List<Integer> follower = userService.getFollower(board.getUid());
			for (int i : follower) {
				Alarm alarm = new Alarm();
				alarm.setUid(i);
				alarm.setBoardid(board.getBoardid());
				alarm.setAlarmtype(4);
				alarm.setNickname(userService
						.findUserByUid((int) Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid")).getNickname());
				userService.addAlarm(alarm);
			}
			System.out.println("게시글 작성 완료");
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PutMapping("/board")
	@ApiOperation(value = "게시글 수정")
	public Object updateBoard(@RequestBody Map<String, Object> map) throws Exception {
		System.out.println("게시글 수정 시작");
		String jwt = (String) map.get("jwt");
		if (jwt == null || !isOkJwt(jwt))
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		Board board = boardService.findBoardByBoardId((int) map.get("boardid"));
		int uid = (int) Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid");
		if (uid != board.getUid())
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		board.setTitle((String) map.get("title"));
		board.setTripterm((String) map.get("night") + " " + (String) map.get("day"));
		board.setKeyword((String) map.get("keywords"));
		board.setContent((String) map.get("content"));
		board.setInfo(JSONStringer.valueToString(map.get("info")));
		board.setCusInfo((String) map.get(JSONStringer.valueToString(map.get("cusInfo"))));
		board.setUnveiled(1);
		int ok = boardService.updateBoard(new Board());
		boardService.deleteImgByBoardid(board.getBoardid());// 싹 다 날리고 새로 추가한다
		boardService.deleteMarkerByBoardid(board.getBoardid());
		for (String key : map.keySet()) {
			if (key.equals("jwt") || key.equals("title") || key.equals("night") || key.equals("day")
					|| key.equals("keyword") || key.equals("content") || key.equals("info") || key.equals("cusInfo"))
				continue;
			if (key.equals("marker")) {
				String marker = JSONStringer.valueToString(map.get("marker"));
				JSONArray array = (JSONArray) new JSONParser().parse(marker);
				for (int i = 0; i < array.size(); i++) {
					Marker m = new Marker();
					m.setBoardid(board.getBoardid());
					m.setLatitude(String.valueOf((double) ((JSONObject) array.get(i)).get("lat")));
					m.setLongitude(String.valueOf((double) ((JSONObject) array.get(i)).get("lng")));
					m.setOverlaytype("marker");
					boardService.addMarker(m);
				}
			} else if (key.equals("polyline")) {
				String marker = JSONStringer.valueToString(map.get("polyline"));
				JSONArray array = (JSONArray) new JSONParser().parse(marker);
				for (int i = 0; i < array.size(); i++) {
					Marker m = new Marker();
					m.setBoardid(board.getBoardid());
					String latitude = "";
					String longitude = "";
					JSONArray array2 = (JSONArray) array.get(i);
					for (int j = 0; j < array2.size(); j++) {
						latitude += ((JSONObject) array2.get(j)).get("lat") + " ";
						longitude += ((JSONObject) array2.get(j)).get("lng") + " ";
					}
					m.setLatitude(latitude);
					m.setLongitude(longitude);
					m.setOverlaytype("polyline");
					boardService.addMarker(m);
				}
			} else if (key.equals("rectangle")) {
				String marker = JSONStringer.valueToString(map.get("rectangle"));
				JSONArray array = (JSONArray) new JSONParser().parse(marker);
				for (int i = 0; i < array.size(); i++) {
					Marker m = new Marker();
					m.setBoardid(board.getBoardid());
					JSONObject sPoint = (JSONObject) ((JSONObject) array.get(i)).get("sPoint");
					JSONObject ePoint = (JSONObject) ((JSONObject) array.get(i)).get("ePoint");
					m.setLatitude(String.valueOf((double) sPoint.get("lat")) + " "
							+ String.valueOf((double) ePoint.get("lat")));
					m.setLongitude(String.valueOf((double) sPoint.get("lng")) + " "
							+ String.valueOf((double) ePoint.get("lng")));
					m.setOverlaytype("rectangle");
					boardService.addMarker(m);
				}
			} else if (key.equals("circle")) {
				String marker = JSONStringer.valueToString(map.get("circle"));
				JSONArray array = (JSONArray) new JSONParser().parse(marker);
				for (int i = 0; i < array.size(); i++) {
					Marker m = new Marker();
					m.setBoardid(board.getBoardid());
					m.setLatitude(String.valueOf((double) ((JSONObject) array.get(i)).get("lat")));
					m.setLongitude(String.valueOf((double) ((JSONObject) array.get(i)).get("lng")));
					m.setRadius((double) ((JSONObject) array.get(i)).get("radius"));
					m.setOverlaytype("circle");
					boardService.addMarker(m);
				}
			} else if (key.equals("polygon")) {
				String marker = JSONStringer.valueToString(map.get("polygon"));
				JSONArray array = (JSONArray) new JSONParser().parse(marker);
				for (int i = 0; i < array.size(); i++) {
					Marker m = new Marker();
					m.setBoardid(board.getBoardid());
					String latitude = "";
					String longitude = "";
					JSONArray array2 = (JSONArray) array.get(i);
					for (int j = 0; j < array2.size(); j++) {
						latitude += ((JSONObject) array2.get(j)).get("lat") + " ";
						longitude += ((JSONObject) array2.get(j)).get("lng") + " ";
					}
					m.setLatitude(latitude);
					m.setLongitude(longitude);
					m.setOverlaytype("polygon");
					boardService.addMarker(m);
				}
			} else if (key.equals("arrow")) {
				String marker = JSONStringer.valueToString(map.get("arrow"));
				JSONArray array = (JSONArray) new JSONParser().parse(marker);
				for (int i = 0; i < array.size(); i++) {
					Marker m = new Marker();
					m.setBoardid(board.getBoardid());
					String latitude = "";
					String longitude = "";
					JSONArray array2 = (JSONArray) array.get(i);
					for (int j = 0; j < array2.size(); j++) {
						latitude += ((JSONObject) array2.get(j)).get("lat") + " ";
						longitude += ((JSONObject) array2.get(j)).get("lng") + " ";
					}
					m.setLatitude(latitude);
					m.setLongitude(longitude);
					m.setOverlaytype("arrow");
					boardService.addMarker(m);
				}
			} else if (key.equals("ellipse")) {
				String marker = JSONStringer.valueToString(map.get("ellipse"));
				JSONArray array = (JSONArray) new JSONParser().parse(marker);
				for (int i = 0; i < array.size(); i++) {
					Marker m = new Marker();
					m.setBoardid(board.getBoardid());
					m.setLatitude(String.valueOf((double) ((JSONObject) array.get(i)).get("lat")));
					m.setLongitude(String.valueOf((double) ((JSONObject) array.get(i)).get("lng")));
					String rx = String.valueOf(((JSONObject) array.get(i)).get("rx"));
					String ry = String.valueOf(((JSONObject) array.get(i)).get("ry"));
					m.setRx(Double.valueOf(rx));
					m.setRy(Double.valueOf(ry));
					m.setOverlaytype("ellipse");
					boardService.addMarker(m);
				}
			} else if (key.equals("image")) {
				String marker = JSONStringer.valueToString(map.get("image"));
				JSONArray array = (JSONArray) new JSONParser().parse(marker);
				for (int i = 0; i < array.size(); i++) {
					Img img = new Img();
					img.setBoardid(board.getBoardid());
					img.setSrc((String) array.get(i));
					boardService.addImg(img);
				}
			}
		}
		if (ok > 0) {
			System.out.println("게시글 수정 완료");
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/boardList")
	@ApiOperation(value = "게시글 전체보기")
	public Object getBoardList() throws Exception {
		List<Board> boards = boardService.getBoardList();
		for (Board b : boards) {
			List<Img> imgs = boardService.findBoardImg(b.getBoardid());
			b.setImgs(imgs);
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
			if (b.getKeyword() != null)
				b.setKeywords(b.getKeyword());
		}
		return new ResponseEntity<>(boards, HttpStatus.OK);
	}

	@PostMapping("/boardList/{lastDate}")
	@ApiOperation(value = "게시글 전체보기(날짜구분/4개씩)")
	public Object postBoardList(@PathVariable String lastDate) throws Exception {
		if (lastDate == null || lastDate.equals("0")) {
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			lastDate = format1.format(new Date());
		}
		List<Board> boards = boardService.getBoardList5(lastDate);
		for (Board b : boards) {
			List<Img> imgs = boardService.findBoardImg(b.getBoardid());
			b.setImgs(imgs);
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
			if (b.getKeyword() != null)
				b.setKeywords(b.getKeyword());
		}
		return new ResponseEntity<>(boards, HttpStatus.OK);
	}

	@PostMapping("/boardList")
	@ApiOperation(value = "게시글 전체보기(날짜구분)")
	public Object BoardList(@RequestBody Map<String, String> map) throws Exception {
		String lastDate = map.get("lastDate");
		if (lastDate == null || lastDate.equals("0")) {
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			lastDate = format1.format(new Date());
		}
		List<Board> boards = boardService.getBoardListByLastWrite(lastDate);
		for (Board b : boards) {
			List<Img> imgs = boardService.findBoardImg(b.getBoardid());
			b.setImgs(imgs);
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
			if (b.getKeyword() != null)
				b.setKeywords(b.getKeyword());
		}
		return new ResponseEntity<>(boards, HttpStatus.OK);
	}

	@GetMapping("/bestBoard")
	@ApiOperation(value = "베스트 게시글")
	public Object bestBoard() throws Exception {
		List<Board> tempboards = boardService.getBoardList();
		for (Board b : tempboards) {
			boardService.updateFavoriteNum(b.getBoardid(), boardService.getFavoriteNum(b.getBoardid()));
		}
		List<Board> boards = boardService.findBoardBest();
		for (Board b : boards) {
			List<Img> imgs = boardService.findBoardImg(b.getBoardid());
			b.setImgs(imgs);
			List<Comment> comments = boardService.findComment(b.getBoardid());
			for (Comment c : comments) {
				c.setUser(userService.findUserSimple(c.getUid()));
			}
			b.setCommentNum(comments.size());
			int favoriteNum = boardService.getFavoriteNum(b.getBoardid());
			b.setFavoriteNum(favoriteNum);
			b.setMarkers(boardService.findMarker(b.getBoardid()));
			b.setComments(comments);
			List<Integer> usersid = boardService.getFavoriteByBoard(b.getBoardid());
			List<User> users = new ArrayList<User>();
			for (int ui : usersid)
				users.add(userService.findUserSimple(ui));
			b.setFavorite(users);
			b.setUser(userService.findUserSimple(b.getUid()));
			if (b.getKeyword() != null)
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
			String title = b.getTitle().toLowerCase();
			String strlow = str.toLowerCase();
			String[] keywords = { "                 " };
			if (b.getKeyword() != null)
				keywords = b.getKeyword().toLowerCase().split(" ");
			boolean flag = false;
			if (strlow.contains(title) || title.contains(strlow)) {// 제목과 검색어 어느쪽이 완전히 포함할 경우
				flag = true;
			} else {
				for (String k : keywords) {// 검색어가 키워드를 포함할 경우
					if (strlow.contains(k)) {
						flag = true;
						break;
					}
				}
				if (!flag) {
					String[] titles = title.split(" ");// 검색어가 제목의 일부를 포함할 경우
					for (String t : titles) {
						if (t.length() == 0)
							continue;
						if (strlow.contains(t)) {
							flag = true;
							break;
						}
					}
				}
				if (!flag) {
					String[] strs = strlow.split(" ");// 제목이 검색어의 일부를 포함할 경우
					for (String s : strs) {
						if (s.length() == 0)
							continue;
						if (title.contains(s)) {
							flag = true;
							break;
						}
					}
				}
			}
			if (flag) {// 보드 정보를 다 모아서 목록에 넣는다
				List<Img> imgs = boardService.findBoardImg(b.getBoardid());
				b.setImgs(imgs);
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
				if (b.getKeyword() != null)
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
		if (isOkJwt(map.get("jwt"))) {
			int uid = (int) Jwts.parser().parseClaimsJwt(map.get("jwt")).getBody().get("uid");
			List<Board> boards = boardService.findBoardListByUid(uid);
			for (Board b : boards) {
				List<Img> imgs = boardService.findBoardImg(b.getBoardid());
				b.setImgs(imgs);
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
				if (b.getKeyword() != null)
					b.setKeywords(b.getKeyword());
			}
			return new ResponseEntity<>(boards, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
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
		if (isOkJwt(map.get("jwt"))) {
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
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
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
			b.setImgs(imgs);
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
			if (b.getKeyword() != null)
				b.setKeywords(b.getKeyword());
		}
		return new ResponseEntity<>(boards, HttpStatus.OK);
	}

	public boolean isOkJwt(String jwt) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date exp;
			exp = format.parse(format.format(Jwts.parser().parseClaimsJwt(jwt).getBody().getExpiration()));
			int uid = (int) Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid");

			List<String> exps = userService.findBlackListByUid(uid);
			// 최신 로그인 이전 시점 인증 실패
			if (exps != null) {
				for (String e : exps) {
					if (exp.getTime() < format.parse(e).getTime()) {
						System.out.println("이미 로그아웃한 아이디입니다. : 현재 시점에 이 jwt보다 최근 아이디가 로그아웃되어있습니다.");
						return false;
					}
				}
			}
			if (userService.findBlackList(uid,
					format.format(Jwts.parser().parseClaimsJwt(jwt).getBody().getExpiration())) > 0)
				// 블랙 리스트에 있으면 인증 실패
				return false;
		} catch (ExpiredJwtException e1) {
			System.out.println("토큰 기간 만료");
			return false;
		} catch (UnsupportedJwtException e) {
			System.out.println("UnsupportedJwtException 발생");
			return false;
		} catch (MalformedJwtException e) {
			System.out.println("MalformedJwtException 발생");
			return false;
		} catch (Exception e) {
			System.out.println("오류가 발생했습니다.");
			return false;
		}
		return true;
	}
}
