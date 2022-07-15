package com.pyehouse.mcmod.flightcommand.common.handler;

import com.pyehouse.mcmod.flightcommand.api.capability.CapabilityProviderPlayers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.pyehouse.mcmod.flightcommand.api.capability.FlightCapability.FlightCapabilityResourceURL;

public class CommonCapabilityAttachEventHandler {
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
            if (entity instanceof PlayerEntity) {
                final CapabilityProviderPlayers capProvider = new CapabilityProviderPlayers();
                event.addCapability(FlightCapabilityResourceURL, capProvider);
            }
        }
    }
}
