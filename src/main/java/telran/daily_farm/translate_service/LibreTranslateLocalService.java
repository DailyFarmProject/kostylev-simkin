package telran.daily_farm.translate_service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import space.dynomake.libretranslate.Language;
import space.dynomake.libretranslate.Translator;

@Service
@Slf4j
public class LibreTranslateLocalService implements ITranslateService {

	@Override
	public String translate(String fromLang, String toLang, String text) {
		return Translator.translate(Language.ENGLISH, Language.RUSSIAN, text);
	}

	@Override
	public Map<String, String> getAllLanguages() {
		return Arrays.stream(Language.values()).collect(Collectors.toMap(Language::getCode, Language::toString));
	}

	@Override
	public <T> T translateDto(T dto, String targetLang) {
		log.info("translatedto************");
		Class<?> clazz = dto.getClass();
		log.info("translatedto clazz - " + clazz);
		for (Field field : clazz.getDeclaredFields()) {
			field.setAccessible(true);
			log.info("translatedto field - " + field.getType().getSimpleName());
			if (field.getType().getSimpleName().equals("String")) {
				try {
					field.set(dto, Translator.translate(Language.ENGLISH, Language.fromCode(targetLang),
							field.get(dto).toString()));
					log.info("translatedto field - " + field.get(dto).toString());
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		log.info("translatedto field - ");
		return dto;
	}

	@SuppressWarnings("unchecked")
	public <T, K> ResponseEntity<T> translateOkResponse(ResponseEntity<T> response, String targetLang) {
		if (response.getStatusCode().is2xxSuccessful() && response.hasBody()) {
			T body = response.getBody();
			if (body instanceof List) {
				List<K> translatedList = (List<K>) translateList((List<K>) body, targetLang);
				return new ResponseEntity<>((T) translatedList, response.getStatusCode());
			}else if(body instanceof Map) {
				Map<K,K> translatedMap = (Map<K,K>) translateMap( (Map<K,K>) body, targetLang);
				return new ResponseEntity<>((T) translatedMap, response.getStatusCode());
			}
			else {
				T translatedDto = translateDto(body, targetLang);
				return new ResponseEntity<>(translatedDto, response.getStatusCode());
			}

		}
		return response;



	}

	private Map<?, ?> translateMap(Map<?, ?> map, String targetLang) {
		if(map.isEmpty())
			return map;
		
		map.values().stream()
			.map(v-> v instanceof String? 
					Translator.translate(Language.fromCode(targetLang),( String) v) :	translateDto(v, targetLang))
					.collect(Collectors.toList());
		
		return map;
	}

	private List<?> translateList(List<?> list, String targetLang) {
		if (list.isEmpty())
			return list;

		if (list.get(0) instanceof String)
			return list.stream().map(el -> Translator.translate(Language.fromCode(targetLang), (String) el))
					.collect(Collectors.toList());
		else
			return list.stream().map(el -> translateDto(el, targetLang)).collect(Collectors.toList());

	}
}
