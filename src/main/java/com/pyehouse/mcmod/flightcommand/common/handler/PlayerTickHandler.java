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

        boolean modeAllowsFlight = player.isSpectator() || player.isCreative();
        boolean weAllowFlight = flightCap.isAllowedFlight() || GameruleRegistrar.isCreativeFlightEnabled(player);
        boolean canFly = modeAllowsFlight || weAllowFlight;
        boolean checkFlight = flightCap.isShouldCheckFlight();
        boolean ignoreFallDamage = weAllowFlight;

        // when we allow flight, we ignore fall damage
        // do this if nothing else
        if (ignoreFallDamage) {
            player.fallDistance = 0f;
        }

        // the universe wants this player to be allowed to fly but they are not currently allowed to do so
        // fix this and make note that we also need to check actual flight status further down
        if (!player.abilities.mayfly && canFly) {
            checkFlight = true;
        }

        // determine whether we are supposed to check for actual flying now, whether because of our own explicit state transition
        // or because we want to fix mayfly?
        // point of no return, start doing checks now
        if (!checkFlight) {
            flightCap.setShouldCheckFlight(false);
            flightCap.setFlying(player.abilities.flying);
            return;
        }

        boolean triggerAbilitiesUpdate = false;

        // either suppress mayfly because both we and the mode do not try to support it
        // or enable mayfly because one or both of us support it
        // if it changes, make note we need to update abilities
        if (player.abilities.mayfly != canFly) {
            player.abilities.mayfly = canFly;
            triggerAbilitiesUpdate = true;
        }

        /*
         * Now we are trying to determine the correct value of 'player.abilities.flying'
         */
        if (player.isOnGround()) {
            player.abilities.flying = false;
            triggerAbilitiesUpdate = true;
        } else if (player.abilities.flying && !canFly) {
            // we really and truly should not be able to fly
            // but the ability is still enabled
            // fix that
            player.abilities.flying = false;
            triggerAbilitiesUpdate = true;
        } else {
            // we are in the air
            // we are not flying but falling
            // the player is supposed to be able to fly (by us or by mode)

            // if by us
            if (weAllowFlight && player.abilities.flying != flightCap.isFlying()) {
                player.abilities.flying = flightCap.isFlying();
                triggerAbilitiesUpdate = true;
            }
            // if by mode, we don't care
        }

        // Finis
        if (triggerAbilitiesUpdate) {
            player.onUpdateAbilities();
        }

        flightCap.setFlying(player.abilities.flying);
        flightCap.setShouldCheckFlight(false);
    }

    @SubscribeEvent
    public static void playerClone(PlayerEvent.Clone event) {
        FlightCapability.cloneForPlayer(event.getOriginal(), event.getPlayer());
    }

}
