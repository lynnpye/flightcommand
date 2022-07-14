package com.pyehouse.mcmod.flightcommand.common.handler;

import com.pyehouse.mcmod.flightcommand.api.capability.CapabilityProviderPlayers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.pyehouse.mcmod.flightcommand.api.capability.FlightCapability.FlightCapabilityResourceURL;

public class CommonCapabilityAttachEventHandler {
    private static final Logger LOGGER = LogManager.getLogger();
    @SubscribeEvent
    public static void attachCapabilityToEntityHandler(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof Player) {
            CapabilityProviderPlayers capProvider = new CapabilityProviderPlayers();
            event.addCapability(FlightCapabilityResourceURL, capProvider);
            event.addListener(capProvider::invalidate);
        }
    }
}
