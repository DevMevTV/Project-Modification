package de.devmevtv.projectmodification.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;

public class ProjectModificationClient implements ClientModInitializer {
    public int permissionLevel;

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> {
            MinecraftClient.getInstance().player.networkHandler.sendChatCommand("trigger pmod.handshake");
        }));
        ClientPlayConnectionEvents.DISCONNECT.register((clientPlayNetworkHandler, minecraftClient) -> {
            permissionLevel = 0;
        });

        ClientReceiveMessageEvents.ALLOW_GAME.register((message, overlay) -> {
            if (message.getString().startsWith("ȐУȴфঙ")
                    || message.getString().equals("Unknown scoreboard objective 'pmod.handshake'")
                    || message.getString().equals("Triggered [pmod.handshake]")) {
                return false;
            } else {
                return true;
            }
        });
        ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
            if (message.getString().equals("ȐУȴфঙLVL_1")) {
                permissionLevel = 1;
            } else if (message.getString().equals("ȐУȴфঙLVL_0")) {
                permissionLevel = 0;
            }
        });
    }
}
