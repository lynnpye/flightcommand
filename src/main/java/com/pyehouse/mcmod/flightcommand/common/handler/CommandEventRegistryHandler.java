package com.pyehouse.mcmod.flightcommand.common.handler;

import com.mojang.brigadier.CommandDispatcher;
import com.pyehouse.mcmod.flightcommand.common.command.GiveFlightCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommandEventRegistryHandler {
    @SubscribeEvent
    public static void onRegisterCommandEvent(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> commandDispatcher = event.getDispatcher();
        GiveFlightCommand.register(commandDispatcher);
    }
}
