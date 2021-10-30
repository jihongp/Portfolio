package com.docmall.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.docmall.domain.StatChartVO;
import com.docmall.mapper.ChartMapper;

import lombok.Setter;

@Service
public class ChartServiceImpl implements ChartService {

	@Setter(onMethod_ = @Autowired)
	private ChartMapper mapper;
	
	@Override
	public List<StatChartVO> getYearSales() {
		// TODO Auto-generated method stub
		return mapper.getYearSales();
	}

}
