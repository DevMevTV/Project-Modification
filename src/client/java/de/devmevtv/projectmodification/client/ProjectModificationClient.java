package de.devmevtv.projectmodification.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class ProjectModificationClient implements ClientModInitializer {
    public int permissionLevel = 0;

    public static boolean PMOD;
    public static int PermissionLevel;

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> {
            MinecraftClient.getInstance().player.networkHandler.sendChatCommand("trigger pmod.handshake");
        }));
        ClientPlayConnectionEvents.DISCONNECT.register((clientPlayNetworkHandler, minecraftClient) -> {
            permissionLevel = 0;
        });

        ClientReceiveMessageEvents.ALLOW_GAME.register((message, overlay) -> {
            if (message.getString().equals("Unknown scoreboard objective 'pmod.handshake'")) {
                PMOD = false;
                return false;
            } else if (message.getString().equals("Triggered [pmod.handshake]")) {
                PMOD = true;
                return false;
            }

            if (message.getString().startsWith("ȐУȴфঙ")) {
                String msg = message.getString().substring(5);

                if (msg.startsWith("LVL_")) {
                    this.PermissionLevel = Integer.parseInt(msg.substring(4));
                    MinecraftClient.getInstance().player.sendMessage(Text.literal("You have permission level " + this.PermissionLevel), true);
                }
                return false;
            }

            return true;
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
