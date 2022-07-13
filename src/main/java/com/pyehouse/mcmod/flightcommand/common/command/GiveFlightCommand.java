package com.pyehouse.mcmod.flightcommand.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pyehouse.mcmod.flightcommand.api.capability.FlightCapability;
import com.pyehouse.mcmod.flightcommand.api.capability.IFlightCapability;
import com.pyehouse.mcmod.flightcommand.common.network.ClientUpdater;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GiveFlightCommand {
    private static final Logger LOGGER = LogManager.getLogger();

    public static final String CMD_command = "flight";
    public static final String CMD_player = "player";
    public static final String CMD_value = "value";

    public static final String I18N_FLIGHTCAP_MISSING = "flightcommand.flightcapabilitymissing";
    public static final String I18N_NON_PLAYER = "flightcommand.query.nonplayer";
    public static final String I18N_QUERY_SUCCESS = "flightcommand.query.success";
    public static final String I18N_APPLY_SUCCESS = "flightcommand.apply.success";

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> giveFlightCommand =
        Commands.literal(CMD_command)
                .requires((commandSource) -> commandSource.hasPermission(1))
                .then(
                    Commands.argument(CMD_player, EntityArgument.player())
                        .executes((command) -> queryFlight(command))
                    .then(Commands.argument(CMD_value, BoolArgumentType.bool())
                        .executes((command) -> applyFlightCommand(command))
                    )
                );
        dispatcher.register(giveFlightCommand);
    }

    static int queryFlight(CommandContext<CommandSource> commandContext) throws CommandSyntaxException {
        Entity entity = EntityArgument.getEntity(commandContext, CMD_player);
        if (!(entity instanceof PlayerEntity)) {
            LOGGER.warn("Cannot query flight capability of non-player");
            commandContext.getSource().sendFailure(makeTC(I18N_NON_PLAYER));
            return 0;
        }
        PlayerEntity player = (PlayerEntity) entity;
        IFlightCapability flightCap = player.getCapability(FlightCapability.CAPABILITY_FLIGHT).orElse(null);
        if (flightCap == null) {
            LOGGER.warn("No flight capability present, true OR false");
            commandContext.getSource().sendFailure(makeTC(I18N_FLIGHTCAP_MISSING));
            return 0;
        }

        commandContext.getSource().sendSuccess(makeTC(I18N_QUERY_SUCCESS, player.getGameProfile().getName(), Boolean.toString(flightCap.isAllowedFlight())), false);

        return 1;
    }

    static ITextComponent makeTC(String id, String... extra) {
        return new TranslationTextComponent(id, (Object[]) extra);
    }

    static int applyFlightCommand(CommandContext<CommandSource> commandContext) throws CommandSyntaxException {
        Entity entity = EntityArgument.getEntity(commandContext, CMD_player);
        if (!(entity instanceof ServerPlayerEntity)) {
            LOGGER.warn("Cannot query flight capability of non-player");
            commandContext.getSource().sendFailure(makeTC(I18N_NON_PLAYER));
            return 0;
        }

        ServerPlayerEntity player = (ServerPlayerEntity) entity;
        IFlightCapability flightCap = player.getCapability(FlightCapability.CAPABILITY_FLIGHT).orElse(null);
        if (flightCap == null) {
            LOGGER.warn("No flight capability present, true OR false");
            commandContext.getSource().sendFailure(makeTC(I18N_FLIGHTCAP_MISSING));
            return 0;
        }

        boolean appliedFlightValue = BoolArgumentType.getBool(commandContext, CMD_value);
        boolean worldFlightEnabled = GameruleRegistrar.isCreativeFlightEnabled(player);
        flightCap.setAllowedFlight(appliedFlightValue);
        flightCap.setWorldFlightEnabled(worldFlightEnabled);
        flightCap.setShouldCheckFlight(true);

        ClientUpdater.sendFlightApplication(appliedFlightValue, worldFlightEnabled, player);

        commandContext.getSource().sendSuccess(makeTC(I18N_APPLY_SUCCESS, player.getGameProfile().getName(), Boolean.toString(appliedFlightValue)), true);

        return 1;
    }
}
