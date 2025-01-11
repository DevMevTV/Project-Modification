package de.devmevtv.projectmodification.client.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import de.devmevtv.projectmodification.client.ProjectModificationClient;
import de.devmevtv.projectmodification.client.screen.SettingsScreen;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class PModCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
                ClientCommandManager.literal("pmod")
                        .requires(fabricClientCommandSource -> ProjectModificationClient.PMOD = true)
                        .then(
                                ClientCommandManager.literal("settings")
                                        .requires(fabricClientCommandSource -> ProjectModificationClient.PermissionLevel >= 1)
                                        .executes(context -> {
                                            Screen currentScreen = MinecraftClient.getInstance().currentScreen;
                                            MinecraftClient.getInstance().send(() -> MinecraftClient.getInstance().setScreen(new SettingsScreen(Text.empty())));
                                            return Command.SINGLE_SUCCESS;
                                        })
                        )
        );
    }
}
