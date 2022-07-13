package com.pyehouse.mcmod.flightcommand.common.network;

import net.minecraft.network.FriendlyByteBuf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientUpdateMessage {
    private static final Logger LOGGER = LogManager.getLogger();

    private boolean messageValid;
    private boolean flightAllowed;
    private boolean worldFlightEnabled;
    private boolean checkFlight;

    public boolean isMessageValid() { return messageValid; }
    public boolean isFlightAllowed() { return flightAllowed; }
    public boolean isWorldFlightEnabled() { return worldFlightEnabled; }
    public boolean isCheckFlight() { return checkFlight; }

    public ClientUpdateMessage() {
        messageValid = false;
    }

    public ClientUpdateMessage(boolean flightAllowed, boolean worldFlightEnabled, boolean checkFlight) {
        this.flightAllowed = flightAllowed;
        this.worldFlightEnabled = worldFlightEnabled;
        this.checkFlight = checkFlight;
        messageValid = true;
    }

    public static ClientUpdateMessage decode(FriendlyByteBuf buf) {
        ClientUpdateMessage retval = new ClientUpdateMessage();
        try {
            retval.flightAllowed = buf.readBoolean();
            retval.worldFlightEnabled = buf.readBoolean();
            retval.checkFlight = buf.readBoolean();
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            LOGGER.warn("Exception while reading FlightCommandMessageToClient: " + e);
            return retval;
        }
        retval.messageValid = true;
        return retval;
    }

    public void encode(FriendlyByteBuf buf) {
        if (!messageValid) return;

        buf.writeBoolean(flightAllowed);
        buf.writeBoolean(worldFlightEnabled);
        buf.writeBoolean(checkFlight);
    }

    @Override
    public String toString() {
        return "FlightCommandMessageToClient[flightAllowed=" + Boolean.toString(flightAllowed) + ",worldFlightEnabled=" + Boolean.toString(worldFlightEnabled) + "]";
    }
}
