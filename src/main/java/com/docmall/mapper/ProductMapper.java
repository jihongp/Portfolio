package com.docmall.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.docmall.domain.CategoryVO;
import com.docmall.domain.ProductVO;
import com.docmall.util.Criteria;

public interface ProductMapper {

	public List<CategoryVO> mainCategory();
	
	public List<CategoryVO> subCategory(Integer cate_code_pk);
	
	public List<ProductVO> getListWithPaging(@Param("cri") Criteria cri, @Param("cate_code") Integer cate_code);
	
	public int getTotalCount(Integer cate_code);
	
	public ProductVO get(Integer pdt_num);
	
	public List<ProductVO> mainProductList(Integer cate_code_prt);
	
}
