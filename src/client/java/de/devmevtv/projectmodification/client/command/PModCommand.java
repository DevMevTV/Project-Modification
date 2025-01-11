package de.devmevtv.projectmodification.client.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import de.devmevtv.projectmodification.client.ProjectModificationClient;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

public class PModCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
                ClientCommandManager.literal("pmod")
                        .requires(fabricClientCommandSource -> ProjectModificationClient.PMOD)
                        .then(
                                ClientCommandManager.literal("settings")
                                        .requires(fabricClientCommandSource -> ProjectModificationClient.permissionLevel >= 1)
                                        .executes(context -> {
                                            Screen currentScreen = MinecraftClient.getInstance().currentScreen;
                                            MinecraftClient.getInstance().player.networkHandler.sendChatCommand("trigger pmod.request");
                                            return Command.SINGLE_SUCCESS;
                                        })
                        )
        );
    }
}
