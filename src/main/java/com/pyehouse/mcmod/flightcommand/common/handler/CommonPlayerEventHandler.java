package com.pyehouse.mcmod.flightcommand.common.handler;

import com.pyehouse.mcmod.flightcommand.api.capability.FlightCapability;
import com.pyehouse.mcmod.flightcommand.api.capability.IFlightCapability;
import com.pyehouse.mcmod.flightcommand.common.command.GameruleRegistrar;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommonPlayerEventHandler {

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

        boolean isCreativeFlightRuleEnabled = GameruleRegistrar.isCreativeFlightEnabled(player);

        if (flightCap.isWorldFlightEnabled() != isCreativeFlightRuleEnabled) {
            flightCap.setWorldFlightEnabled(isCreativeFlightRuleEnabled);
            flightCap.setShouldCheckFlight(true);
        }

        if (player.tickCount < 40) {
            flightCap.setShouldCheckFlight(true);
        }

        if (!flightCap.isShouldCheckFlight()) {
            return;
        }

        boolean modeProhibitsFlightRemoval = !canRemoveFlightByGamemode(player);

        boolean canfly = isCreativeFlightRuleEnabled || flightCap.isAllowedFlight() || modeProhibitsFlightRemoval;

        player.abilities.mayfly = canfly;
        player.abilities.flying =
                (modeProhibitsFlightRemoval && player.abilities.flying)
                || !(modeProhibitsFlightRemoval || (
                        canfly && !player.isOnGround()
                        ));

        player.onUpdateAbilities();
        flightCap.setShouldCheckFlight(false);
    }

    private static boolean canRemoveFlightByGamemode(PlayerEntity player) {
        return !(player.isSpectator() || player.isCreative());
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        PlayerEntity player = event.getPlayer();
        IFlightCapability flightCap = player.getCapability(FlightCapability.CAPABILITY_FLIGHT).orElse(null);
        if (flightCap == null) {
            LOGGER.error("Missing IFlightCapability on player");
            return;
        }
        IFlightCapability oldFlightCap = event.getOriginal().getCapability(FlightCapability.CAPABILITY_FLIGHT).orElse(null);
        if (oldFlightCap != null) {
            flightCap.copyFrom(oldFlightCap);
        }
    }

}
