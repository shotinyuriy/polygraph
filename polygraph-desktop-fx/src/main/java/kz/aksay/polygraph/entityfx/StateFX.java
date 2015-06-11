package kz.aksay.polygraph.entityfx;

import kz.aksay.polygraph.entity.Order;

public class StateFX {

	private Order.State state;
	private String name;
	
	public final static StateFX[] VALUES_PLUS_ALL;
	public final static StateFX[] VALUES;
	
	static {
		Order.State[] orderStates = Order.State.values();
		VALUES = new StateFX[orderStates.length];
		VALUES_PLUS_ALL = new StateFX[orderStates.length+1];
		
		VALUES_PLUS_ALL[0] = new StateFX(null, "Все");
		for(int i = 0; i < orderStates.length; i++) {
			VALUES[i] = new StateFX(orderStates[i], orderStates[i].getName());
			VALUES_PLUS_ALL[i+1] = new StateFX(orderStates[i], orderStates[i].getName());
		}
	}
	
	public StateFX(Order.State state, String name) {
		this.state = state;
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}

	public Order.State getState() {
		return state;
	}

	public void setState(Order.State state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
