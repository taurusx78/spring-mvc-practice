package controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import spring.RegisterRequest;

public class RegisterRequestValidator implements Validator {

	// 이메일 정규식 표현
	private static final String emailRegExp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private Pattern pattern;
	
	public RegisterRequestValidator() {
		pattern = Pattern.compile(emailRegExp);
	}

	// 전달받은 clazz 객체가 RegisterRequest 클래스로 타입 변환이 가능한지 확인
	// 해당 메서드를 직접 실행하진 않지만, 스프링 MVC가 자동으로 검증 기능을 수행하도록 하려면 supports() 메서드를 올바르게 구현해야 함
	@Override
	public boolean supports(Class<?> clazz) {
		return RegisterRequest.class.isAssignableFrom(clazz);
	}

	// target 객체를 검증하고 오류 결과를 errors에 담음
	@Override
	public void validate(Object target, Errors errors) {
		RegisterRequest regReq = (RegisterRequest) target;
		
		// 이메일 유효성 검사
		if (regReq.getEmail() == null || regReq.getEmail().trim().isEmpty()) {
			errors.rejectValue("email", "required");
		} else {
			Matcher matcher = pattern.matcher(regReq.getEmail());
			
			// 이메일 형식이 아닌 경우
			if (!matcher.matches()) {
				errors.rejectValue("email", "bad");
			}
		}
		
		// 이름 유효성 검사 (null, 빈 문자열, 공백 문자(스페이스, 탭 등)일 때 에러)
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "required");
		// 비밀번호 유효성 검사 (null, 빈 문자열일 때 에러)
		ValidationUtils.rejectIfEmpty(errors, "password", "required");
		// 비밀번호 확인 유효성 검사
		ValidationUtils.rejectIfEmpty(errors, "confirmPassword", "required");
		
		// 비밀번호와 비밀번호 확인 일치 여부 검사
		if (!regReq.getPassword().isEmpty()) {
			if (!regReq.isPasswordEqualToConfirmPassword()) {
				errors.rejectValue("confirmPassword", "nomatch");
			}
		}
	}
}
