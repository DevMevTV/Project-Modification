package de.devmevtv.projectmodification.client.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class SettingsScreen extends Screen {
    public SettingsScreen(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        ButtonWidget uselessButton = ButtonWidget.builder(Text.of("Useless Button"), (btn) -> {
            // Do stuff when the button is clicked here
        }).dimensions(40, 40, 120, 20).build();

        this.addDrawableChild(uselessButton);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        context.drawText(this.textRenderer, "Settings", 40, 40 - this.textRenderer.fontHeight - 10, 0xFFFFFFFF, true);
    }
}