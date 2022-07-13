package com.pyehouse.mcmod.flightcommand.common.handler;

import com.mojang.brigadier.CommandDispatcher;
import com.pyehouse.mcmod.flightcommand.common.command.GiveFlightCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandEventRegistryHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void onRegisterCommandEvent(RegisterCommandsEvent event) {
        final CommandDispatcher<CommandSourceStack> commandDispatcher = event.getDispatcher();
        GiveFlightCommand.register(commandDispatcher);
    }
}
