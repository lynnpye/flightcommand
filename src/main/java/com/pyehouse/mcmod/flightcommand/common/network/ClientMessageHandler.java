package com.pyehouse.mcmod.flightcommand.common.network;

import com.pyehouse.mcmod.flightcommand.api.capability.FlightCapability;
import com.pyehouse.mcmod.flightcommand.api.capability.IFlightCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.function.Supplier;

public class ClientMessageHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void handleMessage(final ClientUpdateMessage message, final Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
        ctx.setPacketHandled(true);

        if (sideReceived != LogicalSide.CLIENT) {
            LOGGER.warn("FlightCommandMessageToClient received on wrong side: " + ctx.getDirection().getReceptionSide());
            return;
        }

        if (!message.isMessageValid()) {
            LOGGER.warn("FlightCommandMessageToClient was invalid: " + message.toString());
            return;
        }

        try {
            Optional<Level> clientWorld = LogicalSidedProvider.CLIENTWORLD.get(sideReceived);
            if (!clientWorld.isPresent()) {
                LOGGER.warn("FlightCommandMessageToClient context could not provide a ClientWorld");
                return;
            }

            ctx.enqueueWork(() -> processMessage(message));
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
    }

    private static void processMessage(ClientUpdateMessage message) {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            LOGGER.warn("FlightCommandMessageToClient.processMessage: no player available from Minecraft.getInstance().player");
            return;
        }

        IFlightCapability flightCap = player.getCapability(FlightCapability.CAPABILITY_FLIGHT).orElse(null);
        if (flightCap == null) {
            LOGGER.warn("FlightCommandMessageToClient.processMessage: No flight capability present, true OR false");
            return;
        }

        flightCap.copyFrom(message);
    }
}
