package util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import logic.User;

// Validator : import했음(springframework)
public class UserValidator implements Validator {
	
	// supports() 메서드 : 유효성 검증 대상이 되는 객체 여부 확인
	@Override
	public boolean supports(Class<?> cls) {
		return User.class.isAssignableFrom(cls);
	}

	/* validate(Object obj, Errors errors)
	 * ===> UserEntryController 클래스에서 전달받음
	 * Object obj ==> User user
	 * Errors errors ==> BindingResult bindResult
	 */
	@Override
	public void validate(Object obj, Errors errors) {
		User user = (User)obj;
		String group = "error.required";
		
		if(user.getUserid() ==null || user.getUserid().length() ==0) {
			// "userid"는 User클래스의 property와 같아야함
			errors.rejectValue("userid", group);
		}
		if(user.getPassword() ==null || user.getPassword().length() ==0) {
			errors.rejectValue("password", group);
		}
		if(user.getUsername() ==null || user.getUsername().length() ==0) {
			errors.rejectValue("username", group);
		}
		if(user.getPhoneno() ==null || user.getPhoneno().length() ==0) {
			errors.rejectValue("phoneno", group);
		}
		if(user.getAddress() ==null || user.getAddress().length() ==0) {
			errors.rejectValue("address", group);
		}
		if(user.getemail() ==null || user.getemail().length() ==0) {
			errors.rejectValue("email", group);
		}
		
		/* birthday 설정하지 않음
		 	==> 그러나 messages.property에서 typeMismatch.birthday를 설정해서 error가 출력됨*/
		
		/* postcode 설정하지 않음 */
		
		if(errors.hasErrors()) {
			errors.reject("error.input.user");
		}
	}
}
