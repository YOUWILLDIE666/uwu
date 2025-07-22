package com.youwilldie666.uwu.util;

import com.youwilldie666.Config;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;

public class UwUify {
    private static final Random RANDOM = new Random();

    private static final boolean bCHAR_REPLACE = true; // char replace flag
    private static final boolean bWHISPER_MODE = true; // whisper mode flag
    // ^^^ ill keep it like this now

    // CONSTANTS

    private static final Map<String, String> WORD_REPLACEMENTS = new HashMap<>();
    private static final Map<Character, Character> CHAR_REPLACEMENTS = new HashMap<>();
    private static final String NYA_REGEX = "(?i)(n)(?=[.,!?\\s]|$)";
    private static final String NE_REGEX = "(?i)(ne)(?=[.,!?\\s]|$)";

    static {
        // i should rework this sometime
        WORD_REPLACEMENTS.put("lol", "lul");
        WORD_REPLACEMENTS.put("cat", "neko");
        WORD_REPLACEMENTS.put("boy", "kun");
        WORD_REPLACEMENTS.put("girl", "chan");
        WORD_REPLACEMENTS.put("hello", "hewwo");
        WORD_REPLACEMENTS.put("hi", "hai");
        WORD_REPLACEMENTS.put("hey", "haii");
        WORD_REPLACEMENTS.put("love", "wuv");
        WORD_REPLACEMENTS.put("you", "u");
        WORD_REPLACEMENTS.put("your", "ur");
        WORD_REPLACEMENTS.put("small", "smol");
        WORD_REPLACEMENTS.put("cute", "kawaii");
        WORD_REPLACEMENTS.put("friend", "fwen");
        WORD_REPLACEMENTS.put("what", "wat");
        WORD_REPLACEMENTS.put("goodbye", "bye-bye");

        CHAR_REPLACEMENTS.put('l', 'w');
        CHAR_REPLACEMENTS.put('r', 'w');

//        CHAR_REPLACEMENTS.put('!', '~');
//        CHAR_REPLACEMENTS.put('?', '~');
    }

    @Contract("_ -> param1")
    public static String uwuify(@NotNull String message) {
        return Optional.of(message)
                .filter(msg -> !msg.isEmpty())
                .map(String::toLowerCase)
                .map(UwUify::apply)
                .orElse(message);
    }

    private static @NotNull String apply(String message) {
        StringBuilder result = new StringBuilder(message);

        int iINT = getIntensity();

        replaceWords(result);
        if (bCHAR_REPLACE) {
            replaceChars(result);
        }
        if (getNyaToggle() && RANDOM.nextDouble() < getNyaChance() * (iINT / 5.0)) {
            applyNya(result);
        }
        if (getStutterToggle() && RANDOM.nextDouble() < getStutterChance() * (iINT / 5.0)) {
            applyStutter(result);
        }
        if (getEmoticonToggle() && RANDOM.nextDouble() < getEmoticonChance() * (iINT / 5.0)) {
            addEmote(result);
        }
        if (bWHISPER_MODE) {
            whisperMode(result);
        }
        if (RANDOM.nextDouble() < getExcitementChance() * (iINT / 5.0)) {
            result.append(addExcitement(result.toString()));
        }

        return result.toString();
    }

    private static void replaceWords(StringBuilder result) {
        WORD_REPLACEMENTS.forEach((key, value) -> {
            int index = result.indexOf(key);
            while (index != -1) {
                result.replace(index, index + key.length(), value);
                index = result.indexOf(key, index + value.length());
            }
        });
    }

    private static void replaceChars(@NotNull StringBuilder result) {
        for (int i = 0; i < result.length(); i++) {
            char c = result.charAt(i);
            if (CHAR_REPLACEMENTS.containsKey(c)) {
                result.setCharAt(i, CHAR_REPLACEMENTS.get(c));
            }
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
        String emoticon = emoticons.get(RANDOM.nextInt(emoticons.size()));
        result.append(" ").append(emoticon);
    }

    private static void whisperMode(@NotNull StringBuilder result) {
        String resultStr = result.toString();
        result.setLength(0);
        result.append(resultStr.toLowerCase());
    }

    private static @NotNull String addExcitement(@NotNull String text) {
        if (text.endsWith("!")) {
            return "!!";
        } else if (text.endsWith(".") || text.endsWith("~")) {
            return "!";
        } else if (text.endsWith("?")) {
            return "?!";
        } else {
            return RANDOM.nextBoolean() ? "! uwu" : "! >w<";
        }
    }


    //////////////////////////////////////////////////////////\\

    private static @NotNull Integer getIntensity() {
        return Config.INTENSITY.get();
    }

    //////////////////////////////////////////////////////////

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

    //////////////////////////////////////////////////////////

    private static @NotNull Boolean getStutterToggle() {
        return Config.STUTTER_TOGGLE.get();
    }

    private static @NotNull Boolean getEmoticonToggle() {
        return Config.EMOTICON_TOGGLE.get();
    }

    private static @NotNull Boolean getNyaToggle() {
        return Config.NYA_TOGGLE.get();
    }

    //////////////////////////////////////////////////////////

    @Contract(" -> new")
    private static @NotNull @UnmodifiableView List<String> getEmoticonList() {
        return Collections.unmodifiableList(Config.EMOTICON_LIST.get());
    }

    //////////////////////////////////////////////////////////


}
