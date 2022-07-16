package com.pyehouse.mcmod.flightcommand.server.handler;

import com.pyehouse.mcmod.flightcommand.api.capability.FlightCapability;
import com.pyehouse.mcmod.flightcommand.common.network.ClientUpdater;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

public class SyncEventHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    public static void syncPlayerData(@Nonnull Player player) {
        ClientUpdater.sendFlightApplication((ServerPlayer) player);
    }

    @SubscribeEvent
    public static void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        syncPlayerData(event.getEntity());
    }

    @SubscribeEvent
    public static void playerChangedWorld(PlayerEvent.PlayerChangedDimensionEvent event) {
        syncPlayerData(event.getEntity());
    }

    @SubscribeEvent
    public static void playerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        syncPlayerData(event.getEntity());
    }

    @SubscribeEvent
    public static void playerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().getCapability(FlightCapability.CAPABILITY_FLIGHT).ifPresent(oldStore -> {
                event.getEntity().getCapability(FlightCapability.CAPABILITY_FLIGHT).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
        }
        syncPlayerData(event.getEntity());
    }
}
