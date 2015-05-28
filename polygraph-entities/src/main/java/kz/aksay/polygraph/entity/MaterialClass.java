package kz.aksay.polygraph.entity;

public enum MaterialClass {

	PAPER("Бумага"),
	BINDING_SPRING("Пружина"),
	LAMINATE("Ламинат"),
	STICKER("Наклейка");
	
	private MaterialClass(String name) {
		this.name = name;
	}
	
	private String name;

	public String getName() {
		return name;
	}
}
