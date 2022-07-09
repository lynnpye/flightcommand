package com.pyehouse.mcmod.flightcommand.common.network;

import com.pyehouse.mcmod.flightcommand.api.capability.FlightCapability;
import com.pyehouse.mcmod.flightcommand.api.capability.IFlightCapability;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fmllegacy.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FlightApplicatorToClient {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void sendFlightApplication(boolean applyFlight, boolean worldFlightEnabled, boolean checkFlight, Player playerEntity) {
        FlightCommandMessageToClient msg = new FlightCommandMessageToClient(applyFlight, worldFlightEnabled, checkFlight);
        NetworkSetup.simpleChannel.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) playerEntity), msg);
    }

    public static void sendFlightApplication(Player player) {
        if (player == null) {
            LOGGER.error("PlayerEntity must be provided");
            return;
        }

        IFlightCapability flightCap = player.getCapability(FlightCapability.CAPABILITY_FLIGHT).orElse(null);
        if (flightCap == null) {
            LOGGER.error("Missing IFlightCapability");
            return;
        }

        FlightCommandMessageToClient msg = new FlightCommandMessageToClient(flightCap.isAllowedFlight(), flightCap.isWorldFlightEnabled(), true);
        NetworkSetup.simpleChannel.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), msg);
    }
}
