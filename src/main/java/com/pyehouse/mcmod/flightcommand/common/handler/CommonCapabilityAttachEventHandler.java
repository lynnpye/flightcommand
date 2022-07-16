package com.pyehouse.mcmod.flightcommand.common.handler;

import com.pyehouse.mcmod.flightcommand.api.capability.CapabilityProviderPlayers;
import net.minecraft.resources.ResourceLocation;
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
        boolean capAdded = false;
        for (ResourceLocation capkey : event.getCapabilities().keySet()) {
            if (capkey.compareTo(FlightCapabilityResourceURL) == 0) {
                capAdded = true;
            }
        }
        if (!capAdded) {
            Entity entity = event.getObject();
            if (entity instanceof Player) {
                final CapabilityProviderPlayers capProvider = new CapabilityProviderPlayers();
                event.addCapability(FlightCapabilityResourceURL, capProvider);
            }
        }
    }
}
