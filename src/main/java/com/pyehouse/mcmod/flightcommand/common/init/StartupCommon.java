package com.pyehouse.mcmod.flightcommand.common.init;

import com.pyehouse.mcmod.flightcommand.api.capability.FlightCapability;
import com.pyehouse.mcmod.flightcommand.common.handler.CapabilityAttachEventHandler;
import com.pyehouse.mcmod.flightcommand.common.handler.CapabilityPlayerTickEventHandler;
import com.pyehouse.mcmod.flightcommand.common.handler.CommandEventRegistryHandler;
import com.pyehouse.mcmod.flightcommand.common.handler.SyncEventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class StartupCommon {
    @SubscribeEvent
    public static void onCommonSetupEvent(FMLCommonSetupEvent event) {
        // register flight capability
        FlightCapability.registerFlightCapability();

        // and register for events
        MinecraftForge.EVENT_BUS.register(CapabilityAttachEventHandler.class);
        MinecraftForge.EVENT_BUS.register(CapabilityPlayerTickEventHandler.class);
        MinecraftForge.EVENT_BUS.register(CommandEventRegistryHandler.class);
        MinecraftForge.EVENT_BUS.register(SyncEventHandler.class);
    }
}
