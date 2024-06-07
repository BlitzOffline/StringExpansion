# StringExpansion
Simple PlaceholderAPI expansion for processing strings.

Configuration:
```yaml
"string":
  # Character(s) used to separate the arguments
  "separator": "_"
  # Replace certain characters in a string when used with %string_replaceCharacters_<configuration>_<string>%
  "replaceCharacters":
    # Configuration name
    "small-numbers":
      # Characters to replace
      "0": "₀"
      "1": "₁"
      "2": "₂"
      "3": "₃"
      "4": "₄"
      "5": "₅"
      "6": "₆"
      "7": "₇"
      "8": "₈"
      "9": "₉"
```

Placeholders:  
  `%string_equals_<string>_<match>%` - Checks if `string` and `match` are the same string in a case-sensitive manner  
  
  `%string_equalsIgnoreCase_<string>_<match>%` - Checks if `string` and `match` are the same in a case-insensitive manner  
  
  `%string_contains_<string>_<match>%` - Checks if `string` contains `match` in a case-sensitive manner  
  
  `%string_containsIgnoreCase_<string>_<match>%` - Checks if `string` contains `match` in a case-insensitive manner  

  `%string_charAt_<index>_<string>%` - Returns the character at `index` from the string `string`  

  `%string_indexOf_<string>_<match>%` - Returns the first index of `match` in `string`. -1 if match does not exist in string  

  `%string_lastIndexOf_<string>_<match>%` - Returns the last index of `match` in `string`. -1 if match does not exist in string  

  `%string_substring_<startIndex>_<string>%` - Returns the substring that starts at `startIndex`  
  **NEGATIVE INDEXES ARE SUPPORTED FOR START INDEX**

  `%string_substring_<startIndex>,<endIndex>_<string>%` - Returns the substring that starts at `startIndex` and ends before `endIndex`  
  **NEGATIVE INDEXES ARE SUPPORTED FOR END INDEX**  
  
  `%string_shuffle_<string>%` - Returns the `string` but with the characters shuffled  
  
  `%string_uppercase_<string>%` - Returns the `string` with all letters being uppercase  
  
  `%string_lowercase_<string>%` - Returns the `string` with all letters being lowercase  

  `%string_sentencecase_<string>%` - Returns the `string` with the first letter being uppercase and all other letters being lowercase 

  `%string_capitalize_<string>%` - Returns the `string` with the first character being uppercase

  `%string_length_<string>%` - Returns the length of the `string`  
  
  `%string_random_<string1>,<string2>,<string3>,<etc>%` - Returns a random string from the list given  

  `%string_replaceCharacters_<configuration>_<string>%` - Replace certain characters in a `string` as defined in config
  
  `%string_alternateuppercase_<string>%` - Uppercase every second character

  `%string_startswith_<string>_<match>%` - Checks if the `string` starts with the `match`

  `%string_endswith_<string>_<match>%` - Checks if the `string` ends with the `match`

  `%string_trim_<string>%` - Trims the starting and ending spaces from the `string`

  `%string_occurences_count_<string>_<match>%` - Counts how many times the `match` appears in the `string`
  
  
  
  **Supports PlaceholderAPI placeholders. Just use brackets(`{}`) instead of percents(`%%`).**  
  For example: `%string_equals_{server_name}_Lobby%` - Will check if the current server name is lobby based on the `%server_name%` placeholder.
