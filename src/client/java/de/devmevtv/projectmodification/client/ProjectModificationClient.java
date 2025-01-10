package de.devmevtv.projectmodification.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;

public class ProjectModificationClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> {
            MinecraftClient.getInstance().player.networkHandler.sendChatCommand("trigger pmod.handshake");
        }));

        ClientReceiveMessageEvents.ALLOW_GAME.register((message, overlay) -> {
            if (message.getString().startsWith("ȐУȴфঙ")
                    || message.getString().equals("Unknown scoreboard objective 'pmod.handshake'")
                    || message.getString().equals("Triggered [pmod.handshake]")) {
                return false;
            } else {
                return true;
            }
        });
    }
}
