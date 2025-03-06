package de.devmevtv.projectmodification.client.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import de.devmevtv.projectmodification.client.ProjectModificationClient;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

public class PModCommand {
    public static boolean SettingsRequested = false;

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
                ClientCommandManager.literal("pmod")
                        .requires(fabricClientCommandSource -> ProjectModificationClient.PMOD)
                        .then(
                                ClientCommandManager.literal("settings")
                                        .requires(fabricClientCommandSource -> ProjectModificationClient.permissionLevel >= 1)
                                        .executes(context -> {
                                            ProjectModificationClient.bypassCommand = true;
                                            MinecraftClient.getInstance().player.networkHandler.sendChatCommand("trigger pmod.request");
                                            SettingsRequested = true;
                                            return Command.SINGLE_SUCCESS;
                                        })
                        ).then(ClientCommandManager.literal("testBlock").executes(context -> {

                            MinecraftClient.getInstance().player.getWorld().setBlockState(new BlockPos(0, 0, 0), ProjectModificationClient.testBlock.getDefaultState());
                            return Command.SINGLE_SUCCESS;
                        }))
        );
    }
}
