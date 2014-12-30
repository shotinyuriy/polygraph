package kz.aksay.polygraph.util;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class StringUtils {

	public static final Map<String, String> rusToEngDictionary = new HashMap<>();
	
	static {
		rusToEngDictionary.put("а", "a");
		rusToEngDictionary.put("б", "b");
		rusToEngDictionary.put("в", "v");
		rusToEngDictionary.put("г", "g");
		rusToEngDictionary.put("д", "d");
		rusToEngDictionary.put("е", "e");
		rusToEngDictionary.put("ё", "yo");
		rusToEngDictionary.put("ж", "zh");
		rusToEngDictionary.put("з", "z");
		rusToEngDictionary.put("и", "i");
		rusToEngDictionary.put("й", "y");
		rusToEngDictionary.put("к", "k");
		rusToEngDictionary.put("л", "l");
		rusToEngDictionary.put("м", "m");
		rusToEngDictionary.put("н", "n");
		rusToEngDictionary.put("о", "o");
		rusToEngDictionary.put("п", "p");
		rusToEngDictionary.put("р", "r");
		rusToEngDictionary.put("с", "s");
		rusToEngDictionary.put("т", "t");
		rusToEngDictionary.put("у", "u");
		rusToEngDictionary.put("ф", "f");
		rusToEngDictionary.put("х", "kh");
		rusToEngDictionary.put("ц", "ts");
		rusToEngDictionary.put("ч", "ch");
		rusToEngDictionary.put("ш", "sh");
		rusToEngDictionary.put("щ", "sch");
		rusToEngDictionary.put("ъ", "'");
		rusToEngDictionary.put("ы", "y");
		rusToEngDictionary.put("ь", "'");
		rusToEngDictionary.put("э", "e");
		rusToEngDictionary.put("ю", "yu");
		rusToEngDictionary.put("я", "ya");
	}
	
	public static String rusToEngTranslit (String text){
		StringBuffer newString = new StringBuffer();
		for(int i = 0; i < text.length(); i++) {
			Character curChar = text.charAt(i);
			Character curCharLower = Character.toLowerCase(curChar);
			String newLetter = rusToEngDictionary.get(curCharLower.toString());
			
			if(newLetter != null) {
				if(!curChar.equals(curCharLower)) {
					newLetter = newLetter.substring(0,1).toUpperCase() + newLetter.substring(1);
				}
				newString.append(newLetter);
			} else {
				newString.append(text.charAt(i));
			}
		}
		return newString.toString();
	}
}
