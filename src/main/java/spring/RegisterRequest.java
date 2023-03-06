package spring;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

public class RegisterRequest {

	@NotBlank(message = "필수 항목입니다.")
	@Email(message = "이메일 형식이 올바르지 않습니다.")
	private String email;

	@NotEmpty(message = "필수 항목입니다.")
	private String name;

	@Size(min = 6, message = "비밀번호는 최소 6자리로 입력해주세요.")
	private String password;

	@NotEmpty(message = "필수 항목입니다.")
	private String confirmPassword;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public boolean isPasswordEqualToConfirmPassword() {
		return password.equals(confirmPassword);
	}
}
