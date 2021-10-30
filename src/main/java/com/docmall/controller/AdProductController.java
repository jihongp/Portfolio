package com.docmall.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.docmall.domain.CategoryVO;
import com.docmall.domain.ProductVO;
import com.docmall.service.AdProductService;
import com.docmall.util.*;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

//관리자 : 상품관리기능

@Controller
@Log4j
@AllArgsConstructor
@RequestMapping("/admin/product/*")
public class AdProductController {

	private AdProductService service;
	
	// 업로드되는 웹프로젝트 영역의 외부에 파일을 저장할 경로정보 주입
	@Resource(name ="uploadPath")
	private String uploadPath;
	
	// 상품등록폼
	@GetMapping("/insert")
	public void productInsert(Model model) {
		
		model.addAttribute("mainCategory", service.mainCategory());
	}
	
	@PostMapping("/insert")
	public String productInsert(ProductVO vo, RedirectAttributes rttr) throws Exception{
		
		vo.setPdt_img(UploadFileUtils.uploadFile(uploadPath, vo.getFile1().getOriginalFilename(), vo.getFile1().getBytes()));
		
		// <input type="checkbox"> -> 선택 "on" 값
		// 선택을 안할 경우 널로인하여 에러발생됨 . 널처리 해야함.
		if(vo.getPdt_buy().equals("on"))
		{
			vo.setPdt_buy("Y");
		}else {
			vo.setPdt_buy("N");
		}
		
		
		log.info(vo.toString());
		
		// 상품정보 저장작업
		
		service.Insert(vo);
		rttr.addFlashAttribute("msg", "insertOk");
		
		return "redirect:/admin/product/list";
	}
	
	// 상품조회, 상품수정폼 페이지
	@GetMapping(value = {"get", "edit"})
	public void productEdit(@ModelAttribute("cri") Criteria cri, @RequestParam("pdt_num") Integer pdt_num , Model model) throws Exception{
		
		ProductVO vo = service.edit(pdt_num); // 1차카테고리 정보, 2차카테고리 정보
		
		// 1차카테고리 정보
		model.addAttribute("mainCategory", service.mainCategory());
		
		Integer categoryCode = vo.getCate_code_prt();
		// 1차 카테고리를 참조하는 2차카테고리 목록
		List<CategoryVO> subCategory = service.subCategory(categoryCode);
		model.addAttribute("subCategory", subCategory);
		
		model.addAttribute("productVO", vo);
		
		
		// 현재 상품의
		
		//ProductVO vo = service.edit(pdt_num); 이 구문을 사용시  아래 service 자리에 vo 가 들어가도 된다.  
		//model.addAttribute("vo", service.edit(pdt_num));
		//model.addAttribute(service.edit(pdt_num)); // model.addAttribute("productVO", service.edit(pdt_num));  이렇게 해석됨. //"productVO" -> jsp에서 참조하는 이름
	}

	
	@PostMapping("/edit")
	public String productEdit(Criteria cri, @RequestParam("pdt_num") Integer pdt_num, ProductVO vo, RedirectAttributes rttr) throws Exception{
		
		// 상품정보 수정작업
		//상품이미지가 변경된 경우에 진행되는 작업
		if(vo.getFile1().getSize() > 0) {
			
			// 변경이미지 업로드작업
			vo.setPdt_img(UploadFileUtils.uploadFile(uploadPath, vo.getFile1().getOriginalFilename(), vo.getFile1().getBytes()));
			// 기존이미지 파일삭제작업 - 직접처리요망 내가 만들어보자.
			
		}
			
		
		// <input type="checkbox"> -> 선택 "on" 값
		// 선택을 안할 경우 널로인하여 에러발생됨 . 널처리 해야함.
		if(vo.getPdt_buy().equals("on"))
		{
			vo.setPdt_buy("Y");
		}else {
			vo.setPdt_buy("N");
		}
		
		
		log.info(vo.toString());
		
		service.editOk(vo);
		
		// 상품 이미지 수정여부에 따른 작업
		
		rttr.addFlashAttribute("msg", "editOk");
		
		rttr.addAttribute("pageNum", cri.getPageNum());
		rttr.addAttribute("amount", cri.getAmount());
		rttr.addAttribute("type", cri.getType());
		rttr.addAttribute("keyword", cri.getKeyword());
		
		return "redirect:/admin/product/list";
	}
	
	// 개별삭제
	@PostMapping("delete")
	public String delete(Criteria cri, @RequestParam("pdt_num") Integer pdt_num, RedirectAttributes rttr)
	{
		//삭제기능 작업
		service.delete(pdt_num);
		
		//이미지 삭제
		
		rttr.addFlashAttribute("msg", "deleteOk");
		
		rttr.addAttribute("pageNum", cri.getPageNum());
		rttr.addAttribute("amount", cri.getAmount());
		rttr.addAttribute("type", cri.getType());
		rttr.addAttribute("keyword", cri.getKeyword());
		
		return "redirect:/admin/product/list";		
	}
	
