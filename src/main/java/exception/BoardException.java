package exception;

// 잊지말고 spring-mvc.xml에 가서 exceptionHandler를 수정해줘야함
public class BoardException extends RuntimeException {
private String url;
	
	// 생성자
	public BoardException(String msg, String url) {
		super(msg);
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}
}
