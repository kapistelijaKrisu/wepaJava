package wad.service.dataHandlers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class MockRedirectAttributes implements RedirectAttributes {

    private Map<String, ?> map;

    public MockRedirectAttributes() {
        map = new HashMap<>();
    }

    @Override
    public RedirectAttributes addAttribute(String string, Object o) {
        return null;
    }

    @Override
    public RedirectAttributes addAttribute(Object o) {
        return null;
    }

    @Override
    public RedirectAttributes addAllAttributes(Collection<?> clctn) {
        return null;
    }

    @Override
    public RedirectAttributes mergeAttributes(Map<String, ?> map) {
        return null;
    }

    @Override
    public RedirectAttributes addFlashAttribute(String string, Object o) {
        map.put(string, null);
        return null;
    }

    @Override
    public RedirectAttributes addFlashAttribute(Object o) {
        return null;
    }

    @Override
    public Map<String, ?> getFlashAttributes() {
        return map;
    }

    @Override
    public Model addAllAttributes(Map<String, ?> map) {
        return null;
    }

    @Override
    public boolean containsAttribute(String string) {
        return false;
    }

    @Override
    public Map<String, Object> asMap() {
        return null;
    }

}
