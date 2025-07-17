package com.youwilldie666.uwu;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.jetbrains.annotations.NotNull;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Uwu.MOD_ID)
public class Uwu {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "uwu";

    public Uwu(@NotNull IEventBus modEventBus, @NotNull ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

       // NeoForge.EVENT_BUS.register(this);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {}

}
