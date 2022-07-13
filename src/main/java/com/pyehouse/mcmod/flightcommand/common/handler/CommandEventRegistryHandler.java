package com.pyehouse.mcmod.flightcommand.common.handler;

import com.mojang.brigadier.CommandDispatcher;
import com.pyehouse.mcmod.flightcommand.common.command.GiveFlightCommand;
import net.minecraft.command.CommandSource;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommandEventRegistryHandler {
    @SubscribeEvent
    public static void onRegisterCommandEvent(RegisterCommandsEvent event) {
        final CommandDispatcher<CommandSource> commandDispatcher = event.getDispatcher();
        GiveFlightCommand.register(commandDispatcher);
    }
}
