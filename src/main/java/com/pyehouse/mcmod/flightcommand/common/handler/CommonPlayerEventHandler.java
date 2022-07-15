package com.pyehouse.mcmod.flightcommand.common.handler;

import com.pyehouse.mcmod.flightcommand.api.capability.FlightCapability;
import com.pyehouse.mcmod.flightcommand.api.capability.IFlightCapability;
import com.pyehouse.mcmod.flightcommand.common.command.GameruleRegistrar;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
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

        Player player = event.player;
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

        if (!flightCap.isShouldCheckFlight()) {
            return;
        }

        boolean triggerAbilitiesUpdate = false;
        boolean isFlying = player.getAbilities().flying;
        boolean isGrounded = player.isOnGround();
        boolean modeAllowsFlight = player.isSpectator() || player.isCreative();
        boolean weAllowFlight = flightCap.isWorldFlightEnabled() || flightCap.isAllowedFlight();
        boolean canFly = modeAllowsFlight || weAllowFlight;

        if (player.getAbilities().mayfly != canFly) {
            player.getAbilities().mayfly = canFly;
            triggerAbilitiesUpdate = true;
        }

        boolean shouldFly = isFlying;

        if (isGrounded) {
            shouldFly = false;
        } else if (isFlying != canFly) {
            if (!isFlying) {
                shouldFly = canFly;
            } else {
                shouldFly = weAllowFlight;
            }
        }

        if (shouldFly != isFlying) {
            player.getAbilities().flying = shouldFly;
            triggerAbilitiesUpdate = true;
        }

        if (triggerAbilitiesUpdate) {
            player.onUpdateAbilities();
        }
        flightCap.setShouldCheckFlight(false);
    }

}
