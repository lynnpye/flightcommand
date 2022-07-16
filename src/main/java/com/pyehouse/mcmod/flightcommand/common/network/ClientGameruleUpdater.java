package com.pyehouse.mcmod.flightcommand.common.network;

import com.pyehouse.mcmod.flightcommand.common.command.GameruleRegistrar;
import net.minecraft.client.Minecraft;

public class ClientGameruleUpdater {
    public static void updateGamerules(boolean creativeFlightEnabled) {
        Minecraft.getInstance().player.level.getGameRules().getRule(GameruleRegistrar.doCreativeFlight)
                .set(creativeFlightEnabled, Minecraft.getInstance().player.level.getServer());
    }
}
