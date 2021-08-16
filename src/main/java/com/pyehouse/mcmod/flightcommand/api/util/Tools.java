package com.pyehouse.mcmod.flightcommand.api.util;

import com.pyehouse.mcmod.flightcommand.common.command.GameruleRegistry;
import net.minecraft.entity.player.PlayerEntity;

public class Tools {

    public static boolean isWorldFlightOn(PlayerEntity player) {
        try {
            return player.getCommandSenderWorld().getGameRules().getBoolean(GameruleRegistry.doCreativeFlight);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean canRemoveFlightByGamemode(PlayerEntity player) {
        return !(player.isSpectator() || player.isCreative());
    }

    public static boolean isServer(PlayerEntity entity) {
        return !entity.getCommandSenderWorld().isClientSide;
    }
}
