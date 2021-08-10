package com.pyehouse.mcmod.flightcommand.common.network;

public class MessageHandlerOnServer {

    public static boolean isProtocolAcceptedByClient(String protocolVersion) {
        return NetworkSetup.MESSAGE_PROTOCOL_VERSION.equals(protocolVersion);
    }
}
