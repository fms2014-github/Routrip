package com.web.curation.controller.page;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.web.curation.model.BasicResponse;
import com.web.curation.model.board.*;
import com.web.curation.model.user.User;
import com.web.curation.service.BoardService;
import com.web.curation.service.UserService;

import io.swagger.annotations.*;

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
	public Object addFavorite(@RequestBody User user, int boardid) throws Exception {
		int ok = 0;
		List<Integer> usersid = boardService.getFavoriteByBoard(boardid);
		boolean flag = true;
		for (int i : usersid) {
			if (i == user.getUid()) {
				flag = false;
				break;
			}
		}
		if (flag)
			ok = boardService.addFavorite(user.getUid(), boardid);
		if (ok > 0)
			return new ResponseEntity<>(HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/favoriteUser")
	@ApiOperation(value = "좋아요 누른 사람")
	public Object FavoriteListByBoard(@RequestBody Board board) throws Exception {
		List<Integer> usersid = boardService.getFavoriteByBoard(board.getBoardid());
		List<User> users = new ArrayList<User>();
		for (int i : usersid)
			users.add(userService.findUserByUid(i));
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@PostMapping("/favoriteBoard")
	@ApiOperation(value = "좋아요 누른 게시글")
	public Object FavoriteListByUser(@RequestBody User user) throws Exception {
		List<Integer> boardsid = boardService.getFavoriteByUser(user.getUid());
		List<Board> boards = new ArrayList<Board>();
		for (int i : boardsid) {
			Board b = boardService.findBoardByBoardId(i);
			List<Img> imgs = boardService.findBoardImg(b.getBoardid());
			List<Img> repimg = new ArrayList<Img>();// 대표 이미지들 들어갈 리스트
			for (Img img : imgs) {
				if (img.getRep() == 1)
					repimg.add(img);
			}
			b.setImgs(repimg);
			List<Comment> comments = boardService.findComment(b.getBoardid());
			b.setCommentNum(comments.size());
			b.setFavoriteNum(boardService.getFavoriteNum(b.getBoardid()));
			b.setMarkers(boardService.findMarker(b.getBoardid()));
			boards.add(b);
		}
		return new ResponseEntity<>(boards, HttpStatus.OK);
	}

	@DeleteMapping("/favorite")
	@ApiOperation(value = "좋아요 해제")
	public Object deleteFavorite(@RequestBody User user, Board board) throws Exception {
		int ok = boardService.deleteFavorite(user.getUid(), board.getBoardid());
		if (ok > 0)
			return new ResponseEntity<>(HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/boardDetail")
	@ApiOperation(value = "게시글 상세 정보")
	public Object getBoardDetail(@RequestBody User user, int boardid) throws Exception {
		Board board = boardService.findBoardByBoardId(boardid);
		if (board.getUnveiled() == 0 && user.getUid() != board.getUid()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		board.setImgs(boardService.findBoardImg(board.getBoardid()));
		List<Comment> comments = boardService.findComment(board.getBoardid());
		board.setCommentNum(comments.size());
		board.setComments(comments);
		board.setFavoriteNum(boardService.getFavoriteNum(board.getBoardid()));
		board.setMarkers(boardService.findMarker(board.getBoardid()));
		return new ResponseEntity<>(board, HttpStatus.OK);
	}

	@PostMapping("/board")
	@ApiOperation(value = "게시글 등록")
	public Object addBoard(@RequestBody Board board, int uid) throws Exception {
		board.setUid(uid);
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

			for (Marker m : board.getMarkers()) {
				m.setBoardid(board.getBoardid());
			}
			
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PutMapping("/board")
	@ApiOperation(value = "게시글 수정")
	public Object updateBoard(@RequestBody Board board) throws Exception {
		Board b = boardService.findBoardByBoardId(board.getBoardid());
		if (board.getTitle() != null)
			b.setTitle(board.getTitle());
		if (board.getTripterm() != null)
			b.setTripterm(board.getTripterm());
		if (board.getKeyword() != null)
			b.setKeyword(board.getKeyword());
		if (board.getUnveiled() != b.getUnveiled())
			b.setUnveiled(board.getUnveiled());

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

		for(Marker m : board.getMarkers()) {
			//이것도 다 삭제하고 다시 등록하도록 변경
			boardService.updateMarker(m);
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
			List<Img> repimg = new ArrayList<Img>();// 대표 이미지들 들어갈 리스트
			for (Img i : imgs) {
				if (i.getRep() == 1)
					repimg.add(i);
			}
			b.setImgs(repimg);
			List<Comment> comments = boardService.findComment(b.getBoardid());
			b.setCommentNum(comments.size());
			b.setFavoriteNum(boardService.getFavoriteNum(b.getBoardid()));
			b.setMarkers(boardService.findMarker(b.getBoardid()));
		}
		return new ResponseEntity<>(boards, HttpStatus.OK);
	}

	@PostMapping("/searchBoard")
	@ApiOperation(value = "게시글 검색")
	public Object search(@RequestBody String str) throws Exception {
		List<Board> boards = boardService.getBoardList();
		List<Board> board = new ArrayList<Board>();

		for (Board b : boards) {
			String[] keywords = b.getKeyword().split(" ");
			boolean flag = false;
			if (str.contains(b.getTitle()) || b.getTitle().contains(str)) {
				flag = true;
			} else {
				for (String k : keywords) {
					if (str.contains(k)) {
						flag = true;
						break;
					}
				}
				if (!flag) {
					String[] titles = b.getTitle().split(" ");
					for (String t : titles) {
						if (str.contains(t)) {
							flag = true;
							break;
						}
					}
				}
			}
			if (flag) {// 보드 정보를 다 모아서 목록에 넣는다
				List<Img> imgs = boardService.findBoardImg(b.getBoardid());
				List<Img> repimg = new ArrayList<Img>();// 대표 이미지들 들어갈 리스트
				for (Img i : imgs) {
					if (i.getRep() == 1)
						repimg.add(i);
				}
				b.setImgs(repimg);
				List<Comment> comments = boardService.findComment(b.getBoardid());
				b.setCommentNum(comments.size());
				b.setFavoriteNum(boardService.getFavoriteNum(b.getBoardid()));
				b.setMarkers(boardService.findMarker(b.getBoardid()));
				board.add(b);
			}
		}
		return new ResponseEntity<>(board, HttpStatus.OK);
	}

	@PostMapping("/writeBoard")
	@ApiOperation(value = "작성한 게시글")
	public Object writedBoard(@RequestBody User user) throws Exception {
		List<Board> boards = boardService.findBoardListByUid(user.getUid());
		for (Board b : boards) {
			List<Img> imgs = boardService.findBoardImg(b.getBoardid());
			List<Img> repimg = new ArrayList<Img>();// 대표 이미지들 들어갈 리스트
			for (Img i : imgs) {
				if (i.getRep() == 1)
					repimg.add(i);
			}
			b.setImgs(repimg);
			List<Comment> comments = boardService.findComment(b.getBoardid());
			b.setCommentNum(comments.size());
			b.setFavoriteNum(boardService.getFavoriteNum(b.getBoardid()));
			b.setMarkers(boardService.findMarker(b.getBoardid()));
		}
		return new ResponseEntity<>(boards, HttpStatus.OK);
	}

	@PostMapping("/comment")
	@ApiOperation(value = "댓글 등록")
	public Object addComment(@RequestBody Comment comment, int boardid, int uid) throws Exception {
		comment.setBoardid(boardid);
		comment.setUid(uid);
		int ok = boardService.addComment(comment);
		// comment 에 listener 가 있으면 listener 한테 알람? 백엔드에서 보내야하나?
		if (ok > 0)
			return new ResponseEntity<>(HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/deleteComment")
	@ApiOperation(value = "댓글 삭제")
	public Object deleteComment(@RequestBody Comment comment) throws Exception {
		int ok = boardService.deleteComment(comment.getCommentid());
		if (ok > 0)
			return new ResponseEntity<>(HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
}
