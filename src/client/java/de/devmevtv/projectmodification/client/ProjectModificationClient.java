package de.devmevtv.projectmodification.client;

import com.mojang.brigadier.CommandDispatcher;
import de.devmevtv.projectmodification.client.command.PModCommand;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class ProjectModificationClient implements ClientModInitializer {
    public static boolean PMOD;
    public static int PermissionLevel;

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> {
            PModCommand.register(dispatcher);
            MinecraftClient.getInstance().player.networkHandler.sendChatCommand("trigger pmod.handshake");
        }));
        ClientPlayConnectionEvents.DISCONNECT.register((clientPlayNetworkHandler, minecraftClient) -> {
            PermissionLevel = 0;
        });

        ClientReceiveMessageEvents.ALLOW_GAME.register((message, overlay) -> {
            if (message.getString().equals("Unknown scoreboard objective 'pmod.handshake'")) {
                PMOD = false;
                return false;
            } else if (message.getString().equals("Triggered [pmod.handshake]")) {
                PMOD = true;
                return false;
            }

            if (PMOD == false)
                return true;

            if (message.getString().startsWith("ȐУȴфঙ")) {
                String msg = message.getString().substring(5);

                if (msg.startsWith("LVL_")) {
                    PermissionLevel = Integer.parseInt(msg.substring(4));
                    MinecraftClient.getInstance().player.sendMessage(Text.literal("You have permission level " + PermissionLevel), true);
                }
                return false;
            }

            return true;
        });
    }
}
