package com.pyehouse.mcmod.flightcommand.common;

import com.pyehouse.mcmod.flightcommand.api.capability.FlightCapability;
import com.pyehouse.mcmod.flightcommand.api.util.IDeferredSetupRegistrar;
import com.pyehouse.mcmod.flightcommand.api.util.ModEventRegistrar;
import com.pyehouse.mcmod.flightcommand.common.command.GameruleRegistry;
import com.pyehouse.mcmod.flightcommand.common.handler.CommandEventRegistryHandler;
import com.pyehouse.mcmod.flightcommand.common.handler.CommonCapabilityAttachEventHandler;
import com.pyehouse.mcmod.flightcommand.common.handler.CommonPlayerTickEventHandler;
import com.pyehouse.mcmod.flightcommand.common.handler.SyncEventHandler;
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
        modEventBus.register(NetworkSetup.class);
        modEventBus.register(GameruleRegistry.class);

        // register flight capability
        ((IDeferredSetupRegistrar) () -> FlightCapability.registerFlightCapability()).common();

        // and register for events
        forgeEventBus.register(CommandEventRegistryHandler.class);
        forgeEventBus.register(CommonCapabilityAttachEventHandler.class);
        forgeEventBus.register(CommonPlayerTickEventHandler.class);
        forgeEventBus.register(SyncEventHandler.class);
    }
}
