package com.openwt.officetracking.domain.utils;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.openwt.officetracking.domain.utils.StringUtils.findCaptureGroup;
import static com.openwt.officetracking.domain.utils.StringUtils.replaceText;
import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {

    @Test
    public void testReplaceText() {
        final String raw = "Hello {{name}}, welcome to {{company}}!";
        final Map<String, String> replacements = new HashMap<>();
        replacements.put("name", "John");
        replacements.put("company", "ABC Corp");

        final String expected = "Hello John, welcome to ABC Corp!";
        final String actual = replaceText(raw, replacements);

        assertEquals(expected, actual);
    }

    @Test
    public void testFindCaptureGroup() {
        final String text = "The answer is 42";
        final String regex = "The answer is (\\d+)";

        final String expected = "42";
        final String actual = findCaptureGroup(text, regex);

        assertEquals(expected, actual);
    }

    @Test
    public void testFindCaptureGroup_NoMatch() {
        final String text = "No matching group";
        final String regex = "The answer is (\\d+)";

        assertThrows(RuntimeException.class, () -> findCaptureGroup(text, regex));
    }
}