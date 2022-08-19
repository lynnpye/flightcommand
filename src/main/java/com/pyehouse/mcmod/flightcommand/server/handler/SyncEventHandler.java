package com.pyehouse.mcmod.flightcommand.server.handler;

import com.pyehouse.mcmod.flightcommand.api.capability.FlightCapability;
import com.pyehouse.mcmod.flightcommand.common.network.ClientUpdater;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

public class SyncEventHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    public static void syncPlayerData(@Nonnull PlayerEntity player) {
        ClientUpdater.sendFlightApplication((ServerPlayerEntity) player);
    }

    @SubscribeEvent
    public static void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        syncPlayerData(event.getPlayer());
    }

    @SubscribeEvent
    public static void playerChangedWorld(PlayerEvent.PlayerChangedDimensionEvent event) {
        syncPlayerData(event.getPlayer());
    }

    @SubscribeEvent
    public static void playerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        syncPlayerData(event.getPlayer());
    }

    @SubscribeEvent
    public static void playerClone(PlayerEvent.Clone event) {
        event.getOriginal().getCapability(FlightCapability.CAPABILITY_FLIGHT).ifPresent(oldStore -> {
            event.getPlayer().getCapability(FlightCapability.CAPABILITY_FLIGHT).ifPresent(newStore -> {
                newStore.copyFrom(oldStore);
            });
        });
        syncPlayerData(event.getPlayer());
    }
}
