package controller;

import org.springframework.beans.TypeMismatchException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import spring.Member;
import spring.MemberDao;
import spring.MemberNotFoundException;

@Controller
public class MemberDetailController {

	private MemberDao memberDao;
	
	public void setMemberDao(MemberDao memberDao) {
		this.memberDao = memberDao;
	}
	
	@GetMapping("/member/{id}")
	public String detail(@PathVariable("id") Long id, Model model) {
		Member member = memberDao.selectById(id);
		
		if (member == null) {
			throw new MemberNotFoundException();
		}
		
		model.addAttribute("member", member);
		return "member/memberDetail";
	}
	
	// 회원 ID 타입 오류일 경우 발생하는 익셉션 처리
	@ExceptionHandler(TypeMismatchException.class)
	public String handleTypeMismatchException() {
		return "member/invalidId";
	}
	
	// 존재하지 않는 회원 ID에 해당하는 페이지를 요청할 경우 발생하는 익셉션 처리
	@ExceptionHandler(MemberNotFoundException.class)
	public String handleNotFoundException() {
		return "member/noMember";
	}
}
