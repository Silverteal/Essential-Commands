package com.fibermc.essentialcommands.commands.bench;

import com.fibermc.essentialcommands.ECText;
import com.fibermc.essentialcommands.screen.AnvilCommandScreenHandler;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Language;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import static com.fibermc.essentialcommands.EssentialCommands.CONFIG;

public class AnvilCommand implements Command<ServerCommandSource> {

    public AnvilCommand() {}

    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity senderPlayer = source.getPlayer();

        senderPlayer.openHandledScreen(createScreenHandlerFactory(senderPlayer.getEntityWorld(), senderPlayer.getBlockPos()));
        senderPlayer.incrementStat(Stats.INTERACT_WITH_ANVIL);

        source.sendFeedback(
            ECText.getInstance().getText("cmd.workbench.feedback", Language.getInstance().get("block.minecraft.anvil")),
            CONFIG.BROADCAST_TO_OPS.getValue()
        );

        return 0;
    }

    private @NotNull NamedScreenHandlerFactory createScreenHandlerFactory(World world, BlockPos pos) {
        return new SimpleNamedScreenHandlerFactory(
            (syncId, inventory, player) ->
                new AnvilCommandScreenHandler(syncId, inventory, ScreenHandlerContext.create(world, pos)),
            Text.translatable("block.minecraft.anvil")
        );
    }

}