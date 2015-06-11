package kz.aksay.polygraph.entityfx;

import kz.aksay.polygraph.entity.Format;

public class FormatFX {
	
	private Format format;
	
	public FormatFX(Format format) {
		this.format = format;
	}
	
	public Format getFormat() {
		return format;
	}
	
	public String toString() {
		return format.name();
	}
}
