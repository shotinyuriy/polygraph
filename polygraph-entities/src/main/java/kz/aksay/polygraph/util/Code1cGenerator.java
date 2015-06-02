package kz.aksay.polygraph.util;


public class Code1cGenerator {

	private static long code = 1;
	private static final int code1cLength = 6;
	
	public static String generateCode() {
		
		String code1c = code+"";
		StringBuffer sb = new StringBuffer();
		int n = code1cLength - code1c.length();
		for(int i = 0; i<n; i++) {
			sb.append("0");
		}
		sb.append(code);
		
		code++;
		return sb.toString();
		
	}
}
