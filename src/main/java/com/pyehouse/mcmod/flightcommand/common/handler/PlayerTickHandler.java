package com.pyehouse.mcmod.flightcommand.common.handler;

import com.pyehouse.mcmod.flightcommand.api.capability.FlightCapability;
import com.pyehouse.mcmod.flightcommand.api.capability.IFlightCapability;
import com.pyehouse.mcmod.flightcommand.common.command.GameruleRegistrar;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlayerTickHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START) {
            return; // move on if we aren't firing early
        }

        PlayerEntity player = event.player;

        IFlightCapability flightCap = player.getCapability(FlightCapability.CAPABILITY_FLIGHT).orElse(null);

        if (flightCap == null) {
            return; // no capability present for some reason
        }

        boolean checkFlight = flightCap.isShouldCheckFlight();
        boolean triggerAbilitiesUpdate = false;
        boolean isFlying = player.abilities.flying;
        boolean isGrounded = player.isOnGround();
        boolean modeAllowsFlight = player.isSpectator() || player.isCreative();
        boolean isCreativeFlightRuleEnabled = GameruleRegistrar.isCreativeFlightEnabled(player);
        boolean weAllowFlight = flightCap.isAllowedFlight() || isCreativeFlightRuleEnabled;
        boolean ignoreFallDamage = weAllowFlight;
        boolean canFly = modeAllowsFlight || weAllowFlight;
        boolean shouldFly = isFlying && canFly;

        if (ignoreFallDamage) {
            player.fallDistance = 0f;
        }

        if (!player.abilities.mayfly && canFly) {
            checkFlight = true;
        }

        //
        // point of no return, start doing checks now
        //
        if (!checkFlight) {
            return;
        }

        if (player.abilities.mayfly != canFly) {
            player.abilities.mayfly = canFly;
            triggerAbilitiesUpdate = true;
        }

        if (isGrounded) {
            shouldFly = false;
        //not grounded so now either flying or falling
        } else if (isFlying != canFly) {
            if (!isFlying) {
                shouldFly = canFly;
            }
        }

        if (shouldFly != isFlying) {
            player.abilities.flying = shouldFly;
            triggerAbilitiesUpdate = true;
        }

        if (triggerAbilitiesUpdate) {
            player.onUpdateAbilities();
        }
        flightCap.setShouldCheckFlight(false);
    }

}
