package com.pyehouse.mcmod.flightcommand.common.network;

import com.pyehouse.mcmod.flightcommand.api.capability.FlightCapability;
import com.pyehouse.mcmod.flightcommand.api.capability.IFlightCapability;
import com.pyehouse.mcmod.flightcommand.common.command.GameruleRegistrar;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientUpdater {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void sendFlightApplication(boolean applyFlight, boolean worldFlightEnabled, Player playerEntity) {
        ClientUpdateMessage msg = new ClientUpdateMessage(applyFlight, worldFlightEnabled, true);
        NetworkSetup.simpleChannel.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) playerEntity), msg);
    }

    public static void sendFlightApplication(Player player) {
        if (player == null) {
            LOGGER.error("PlayerEntity must be provided");
            return;
        }

        player.getCapability(FlightCapability.CAPABILITY_FLIGHT).ifPresent(capFlight -> {
            sendFlightApplication(capFlight.isAllowedFlight(), GameruleRegistrar.isCreativeFlightEnabled(player), player);
        });
    }
}
