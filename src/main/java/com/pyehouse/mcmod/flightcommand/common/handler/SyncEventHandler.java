package com.pyehouse.mcmod.flightcommand.common.handler;

import com.pyehouse.mcmod.flightcommand.common.network.FlightApplicatorToClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SyncEventHandler {

    public static void syncPlayerData(PlayerEntity player) {
        FlightApplicatorToClient.sendFlightApplication(player);
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
}
