package util;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import logic.User;

// 유효성 검증
public class LoginValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		User user = (User)target;
		String group = "error.required";
		
		if(user.getUserid() ==null || user.getUserid().length() ==0) {
			// "userid"는 User클래스의 property와 같아야함
			errors.rejectValue("userid", group);
		}
		if(user.getPassword() ==null || user.getPassword().length() ==0) {
			// rejectValue : 각각에 해당하는 message
			errors.rejectValue("password", group);
		}
				
		if(errors.hasErrors()) {
			//reject : global message
			errors.reject("error.input.user"); // 입력정보에 문제가 있습니다.
		}	
	}
}
