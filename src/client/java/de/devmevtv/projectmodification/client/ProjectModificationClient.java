package de.devmevtv.projectmodification.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;

public class ProjectModificationClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> {
            MinecraftClient.getInstance().player.networkHandler.sendChatCommand("trigger pmodhandshake");
        }));

        ClientReceiveMessageEvents.ALLOW_GAME.register((message, overlay) -> !message.getString().startsWith("ȐУȴфঙ"));
    }
}
