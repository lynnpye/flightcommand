package com.pyehouse.mcmod.flightcommand.common.handler;

import com.pyehouse.mcmod.flightcommand.api.capability.CapabilityProviderPlayers;
import com.pyehouse.mcmod.flightcommand.api.capability.FlightCapability;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlayerEventHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().reviveCaps();

            event.getPlayer().getCapability(FlightCapability.CAPABILITY_FLIGHT).ifPresent(newCap -> {
                CapabilityDispatcher capabilityDispatcher = event.getOriginal().getCapabilities();
                for (var capPro : capabilityDispatcher.caps) {
                    if (capPro instanceof CapabilityProviderPlayers) {
                        CapabilityProviderPlayers myPro = (CapabilityProviderPlayers) capPro;
                        newCap.copyFrom(myPro.getFlightCapability());
                    }
                }
            });

            event.getOriginal().invalidateCaps();
        }
    }
}
