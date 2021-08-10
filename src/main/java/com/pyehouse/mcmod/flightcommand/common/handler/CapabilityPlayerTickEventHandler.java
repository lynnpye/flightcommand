package com.pyehouse.mcmod.flightcommand.common.handler;

import com.pyehouse.mcmod.flightcommand.api.capability.FlightCapability;
import com.pyehouse.mcmod.flightcommand.api.capability.IFlightCapability;
import com.pyehouse.mcmod.flightcommand.common.command.GameruleRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CapabilityPlayerTickEventHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START) {
            return; // move on if we aren't firing early
        }

        PlayerEntity player = event.player;
        if (player == null) {
            LOGGER.error("player is null, skipping");
            return; // no player, leave
        }

        IFlightCapability flightCap = player.getCapability(FlightCapability.CAPABILITY_FLIGHT).orElse(null);

        if (flightCap == null) {
            return; // no capability present for some reason
        }

        if (flightCap.isWorldFlightEnabled() != GameruleRegistry.isEnabled(player.getCommandSenderWorld(), GameruleRegistry.doCreativeFlight)) {
            flightCap.setWorldFlightEnabled(!flightCap.isWorldFlightEnabled());
            flightCap.setShouldCheckFlight(true);
        }

        // now check capabilities and set abilities
        if (!player.abilities.mayfly && (isWorldFlightOn(player) || !canRemoveFlightByGamemode(player) || flightCap.isAllowedFlight() || flightCap.isShouldCheckFlight())) {
            // should be able to fly but can't, we can fix that
            player.abilities.mayfly = true;
            player.onUpdateAbilities();
        } else if (player.abilities.mayfly && !isWorldFlightOn(player) && canRemoveFlightByGamemode(player) && !flightCap.isAllowedFlight() && flightCap.isShouldCheckFlight()) {
            LOGGER.info("Disabling flight and possibly causing fall damage");
            player.abilities.mayfly = false; // hopefully anything else allowing flight will revert this
            if (player.abilities.flying) {
                player.abilities.flying = false; // and you should no longer be flying
            }
            player.onUpdateAbilities();
            flightCap.setShouldCheckFlight(false);
        }
    }

    private static boolean isWorldFlightOn(PlayerEntity player) {
        try {
            return player.getCommandSenderWorld().getGameRules().getBoolean(GameruleRegistry.doCreativeFlight);
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean canRemoveFlightByGamemode(PlayerEntity player) {
        return !(player.isSpectator() || player.isCreative());
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        PlayerEntity player = event.getPlayer();
        IFlightCapability flightCap = player.getCapability(FlightCapability.CAPABILITY_FLIGHT).orElse(null);
        IFlightCapability oldFlightCap = event.getOriginal().getCapability(FlightCapability.CAPABILITY_FLIGHT).orElse(null);
        flightCap.setAllowedFlight(oldFlightCap.isAllowedFlight());
    }
}
