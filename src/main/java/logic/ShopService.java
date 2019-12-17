package logic;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dao.ItemDao;
import dao.UserDao;

// @Component + Service 기능 (컨트롤러와 db를 중간에서 연결)
@Service
public class ShopService {
	
	// 주입됨
	@Autowired
	private ItemDao itemDao;
	
	@Autowired
	private UserDao userDao;

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

	public Item itemInfo(Integer id) {
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
}
