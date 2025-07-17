package com.youwilldie666.uwu.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class UwUify {
    private static final Random RANDOM = new Random();

    private static final boolean bSTUTTER = true; // stutter flag
    private static final boolean bEMOTE = true; // add emoticons flag
    private static final boolean bNYA = true; // uhh flag
    private static final boolean bCHAR_REPLACE = true; // char replace flag
    private static final double dST_CHANCE = 0.2; // stutter chance
    private static final double dEMO_CHANCE = 0.15; // add emoticons chance
    private static final double dNYA_CHANCE = 0.1; // add nya chance
    private static final double dEX_CHANCE = 0.25; // excitement chance
    private static final int iINT = 3; // intensity (1-5 scale)

    private static final Map<String, String> WORD_REPLACEMENTS = new HashMap<>();
    private static final Map<Character, Character> CHAR_REPLACEMENTS = new HashMap<>();

    static {
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

        CHAR_REPLACEMENTS.put('l', 'w');
        CHAR_REPLACEMENTS.put('r', 'w');
        // CHAR_REPLACEMENTS.put('n', );
        CHAR_REPLACEMENTS.put('!', '~');
        CHAR_REPLACEMENTS.put('?', '~');
    }

    /* good boy
    public static void setStutter(boolean enabled) { stutter = enabled; }
    public static void setEmote(boolean enabled) { emote = enabled; }
    public static void setNya(boolean enabled) { bNYA = enabled; }
    public static void setCharacterReplaceEnabled(boolean enabled) { cReplace = enabled; }
    public static void setIntensity(int level) { intensity = Math.clamp(level, 1, 5); }
    //Math.min(5, Math.max(1, level))*/

    @Contract("null -> param1")
    // p.s. i know this is a heavy function, no care
    public static String uwuify(String message) {
        if (message == null || message.isEmpty()) {
            return message;
        }

        String result = message.toLowerCase();

        for (Map.Entry<String, String> entry : WORD_REPLACEMENTS.entrySet()) {
            result = result.replace(entry.getKey(), entry.getValue());
        }

        if (bCHAR_REPLACE) {
            StringBuilder charBuilder = new StringBuilder();
            for (char c : result.toCharArray()) {
                if (CHAR_REPLACEMENTS.containsKey(c)) {
                    charBuilder.append(CHAR_REPLACEMENTS.get(c));
                } else {
                    charBuilder.append(c);
                }
            }
            result = charBuilder.toString();
        }

        if (bNYA && RANDOM.nextDouble() < dNYA_CHANCE * (iINT / 5.0)) {
            result = result.replaceAll("(?i)(n)(?=[.,!?\\s]|$)", "ny$1");
            result = result.replaceAll("(?i)(ne)(?=[.,!?\\s]|$)", "bNYA");
        }

        if (bSTUTTER && RANDOM.nextDouble() < dST_CHANCE * (iINT / 5.0)) {
            String[] words = result.split(" ");
            if (words.length > 0) {
                int idx = RANDOM.nextInt(words.length);
                if (words[idx].length() > 2) {
                    String firstChar = words[idx].substring(0, 1);
                    words[idx] = firstChar + "-" + words[idx];
                    result = String.join(" ", words);
                }
            }
        }

        if (bEMOTE && RANDOM.nextDouble() < dEMO_CHANCE * (iINT / 5.0)) {
            String[] emotes = {"OwO", "OwU", "UwU", ">w<", "^w^", ">_<", "O///O", ">///<", "(*ᵕ ᵕ⁎)", "(⁄ ⁄•⁄ω⁄•⁄ ⁄)", "(●´ω｀●)"};
            String emote = emotes[RANDOM.nextInt(emotes.length)];

            int idx = RANDOM.nextInt(result.length());
            result = result.substring(0, idx) + " " + emote + " " + result.substring(idx);
        }

        if (RANDOM.nextDouble() < dEX_CHANCE * (iINT / 5.0)) {
            result += addExcitement(result);
        }

        return result;
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
