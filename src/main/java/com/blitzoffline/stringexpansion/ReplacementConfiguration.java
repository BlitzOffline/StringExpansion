package com.blitzoffline.stringexpansion;

import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

public class ReplacementConfiguration {

    private final String[] searchList;
    private final String[] replacementList;

    public ReplacementConfiguration(String[] searchList, String[] replacementList) {
        this.searchList = searchList;
        this.replacementList = replacementList;
    }

    public @NotNull String replace(final @NotNull String text) {
        return StringUtils.replaceEach(text, searchList, replacementList);
    }

}
