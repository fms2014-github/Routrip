package com.web.curation.controller.account;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;

import org.mindrot.jbcrypt.BCrypt;
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
@RequestMapping("/account")
@RestController
public class AccountController {

	@Autowired
	private UserService userService;

	@Autowired
	private BoardService boardService;

//	private String key = "webcuration-routrip-secretkey";
//	byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(key);
//	Key signingKey = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());

	@PostMapping("/login")
	@ApiOperation(value = "로그인")
	public Object login(@RequestBody User user) throws Exception {
		User loginUser = userService.findUserByEmail(user.getEmail(), 0);
		if (loginUser == null || !BCrypt.checkpw(user.getPassword(), loginUser.getPassword()))
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		String jwt = Jwts.builder().setHeaderParam("typ", "JWT").setSubject(String.valueOf(loginUser.getUid()))
				.claim("uid", loginUser.getUid()).claim("email", loginUser.getEmail())
				.claim("nickname", loginUser.getNickname()).claim("profileImg", loginUser.getProfileImg())
				.claim("loginApi", loginUser.getLoginApi())
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 12))
				// .signWith(SignatureAlgorithm.HS256, key)
				.compact();
		return new ResponseEntity<>(jwt, HttpStatus.OK);
	}

	@PostMapping("/snslogin")
	@ApiOperation(value = "sns로그인")
	public Object snslogin(@RequestBody User user) throws Exception {
		User loginUser = null;
		List<User> temp = userService.findUserByLoginApi(user.getLoginApi());
		for (User u : temp) {
			if (BCrypt.checkpw(user.getUserid(), u.getUserid())) {
				loginUser = u;
				break;
			}
		}
		if (loginUser == null)
			return new ResponseEntity<>(HttpStatus.OK);
		String jwt = Jwts.builder().setHeaderParam("typ", "JWT").setSubject(String.valueOf(loginUser.getUid()))
				.claim("uid", loginUser.getUid()).claim("email", loginUser.getEmail())
				.claim("nickname", loginUser.getNickname()).claim("profileImg", loginUser.getProfileImg())
				.claim("loginApi", loginUser.getLoginApi())
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 12))
				// .signWith(SignatureAlgorithm.HS256, key)
				.compact();
		return new ResponseEntity<>(jwt, HttpStatus.OK);
	}

	@PostMapping("/logout")
	@ApiOperation(value = "로그아웃")
	public Object logout(@RequestBody Map<String, String> map) throws Exception {
		String jwt = map.get("jwt");
		if (jwt == null)
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		if (isOkJwt(jwt)) {
			int uid = (int) Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid");
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String exp = format.format(Jwts.parser().parseClaimsJwt(jwt).getBody().getExpiration());
			userService.deleteBlackList();
			userService.addBlackList(uid, exp, jwt);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
	}

	@PostMapping("/follow")
	@ApiOperation(value = "팔로우 등록")
	public Object following(@RequestBody Map<String, String> map) throws Exception {
		String jwt = map.get("jwt");
		if (jwt == null)
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		if (isOkJwt(jwt)) {
			int uid = Integer.parseInt(map.get("uid"));
			int ok = 0;
			List<Integer> follower = userService
					.getFollow((int) Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid"));
			boolean flag = true;
			for (int i : follower) {
				if (i == uid) {
					flag = false;
					deleteFollow(map);
					return new ResponseEntity<>(HttpStatus.OK);
				}
			}
			if (flag)
				ok = userService.addFollow((int) Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid"), uid);
			if (ok > 0) {
				Alarm alarm = new Alarm();
				alarm.setUid(uid);
				alarm.setActionid((int) Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid"));
				alarm.setAlarmtype(1);
				userService.addAlarm(alarm);
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/following")
	@ApiOperation(value = "팔로우 정보 조회")
	public Object followList(@RequestBody Map<String, String> map) throws Exception {
		List<Integer> list = new ArrayList<Integer>();
		if (map.get("uid") != null) {
			list = userService.getFollow(Integer.parseInt(map.get("uid")));
		} else if (map.get("jwt") != null) {
			if (isOkJwt(map.get("jwt")))
				list = userService.getFollow((int) Jwts.parser().parseClaimsJwt(map.get("jwt")).getBody().get("uid"));
			else
				return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}
		List<User> userlist = new ArrayList<User>();
		for (Integer i : list)
			userlist.add(userService.findUserSimple(i));
		return new ResponseEntity<>(userlist, HttpStatus.OK);
	}

	@PostMapping("/follower")
	@ApiOperation(value = "팔로워 정보 조회")
	public Object followerList(@RequestBody Map<String, String> map) throws Exception {
		List<Integer> list = new ArrayList<Integer>();
		if (map.get("uid") != null) {
			list = userService.getFollower(Integer.parseInt(map.get("uid")));
		} else if (map.get("jwt") != null) {
			if (isOkJwt(map.get("jwt")))
				list = userService.getFollower((int) Jwts.parser().parseClaimsJwt(map.get("jwt")).getBody().get("uid"));
			else
				return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}
		List<User> userlist = new ArrayList<User>();
		for (Integer i : list)
			userlist.add(userService.findUserSimple(i));
		return new ResponseEntity<>(userlist, HttpStatus.OK);
	}

	public Object deleteFollow(Map<String, String> map) throws Exception {
		String jwt = map.get("jwt");
		if (jwt == null)
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		if (isOkJwt(jwt)) {
			int uid = Integer.parseInt(map.get("uid"));
			int ok = userService.deleteFollow((int) Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid"), uid);
			if (ok > 0)
				return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@DeleteMapping("/user")
	@ApiOperation(value = "탈퇴하기")
	public Object deleteUser(@RequestBody Map<String, String> map) throws Exception {
		String jwt = map.get("jwt");
		if (jwt == null)
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		if (isOkJwt(jwt)) {
			int uid = (int) Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid");
			User user = userService.findUserByUid(uid);
			int ok = 0;
			boolean flag = false;
			if ((int) Jwts.parser().parseClaimsJwt(jwt).getBody().get("loginApi") != 0
					|| BCrypt.checkpw(map.get("password"), user.getPassword()))
				flag = true;
			if (flag)
				ok = userService.deleteUser(uid);
			if (ok > 0) {
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PutMapping("/password")
	@ApiOperation(value = "비밀번호 변경")
	public Object updatePassword(@RequestBody User user) throws Exception {
		user.setUid(userService.findUserByEmail(user.getEmail(), 0).getUid());
		user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
		int ok = userService.updateProfile(user);
		if (ok > 0) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PutMapping("/user")
	@ApiOperation(value = "회원정보 변경") // 프로필 이미지, 닉네임 변경
	public Object updateProfile(@RequestBody Map<String, String> map) throws Exception {
		String jwt = map.get("jwt");
		if (jwt == null)
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		if (isOkJwt(jwt)) {
			int uid = (int) Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid");
			String profileImg = (String) Jwts.parser().parseClaimsJwt(jwt).getBody().get("profileImg");
			String nickname = (String) Jwts.parser().parseClaimsJwt(jwt).getBody().get("nickname");
			User user = new User();
			int ok = 0;
			user.setUid(uid);
			if (map.get("profileImg") != null && !profileImg.equals(map.get("profileImg"))) {
				user.setProfileImg(map.get("profileImg"));
				ok = userService.updateProfile(user);
			} else if (map.get("nickname") != null && !nickname.equals(map.get("nickname"))) {
				user.setNickname(map.get("nickname"));
				ok = userService.updateProfile(user);
			}
			if (ok > 0) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String exp = format.format(Jwts.parser().parseClaimsJwt(jwt).getBody().getExpiration());
				userService.deleteBlackList();
				userService.addBlackList(uid, exp, jwt);
				user = userService.findUserByUid(uid);
				jwt = Jwts.builder().setHeaderParam("typ", "JWT").setSubject(String.valueOf(user.getUid()))
						.claim("uid", user.getUid()).claim("email", user.getEmail())
						.claim("nickname", user.getNickname()).claim("profileImg", user.getProfileImg())
						.claim("loginApi", user.getLoginApi())
						.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 12))
						// .signWith(SignatureAlgorithm.HS256, key)
						.compact();
				return new ResponseEntity<>(jwt, HttpStatus.OK);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/snssignup")
	@ApiOperation(value = "sns가입하기")
	public Object snsSignup(@Valid @RequestBody User user) throws Exception {
		int ok = 0;
		user.setUserkey("Y");
		user.setUserid(BCrypt.hashpw(user.getUserid(), BCrypt.gensalt()));
		ok = userService.addUser(user);
		if (ok > 0) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/signup")
	@ApiOperation(value = "가입하기")
	public Object signup(@Valid @RequestBody User user) throws Exception {
		boolean flag = true;
		if (user.getLoginApi() == 0) {
			List<User> userlist = userService.getUserList();
			for (User u : userlist) {
				if (u.getEmail().equalsIgnoreCase(user.getEmail())) {
					int d = userService.deleteUserNoJoin(u.getUid());
					if (d == 0) {
						System.out.println("이미 존재하는 이메일입니다.");
						flag = false;
					}
					break;
				}
			}
		}
		int ok = 0;
		if (flag) {
			String authNum = RandomNum();
			user.setUserkey(authNum);
			user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
			ok = userService.addUser(user);
			String body = "인증번호는 [ " + authNum + " ] 입니다.";
			sendEmail(user.getEmail(), body);
		}
		if (ok > 0) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PutMapping("/signup")
	@ApiOperation(value = "가입완료")
	public Object updateUserKey(@RequestBody User tempuser) throws Exception {
		User user = userService.findUserNoJoin(tempuser.getEmail(), 0);
		if (user.getUserkey().equals(tempuser.getUserkey())) {
			int ok = userService.updateUserKey(user.getUid());
			if (ok > 0) {
				return new ResponseEntity<>(HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/email")
	@ApiOperation(value = "이메일 찾기")
	public Object findEmail(@RequestBody User user) throws Exception {
		List<String> email = userService.findEmail(user);
		if (email.isEmpty())
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		for (int e = 0; e < email.size(); e++) {
			String s = email.get(e);
			char[] c = s.toCharArray();
			int g = 0;
			for (g = 1; g < c.length; g++) {
				if (c[g] == '@')
					break;
			}
			for (int i = g - 1; i >= g / 3 * 2; i--) {
				c[i] = '*';
			}
			s = String.valueOf(c);
			email.set(e, s);
		}
		return new ResponseEntity<>(email, HttpStatus.OK);
	}

	@GetMapping("/password/{email}")
	@ApiOperation(value = "비밀번호 변경 인증번호")
	public Object findPassword(@PathVariable String email) throws Exception {
		String certNum = RandomNum();
		String body = "인증번호는 [ " + certNum + " ] 입니다.";
		sendEmail(email, body);
		return new ResponseEntity<>(certNum, HttpStatus.OK);
	}

	@PostMapping("/decode")
	@ApiOperation(value = "유저 토큰 해석")
	public Object decode2(@RequestBody Map<String, String> map) throws Exception {
		String jwt = map.get("jwt");
		if (jwt == null)
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		if (isOkJwt(jwt)) {
			User user = new User();
			user.setUid((int) Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid"));
			user.setEmail((String) Jwts.parser().parseClaimsJwt(jwt).getBody().get("email"));
			user.setNickname((String) Jwts.parser().parseClaimsJwt(jwt).getBody().get("nickname"));
			user.setProfileImg((String) Jwts.parser().parseClaimsJwt(jwt).getBody().get("profileImg"));
			user.setLoginApi((int) Jwts.parser().parseClaimsJwt(jwt).getBody().get("loginApi"));
			return new ResponseEntity<>(user, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
	}

	@PostMapping("/alarm")
	@ApiOperation(value = "유저 알림")
	public Object alarm(@RequestBody Map<String, String> map) throws Exception {
		String jwt = map.get("jwt");
		if (jwt == null)
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		if (isOkJwt(jwt)) {
			List<Alarm> alarms = userService.getAlarm((int) Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid"));
			userService.updateAlarm((int) Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid"));
			for (Alarm a : alarms) {
				if (a.getAlarmtype() == 1) {
					User user = userService.findUserSimple(a.getActionid());
					a.setText(user.getNickname() + " 님이 팔로우하셨습니다.");
					a.setUser(user);
				} else if (a.getAlarmtype() == 2) {
					User user = userService.findUserSimple(a.getActionid());
					Comment comment = boardService.findCommentByCommentid(Integer.parseInt(a.getCommentid()));
					a.setText(user.getNickname() + " 님이 댓글다셨습니다.");
					a.setDetail(comment.getContents());
					a.setUser(user);
				} else if (a.getAlarmtype() == 3) {
					User user = userService.findUserSimple(a.getActionid());
					Comment comment = boardService.findCommentByCommentid(Integer.parseInt(a.getCommentid()));
					a.setText(user.getNickname() + " 님이 대댓글다셨습니다.");
					a.setDetail(comment.getContents());
					a.setUser(user);
				} else if (a.getAlarmtype() == 4) {
					User user = userService.findUserSimple(a.getActionid());
					Board board = boardService.findBoardByBoardId(Integer.parseInt(a.getBoardid()));
					a.setText(user.getNickname() + " 님이 글을 올리셨습니다.");
					a.setDetail(board.getTitle());
					a.setUser(user);
				} else if (a.getAlarmtype() == 5) {
					User user = userService.findUserSimple(a.getActionid());
					Board board = boardService.findBoardByBoardId(Integer.parseInt(a.getBoardid()));
					a.setText(user.getNickname() + " 님이 [" + board.getTitle() + "] 글을 좋아합니다.");
					a.setDetail(board.getTitle());
					a.setUser(user);
				}
			}
			return new ResponseEntity<>(alarms, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
	}

	@DeleteMapping("/alarm")
	@ApiOperation(value = "알림 삭제")
	public Object deleteAlarm(@RequestBody Map<String, String> map) throws Exception {
		int alarmid = Integer.parseInt(map.get("alarmid"));
		int ok = userService.deleteAlarm(alarmid);
		if (ok > 0)
			return new ResponseEntity<>(HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	// 인증번호 생성기 (6글자)
	public String RandomNum() {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < 6; i++) {
			int n = (int) (Math.random() * 10);
			buffer.append(n);
		}
		return buffer.toString();
	}

	public void sendEmail(String usermail, String body) {
		String result = usermail.substring(usermail.lastIndexOf("@") + 1);
		Session s = null;

		if (result.equals("naver.com")) {
			s = naver();
		} else if (result.equals("gmail.com")) {
			s = gmail();
		} else if (result.equals("daum.net")) {
			s = daum();
		} else {
			System.out.println("잘못된 형식 : 콘솔에 출력");
		}
		MimeMessage msg = new MimeMessage(s);
		try {
			msg.setSentDate(new Date());
			msg.setFrom(new InternetAddress("routrip@naver.com", "루트립관리자")); // 발송자
			InternetAddress to = new InternetAddress(usermail); // 수신자
			msg.setRecipient(Message.RecipientType.TO, to);
			msg.setSubject("루트립입니다", "UTF-8");
			msg.setText(body, "UTF-8", "html");
			Transport.send(msg);

		} catch (AddressException ae) {
			System.out.println("AddressException");
		} catch (MessagingException me) {
			System.out.println("MessagingException");
		} catch (UnsupportedEncodingException e) {
			System.out.println("UnsupportedEncodingException");
		}
	}

	public Session daum() {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.daum.net");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("routrip@daum.net", "fnxmflq12!");
			}
		});
		return session;
	}

	public Session naver() {
		Properties props = new Properties();

		props.put("mail.smtp.host", "smtp.naver.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("routrip@naver.com", "fnxmflq12@");
			}
		});
		return session;
	}

	public Session gmail() {
		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("routrip12@gmail.com", "fnxmflq12!");
			}
		});
		return session;
	}

	public boolean isOkJwt(String jwt) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date exp;
			exp = format.parse(format.format(Jwts.parser().parseClaimsJwt(jwt).getBody().getExpiration()));
			int uid = (int) Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid");

			List<String> exps = userService.findBlackListByUid(uid);
			if (exps != null) {
				for (String e : exps) {
					if (exp.getTime() < format.parse(e).getTime()) {
						System.out.println("이미 로그아웃한 아이디입니다. : 현재 시점에 이 jwt보다 최근 아이디가 로그아웃되어있습니다.");
						return false;
					}
				}
			}
			if (userService.findBlackList(uid,
					format.format(Jwts.parser().parseClaimsJwt(jwt).getBody().getExpiration())) > 0) {
				return false;
			}
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
