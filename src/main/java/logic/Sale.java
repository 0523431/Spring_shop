package logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Sale {
	private int saleid;
	private String userid;
	private Date updatetime;
	private User user;
	
	// 상품정보를 가지고 있는 상태로 view(end.shop)에 넘어가게 됨
	private List<SaleItem> itemList = new ArrayList<SaleItem>();
	
	// 추가
	private long total;
	
	public long getTotal() {
		long sum =0;
		for(SaleItem saleItem : itemList) {
			sum += saleItem.getItem().getPrice() * saleItem.getQuantity();
		}
		return sum;
	}
	
	public int getSaleid() {
		return saleid;
	}
	public void setSaleid(int saleid) {
		this.saleid = saleid;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<SaleItem> getItemList() {
		return itemList;
	}
	public void setItemList(List<SaleItem> itemList) {
		this.itemList = itemList;
	}}
