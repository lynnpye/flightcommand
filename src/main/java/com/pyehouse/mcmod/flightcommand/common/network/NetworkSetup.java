package com.pyehouse.mcmod.flightcommand.common.network;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.Optional;

import static net.minecraftforge.fml.network.NetworkDirection.PLAY_TO_CLIENT;

public class NetworkSetup {
    public static SimpleChannel simpleChannel;

    public static final String MESSAGE_PROTOCOL_VERSION = "1";

    public static final byte APPLY_FLIGHT_ID = 47;

    public static final ResourceLocation simpleChannelURL = new ResourceLocation("flightcommand", "flightcommandchannel");

    @SubscribeEvent
    public static void onCommonSetupEvent(FMLCommonSetupEvent event) {
        simpleChannel = NetworkRegistry.newSimpleChannel(simpleChannelURL, () -> MESSAGE_PROTOCOL_VERSION,
                MessageHandlerOnClient::isProtocolAcceptedByClient,
                MessageHandlerOnServer::isProtocolAcceptedByServer
                );

        simpleChannel.registerMessage(APPLY_FLIGHT_ID, FlightCommandMessageToClient.class,
                FlightCommandMessageToClient::encode, FlightCommandMessageToClient::decode,
                MessageHandlerOnClient::onMessageReceived,
                Optional.of(PLAY_TO_CLIENT)
                );
    }
}
