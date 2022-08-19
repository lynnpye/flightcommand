package com.pyehouse.mcmod.flightcommand.common.network;

import com.pyehouse.mcmod.flightcommand.api.capability.FlightCapability;
import com.pyehouse.mcmod.flightcommand.api.capability.IFlightCapability;
import com.pyehouse.mcmod.flightcommand.common.command.GameruleRegistrar;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

public class ClientUpdater {
    private static final Logger LOGGER = LogManager.getLogger();

    private static void sendFlightApplication(boolean applyFlight, boolean flying, @Nonnull PlayerEntity player) {
        ClientUpdateMessage msg = new ClientUpdateMessage(applyFlight, GameruleRegistrar.isCreativeFlightEnabled(player), flying);
        NetworkSetup.simpleChannel.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), msg);
    }

    public static void sendFlightApplication(@Nonnull IFlightCapability flightCapability, @Nonnull PlayerEntity player) {
        sendFlightApplication(flightCapability.isAllowedFlight(), flightCapability.isFlying(), player);
    }

    public static void sendFlightApplication(@Nonnull ServerPlayerEntity player) {
        player.getCapability(FlightCapability.CAPABILITY_FLIGHT).ifPresent(capFlight -> {
            sendFlightApplication(capFlight.isAllowedFlight(), capFlight.isFlying(), player);
        });
    }

    public static void sendFlightApplication(@Nonnull MinecraftServer minecraftServer) {
        for (ServerPlayerEntity player : minecraftServer.getPlayerList().getPlayers()) {
            sendFlightApplication(player);
        }
    }
}
