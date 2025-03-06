package de.devmevtv.projectmodification.client.screen;

import de.devmevtv.projectmodification.client.ProjectModificationClient;
import de.devmevtv.projectmodification.client.Util;
import de.devmevtv.projectmodification.client.command.PModCommand;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class SettingsScreen extends Screen {
    public SettingsScreen(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        ButtonWidget onlyPMODButton = ButtonWidget.builder(Text.of("Only PMOD: " + Util.asBoolean(ProjectModificationClient.onlyPMod)), (btn) -> {
            ProjectModificationClient.bypassCommand = true;
            MinecraftClient.getInstance().player.networkHandler.sendChatCommand("scoreboard players set onlyPmod pmod.settings " + (1 - ProjectModificationClient.onlyPMod));
            ProjectModificationClient.bypassCommand = true;
            MinecraftClient.getInstance().player.networkHandler.sendChatCommand("trigger pmod.request");
            PModCommand.SettingsRequested = true;
        }).dimensions(40, 40, 120, 20).build();
        onlyPMODButton.setTooltip(Tooltip.of(Text.of("Determines if people that do not have PMOD installed can join")));

        this.addDrawableChild(onlyPMODButton);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        context.drawText(this.textRenderer, "Settings", 40, 40 - this.textRenderer.fontHeight - 10, 0xFFFFFFFF, true);
    }
}
