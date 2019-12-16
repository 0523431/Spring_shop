package logic;

import dao.UserDao;

public class ShopService {
	private UserDao userDao;
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	// ↑ spring-mvc.xml 정보만 가지고 만듦-------------------------//
	
	// from UserEntryController
	public void insertUser(User user) {
		userDao.insert(user);
	}
	
	// from LoginController에서
	// 입력한 아이디와 비밀번호가 동일한 아이디가 있는지 찾아와라
	public User getUser(String userid) {
		return userDao.selectOne(userid);
	}
}
