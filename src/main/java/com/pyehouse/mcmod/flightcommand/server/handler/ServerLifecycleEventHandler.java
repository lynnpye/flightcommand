package com.pyehouse.mcmod.flightcommand.server.handler;

import com.pyehouse.mcmod.flightcommand.common.handler.CommonConfigHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ServerLifecycleEventHandler {

    @SubscribeEvent
    public static void onServerStartedEvent(ServerStartedEvent event) {
        MinecraftServer server = event.getServer();
        if (CommonConfigHandler.COMMON.isEnableGameruleOnWorldStart()) {
            server.getCommands().performCommand(server.createCommandSourceStack(), "gamerule doCreativeFlight true");
        }
    }
}
