package com.pyehouse.mcmod.flightcommand.common.handler;

import com.pyehouse.mcmod.flightcommand.api.capability.FlightCapability;
import com.pyehouse.mcmod.flightcommand.api.capability.IFlightCapability;
import com.pyehouse.mcmod.flightcommand.api.util.Tools;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommonPlayerTickEventHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        PlayerEntity player = event.getPlayer();
        IFlightCapability flightCap = player.getCapability(FlightCapability.CAPABILITY_FLIGHT).orElse(null);
        IFlightCapability oldFlightCap = event.getOriginal().getCapability(FlightCapability.CAPABILITY_FLIGHT).orElse(null);
        flightCap.setAllowedFlight(oldFlightCap.isAllowedFlight());
    }

    private static void giveFlightIfOffGround(ServerPlayerEntity player) {
        if (!player.isOnGround() && Tools.isWorldFlightOn(player)) {
            player.abilities.mayfly = true;
            player.abilities.flying = true;
            player.onUpdateAbilities();
        }
    }

    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinWorldEvent event) {
        if (!(event.getEntity() instanceof ServerPlayerEntity)) return;

        ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) event.getEntity();

        giveFlightIfOffGround(serverPlayerEntity);

        SyncEventHandler.syncPlayerData(serverPlayerEntity);
    }

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        if (player == null) {
            LOGGER.error("player is null, skipping");
            return; // no player, leave
        }
        IFlightCapability flightCap = player.getCapability(FlightCapability.CAPABILITY_FLIGHT).orElse(null);

        if (flightCap == null) {
            return; // no capability present for some reason
        }

        // only check gamerule on the server, can't be trusted on the client?
        boolean worldFlightOn = flightCap.isWorldFlightEnabled();
        if (Tools.isServer(player)) {
            worldFlightOn = Tools.isWorldFlightOn(player);
            if (flightCap.isWorldFlightEnabled() != worldFlightOn) {
                flightCap.setWorldFlightEnabled(!flightCap.isWorldFlightEnabled());
                SyncEventHandler.syncPlayerData((ServerPlayerEntity) player);
            }
        }

        switch (event.phase) {
            case START: {
                // turn it off early
                if (player.abilities.mayfly && !worldFlightOn && Tools.canRemoveFlightByGamemode(player) && !flightCap.isAllowedFlight() /*&& flightCap.isShouldCheckFlight()*/) {
                    LOGGER.info("Disabling flight and possibly causing fall damage");
                    player.abilities.mayfly = false; // hopefully anything else allowing flight will revert this
                    if (player.abilities.flying) {
                        player.abilities.flying = false; // and you should no longer be flying
                    }
                    player.onUpdateAbilities();
                }
            };
            break;
            case END: {
                // turn it on late:
                if (!player.abilities.mayfly && (worldFlightOn || !Tools.canRemoveFlightByGamemode(player) || flightCap.isAllowedFlight() /*|| flightCap.isShouldCheckFlight()*/)) {
                    LOGGER.info("Enabling flight and possibly annoying a genius fox");
                    // should be able to fly but can't, we can fix that
                    player.abilities.mayfly = true;
                    if (!player.isOnGround()) {
                        player.abilities.flying = true;
                    }
                    player.onUpdateAbilities();
                }
            }
            break;

            default:
                //no-op
                break;

        }
    }
}
