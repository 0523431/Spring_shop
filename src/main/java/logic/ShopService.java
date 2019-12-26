package logic;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dao.BoardDao;
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
	
	@Autowired
	private BoardDao boardDao;
	
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
	
	// uploadFileCreate(파일내용, 위치, 경로)
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

	public Item itemInfo(String id) { // 지역변수(여기서만 해당)
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
	
	// user가 산 주문 목록
	public List<Sale> salelist(String id) {
		return saleDao.list(id);
	}
	
	// 1개의 주문목록에 대한 아이템 리스트
	public List<SaleItem> saleItemList(int saleid) {
		return saleItemDao.list(saleid);
	}

	public void userUpdate(User user) {
		userDao.update(user);
	}

	public void userDelete(String userid) {
		userDao.delete(userid);	
	}

	public int boardcount(String searchtype, String searchcontent) {
		return boardDao.count(searchtype, searchcontent);
	}

	public List<Board> boardlist(Integer pageNum, int limit, String searchtype, String searchcontent) {
		return boardDao.list(pageNum, limit, searchtype, searchcontent);
	}

	public void boardWrite(Board board, HttpServletRequest request) {
		if(board.getFile1() !=null && !board.getFile1().isEmpty()) {
			// uploadFileCreate() 메서드 위에 있음 | 상품업로드할때 썼던 메서드
			// uploadFileCreate(파일객체, 위치, 경로)
			uploadFileCreate(board.getFile1(), request, "board/file/");
			// file1의 값이 fileurl에 들어감 그래서 dao에서 file1의 값은 fileurl에서 가져와야함
			board.setFileurl(board.getFile1().getOriginalFilename());
		}
		// 현재 등록된 게시물 번호의 최대값
		int max = boardDao.maxnum();
		board.setNum(++max);
		board.setGrp(max); // 원글이니까 게시물번호와 동일한 그룹값이 됨
		boardDao.insert(board);
	}

	public Board getBoard(Integer num, HttpServletRequest request) {
		if(request.getRequestURI().contains("detail.shop")) {
			boardDao.readcntadd(num);
		}
		return boardDao.selectOne(num);
	}
	
	public Board getBoard(int num) {
		return boardDao.selectOne(num);
	}	
	
	public void boardReply(Board board) {
		// 기존에 있던 모든 글에 대한 step (밀기)
		boardDao.grpstepAdd(board.getGrp(), board.getGrpstep());
		
		int max = boardDao.maxnum();
		board.setNum(++max);
		
		// 새로 쓴 답글의 step
		board.setGrplevel(board.getGrplevel()+1);
		board.setGrpstep(board.getGrpstep()+1);
		
		boardDao.insert(board);
	}

	public void boardUpdate(Board board, HttpServletRequest request) {
		if(board.getFile1() !=null && !board.getFile1().isEmpty()) {
			// uploadFileCreate() 메서드 위에 있음 | 상품업로드할때 썼던 메서드
			// uploadFileCreate(파일객체, 위치, 경로)
			uploadFileCreate(board.getFile1(), request, "board/file/");
			// file1의 값이 fileurl에 들어감 그래서 dao에서 file1의 값은 fileurl에서 가져와야함
			board.setFileurl(board.getFile1().getOriginalFilename());
		}
		
		boardDao.update(board);
	}

	public void boardDelete(int num) {
		boardDao.delete(num);
	}

	public List<User> adminlist() {
//		return adminDao.list();
		return userDao.adminlist();
	}

	public List<User> adminlist(String[] idchks) {
		return userDao.adminlist(idchks);
	}


}
