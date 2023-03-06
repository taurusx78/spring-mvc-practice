package controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import spring.AuthInfo;
import spring.ChangePasswordService;
import spring.WrongIdPasswordException;

@Controller
@RequestMapping("/edit/changePassword")
public class ChangePwdController {

	private ChangePasswordService changePwdSvc;

	public void setChangePasswordService(ChangePasswordService changePwdSvc) {
		this.changePwdSvc = changePwdSvc;
	}

	@GetMapping
	public String form(@ModelAttribute("command") ChangePwdCommand pwdCmd, HttpSession session) {
		AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
		
		if (authInfo == null) {
			return "redirect:/login";
		}
		
		return "edit/changePwdForm";
	}

	@PostMapping
	public String submit(@ModelAttribute("command") ChangePwdCommand pwdCmd, Errors errors, HttpSession session) {
		// 비밀번호 변경 폼 입력값 유효성 검사
		new ChangePwdCommandValidator().validate(pwdCmd, errors);

		if (errors.hasErrors()) {
			return "edit/changePwdForm";
		}

		AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");

		try {
			changePwdSvc.changePassword(authInfo.getEmail(), pwdCmd.getCurrentPassword(), pwdCmd.getNewPassword());
			return "edit/changedPwd";
		} catch (WrongIdPasswordException e) {
			errors.rejectValue("currentPassword", "notMatching");
			return "edit/changePwdForm";
		}
	}
}
