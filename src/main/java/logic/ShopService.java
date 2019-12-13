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
}
