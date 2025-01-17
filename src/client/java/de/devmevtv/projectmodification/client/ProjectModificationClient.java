package de.devmevtv.projectmodification.client;

import de.devmevtv.projectmodification.client.command.PModCommand;
import de.devmevtv.projectmodification.client.screen.SettingsScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class ProjectModificationClient implements ClientModInitializer {
    public static boolean PMOD;
    public static int onlyPMod;
    public static int permissionLevel;

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> {
            PModCommand.register(dispatcher);
            MinecraftClient.getInstance().player.networkHandler.sendChatCommand("trigger pmod.handshake");
        }));
        ClientPlayConnectionEvents.DISCONNECT.register((clientPlayNetworkHandler, minecraftClient) -> {
            permissionLevel = 0;
            onlyPMod = 0;
        });

        ClientReceiveMessageEvents.ALLOW_GAME.register((message, overlay) -> {
            if (message.getString().equals("Unknown scoreboard objective 'pmod.handshake'")) {
                PMOD = false;
                return false;
            } else if (message.getString().equals("Triggered [pmod.handshake]")) {
                PMOD = true;
                return false;
            } else if (message.getString().equals("Triggered [pmod.request]")) {
                return false;
            }

            if (!PMOD)
                return true;

            if (message.getString().startsWith("ȐУȴфঙ")) {
                String msg = message.getString().substring(5);

                if (msg.startsWith("LVL")) {
                    permissionLevel = Integer.parseInt(msg.substring(3));
                    MinecraftClient.getInstance().player.sendMessage(Text.literal("You have permission level " + permissionLevel), true);
                } else if (msg.startsWith("SettingsResponse")) {
                    if (permissionLevel < 1) return false;
                    if (!PModCommand.SettingsRequested) return false;
                    PModCommand.SettingsRequested = false;
                    onlyPMod = Integer.parseInt(msg.substring(16));
                    MinecraftClient.getInstance().send(() -> MinecraftClient.getInstance().setScreen(new SettingsScreen(Text.empty())));
                }
                return false;
            } else if (message.getString().startsWith("[") && message.getString().contains("[pmod.")) {
                return false;
            } else if (message.getString().startsWith("Set [pmod.")) {
                return false;
            }

            return true;
        });
    }
}
