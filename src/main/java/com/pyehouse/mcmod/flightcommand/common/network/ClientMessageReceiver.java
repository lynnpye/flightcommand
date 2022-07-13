package com.pyehouse.mcmod.flightcommand.common.network;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class ClientMessageReceiver {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void onMessageReceived(final ClientUpdateMessage message, final Supplier<NetworkEvent.Context> ctxSupplier) {
        ctxSupplier.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientMessageHandler.handleMessage(message, ctxSupplier))
        );
        ctxSupplier.get().setPacketHandled(true);
    }

}
