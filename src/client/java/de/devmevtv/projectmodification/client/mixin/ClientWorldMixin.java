package de.devmevtv.projectmodification.client.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {
    @Inject(method="handleBlockUpdate", at=@At(value="HEAD"), cancellable = true)
    public void handleBlockUpdate(BlockPos pos, BlockState state, int flags, CallbackInfo ci) {
        if (state == Blocks.TNT.getDefaultState() && MinecraftClient.getInstance().world.getBlockState(pos).getRegistryEntry().getIdAsString().startsWith("pmod:"))
            ci.cancel();
    }
}
