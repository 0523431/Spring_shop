package logic;

import java.util.ArrayList;
import java.util.List;

public class Cart {
	private List<ItemSet> itemSetList = new ArrayList<ItemSet>();
	private long total;
	
	public List<ItemSet> getItemSetList() {
		return itemSetList;
	}
	
	// itemSetList에 추가 됨
	public void pushError(ItemSet itemSet) {
		itemSetList.add(itemSet);
	}
	// itemSetList에 추가 됨
	public void push(ItemSet inputItemSet) {
		for(ItemSet listItem : itemSetList) {
			if(listItem.getItem().getId().equals(inputItemSet.getItem().getId())) {
				// 리스트에 있는 아이템의 수량증가만 증가
				listItem.setQuantity(listItem.getQuantity() + inputItemSet.getQuantity());
				return;
			}
		}
		itemSetList.add(inputItemSet);
	}

	public long getTotal() {
		long sum =0;
		for(ItemSet listItem : itemSetList) {
			sum += listItem.getItem().getPrice() * listItem.getQuantity();
		}
		return sum;
	}
	
}
