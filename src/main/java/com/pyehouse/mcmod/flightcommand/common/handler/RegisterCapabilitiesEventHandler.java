package com.pyehouse.mcmod.flightcommand.common.handler;

import com.pyehouse.mcmod.flightcommand.api.capability.FlightCapability;
import com.pyehouse.mcmod.flightcommand.api.capability.IFlightCapability;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RegisterCapabilitiesEventHandler {

    @SubscribeEvent
    public static void  registerCapabilitiesEvent(RegisterCapabilitiesEvent event) {
        event.register(FlightCapability.class);
    }
}
