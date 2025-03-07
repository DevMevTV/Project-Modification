package de.devmevtv.projectmodification.client;

import de.devmevtv.projectmodification.client.command.PModCommand;
import de.devmevtv.projectmodification.client.screen.SettingsScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ProjectModificationClient implements ClientModInitializer {
    public static boolean PMOD;
    public static int onlyPMod;
    public static int permissionLevel;

    public static boolean bypassCommand = false;

    public static Block testBlock;
    public static Item testItem;

    @Override
    public void onInitializeClient() {

        Identifier testBlockId = Identifier.of("pmod", "test_block");

        Block.Settings testBlockSettings = AbstractBlock.Settings.create()
                .registryKey(RegistryKey.of(RegistryKeys.BLOCK, testBlockId));

        testBlock = Registry.register(Registries.BLOCK, RegistryKey.of(RegistryKeys.BLOCK, testBlockId), new Block(testBlockSettings));

        Identifier testItemId = Identifier.of("pmod", "test_item");
        testItem = Registry.register(Registries.ITEM, RegistryKey.of(RegistryKeys.ITEM, testItemId), new Item(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, testItemId))));


        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> {
            entries.add(testItem.getDefaultStack());
        });


        // -----------

        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> {
            PModCommand.register(dispatcher);
            bypassCommand = true;
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
            } else if (message.getString().startsWith("[+]") && message.getString().contains("|")) {
                // this is for later
            }

            return true;
        });

        ClientSendMessageEvents.ALLOW_COMMAND.register((message) -> {
            if (!bypassCommand && message.startsWith("trigger pmod")) {
                MinecraftClient.getInstance().player.sendMessage(Text.of("§cUnknown scoreboard objective '" + message.split(" ")[1] + "'"), false);
                return false;
            }
            bypassCommand = false;
            return true;
        });
    }
}
