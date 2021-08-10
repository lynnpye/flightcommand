package com.pyehouse.mcmod.flightcommand;

import com.pyehouse.mcmod.flightcommand.client.ClientSideOnlyModEventRegistrar;
import com.pyehouse.mcmod.flightcommand.common.command.GameruleRegistry;
import com.pyehouse.mcmod.flightcommand.common.init.StartupCommon;
import com.pyehouse.mcmod.flightcommand.common.network.NetworkSetup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(FlightCommandMod.MODID)
public class FlightCommandMod {
    public static final String MODID = "flightcommand";

    private static final Logger LOGGER = LogManager.getLogger();

    public FlightCommandMod() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        final ClientSideOnlyModEventRegistrar clientSideOnlyModEventRegistrar = new ClientSideOnlyModEventRegistrar(modEventBus);

        registerCommonEvents(modEventBus);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> clientSideOnlyModEventRegistrar::registerClientOnlyEvents);
    }

    private void registerCommonEvents(IEventBus eventBus) {
        eventBus.register(StartupCommon.class);
        eventBus.register(NetworkSetup.class);
        eventBus.register(GameruleRegistry.class);
    }
}
