package controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import spring.DuplicateMemberException;
import spring.MemberRegisterService;
import spring.RegisterRequest;

@Controller
public class RegisterController {
	
	private MemberRegisterService memberRegSvc;
	
	public void setMemberRegisterService(MemberRegisterService memberRegSvc) {
		this.memberRegSvc = memberRegSvc;
	}

	@GetMapping("/register/step1")
	public String handleStep1() {
		return "register/step1";
	}
	
	@PostMapping("/register/step2")
	public String handleStep2(@RequestParam(value = "agree", defaultValue = "false") Boolean agree, Model model) {
		if (!agree) {
			return "register/step1";
		}
		model.addAttribute("registerRequest", new RegisterRequest());
		return "register/step2";
	}
	
	@GetMapping("/register/step2")
	public String handleStop2Get() {
		return "redirect:/register/step1";
	}
	
	// @Valid: 글로벌 Validator가 해당 타입의 커맨드 객체에 대해 유효성 검사 수행
	// 검증 수행 후 그 결과를 Errors에 저장함
	// 유효성 검사는 메서드 실행 전에 수행됨
	@PostMapping("/register/step3")
	public String handleStep3(@Valid RegisterRequest regReq, Errors errors) {
		// 커맨드 객체 유효성 검사
//		new RegisterRequestValidator().validate(regReq, errors);
		
		if (errors.hasErrors()) {
			return "register/step2";
		}
		
		try {
			memberRegSvc.regist(regReq);
			return "register/step3";
		} catch (DuplicateMemberException e) {
			errors.rejectValue("email", "duplicate");
			return "register/step2";
		}
	}
	
//	@InitBinder
//	protected void initBinder(WebDataBinder binder) {
//		binder.setValidator(new RegisterRequestValidator());
//	}
}
