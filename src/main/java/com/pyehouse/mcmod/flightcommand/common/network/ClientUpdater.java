package com.pyehouse.mcmod.flightcommand.common.network;

import com.pyehouse.mcmod.flightcommand.api.capability.FlightCapability;
import com.pyehouse.mcmod.flightcommand.api.capability.IFlightCapability;
import com.pyehouse.mcmod.flightcommand.common.command.GameruleRegistrar;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

public class ClientUpdater {
    private static final Logger LOGGER = LogManager.getLogger();

    private static void sendFlightApplication(boolean applyFlight, boolean flying, @Nonnull Player player) {
        ClientUpdateMessage msg = new ClientUpdateMessage(applyFlight, GameruleRegistrar.isCreativeFlightEnabled(player), true, flying);
        NetworkSetup.simpleChannel.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), msg);
    }

    public static void sendFlightApplication(@Nonnull IFlightCapability flightCapability, @Nonnull Player player) {
        sendFlightApplication(flightCapability.isAllowedFlight(), flightCapability.isFlying(), player);
    }
    public static void sendFlightApplication(@Nonnull ServerPlayer player) {
        player.getCapability(FlightCapability.CAPABILITY_FLIGHT).ifPresent(capFlight -> {
            sendFlightApplication(capFlight.isAllowedFlight(), capFlight.isFlying(), player);
        });
    }

    public static void sendFlightApplication(@Nonnull MinecraftServer minecraftServer) {
        for (ServerPlayer player : minecraftServer.getPlayerList().getPlayers()) {
            sendFlightApplication(player);
        }
    }
}
