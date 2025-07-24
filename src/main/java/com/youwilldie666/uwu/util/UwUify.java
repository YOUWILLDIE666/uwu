package com.youwilldie666.uwu.util;

import com.youwilldie666.uwu.Config;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UwUify {
    private static final Random RANDOM = new Random();

    private static final boolean CHAR_REPLACE_ENABLED = true; // char replace flag
    // ^^^ ill keep it like this for now

    // CONSTANTS

    private static final Map<String, String> WORD_REPLACEMENTS = new HashMap<>();
    private static final Map<Character, Character> CHAR_REPLACEMENTS = new HashMap<>();
    private static final String NYA_REGEX = "(?i)(n)(?=[.,!?\\s]|$)";
    private static final String NE_REGEX = "(?i)(ne)(?=[.,!?\\s]|$)";

    private static Set<Integer> replaced = new HashSet<>(); // DO NOT FINALIZE

    static {
        initReplacements();
    }

    private static void initReplacements() {
        WORD_REPLACEMENTS.put("lol", "lul");
        WORD_REPLACEMENTS.put("cat", "neko");
        WORD_REPLACEMENTS.put("boy", "kun");
        WORD_REPLACEMENTS.put("girl", "chan");
        WORD_REPLACEMENTS.put("hey", "hiii");
        WORD_REPLACEMENTS.put("love", "wuv");
        WORD_REPLACEMENTS.put("you", "u");
        WORD_REPLACEMENTS.put("your", "ur");
        WORD_REPLACEMENTS.put("small", "smol");
        WORD_REPLACEMENTS.put("cute", "kawaii");
        WORD_REPLACEMENTS.put("friend", "fwen");
        WORD_REPLACEMENTS.put("what", "wat");
        WORD_REPLACEMENTS.put("roar", "rawr");

        CHAR_REPLACEMENTS.put('l', 'w');
        CHAR_REPLACEMENTS.put('r', 'w');
    }

    @Contract("_ -> param1")
    public static @NotNull String uwuify(@NotNull String message) {
        if (message.isEmpty() || message.matches("^[\\p{Punct}\\d]+$")) {
            return message;
        }
        return applyTransformations(message);
    }

    private static @NotNull String applyTransformations(String message) {
        StringBuilder result = new StringBuilder(message);
        int intensity = getIntensity();

        replaceWords(result);
        if (CHAR_REPLACE_ENABLED) {
            replaceChars(result);
        }
        applyOptional(result, intensity);

        return result.toString();
    }

    private static void replaceWords(StringBuilder result) {
        WORD_REPLACEMENTS.forEach((key, value) -> {
            String regex = "(?i)\\b" + Pattern.quote(key) + "\\b";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(result);

            StringBuilder sb = new StringBuilder();
            int l = 0;

            while (matcher.find()) {
                sb.append(result, l, matcher.start());

                String replacement = value;
                String matched = matcher.group();

                for (int i = 0; i < matched.length(); i++) {
                    char orig = matched.charAt(i);
                    if (Character.isUpperCase(orig) && i < replacement.length()) {
                        replacement = replacement.substring(0, i) +
                                Character.toUpperCase(replacement.charAt(i)) +
                                replacement.substring(i + 1);
                    }
                }
                sb.append(replacement);

                for (int i = matcher.start(); i < matcher.end(); i++) {
                    replaced.add(i);
                }

                l = matcher.end();
            }
            sb.append(result, l, result.length());

            result.setLength(0);
            result.append(sb);
        });
    }

    private static void replaceChars(@NotNull StringBuilder result) {
        for (int i = 0; i < result.length(); i++) {
            if (replaced.contains(i)) {
                continue;
            }

            String word = getWordAtIdx(result, i);
            if (getBlacklist().contains(word.toLowerCase())) {
                continue;
            }

            char c = result.charAt(i);
            char lowerC = Character.toLowerCase(c);

            if (CHAR_REPLACEMENTS.containsKey(lowerC)) {
                char replacement = CHAR_REPLACEMENTS.get(lowerC);
                if (Character.isUpperCase(c)) {
                    replacement = Character.toUpperCase(replacement);
                }
                result.setCharAt(i, replacement);
            }
        }
    }

    private static void applyOptional(@NotNull StringBuilder result, int intensity) {
        if (isNyaToggleEnabled() && RANDOM.nextDouble() < getNyaChance() * (intensity / 5.0)) {
            applyNya(result);
        }
        if (isStutterToggleEnabled() && RANDOM.nextDouble() < getStutterChance() * (intensity / 5.0)) {
            applyStutter(result);
        }
        if (isEmoticonToggleEnabled() && RANDOM.nextDouble() < getEmoticonChance() * (intensity / 5.0)) {
            addEmote(result);
        }
        if (isWhisperModeToggleEnabled()) {
            whisperMode(result);
        }
        if (RANDOM.nextDouble() < getExcitementChance() * (intensity / 5.0)) {
            result.append(addExcitement(result.toString()));
        }
    }

    private static void applyNya(@NotNull StringBuilder result) {
        String resultStr = result.toString();
        resultStr = resultStr.replaceAll(NYA_REGEX, "ny$1");
        resultStr = resultStr.replaceAll(NE_REGEX, "bNYA");
        result.setLength(0); // clear
        result.append(resultStr);
    }

    private static void applyStutter(@NotNull StringBuilder result) {
        String[] words = result.toString().split(" ");
        if (words.length > 0) {
            int idx = RANDOM.nextInt(words.length);
            if (words[idx].length() > 2) {
                String firstChar = words[idx].substring(0, 1);
                words[idx] = firstChar + "-" + words[idx];
                result.setLength(0); // clear
                result.append(String.join(" ", words));
            }
        }
    }

    private static void addEmote(@NotNull StringBuilder result) {
        List<String> emoticons = getEmoticonList();
        if (!emoticons.isEmpty()) {
            String emoticon = emoticons.get(RANDOM.nextInt(emoticons.size()));
            result.append(" ").append(emoticon);
        }
    }

    private static void whisperMode(@NotNull StringBuilder result) {
        String resultStr = result.toString();
        result.setLength(0);
        result.append(resultStr.toLowerCase());
    }

    private static @NotNull String addExcitement(@NotNull String text) {
        char last = text.charAt(text.length() - 1);
        return switch (last) {
            case '!' -> "!!";
            case '.', '~' -> "!";
            case '?' -> "?!";
            default -> "";
        };
    }


    //////////////////////////////////////////////////////////

    /**
     * Helper method to retrieve the word at a specific index
     */
    private static String getWordAtIdx(StringBuilder result, int idx) {
        int start = idx;
        while (start > 0 && !isWordBoundary(result.charAt(start - 1))) {
            start--;
        }
        int end = start;
        while (end < result.length() && !isWordBoundary(result.charAt(end))) {
            end++;
        }
        return result.substring(start, end);
    }

    /**
     * Helper method to determine if a character is a word boundary
     */
    private static boolean isWordBoundary(char c) {
        return Character.isWhitespace(c) || !Character.isLetterOrDigit(c);
    }

    /// ///////////////////////////////////////////////////////

    private static @NotNull Integer getIntensity() {
        return Config.INTENSITY.get();
    }

    /// ///////////////////////////////////////////////////////

    private static @NotNull Double getStutterChance() {
        return Config.STUTTER_CHANCE.get();
    }

    private static @NotNull Double getEmoticonChance() {
        return Config.EMOTICON_CHANCE.get();
    }

    private static @NotNull Double getNyaChance() {
        return Config.NYA_CHANCE.get();
    }

    private static @NotNull Double getExcitementChance() {
        return Config.EXCITEMENT_CHANCE.get();
    }

    /// ///////////////////////////////////////////////////////

    private static @NotNull Boolean isStutterToggleEnabled() {
        return Config.STUTTER_TOGGLE.get();
    }

    private static @NotNull Boolean isEmoticonToggleEnabled() {
        return Config.EMOTICON_TOGGLE.get();
    }

    private static @NotNull Boolean isNyaToggleEnabled() {
        return Config.NYA_TOGGLE.get();
    }

    private static @NotNull Boolean isWhisperModeToggleEnabled() {
        return Config.WHISPER_MODE_TOGGLE.get();
    }

    /// ///////////////////////////////////////////////////////

    @Contract(" -> new")
    private static @NotNull @UnmodifiableView List<String> getEmoticonList() {
        return Collections.unmodifiableList(Config.EMOTICON_LIST.get());
    }

    @Contract(" -> new")
    private static @NotNull @UnmodifiableView List<String> getBlacklist() {
        return Collections.unmodifiableList(Config.BLACKLIST.get());
    }

    //////////////////////////////////////////////////////////


}
