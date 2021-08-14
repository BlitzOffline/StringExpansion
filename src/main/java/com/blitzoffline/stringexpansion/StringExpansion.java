package com.blitzoffline.stringexpansion;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class StringExpansion extends PlaceholderExpansion {
    @Override
    public @NotNull
    String getIdentifier() {
        return "string";
    }

    @Override
    public @NotNull String getAuthor() {
        return "BlitzOffline";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    private String getBoolean(boolean b) {
        return b ? PlaceholderAPIPlugin.booleanTrue() : PlaceholderAPIPlugin.booleanFalse();
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String args) {
        if (args.split("_").length <= 1) {
            return null;
        }

        String action = args.split("_", 2)[0].toLowerCase(Locale.ENGLISH);
        String arguments = PlaceholderAPI.setBracketPlaceholders(player, args.split("_", 2)[1]);
        String[] split;

        switch (action) {
            case "equals":
                split = arguments.split("_", 2);
                if (split.length < 2) {
                    return null;
                }
                return getBoolean(split[0].equals(split[1]));
            case "equalsignorecase":
                split = arguments.split("_", 2);
                if (split.length < 2) {
                    return null;
                }
                return getBoolean(split[0].equalsIgnoreCase(split[1]));
            case "contains":
                split = arguments.split("_", 2);
                if (split.length < 2) {
                    return null;
                }
                return getBoolean(split[0].contains(split[1]));
            case "containsignorecase":
                split = arguments.split("_", 2);
                if (split.length < 2) {
                    return null;
                }
                return getBoolean(StringUtils.containsIgnoreCase(split[0], split[1]));
            case "charat":
                split = arguments.split("_", 2);
                if (split.length < 2) {
                    return null;
                }
                if (Integer.parseInt(split[0]) >= split[1].length()) {
                    return "";
                }
                return String.valueOf(split[1].charAt(Integer.parseInt(split[0])));
            case "indexof":
                split = arguments.split("_", 2);
                if (split.length < 2) {
                    return null;
                }
                return String.valueOf(split[0].indexOf(split[1]));
            case "lastindexof":
                split = arguments.split("_", 2);
                if (split.length < 2) {
                    return null;
                }
                return String.valueOf(split[0].lastIndexOf(split[1]));
            case "substring":
                split = arguments.split("_", 2);
                if (split.length < 2) {
                    return null;
                }
                if (!split[0].contains(",")) {
                    int index = Integer.parseInt(split[0]);
                    if (index < 0) {
                        index = 0;
                    }
                    if (index >= split[1].length()) {
                        return "";
                    }
                    return split[1].substring(index);
                }
                int firstIndex = Integer.parseInt(split[0].split(",", 2)[0]);
                int secondIndex = Integer.parseInt(split[0].split(",", 2)[1]);
                if (firstIndex < 0) {
                    firstIndex = 0;
                }
                if (secondIndex > split[1].length()) {
                    secondIndex = split[1].length();
                }
                if (firstIndex >= split[1].length() || secondIndex <= firstIndex) {
                    return "";
                }
                return split[1].substring(firstIndex, secondIndex);
            case "random":
                split = arguments.split(",");
                int random = (int) Math.floor(Math.random()*(split.length));
                return split[random];
            case "shuffle":
                List<String> letters = Arrays.asList(arguments.split(""));
                Collections.shuffle(letters);
                return String.join("", letters);
            case "uppercase":
                return arguments.toUpperCase(Locale.ENGLISH);
            case "lowercase":
                return arguments.toLowerCase(Locale.ENGLISH);
            case "length":
                return String.valueOf(arguments.length());
        }

        return null;
    }
}
