package com.blitzoffline.stringexpansion;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import at.helpch.placeholderapi.PlaceholderAPI;
import at.helpch.placeholderapi.expansion.Configurable;
import at.helpch.placeholderapi.expansion.PlaceholderExpansion;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.jetbrains.annotations.NotNull;

public class StringExpansion extends PlaceholderExpansion implements Configurable<ExpansionConfig> {
    private final Map<String, ReplacementConfiguration> replacementConfigurations = new HashMap<>();
    private final Map<String, Pattern> patterns = new ConcurrentHashMap<>();
    private String separator;

    @Override
    public @NotNull String getIdentifier() {
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

    @Override
    public @NotNull Class<ExpansionConfig> provideConfigType() {
        return ExpansionConfig.class;
    }

    @Override
    public @NotNull ExpansionConfig provideDefault() {
        return new ExpansionConfig("_", Map.of("small-numbers", Map.of(
                "0", "₀",
                "1", "₁",
                "2", "₂",
                "3", "₃",
                "4", "₄",
                "5", "₅",
                "6", "₆",
                "7", "₇",
                "8", "₈",
                "9", "₉"
        )));
    }

    @Override
    public boolean canRegister() {
        final ExpansionConfig config = getExpansionConfig(StringExpansion.class);
        
        if (config == null) {
            HytaleLogger.get("[PlaceholderAPI] [String-Expansion]").atSevere().log("Unable to load due to missing config");
            return false;
        }
        
        separator = Pattern.quote(config.getSeparator());
        
        for (final Map.Entry<String, Map<String, String>> replacementsEntry : config.getReplacements().entrySet()) {
            final String name = replacementsEntry.getKey();
            final Map<String, String> replacements = replacementsEntry.getValue();
            
            if (replacements.isEmpty()) {
                continue;
            }
            
            final List<String> searchList = new ArrayList<>(replacements.keySet());
            final String[] replacementList = searchList.stream()
                    .map(r -> replacements.getOrDefault(r, ""))
                    .toArray(String[]::new);
            
            replacementConfigurations.put(name, new ReplacementConfiguration(searchList.toArray(new String[0]), replacementList));
        }

        return super.canRegister();
    }

    @Override
    public String onPlaceholderRequest(PlayerRef player, @NotNull String args) {
        final String[] parts = args.split(separator, 2);

        if (parts.length <= 1) {
            return null;
        }

        String action = parts[0].toLowerCase(Locale.ENGLISH);
        String arguments = PlaceholderAPI.setBracketPlaceholders(player, parts[1]);

        String[] split;

        switch (action) {
            case "equals":
                split = arguments.split(separator, 2);
                if (split.length < 2) {
                    return null;
                }
                return PlaceholderAPI.booleanValue(split[0].equals(split[1]));
            case "equalsignorecase":
                split = arguments.split(separator, 2);
                if (split.length < 2) {
                    return null;
                }
                return PlaceholderAPI.booleanValue(split[0].equalsIgnoreCase(split[1]));
            case "contains":
                split = arguments.split(separator, 2);
                if (split.length < 2) {
                    return null;
                }
                return PlaceholderAPI.booleanValue(split[0].contains(split[1]));
            case "containsignorecase":
                split = arguments.split(separator, 2);
                if (split.length < 2) {
                    return null;
                }
                return PlaceholderAPI.booleanValue(StringUtils.containsIgnoreCase(split[0], split[1]));
            case "charat":
                split = arguments.split(separator, 2);
                if (split.length < 2) {
                    return null;
                }
                if (Integer.parseInt(split[0]) >= split[1].length()) {
                    return "";
                }
                return String.valueOf(split[1].charAt(Integer.parseInt(split[0])));
            case "indexof":
                split = arguments.split(separator, 2);
                if (split.length < 2) {
                    return null;
                }
                return String.valueOf(split[0].indexOf(split[1]));
            case "lastindexof":
                split = arguments.split(separator, 2);
                if (split.length < 2) {
                    return null;
                }
                return String.valueOf(split[0].lastIndexOf(split[1]));
            case "substring":
                split = arguments.split(separator, 2);
                if (split.length < 2) {
                    return null;
                }
                if (!split[0].contains(",")) {
                    int index = Integer.parseInt(split[0]);
                    int absIndex = Math.abs(index);
                    int textLength = split[1].length();

                    if (absIndex > textLength) {
                        return "";
                    }

                    if (index < 0) {
                        return split[1].substring(0, textLength - absIndex);
                    }

                    return split[1].substring(index);
                }

                String[] indexes = split[0].split(",", 2);

                int firstIndex = Integer.parseInt(indexes[0]);
                int secondIndex = Integer.parseInt(indexes[1]);

                int textLength = split[1].length();

                if (firstIndex < 0) {
                    firstIndex = 0;
                }

                if (firstIndex >= textLength) {
                    return "";
                }

                if (secondIndex > textLength) {
                    secondIndex = textLength;
                }

                if (secondIndex < 0) {
                    secondIndex = textLength + secondIndex;
                }

                if (firstIndex >= secondIndex) {
                    return "";
                }

                return split[1].substring(firstIndex, secondIndex);
            case "random":
                split = arguments.split(",");
                int random = (int) Math.floor(Math.random()*(split.length));
                return split[random];
            case "replacecharacters":
                split = arguments.split(separator, 2);
                final ReplacementConfiguration configuration = replacementConfigurations.get(split[0]);
                return configuration == null ? split[1] : configuration.replace(split[1]);
            case "shuffle":
                List<String> letters = Arrays.asList(arguments.split(""));
                Collections.shuffle(letters);
                return String.join("", letters);
            case "uppercase":
                return arguments.toUpperCase(Locale.ENGLISH);
            case "lowercase":
                return arguments.toLowerCase(Locale.ENGLISH);
            case "capitalize":
                return arguments.substring(0, 1).toUpperCase() + arguments.substring(1);
            case "sentencecase":
                return arguments.substring(0, 1).toUpperCase() + arguments.substring(1).toLowerCase(Locale.ENGLISH);
            case "length":
                return String.valueOf(arguments.length());
            case "alternateuppercase":
                char[] tempString = arguments.toLowerCase(Locale.ENGLISH).toCharArray();
                for(int index = 0; index < tempString.length; index+=2)
                    tempString[index] = Character.toUpperCase(tempString[index]);
                return String.valueOf(tempString);
            case "startswith":
                split = arguments.split(separator, 2);
                return String.valueOf(split[0].startsWith(split[1]));
            case "endswith":
                split = arguments.split(separator, 2);
                return String.valueOf(split[0].endsWith(split[1]));
            case "trim":
                return arguments.trim();
            case "occurences":
            case "occurrences":
                split = arguments.split(separator, 3);
                if (split.length < 3) {
                    return null;
                }

                if (!split[0].toLowerCase(Locale.ENGLISH).equals("count")) {
                    return null;
                }

                return String.valueOf(StringUtils.countOccurrences(split[1], split[2]));
            case "regex":
                split = arguments.split(separator, 2);
                if (split.length < 2) {
                    return null;
                }

                final Pattern pattern;
                if (patterns.containsKey(split[1])) {
                    pattern = patterns.get(split[1]);
                } else {
                    try {
                        pattern = Pattern.compile(split[1]);
                        patterns.put(split[1], pattern);
                    } catch (PatternSyntaxException exception) {
                        return "Invalid Pattern";
                    }
                }

                return PlaceholderAPI.booleanValue(pattern.matcher(split[0]).matches());
        }

        return null;
    }
}
