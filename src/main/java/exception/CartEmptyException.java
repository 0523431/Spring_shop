package exception;

public class CartEmptyException extends RuntimeException {
	private String url;
	
	// 생성자
	public CartEmptyException(String msg, String url) {
		super(msg);
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}
}
