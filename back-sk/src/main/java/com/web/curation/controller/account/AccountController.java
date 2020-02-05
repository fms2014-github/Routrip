package com.web.curation.controller.account;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.web.curation.model.BasicResponse;
import com.web.curation.model.user.User;
import com.web.curation.service.UserService;

import io.jsonwebtoken.Jwts;
import io.swagger.annotations.*;

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

	// private String key = "webcuration-routrip-secretkey";

	@PostMapping("/login")
	@ApiOperation(value = "로그인")
	public Object login(@RequestBody User user) throws Exception {
		User loginUser = userService.findUserByEmailAndPassword(user.getEmail(), user.getPassword());
		if (loginUser == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		System.out.println(loginUser.getEmail() + " 님이 로그인하셨습니다.");
		String jwt = Jwts.builder().setHeaderParam("typ", "JWT").setSubject(String.valueOf(loginUser.getUid()))
				// .claim("user", new Gson().toJson(loginUser)) //로그인 객체 통으로 필요하면 주석 풀기
				.claim("uid", loginUser.getUid()).claim("email", loginUser.getEmail())
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
		// System.out.println(jwt);
		// System.out.println(Jwts.parser().parseClaimsJwt(jwt).getBody()); //복호화
		// System.out.println(Jwts.parser().parseClaimsJwt(jwt).getBody().get("email"));
		// System.out.println(Jwts.parser().parseClaimsJwt(jwt).getBody().getExpiration());
		return new ResponseEntity<>(jwt, HttpStatus.OK);
	}

	@PostMapping("/snslogin")
	@ApiOperation(value = "sns로그인")
	public Object snslogin(@RequestBody String email, int api) throws Exception {
		User user = userService.findUserByEmail(email, api);
		System.out.println("sns로그인이 시도되었습니다.");
		if (user == null)
			return new ResponseEntity<>(HttpStatus.OK);
		String jwt = Jwts.builder().setHeaderParam("typ", "JWT").setSubject(String.valueOf(user.getUid()))
				.claim("uid", user.getUid()).claim("email", user.getEmail())
				// .claim("password", user.getPassword())
				// .claim("name", user.getName())
				.claim("nickname", user.getNickname())
				// .claim("phone", user.getPhone())
				// .claim("birth", user.getBirth())
				.claim("profileImg", user.getProfileImg()).claim("loginApi", user.getLoginApi())
				.claim("userkey", user.getUserkey())
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
				// .signWith(SignatureAlgorithm.HS256, key)
				.compact();
		System.out.println(user.getEmail() + " 님 sns로그인 되었습니다.");
		return new ResponseEntity<>(jwt, HttpStatus.OK);
	}

	@PostMapping("/logout")
	@ApiOperation(value = "로그아웃")
	public Object logout(@RequestBody String jwt) throws Exception {
		int uid = (int) Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String exp = String.valueOf(format.format(Jwts.parser().parseClaimsJwt(jwt).getBody().getExpiration()));
		userService.deleteBlackList();
		if (userService.findBlackList(uid, exp) == 0) {
			userService.addBlackList(uid, exp);
			System.out.println("로그아웃되었습니다.");
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			System.out.println("이미 로그아웃된 사용자입니다.");
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/follow")
	@ApiOperation(value = "팔로우 등록")
	public Object following(@RequestBody String jwt, int uid) throws Exception {
		int ok = 0;
		List<Integer> follower = userService.getFollow((int)Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid"));
		boolean flag = true;
		for (int i : follower) {
			if (i == uid) {
				flag = false;
				break;
			}
		}
		if (flag)
			ok = userService.addFollow((int)Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid"), uid);
		if (ok > 0)
			return new ResponseEntity<>(HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/following")
	@ApiOperation(value = "팔로우 정보 조회")
	public Object followList(@RequestBody int uid) throws Exception {
		List<Integer> list = userService.getFollow(uid);
		List<User> userlist = new ArrayList<User>();
		for (Integer i : list)
			userlist.add(userService.findUserByUid(i));
		return new ResponseEntity<>(userlist, HttpStatus.OK);
	}

	@PostMapping("/follower")
	@ApiOperation(value = "팔로워 정보 조회")
	public Object followerList(@RequestBody int uid) throws Exception {
		List<Integer> list = userService.getFollower(uid);
		List<User> userlist = new ArrayList<User>();
		for (Integer i : list)
			userlist.add(userService.findUserByUid(i));
		return new ResponseEntity<>(userlist, HttpStatus.OK);
	}

	@DeleteMapping("/follow")
	@ApiOperation(value = "팔로우 해제")
	public Object deleteFollow(@RequestBody String jwt, int uid) throws Exception {
		int ok = userService.deleteFollow((int)Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid"), uid);
		if (ok > 0)
			return new ResponseEntity<>(HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@DeleteMapping("/user")
	@ApiOperation(value = "탈퇴하기")
	public Object deleteUser(@RequestBody String jwt) throws Exception {
		int uid = (int)Jwts.parser().parseClaimsJwt(jwt).getBody().get("uid");
		int ok = 0;
		ok = userService.deleteUser(uid);
		if (ok > 0) {
			// 이미지를 저장한 img/uid 폴더를 프로젝트에서 삭제(프로젝트 내 저장일 경우)
			//deleteFolder(System.getProperty("user.dir") + "\\img\\" + uid);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PutMapping("/password")
	@ApiOperation(value = "비밀번호 변경")
	public Object updatePassword(@RequestBody User user) throws Exception {
		user.setUid(userService.findUserByEmail(user.getEmail(), 0).getUid());
		int ok = userService.updateProfile(user);//안되면 changePW함수로 다시 변경
		if (ok > 0) {
			System.out.println("비밀번호가 변경되었습니다.");
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PutMapping("/user")
	@ApiOperation(value = "회원정보 변경")//프로필 이미지, 닉네임 변경
	public Object updateProfile(@RequestBody User user) throws Exception {
		if(user.getProfileImg()!=null) {
			//DB안의 내용물은 uid.png 나 디폴트 이미지나 둘 중 하나
			//받아온 파일을 정해진 폴더에 uid.png 형식으로 다운받는 코드 짜서 넣기
		}
		int ok = userService.updateProfile(user);
		if (ok > 0)
			return new ResponseEntity<>(HttpStatus.OK);
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
			System.out.println("메일이 발송되었습니다 : " + user.getEmail());
		}
		if (ok > 0) {
			// 이미지를 저장할 img/uid 폴더를 프로젝트에 제작(프로젝트 내 저장일 경우)
//			File folder = new File(System.getProperty("user.dir") + "\\img\\" + user.getUid());
//			if (!folder.exists()) {
//				folder.mkdirs(); // 폴더 생성합니다.
//			}
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
		System.out.println("메일이 재발송되었습니다 : " + user.getEmail());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PutMapping("/signup")
	@ApiOperation(value = "가입완료")
	public Object updateUserKey(@RequestBody User tempuser) throws Exception {
		User user = userService.findUserByUid(tempuser.getUid());
		if (user.getUserkey().equals(tempuser.getUserkey())) {
			int ok = userService.updateUserKey(tempuser.getUid());
			if (ok > 0) {
				System.out.println("가입 완료 : " + user.getEmail());
				return new ResponseEntity<>(HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/email")
	@ApiOperation(value = "이메일 찾기")
	public Object findEmail(@RequestBody User user) throws Exception {
		List<String> email = userService.findEmail(user);
		if(email.isEmpty())
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		for (int e=0;e<email.size();e++) {
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
		System.out.println("이메일 찾기 성공했습니다.");
		return new ResponseEntity<>(email, HttpStatus.OK);
	}

	@GetMapping("/password/{email}")
	@ApiOperation(value = "비밀번호 변경 인증번호")
	public Object findPassword(@PathVariable String email) throws Exception {
		String certNum = RandomNum();
		String body = "인증번호는 [ " + certNum + " ] 입니다.<br>자정이 지나기전에 입력해주십시오.";
		sendEmail(email, body);
		System.out.println("비밀번호 변경 인증번호가 발송되었습니다.");
		return new ResponseEntity<>(certNum, HttpStatus.OK);
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
		System.out.println("EMAIL: " + usermail);
		// 골뱅이가 여러개 있을 경우, 특문 막 들어가 있을 경우는 어떻게?? 프론트에서?
		String result = usermail.substring(usermail.lastIndexOf("@") + 1);
		System.out.println(result);
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
		// String authNum = "";
		try {
			msg.setSentDate(new Date());
			msg.setFrom(new InternetAddress("routrip@naver.com", "루트립관리자")); // 발송자
			InternetAddress to = new InternetAddress(usermail); // 수신자
			msg.setRecipient(Message.RecipientType.TO, to);
			msg.setSubject("루트립입니다", "UTF-8");
			// authNum = RandomNum();
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

	public void deleteFolder(String path) {

		File folder = new File(path);
		try {
			if (folder.exists()) {
				File[] folder_list = folder.listFiles(); // 파일리스트 얻어오기

				for (int i = 0; i < folder_list.length; i++) {
					if (folder_list[i].isFile()) {
						folder_list[i].delete();
						System.out.println("파일이 삭제되었습니다.");
					} else {
						deleteFolder(folder_list[i].getPath()); // 재귀함수호출
						System.out.println("폴더가 삭제되었습니다.");
					}
					folder_list[i].delete();
				}
				folder.delete(); // 폴더 삭제
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
	}
}
