package com.pyehouse.mcmod.flightcommand.common.network;

import com.pyehouse.mcmod.flightcommand.api.capability.FlightCapability;
import com.pyehouse.mcmod.flightcommand.api.capability.IFlightCapability;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FlightApplicatorToClient {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void sendFlightApplication(boolean applyFlight, boolean worldFlightEnabled, ServerPlayerEntity playerEntity) {
        FlightCommandMessageToClient msg = new FlightCommandMessageToClient(applyFlight, worldFlightEnabled);
        NetworkSetup.simpleChannel.send(PacketDistributor.PLAYER.with(() -> playerEntity), msg);
    }

    public static void sendFlightApplication(ServerPlayerEntity player) {
        if (player == null) {
            LOGGER.error("PlayerEntity must be provided");
            return;
        }

        IFlightCapability flightCap = player.getCapability(FlightCapability.CAPABILITY_FLIGHT).orElse(null);
        if (flightCap == null) {
            LOGGER.error("Missing IFlightCapability");
            return;
        }

        FlightCommandMessageToClient msg = new FlightCommandMessageToClient(flightCap.isAllowedFlight(), flightCap.isWorldFlightEnabled());
        NetworkSetup.simpleChannel.send(PacketDistributor.PLAYER.with(() -> player), msg);
    }
}
