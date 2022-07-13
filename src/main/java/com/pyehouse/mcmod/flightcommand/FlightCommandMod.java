package com.pyehouse.mcmod.flightcommand;

import com.pyehouse.mcmod.flightcommand.client.ClientEventRegistrar;
import com.pyehouse.mcmod.flightcommand.client.handler.ClientConfigHandler;
import com.pyehouse.mcmod.flightcommand.common.CommonEventRegistrar;
import com.pyehouse.mcmod.flightcommand.common.handler.CommonConfigHandler;
import com.pyehouse.mcmod.flightcommand.server.ServerEventRegistrar;
import com.pyehouse.mcmod.flightcommand.server.handler.ServerConfigHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(FlightCommandMod.MODID)
public class FlightCommandMod {
    public static final String MODID = "flightcommand";

    public FlightCommandMod() {
        registerConfigs();

        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        final IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        final CommonEventRegistrar commonEventRegistrar = new CommonEventRegistrar(modEventBus, forgeEventBus);
        commonEventRegistrar.registration();

        final ClientEventRegistrar clientEventRegistrar = new ClientEventRegistrar(modEventBus, forgeEventBus);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> clientEventRegistrar::registration);

        final ServerEventRegistrar serverEventRegistrar = new ServerEventRegistrar(modEventBus, forgeEventBus);
        DistExecutor.safeRunWhenOn(Dist.DEDICATED_SERVER, () -> serverEventRegistrar::registration);
    }

    public static void registerConfigs() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfigHandler.COMMON_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerConfigHandler.SERVER_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfigHandler.CLIENT_SPEC);
    }
}
