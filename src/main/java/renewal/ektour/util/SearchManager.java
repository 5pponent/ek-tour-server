package renewal.ektour.util;

import org.springframework.stereotype.Component;
import renewal.ektour.dto.request.AdminSearchForm;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SearchManager {

    private final Map<String, AdminSearchForm> searchMap = new ConcurrentHashMap<>();

    public void setValue(String key, AdminSearchForm form) {
        searchMap.put(key, form);
    }

    public AdminSearchForm getValue(String key) {
        return searchMap.get(key);
    }
}
