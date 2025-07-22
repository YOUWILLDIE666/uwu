package com.youwilldie666;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Arrays;
import java.util.List;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.ConfigValue<Integer> INTENSITY = BUILDER
            .comment("Changes how intense the uwu-replacement should be")
            .translation("uwu.configuration.intensity") // +
            .defineInRange("intensity", 3, 1, 5);

    public static final ModConfigSpec.ConfigValue<Double> STUTTER_CHANCE = BUILDER
            .comment("Changes the stutter chance")
            .translation("uwu.configuration.stutter_chance") // -
            .defineInRange("stutterChance", .2, 0., 1.);

    public static final ModConfigSpec.ConfigValue<Boolean> STUTTER_TOGGLE = BUILDER
            .comment("Toggles stuttering")
            .translation("uwu.configuration.stutter_toggle") // -
            .define("stutterToggle", true);

    public static final ModConfigSpec.ConfigValue<Double> EMOTICON_CHANCE = BUILDER
            .comment("Changes the chance of randomly inserting an emoticon at the end of your message")
            .translation("uwu.configuration.emoticon_chance") // -
            .defineInRange("emoticonChance", .15, 0., 1.);

    public static final ModConfigSpec.ConfigValue<Boolean> EMOTICON_TOGGLE = BUILDER
            .comment("Toggles emoticon insertion")
            .translation("uwu.configuration.emoticon_toggle") // -
            .define("emoticonToggle", true);

    public static final ModConfigSpec.ConfigValue<Double> NYA_CHANCE = BUILDER
            .comment("Changes the chance of randomly inserting a \"nya\" at the end of your message")
            .translation("uwu.configuration.nya_chance") // -
            .defineInRange("nyaChance", .1, 0., 1.);

    public static final ModConfigSpec.ConfigValue<Boolean> NYA_TOGGLE = BUILDER
            .comment("Toggles the \"nya\" insertion")
            .translation("uwu.configuration.nya_toggle") // -
            .define("nyaToggle", true);

    public static final ModConfigSpec.ConfigValue<Double> EXCITEMENT_CHANCE = BUILDER
            .comment("Changes the chance of randomly inserting excitement punctuation at the end of your message")
            .translation("uwu.configuration.excitement_chance") // -
            .defineInRange("excitementChance", .25, 0., 1.);

    public static final ModConfigSpec.ConfigValue<List<? extends String>> EMOTICON_LIST = BUILDER
            .comment("List of emoticons that would get inserted at the end of your message")
            .translation("uwu.configuration.emoticon_list")
            .defineList(
                    "emoticonList",
                    Arrays.asList("OwO", "OwU", "UwU", ">w<", "^w^", ">_<", ":3", "O///O",
                            ">///<", "(*ᵕ ᵕ⁎)", "(⁄ ⁄•⁄ω⁄•⁄ ⁄)", "(●´ω｀●)"),
                    () -> "",
                    element -> element instanceof String string && !string.isEmpty()
            );

    public static final ModConfigSpec SPEC = BUILDER.build();
}
