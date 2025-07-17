package com.youwilldie666.uwu.util;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class UwUify {
    private static final Random RANDOM = new Random();

    private static final boolean bSTUTTER = true; // stutter flag
    private static final boolean bEMOTE = true; // add emoticons flag
    private static final boolean bNYA = true; // uhh flag
    private static final boolean bCHAR_REPLACE = true; // char replace flag
    // private static final boolean bCUTE_SUFFIX = true; // add cute suffix flag
    // private static final boolean bWHISPER_MODE = true; // whisper mode flag
    private static final double dST_CHANCE = 0.2; // stutter chance
    private static final double dEMO_CHANCE = 0.15; // add emoticons chance
    private static final double dNYA_CHANCE = 0.1; // add nya chance
    private static final double dEX_CHANCE = 0.25; // excitement chance
    private static final double dCUTE_SUFFIX_CHANCE = 0.2; // cute suffix chance
    private static final int iINT = 3; // intensity (1-5 scale)

    // CONSTANTS

    private static final Map<String, String> WORD_REPLACEMENTS = new HashMap<>();
    private static final Map<Character, Character> CHAR_REPLACEMENTS = new HashMap<>();
    private static final String[] EMOTES = {"OwO", "OwU", "UwU", ">w<", "^w^", ">_<", "O///O", ">///<", "(*ᵕ ᵕ⁎)", "(⁄ ⁄•⁄ω⁄•⁄ ⁄)", "(●´ω｀●)"};
    // hmm private static final String[] CUTE_SUFFIXES = {"~nya", "~wuw", "~kawaii", "~desu", "~chan"};
    private static final String NYA_REGEX = "(?i)(n)(?=[.,!?\\s]|$)";
    private static final String NE_REGEX = "(?i)(ne)(?=[.,!?\\s]|$)";

    static {
        // idk what do i do with this lol thing ugh (i'm lazy to create a blacklist)
        // [22:23:49] [Render thread/INFO] [minecraft/ChatComponent]: [CHAT] <Dev> wuw! uwu
        // wtf broooooooooo
        WORD_REPLACEMENTS.put("lol", "lul"); // lol was wow lmaoo
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

        CHAR_REPLACEMENTS.put('!', '~');
        CHAR_REPLACEMENTS.put('?', '~');
    }

    //  @Contract("null -> param1")
    public static String uwuify(String message) {
        return Optional.ofNullable(message)
                .filter(msg -> !msg.isEmpty())
                .map(String::toLowerCase)
                .map(UwUify::appTrans)
                .orElse(message);
    }

    private static @NotNull String appTrans(String message) {
        StringBuilder result = new StringBuilder(message);

        replaceWords(result);
        if (bCHAR_REPLACE) {
            replaceChars(result);
        }
        if (bNYA && RANDOM.nextDouble() < dNYA_CHANCE * (iINT / 5.0)) {
            applyNya(result);
        }
        if (bSTUTTER && RANDOM.nextDouble() < dST_CHANCE * (iINT / 5.0)) {
            applyStutter(result);
        }
        if (bEMOTE && RANDOM.nextDouble() < dEMO_CHANCE * (iINT / 5.0)) {
            addEmote(result);
        }
//        if (bCUTE_SUFFIX && RANDOM.nextDouble() < dCUTE_SUFFIX_CHANCE * (iINT / 5.0)) {
//            addSuffix(result);
//        }
        if (bWHISPER_MODE) {
            whisperMode(result);
        }
        if (RANDOM.nextDouble() < dEX_CHANCE * (iINT / 5.0)) {
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
        String emote = EMOTES[RANDOM.nextInt(EMOTES.length)];
        result.append(" ").append(emote);
    }

//    private static void addSuffix(@NotNull StringBuilder result) {
//        String suffix = CUTE_SUFFIXES[RANDOM.nextInt(CUTE_SUFFIXES.length)];
//        result.append(suffix);
//    }

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
}
