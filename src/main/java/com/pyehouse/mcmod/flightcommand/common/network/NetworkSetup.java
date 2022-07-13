package com.pyehouse.mcmod.flightcommand.common.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

import static net.minecraftforge.network.NetworkDirection.PLAY_TO_CLIENT;

public class NetworkSetup {
    private static final Logger LOGGER = LogManager.getLogger();

    public static SimpleChannel simpleChannel;

    public static final String MESSAGE_PROTOCOL_VERSION = "1";

    public static final byte APPLY_FLIGHT_ID = 47; // random numbers, let's goooo!!!

    public static final ResourceLocation simpleChannelURL = new ResourceLocation("flightcommand", "flightcommandchannel");

    @SubscribeEvent
    public static void onCommonSetupEvent(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> registration());
    }

    public static void registration() {
        simpleChannel = NetworkRegistry.newSimpleChannel(simpleChannelURL, () -> MESSAGE_PROTOCOL_VERSION,
                MESSAGE_PROTOCOL_VERSION::equals,
                MESSAGE_PROTOCOL_VERSION::equals
        );

        simpleChannel.registerMessage(APPLY_FLIGHT_ID, ClientUpdateMessage.class,
                ClientUpdateMessage::encode, ClientUpdateMessage::decode,
                ClientMessageReceiver::onMessageReceived,
                Optional.of(PLAY_TO_CLIENT)
        );
    }
}
