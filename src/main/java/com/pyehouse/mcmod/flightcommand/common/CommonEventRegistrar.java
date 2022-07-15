package com.pyehouse.mcmod.flightcommand.common;

import com.pyehouse.mcmod.flightcommand.api.util.ModEventRegistrar;
import com.pyehouse.mcmod.flightcommand.common.command.GameruleRegistrar;
import com.pyehouse.mcmod.flightcommand.common.handler.*;
import com.pyehouse.mcmod.flightcommand.common.network.NetworkSetup;
import net.minecraftforge.eventbus.api.IEventBus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommonEventRegistrar extends ModEventRegistrar {
    private static final Logger LOGGER = LogManager.getLogger();

    public CommonEventRegistrar(IEventBus modEventBus, IEventBus forgeEventBus)
    {
         super(modEventBus, forgeEventBus);
    }

    public void registration() {
        modEventBus.register(GameruleRegistrar.class);
        modEventBus.register(NetworkSetup.class);

        forgeEventBus.register(RegisterCapabilitiesEventHandler.class);
        forgeEventBus.register(CommandEventRegistryHandler.class);
        forgeEventBus.register(CommonCapabilityAttachEventHandler.class);
        forgeEventBus.register(CommonPlayerEventHandler.class);
        forgeEventBus.register(SyncEventHandler.class);
        forgeEventBus.register(PlayerEventHandler.class);
    }

}
