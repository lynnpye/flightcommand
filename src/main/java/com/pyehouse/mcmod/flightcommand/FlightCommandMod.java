package com.pyehouse.mcmod.flightcommand;

import com.pyehouse.mcmod.flightcommand.common.command.GameruleRegistry;
import com.pyehouse.mcmod.flightcommand.common.init.StartupCommon;
import com.pyehouse.mcmod.flightcommand.common.network.NetworkSetup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(FlightCommandMod.MODID)
public class FlightCommandMod {
    public static final String MODID = "flightcommand";

    public FlightCommandMod() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        registerCommonEvents(modEventBus);
    }

    private void registerCommonEvents(IEventBus eventBus) {
        eventBus.register(StartupCommon.class);
        eventBus.register(NetworkSetup.class);
        eventBus.register(GameruleRegistry.class);
    }
}
