package telran.daily_farm.translate_service;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import space.dynomake.libretranslate.Language;
import space.dynomake.libretranslate.Translator;

@Service
@Slf4j
public class LibreTranslateLocalService  implements ITranslateService{

	@Override
	public String translate(String fromLang, String toLang, String text) {
		return Translator.translate(Language.ENGLISH, Language.RUSSIAN, text);
	}

	@Override
	public Map<String, String> getAllLanguages() {
	return 	Arrays.stream(Language.values())
			.collect(Collectors.toMap( Language::getCode,  Language::toString));
	}

}
