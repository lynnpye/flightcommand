package com.pyehouse.mcmod.flightcommand.server;

import com.pyehouse.mcmod.flightcommand.api.util.ModEventRegistrar;
import com.pyehouse.mcmod.flightcommand.server.handler.SyncEventHandler;
import net.minecraftforge.eventbus.api.IEventBus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerEventRegistrar extends ModEventRegistrar {
    private static final Logger LOGGER = LogManager.getLogger();

    public ServerEventRegistrar(IEventBus modEventBus, IEventBus forgeEventBus) {
        super(modEventBus, forgeEventBus);
    }

    public void registration() {
        forgeEventBus.register(SyncEventHandler.class);
    }
}
