package com.web.curation.controller.account;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import javax.websocket.server.PathParam;

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
import com.web.curation.model.user.User;
import com.web.curation.service.UserService;

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

	@PostMapping("/login")
	@ApiOperation(value = "로그인")
	public Object login(@RequestBody User user) throws Exception {
		User loginUser = userService.findUserByEmailAndPassword(user.getEmail(), user.getPassword());
		if (loginUser == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		return new ResponseEntity<>(loginUser, HttpStatus.OK);
	}

	@PostMapping("/follow")
	@ApiOperation(value = "팔로우 등록")
	public Object following(@RequestBody User user, int uid) throws Exception {
		int ok = 0;
		List<Integer> follower = userService.getFollow(user.getUid());
		boolean flag = true;
		for (int i : follower) {
			if (i == uid) {
				flag = false;
				break;
			}
		}
		if (flag)
			ok = userService.addFollow(user.getUid(), uid);
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
	public Object deleteFollow(@RequestBody User user, int uid) throws Exception {
		int ok = userService.deleteFollow(user.getUid(), uid);
		if (ok > 0)
			return new ResponseEntity<>(HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@DeleteMapping("/user")
	@ApiOperation(value = "탈퇴하기")
	public Object deleteUser(@RequestBody User user) throws Exception {
		User check = userService.findUserByUid(user.getUid());
		int ok = 0;
		if (user.getPassword().equals(check.getPassword()))
			ok = userService.deleteUser(user.getUid());
		if (ok > 0) {
			//이미지를 저장한 img/uid 폴더를 프로젝트에서 삭제(프로젝트 내 저장일 경우)
			deleteFolder(System.getProperty("user.dir")+"\\img\\"+user.getUid());
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PutMapping("/password")
	@ApiOperation(value = "비밀번호 변경")
	public Object updatePassword(@RequestBody User user) throws Exception {
		int ok = userService.changePw(user);
		if (ok > 0)
			return new ResponseEntity<>(userService.findUserByUid(user.getUid()), HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PutMapping("/profile")
	@ApiOperation(value = "프로필이미지 변경")
	public Object updateProfile(@RequestBody User user) throws Exception {
		int ok = userService.updateProfileImg(user);
		User loginUser = userService.findUserByEmail(user.getEmail());
		if (ok > 0)
			return new ResponseEntity<>(loginUser, HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/signup")
	@ApiOperation(value = "가입하기")
	public Object signup(@Valid @RequestBody User user) throws Exception {// 성공

		List<User> userlist = userService.getUserList();
		boolean flag = true;
		for (User u : userlist) {// 해당 이메일이 이미 존재하는지 확인
			if (u.getEmail().equalsIgnoreCase(user.getEmail())) {
				flag = false;
				break;
			}
		}
		int ok = 0;
		if (flag) {
			String authNum = RandomNum();
			user.setUserkey(authNum);
			
			ok = userService.addUser(user);
			String body = "가입완료하시려면 <a href='"+"http://192.168.100.70:8083/account/key/"+user.getUid()+"/"+authNum+"'>여기</a>를 클릭하세요.";
			sendEmail(user.getEmail(), body);
			System.out.println("메일이 발송되었습니다 : "+user.getEmail());
		}
		if (ok > 0) {
			// 이미지를 저장할 img/uid 폴더를 프로젝트에 제작(프로젝트 내 저장일 경우)
			File folder = new File(System.getProperty("user.dir") + "\\img\\" + user.getUid());
			if (!folder.exists()) {
				folder.mkdirs(); // 폴더 생성합니다.
				//프로필 이미지가 이미 가입때 등록됐으면 그걸 저장, 아니면 default 이미지 저장하기
			}
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/key/{uid}/{userkey}")
	@ApiOperation(value = "가입완료")
	public Object updateUserKey(@PathVariable int uid, @PathVariable String userkey) throws Exception {
		User user = userService.findUserByUid(uid);
		if(user.getUserkey().equals(userkey)) {
			int ok = userService.updateUserKey(uid);
			if (ok > 0) {
				System.out.println("가입 완료 : "+uid);
				return new ResponseEntity<>(HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

//	public Object emailAuth(@Valid @RequestBody HttpServletRequest request, @RequestBody User user) throws Exception {

		// 사용자가 이메일을 입력하면, 이메일로 인증번호 보내기
//		String usermail = request.getParameter("email");
//		sendEmail(usermail);

//		return null;
//	}

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
		//String authNum = "";
		try {
			msg.setSentDate(new Date());

			msg.setFrom(new InternetAddress("routrip@naver.com", "루트립관리자")); // 발송자
			InternetAddress to = new InternetAddress(usermail); // 수신자
			msg.setRecipient(Message.RecipientType.TO, to);
			msg.setSubject("루트립입니다", "UTF-8");
			//authNum = RandomNum();
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

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
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

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
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

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("routrip12@gmail.com", "fnxmflq12!");
			}
		});
		return session;
	}
	
	public void deleteFolder(String path) {
		
	    File folder = new File(path);
	    try {
		if(folder.exists()){
                File[] folder_list = folder.listFiles(); //파일리스트 얻어오기
				
		for (int i = 0; i < folder_list.length; i++) {
		    if(folder_list[i].isFile()) {
			folder_list[i].delete();
			System.out.println("파일이 삭제되었습니다.");
		    }else {
			deleteFolder(folder_list[i].getPath()); //재귀함수호출
			System.out.println("폴더가 삭제되었습니다.");
		    }
		    folder_list[i].delete();
		 }
		 folder.delete(); //폴더 삭제
	       }
	   } catch (Exception e) {
		e.getStackTrace();
	   }
    }
}
