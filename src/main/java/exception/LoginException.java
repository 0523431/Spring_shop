package exception;

public class LoginException extends RuntimeException {
	private String url;
	
	// 생성자
	public LoginException(String msg, String url) {
		super(msg);
		this.url = url;
	}
	
	// 등장의 이유?
	public String getUrl() {
		return url;
	}
	
}
