package com.blitzoffline.stringexpansion;

import java.util.Map;

public class ExpansionConfig {
    private String separator;
    private Map<String, Map<String, String>> replacements;

    public ExpansionConfig(String separator, Map<String, Map<String, String>> replacements) {
        this.separator = separator;
        this.replacements = replacements;
    }

    public String getSeparator() {
        return separator;
    }

    public Map<String, Map<String, String>> getReplacements() {
        return replacements;
    }
}
