package logic;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dao.ItemDao;
import dao.SaleDao;
import dao.SaleItemDao;
import dao.UserDao;

// @Component + Service 기능 (컨트롤러와 db를 중간에서 연결)
@Service
public class ShopService {
	
	// 주입됨
	@Autowired
	private ItemDao itemDao;
	
	@Autowired
	private UserDao userDao;

	@Autowired
	private SaleDao saleDao;
	
	@Autowired
	private SaleItemDao saleItemDao;
	
	public List<Item> getItemList() {
		return itemDao.list();
	}

	// multipartResolver가 알아서 파일까지 다 item이 됨
	public void itemCreate(Item item, HttpServletRequest request) {
		
		// 업로드된 이미지파일이 존재
		if(item.getPicture() !=null && !item.getPicture().isEmpty()) {
			// 밑에 만들어놓은 method : 파일 업로드(업로드된 파일의 내용을 파일에 저장)
			uploadFileCreate(item.getPicture(), request, "item/img/");
			// 업로드된 파일이 있으면, 그 값이 들어가있고 아니면 null
			item.setPictureUrl(item.getPicture().getOriginalFilename());
		}
		
		// 그렇게 모든게 저장된 item을 insert하러 가자
		itemDao.insert(item);
		
	}
	
	// uploadFileCreate는
	// 		picture.transferTo(new File(uploadPath + orgFile));
	// 알아서 업로드된 파일의 내용을 파일로 생성시킴
	// item.getPicture() == MultipartFile picture == 업로드된 파일의 내용
	private void uploadFileCreate(MultipartFile picture, HttpServletRequest request, String path) {
		String orgFile = picture.getOriginalFilename();
		String uploadPath = request.getServletContext().getRealPath("/") + path;
		File fpath = new File(uploadPath);
		if(!fpath.exists()) {
			fpath.mkdirs();
		}
		try {
			// 파일의 내용이 저장된 picture를 파일로 생성시킴
			picture.transferTo(new File(uploadPath + orgFile));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public Item itemInfo(Integer id) { // 지역변수(여기서만 해당)
		return itemDao.itemInfo(id);
	}

	public void itemUpdate(Item item, HttpServletRequest request) {
		// 수정된 이미지파일이 존재
		if(item.getPicture() !=null && !item.getPicture().isEmpty()) {
			// 만들어놓은 파일 업로드 method : 업로드된 파일의 내용을 파일에 저장
			uploadFileCreate(item.getPicture(), request, "item/img/");
			// 업로드된 파일이 있으면, 그 값이 들어가있고 아니면 null
			// PictureUrl : 수정된 파일이 있으면, 업데이트하면 되고 아니면 hidden에 있는 값을 가져가면 됨
			item.setPictureUrl(item.getPicture().getOriginalFilename());
		}
		
		itemDao.update(item);
		
	}

	public void itemDelete(String id) {
		itemDao.itemDelete(id);
	}

	public void userInsert(User user) {
		userDao.userInsert(user);
	}

	public User getUser(String userid) {
		return userDao.selectOne(userid);
	}
	
	// 주문확정 ..
	// User loginUser : 로그인 정보
	// Cart cart : 주문 정보
	public Sale checkend(User loginUser, Cart cart) {
		Sale sale = new Sale();
		sale.setSaleid(saleDao.getMaxSaleId()); // saleid는 (기존 saleid +1)값
		sale.setUser(loginUser); // 구매자 정보
		sale.setUserid(loginUser.getUserid()); // 구매자 정보에서 >>> 구매자 아이디
		sale.setUpdatetime(new Date()); // 주문 시간
		saleDao.insert(sale); // 한번에 구매한 정보를 db에 저장하게 됨
		
		// 주문상품정보를 cart에서 조회
		List<ItemSet> itemList = cart.getItemSetList();
		int i =0;
		for(ItemSet separateItem : itemList) {
			
			int saleItemId = ++i;
			// saleItem 객체를 만드는 과정
			// 생성자의 매개변수와 맞게
			// sale.getSaleid() : saleid값 고정
			// saleItemId : for문이 반복될 때 마다, 1>2>3 이렇게 증가하게 되
			// separateItem : ItemSet 객체
			SaleItem saleItem = new SaleItem(sale.getSaleid(), saleItemId, separateItem);
			sale.getItemList().add(saleItem);
			saleItemDao.insert(saleItem);
		}
		return sale;
	}	
}
