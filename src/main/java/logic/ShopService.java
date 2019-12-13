package logic;

import java.util.List;

import dao.ItemDao;

public class ShopService {
	private ItemDao itemDao;
	
	// spring-mvc.xml을 보면 왜 이 메서드가 필요한지 알 수 있음 (p:itemDao-ref="itemDao")
	// DB와 연결된 ItemDao 객체 주입
	public void setItemDao(ItemDao itemDao) {
		this.itemDao = itemDao;
	}
	
	// IndexController에서
	public List<Item> getItemList() {
		return itemDao.list();
	}
	
	// DetailController에서 ...
	public Item getItemById(Integer id) {
		return itemDao.selectOne(id);
	}
}
