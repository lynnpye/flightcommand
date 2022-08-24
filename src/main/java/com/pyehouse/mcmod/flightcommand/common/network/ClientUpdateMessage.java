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
    private boolean flying;

    public boolean isMessageValid() { return messageValid; }
    public boolean isFlightAllowed() { return flightAllowed; }
    public boolean isWorldFlightEnabled() { return worldFlightEnabled; }
    public boolean isCheckFlight() { return checkFlight; }
    public boolean isFlying() { return flying; }

    public ClientUpdateMessage() {
        messageValid = false;
    }

    public ClientUpdateMessage(boolean flightAllowed, boolean worldFlightEnabled, boolean checkFlight, boolean flying) {
        this.flightAllowed = flightAllowed;
        this.worldFlightEnabled = worldFlightEnabled;
        this.checkFlight = checkFlight;
        this.flying = flying;
        messageValid = true;
    }

    public static ClientUpdateMessage decode(FriendlyByteBuf buf) {
        ClientUpdateMessage retval = new ClientUpdateMessage();
        try {
            retval.flightAllowed = buf.readBoolean();
            retval.worldFlightEnabled = buf.readBoolean();
            retval.checkFlight = buf.readBoolean();
            retval.flying = buf.readBoolean();
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
        buf.writeBoolean(flying);
    }

    @Override
    public String toString() {
        return String.format("ClientUpdateMessage= flightAllowed=%s worldFlightEnabled=%s flying=%s", flightAllowed, worldFlightEnabled, flying);
    }
}
