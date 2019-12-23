package controller;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import exception.BoardException;
import logic.Board;
import logic.ShopService;

@Controller
@RequestMapping("board")
public class BoardController {
	@Autowired
	private ShopService service;
	
	// view의 경로 결정은 spring-mvc.xml ===> id="viewResolver"에 의해서
	// /WEB-INF/view/board/list.jsp
	@RequestMapping("list")
	public ModelAndView list(Integer pageNum) {
		ModelAndView mav = new ModelAndView(); // view
		
		if(pageNum ==null || pageNum.toString().equals("")) {
			pageNum =1;
		}
		// 페이지당 보여지는 게시물 건수
		int limit =10;
		// 전체 등록된 게시물 건수
		int listcount = service.boardcount();
		// 해당 페이지에서 보여줘야할 게시물을 가져올 객체
		List<Board> boardlist = service.boardlist(pageNum, limit);
		// 최대 페이지
		int maxpage = (int)((double)listcount/limit +0.95);
		// 보여지는 첫번째 페이지
		int startpage = (int)((pageNum/10.0 +0.9) -1) *10 +1;
		int endpage = startpage +9;
		
		if(endpage > maxpage) {
			endpage = maxpage;
		}
		
		// 화면에 보여지는 게시물 번호
		int boardno = listcount - (pageNum -1) *limit;
		
		// list.jsp로 넘어가는 7개의 정보
		mav.addObject("pageNum", pageNum);
		mav.addObject("maxpage", maxpage);
		mav.addObject("startpage", startpage);
		mav.addObject("endpage", endpage);
		mav.addObject("listcount", listcount);
		mav.addObject("boardlist", boardlist);
		mav.addObject("boardno", boardno);
		return mav;	
	}
	
//	// write.jsp에서 화면이 보여지게 될 거야
//	@GetMapping("write")
//	public ModelAndView getBoard() {
//		ModelAndView mav = new ModelAndView();
//		mav.addObject("board", new Board());
//		return mav;
//	}
	
	// BindingResult bresult : 유효성 검증
	// HttpServletRequest request : 업로드시 위치값을 가져오기 위해서
	@PostMapping("write")
	public ModelAndView write(@Valid Board board, BindingResult bresult, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		try {
			service.boardWrite(board, request);
			mav.setViewName("redirect:list.shop");
		} catch(Exception e) {
			e.printStackTrace();
			throw new BoardException("게시물 등록에 실패했습니다", "write.shop");
		}
		return mav;
	}
	
//	boardController에 list.shop에서  detail.shop 요청이 들어오면,
//	@GetMapping("detail")
//	public ModelAndView detail(int num) {
//		// db에서 num에 해당하는 게시물을 읽어서 
//		ModelAndView mav = new ModelAndView();		
//	}

	// write.shop 호출 변경
	// HttpServletRequest request : 요청 uri 경로를 확인 할 수 있어
	@GetMapping("*")
	public ModelAndView getBoard(Integer num, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		
		Board board =null;
		if(num ==null) {
			board = new Board();
		} else {
			board = service.getBoard(num, request);
			mav.addObject("board", board);
			return mav;
		}
		mav.addObject("board", board);
		return mav;
	}
	
	// CKEditor에서 올린 사진 받아오기
	// upload : 업로드된 이미지 정보가 저장된 이미지 파일
	@RequestMapping("imgupload")
	public String imgupload(MultipartFile upload, String CKEditorFuncNum, HttpServletRequest request, Model model) {
		
		// 내가 정한 이미지가 저장될 위치
		String path = request.getServletContext().getRealPath("/")+"board/imgfile/";
		File f = new File(path);
		if(!f.exists()) {
			f.mkdirs();
		}
		if(!upload.isEmpty()) {
			// 업로드 된 파일을 저장할 File객체를 지정함
			File file = new File(path, upload.getOriginalFilename());
			try {
				// file에 upload파일을 저장해 (업로드 파일 생성)
				upload.transferTo(file);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		// 파일이름은 절대경로로 들어가야하고
		// 파일이름과 ck넘버를
		// view/ckedit.jsp에 넘겨줘야함 (그래야 클라이언트가 알 수 있음)
		String fileName = "/shop03/board/imgfile/"+upload.getOriginalFilename();
		model.addAttribute("fileName", fileName);
		model.addAttribute("CKEditorFuncNum", CKEditorFuncNum);
		return "ckedit"; // view단
	}
}
