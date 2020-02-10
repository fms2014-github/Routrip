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
import com.web.curation.model.user.Alarm;
import com.web.curation.model.user.User;
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
@RequestMapping("/account")
@RestController
public class AccountController {

	@Autowired
	private UserService userService;

	@PostMapping("/test")
	@ApiOperation("테스트용")
	public Object test() throws Exception {
		String jwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJub25lIn0.eyJzdWIiOiI4IiwidWlkIjo4LCJlbWFpbCI6InRlc3QwNUBzc2FmeS5jb20iLCJuaWNrbmFtZSI6InN0cmluZyIsInByb2ZpbGVJbWciOiJpbWcvcHJvZmlsZS5wbmciLCJsb2dpbkFwaSI6MCwidXNlcmtleSI6IlkiLCJleHAiOjE1ODE0MDA2Mjh9.";
		System.out.println("테스트용 jwt 토큰을 되돌려줍니다.");
		return new ResponseEntity<>(jwt, HttpStatus.OK);
	}

	// private String key = "webcuration-routrip-secretkey";

	@PostMapping("/login")
	@ApiOperation(value = "로그인")
	public Object login(@RequestBody User user) throws Exception {
		User loginUser = userService.findUserByEmailAndPassword(user.getEmail(), user.getPassword());
		if (loginUser == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		String jwt = Jwts.builder().setHeaderParam("typ", "JWT").setSubject(String.valueOf(loginUser.getUid()))
				.claim("uid", loginUser.getUid()).claim("email", loginUser.getEmail())
				.claim("userid", loginUser.getUserid())
				// .claim("password", loginUser.getPassword())
				// .claim("name", loginUser.getName())
				.claim("nickname", loginUser.getNickname())
				// .claim("phone", loginUser.getPhone())
				// .claim("birth", loginUser.getBirth())
				.claim("profileImg", loginUser.getProfileImg()).claim("loginApi", loginUser.getLoginApi())
				.claim("userkey", loginUser.getUserkey())
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))// 하루 뒤 자동 기간 만료됨
				// .signWith(SignatureAlgorithm.HS256, key)
				.compact();
		return new ResponseEntity<>(jwt, HttpStatus.OK);
	}

	@PostMapping("/snslogin")
	@ApiOperation(value = "sns로그인")
	public Object snslogin(@RequestBody User user) throws Exception {
		User loginUser = userService.findUserByUserId(user.getUserid(), user.getLoginApi());
		if (loginUser == null)
			return new ResponseEntity<>(HttpStatus.OK);
		String jwt = Jwts.builder().setHeaderParam("typ", "JWT").setSubject(String.valueOf(loginUser.getUid()))
				.claim("uid", loginUser.getUid()).claim("email", loginUser.getEmail())
				.claim("userid", loginUser.getUserid())
				// .claim("password", loginUser.getPassword())
				// .claim("name", loginUser.getName())
				.claim("nickname", loginUser.getNickname())
				// .claim("phone", loginUser.getPhone())
				// .claim("birth", loginUser.getBirth())
				.claim("profileImg", loginUser.getProfileImg()).claim("loginApi", loginUser.getLoginApi())
				.claim("userkey", loginUser.getUserkey())
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
				// .signWith(SignatureAlgorithm.HS256, key)
				.compact();
		return new ResponseEntity<>(jwt, HttpStatus.OK);
	}

	@PostMapping("/logout")
	@ApiOperation(value = "로그아웃")
	public Object logout(@RequestBody Map<String, String> map) throws Exception {
		String jwt = map.get("jwt");
		int uid = (int) Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String exp = format.format(Jwts.parser().parseClaimsJwt(jwt).getBody().getExpiration());
		userService.deleteBlackList();
		if (isOkJwt(jwt)) {
			userService.addBlackList(uid, exp, jwt);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/follow")
	@ApiOperation(value = "팔로우 등록")
	public Object following(@RequestBody Map<String, String> map) throws Exception {
		String jwt = map.get("jwt");
		int uid = Integer.parseInt(map.get("uid"));
		if (isOkJwt(jwt)) {
			int ok = 0;
			List<Integer> follower = userService
					.getFollow((int) Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid"));
			boolean flag = true;
			for (int i : follower) {
				if (i == uid) {
					flag = false;
					break;
				}
			}
			if (flag)
				ok = userService.addFollow((int) Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid"), uid);
			if (ok > 0) {
				Alarm alarm = new Alarm();
				alarm.setUid(uid);
				alarm.setFollow((int) Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid"));
				alarm.setAlarmtype(1);
				alarm.setNickname((String) Jwts.parser().parseClaimsJwt(jwt).getBody().get("nickname"));
				userService.addAlarm(alarm);
				return new ResponseEntity<>(HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/following")
	@ApiOperation(value = "팔로우 정보 조회")
	public Object followList(@RequestBody int uid) throws Exception {
		List<Integer> list = userService.getFollow(uid);
		List<User> userlist = new ArrayList<User>();
		for (Integer i : list)
			userlist.add(userService.findUserSimple(i));
		return new ResponseEntity<>(userlist, HttpStatus.OK);
	}

	@PostMapping("/follower")
	@ApiOperation(value = "팔로워 정보 조회")
	public Object followerList(@RequestBody int uid) throws Exception {
		List<Integer> list = userService.getFollower(uid);
		List<User> userlist = new ArrayList<User>();
		for (Integer i : list)
			userlist.add(userService.findUserSimple(i));
		return new ResponseEntity<>(userlist, HttpStatus.OK);
	}

	@DeleteMapping("/follow")
	@ApiOperation(value = "팔로우 해제")
	public Object deleteFollow(@RequestBody Map<String, String> map) throws Exception {
		String jwt = map.get("jwt");
		int uid = Integer.parseInt(map.get("uid"));
		if (isOkJwt(jwt)) {
			int ok = userService.deleteFollow((int) Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid"), uid);
			if (ok > 0)
				return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@DeleteMapping("/user")
	@ApiOperation(value = "탈퇴하기")
	public Object deleteUser(@RequestBody Map<String, String> map) throws Exception {
		String jwt = map.get("jwt");
		if (isOkJwt(jwt)) {
			int uid = (int) Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid");
			int ok = 0;
			ok = userService.deleteUser(uid);
			if (ok > 0) {
				return new ResponseEntity<>(HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PutMapping("/password")
	@ApiOperation(value = "비밀번호 변경")
	public Object updatePassword(@RequestBody User user) throws Exception {
		user.setUid(userService.findUserByEmail(user.getEmail(), 0).getUid());
		int ok = userService.updateProfile(user);// 안되면 changePW함수로 다시 변경
		if (ok > 0) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PutMapping("/user")
	@ApiOperation(value = "회원정보 변경") // 프로필 이미지, 닉네임 변경
	public Object updateProfile(@RequestBody Map<String, String> map) throws Exception {
		String jwt = map.get("jwt");
		if (isOkJwt(jwt)) {
			int uid = (int) Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid");
			String profileImg = (String) Jwts.parser().parseClaimsJwt(jwt).getBody().get("profileImg");
			String nickname = (String) Jwts.parser().parseClaimsJwt(jwt).getBody().get("nickname");
			User user = new User();
			int ok = 0;
			user.setUid(uid);
			// 동시에 변경하게 되면 if&else if || 로 묶고 하나로 합치기
			if (!profileImg.equals(map.get("profileImg"))) {
				user.setProfileImg(map.get("profileImg"));
				// DB안의 내용물은 uid.png 나 디폴트 이미지나 둘 중 하나
				// 받아온 파일을 정해진 폴더에 uid.png 형식으로 다운받는 코드 짜서 넣기
				ok = userService.updateProfile(user);
			} else if (!nickname.equals(map.get("nickname"))) {
				user.setNickname(map.get("nickname"));
				ok = userService.updateProfile(user);
			}
			// 업데이트를 적용하고 싶으면 로그아웃 후 다시 로그인하도록
			// 바로 적용하고 싶으면 jwt도 받아서 원래껄 로그아웃시키고 새로 로그인후 반환
			// 혹은 uid 로 바로 정보 받아와서 jwt 반환
			if (ok > 0)
				return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/snssignup")
	@ApiOperation(value = "sns가입하기")
	public Object snsSignup(@Valid @RequestBody User user) throws Exception {
		int ok = 0;
		user.setUserkey("Y");
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
			for (User u : userlist) {// 해당 이메일이 이미 존재하는지 확인
				if (u.getEmail().equalsIgnoreCase(user.getEmail())) {
					flag = false;
					break;
				}
			}
		}
		int ok = 0;
		if (flag) {
			String authNum = RandomNum();
			user.setUserkey(authNum);

			ok = userService.addUser(user);
			String body = "인증번호는 [ " + authNum + " ] 입니다.<br>자정이 지나기전에 입력해주십시오.";
			sendEmail(user.getEmail(), body);
			System.out.println("이메일 발송 에러가 일어나는지 확인중... "+user.getEmail());
		}
		if (ok > 0) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PutMapping("/mail")
	@ApiOperation(value = "인증번호재발송")
	public Object sendMail(@RequestBody User user) throws Exception {
		String authNum = RandomNum();
		user.setUserkey(authNum);
		userService.changeUserKey(user);
		String emailTemp = userService.findUserByUid(user.getUid()).getEmail();
		String body = "인증번호는 [ " + authNum + " ] 입니다.<br>자정이 지나기전에 입력해주십시오.";
		sendEmail(emailTemp, body);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PutMapping("/signup")
	@ApiOperation(value = "가입완료")
	public Object updateUserKey(@RequestBody User tempuser) throws Exception {
		User user = userService.findUserByUid(tempuser.getUid());
		if (user.getUserkey().equals(tempuser.getUserkey())) {
			int ok = userService.updateUserKey(tempuser.getUid());
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
		String body = "인증번호는 [ " + certNum + " ] 입니다.<br>자정이 지나기전에 입력해주십시오.";
		sendEmail(email, body);
		return new ResponseEntity<>(certNum, HttpStatus.OK);
	}

	@PostMapping("/decode")
	@ApiOperation(value = "유저 토큰 해석")
	public Object decode2(@RequestBody Map<String, String> map) throws Exception {
		String jwt = map.get("jwt");
		if (isOkJwt(jwt)) {
			User user = new User();
			user.setUid((int) Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid"));
			user.setEmail((String) Jwts.parser().parseClaimsJwt(jwt).getBody().get("email"));
			user.setUserid((String) Jwts.parser().parseClaimsJwt(jwt).getBody().get("userid"));
			user.setNickname((String) Jwts.parser().parseClaimsJwt(jwt).getBody().get("nickname"));
			user.setProfileImg((String) Jwts.parser().parseClaimsJwt(jwt).getBody().get("profileImg"));
			user.setLoginApi((int) Jwts.parser().parseClaimsJwt(jwt).getBody().get("loginApi"));
			return new ResponseEntity<>(user, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/alarm")
	@ApiOperation(value = "유저 알림")
	public Object alarm(@RequestBody Map<String, String> map) throws Exception {
		String jwt = map.get("jwt");
		if (isOkJwt(jwt)) {
			List<Alarm> alarms = userService.getAlarm((int) Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid"));
			return new ResponseEntity<>(alarms, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@DeleteMapping("/alarm")
	@ApiOperation(value = "알림 삭제")
	public Object deleteAlarm(@RequestBody Map<String, String> map) throws Exception {
		// 알람 유저가 직접 하나씩 삭제
		int alarmid = Integer.parseInt(map.get("alarmid"));
		userService.deleteAlarm(alarmid);
		// 알람 유저가 보고 나면 한꺼번에 삭제
		String jwt = map.get("jwt");
		if (isOkJwt(jwt)) {
			userService.deleteAlarmAll((int) Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid"));
		}
		return new ResponseEntity<>(HttpStatus.OK);
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

	// 이메일 보내는 함수(gmail, naver, daum)
	public void sendEmail(String usermail, String body) {
		// 골뱅이가 여러개 있을 경우, 특문 막 들어가 있을 경우는 어떻게?? 프론트에서?
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
			System.out.println("AddressException : " + ae.getMessage());
		} catch (MessagingException me) {
			System.out.println("MessagingException : " + me.getMessage());
		} catch (UnsupportedEncodingException e) {
			System.out.println("UnsupportedEncodingException : " + e.getMessage());
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
				return new PasswordAuthentication("routrip@naver.com", "fnxmflq12!");
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
