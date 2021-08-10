package com.pyehouse.mcmod.flightcommand.common.network;

public class MessageHandlerOnServer {

    public static boolean isProtocolAcceptedByServer(String protocolVersion) {
        return NetworkSetup.MESSAGE_PROTOCOL_VERSION.equals(protocolVersion);
    }
}
