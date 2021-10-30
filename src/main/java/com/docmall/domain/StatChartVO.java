package com.docmall.domain;

public class StatChartVO {
	
	private String year;
	private int total;
	
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	@Override
	public String toString() {
		return "StatChartVO [year=" + year + ", total=" + total + "]";
	}
	

}
