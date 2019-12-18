package logic;

public class ItemSet {
	private Item item;
	private Integer quantity;
	
	public ItemSet(Item item, Integer quantity) {
		super();
		this.item = item;
		this.quantity = quantity;
	}

	public ItemSet(Integer quantity) {
		super();
		this.quantity = quantity;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "ItemSet [item=" + item + ", quantity=" + quantity + "]";
	}

}
