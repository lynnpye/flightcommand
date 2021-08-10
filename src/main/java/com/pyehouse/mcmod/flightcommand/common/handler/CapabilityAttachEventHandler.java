package com.pyehouse.mcmod.flightcommand.common.handler;

import com.pyehouse.mcmod.flightcommand.api.capability.CapabilityProviderPlayers;
import com.pyehouse.mcmod.flightcommand.api.capability.FlightCapability;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.pyehouse.mcmod.flightcommand.api.capability.FlightCapability.FlightCapabilityResourceURL;

public class CapabilityAttachEventHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void attachCapabilityToEntityHandler(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof PlayerEntity) {
            event.addCapability(FlightCapabilityResourceURL, new CapabilityProviderPlayers());
        }
    }
}
