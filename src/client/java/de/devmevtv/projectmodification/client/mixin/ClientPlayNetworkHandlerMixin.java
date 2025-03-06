package de.devmevtv.projectmodification.client.mixin;

import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import de.devmevtv.projectmodification.client.ProjectModificationClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.CommandSuggestionsS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Shadow @Final private ClientCommandSource commandSource;

    /**
     * @author DevMevTV
     * @reason Hide PMod triggers
     */
    @Overwrite
    public void onCommandSuggestions(CommandSuggestionsS2CPacket packet) {
        List<Suggestion> suggestions = new ArrayList<>();

        packet.getSuggestions().getList().forEach(suggestion -> {
            if (!suggestion.getText().startsWith("pmod"))
                suggestions.add(suggestion);
        });

        this.commandSource.onCommandSuggestions(packet.id(), new Suggestions(StringRange.between(packet.start(), packet.start() + packet.length()), suggestions));
    }

    @Inject(method="onBlockUpdate", at=@At(value="HEAD"), cancellable = true)
    public void onBlockUpdate(BlockUpdateS2CPacket packet, CallbackInfo ci) {
        if (MinecraftClient.getInstance().world.getBlockState(packet.getPos()).getRegistryEntry().getIdAsString().startsWith("pmod:"))
            ci.cancel();
    }

    @Inject(method="onEntitySpawn", at=@At(value="HEAD"), cancellable = true)
    public void onEntitySpawn(EntitySpawnS2CPacket packet, CallbackInfo ci) {
        if (packet.getEntityType() == EntityType.AREA_EFFECT_CLOUD && packet.getPitch() == -23.90625) {
            System.out.println("Block Found");
            MinecraftClient.getInstance().world.setBlockState(new BlockPos(((int) packet.getX()), ((int) packet.getY()), ((int) packet.getZ())), ProjectModificationClient.testBlock.getDefaultState());
            ci.cancel();
        }
    }

}
