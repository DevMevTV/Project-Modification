package de.devmevtv.projectmodification.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class ProjectModificationClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            ClientCommandRegistrationCallback.EVENT.register((dispatcher, access) -> {
                handler.sendCommand("trigger test");
            });
        });
    }
}
