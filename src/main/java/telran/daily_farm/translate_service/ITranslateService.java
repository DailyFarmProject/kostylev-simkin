package telran.daily_farm.translate_service;

import java.util.Map;

public interface ITranslateService {
	
	Map<String, String> getAllLanguages();
	
	String translate(String fromLang, String toLang, String text);

}
