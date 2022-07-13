package com.pyehouse.mcmod.flightcommand.common.handler;

import com.pyehouse.mcmod.flightcommand.api.capability.FlightCapability;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class FlightCapabilityRegistrar {
    @SubscribeEvent
    public static void onCommonSetupEvent(FMLCommonSetupEvent event) {
        FlightCapability.registerFlightCapability();
    }
}
