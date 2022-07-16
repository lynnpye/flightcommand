package com.pyehouse.mcmod.flightcommand.common.network;

import com.pyehouse.mcmod.flightcommand.api.capability.FlightCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class ClientMessageHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void handleMessage(final ClientUpdateMessage message, final Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
        ctx.setPacketHandled(true);

        if (sideReceived != LogicalSide.CLIENT) {
            LOGGER.warn("ClientUpdateMessage received on wrong side: " + ctx.getDirection().getReceptionSide());
            return;
        }

        if (!message.isMessageValid()) {
            LOGGER.warn("ClientUpdateMessage was invalid: " + message.toString());
            return;
        }

        ctx.enqueueWork(() -> processMessage(message));
    }

    private static void processMessage(ClientUpdateMessage message) {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            LOGGER.warn("ClientMessageHandler.processMessage: no player available from Minecraft.getInstance().player");
            return;
        }

        ClientGameruleUpdater.updateGamerules(message.isWorldFlightEnabled());

        player.getCapability(FlightCapability.CAPABILITY_FLIGHT).ifPresent(cap -> {
            cap.copyFrom(message);
        });
    }
}
