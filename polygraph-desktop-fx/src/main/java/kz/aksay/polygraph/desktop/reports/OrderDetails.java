package kz.aksay.polygraph.desktop.reports;

public class OrderDetails {
	private String number;
	private String description;
	private String cost;
	private String amount;
	private String wasted;
	private String rowType;
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCost() {
		return cost;
	}
	public void setCost(String cost) {
		this.cost = cost;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getWasted() {
		return wasted;
	}
	public void setWasted(String wasted) {
		this.wasted = wasted;
	}
	public String getRowType() {
		return rowType;
	}
	public void setRowType(String rowType) {
		this.rowType = rowType;
	}
	
}