	// 선택삭제(Ajax기능사용)
		@ResponseBody
		@PostMapping("deleteChecked")
		public ResponseEntity<String> deleteChecked(@RequestParam("pdtnumArr[]") List<Integer> pdtnumArr,@RequestParam("imgArr[]") List<String> imgArr){
			
			ResponseEntity<String> entity = null;
			
			
			for(int i=0; i<pdtnumArr.size(); i++) {
				
				//상품삭제 진행
				service.delete(pdtnumArr.get(i));
				
				//이미지 삭제
				UploadFileUtils.deleteFile(uploadPath, imgArr.get(i));
			}
			
			
			return entity;
		}
	
	
	// 페이징 검색기능 포함.
	@GetMapping("/list")
	public void productList(Criteria cri, Model model) throws Exception{
		
		cri.setAmount(5);
		
		log.info("list: " + cri); // 페이징정보, 검색정보 같이 출력
		
		// cri : pageNum:1 amount:10
		List<ProductVO> list = service.getListWithPaging(cri);
			
		// 테이블의 전체 데이터 개수 불러오는 작업
				int total = service.getTotalCount(cri);
				
				log.info("total: " + total);
				
				
				model.addAttribute("pageMaker", new PageDTO(total, cri)); //list.jsp에서 "pageMaker"이름으로 테이블의 모든 데이터를 참조
				
				
				model.addAttribute("list", list); //list.jsp에서 "list"이름으로 테이블의 모든 데이터를 참조

	}
	
	
	@ResponseBody
	@GetMapping(value = "/subCategory/{mainCategoryCode}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<List<CategoryVO>> subCategory(@PathVariable("mainCategoryCode") Integer cate_code_pk){

		ResponseEntity<List<CategoryVO>> entity = null;
	
		// cate_code_pk값이 존재하지 않아, 예외가 발생되는 경우 처리하는 구문
		try {
			entity = new ResponseEntity<List<CategoryVO>>(service.subCategory(cate_code_pk), HttpStatus.OK);
		}catch(Exception e) {
			entity = new ResponseEntity<List<CategoryVO>>(HttpStatus.BAD_REQUEST);
		}
		return entity;
	}
	
	// CKEditor 업로드 기능
	@PostMapping("/imgUpload")
	public void ckeditor_upload(HttpServletRequest req, HttpServletResponse res, MultipartFile upload) {
	
		OutputStream out = null;
		PrintWriter printWriter = null;
		
		// 서버에서 클라이언트에서 보내는 정보에 대한 설정
		res.setCharacterEncoding("utf-8");
		res.setContentType("text/html;charset=utf-8");
		
		try {
			String fileName = upload.getOriginalFilename(); // 클라이언트에서 보내온 파일명
			byte[] bytes = upload.getBytes(); // 파일을 바이트배열로 읽어들임.
			
			// 1)프로젝트에서 관리하는 업로드 폴더작업
			String uploadPath = req.getServletContext().getRealPath("/"); // 프로젝트에 해당하는 톰캣의 물리적 경로.

			// 톰캣이 업로드되는 임시폴더 삭제하는 경향이 있다.
			if(!new File(uploadPath,"resources/upload/").exists())
			{
				new File(uploadPath,"resources/upload/").mkdir();
			}
			
			uploadPath = uploadPath + "resources\\upload\\" + fileName;
			
			log.info(uploadPath); // 실제 업로드 되는 물리적 경로
			
			out = new FileOutputStream(new File(uploadPath)); // 파일명 생성(0byte)
			out.write(bytes);  // 출력스트림작업. 파일이 정상적으로 인식.
			
			// 2)ckeditor 4에서 제공하는 형식구문 : 서버에서 ckeditor에게 보내는 업로드된 파일정보
			// 서버에서 response객체에 printWriter객체를 참조하여, 정보를 보낼때 사용.
			printWriter = res.getWriter();
			String fileUrl = "/upload/"  + fileName; // servlet-context.xml 파일에서 <resources mapping="/upload/**" location="/resources/upload/" />
			
			// 서버에서 ckeditor에게 보내는 업로드된 파일정보
			//{"filename": "업로드파일명", "uploaded":1,"url":"fileUrl"}
			printWriter.println("{\"filename\":\"" + fileName + "\", \"uploaded\":1,\"url\":\""  + fileUrl + "\"}");
			printWriter.flush();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(out != null) {
				try {out.close();}catch(Exception e) {e.printStackTrace();};
			}
			if(printWriter != null) {
				try {printWriter.close();}catch(Exception e) {e.printStackTrace();};
			}
		}
		
	}
	
	@ResponseBody
	@GetMapping("/displayFile")
	public ResponseEntity<byte[]> displayFile(String fileName) throws Exception{
		
		ResponseEntity<byte[]> entity = null;
		
		
		entity = UploadFileUtils.getFileByte(fileName, uploadPath);
		
		return entity;
	}
	
	
	
}
