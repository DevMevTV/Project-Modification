package de.devmevtv.projectmodification.client.mixin;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.client.network.ClientCommonNetworkHandler.class)
public abstract class ClientCommonNetworkHandlerMixin {
    @Shadow public abstract void sendPacket(Packet<?> packet);

    @Inject(method="sendPacket", at=@At(value= "HEAD"), cancellable = true)
    private void sendPacket(Packet<?> packet, CallbackInfo ci) {
        if (packet instanceof CreativeInventoryActionC2SPacket(short slot, net.minecraft.item.ItemStack stack) && stack.getRegistryEntry().getIdAsString().startsWith("pmod:")) {
            ci.cancel();

            ItemStack batEgg = Items.BAT_SPAWN_EGG.getDefaultStack();

            NbtComponent.set(DataComponentTypes.ENTITY_DATA, batEgg, compound -> {
                compound.putString("id", "minecraft:marker");
                NbtList tags = new NbtList();
                tags.add(NbtString.of(stack.getRegistryEntry().getIdAsString().replace(":", ".")));
                tags.add(NbtString.of("pmod.entities.new_block"));
                compound.put("Tags", tags);
            });

            sendPacket(new CreativeInventoryActionC2SPacket(slot, batEgg));
        }
    }
}
