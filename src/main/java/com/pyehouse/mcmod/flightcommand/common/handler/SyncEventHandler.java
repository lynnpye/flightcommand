package com.pyehouse.mcmod.flightcommand.common.handler;

import com.pyehouse.mcmod.flightcommand.common.network.FlightApplicatorToClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SyncEventHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    private static void privateSyncPlayerData(PlayerEntity player) {
        if (player instanceof ServerPlayerEntity) {
            FlightApplicatorToClient.sendFlightApplication((ServerPlayerEntity) player);
        }
    }

    public static void syncPlayerData(ServerPlayerEntity player) {
        if (!player.getCommandSenderWorld().isClientSide) {
            privateSyncPlayerData(player);
        }
    }

    @SubscribeEvent
    public static void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getPlayer().getCommandSenderWorld().isClientSide) {
            return;
        }
        syncPlayerData((ServerPlayerEntity)event.getPlayer());
    }

    @SubscribeEvent
    public static void playerChangedWorld(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getPlayer().getCommandSenderWorld().isClientSide) {
            return;
        }
        syncPlayerData((ServerPlayerEntity)event.getPlayer());
    }

    @SubscribeEvent
    public static void playerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getPlayer().getCommandSenderWorld().isClientSide) {
            return;
        }
        syncPlayerData((ServerPlayerEntity)event.getPlayer());
    }
}
