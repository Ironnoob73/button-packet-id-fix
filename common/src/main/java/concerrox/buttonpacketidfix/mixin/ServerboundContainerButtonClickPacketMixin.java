package concerrox.buttonpacketidfix.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ServerboundContainerButtonClickPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/*
 * Replaced the byte type in the packet with int type.
 * I don't know why Mojang used a byte to send an integer field :(
 */
@Mixin(ServerboundContainerButtonClickPacket.class)
public abstract class ServerboundContainerButtonClickPacketMixin {

    @Mutable
    @Shadow
    @Final
    private int containerId;

    @Mutable
    @Shadow
    @Final
    private int buttonId;

    @Inject(method = "<init>(Lnet/minecraft/network/FriendlyByteBuf;)V", at = @At(value = "TAIL"))
    private void wrapConstructor(FriendlyByteBuf friendlyByteBuf, CallbackInfo ci) {
        // It's not easy to replace the original readByte call, so just move to the start and read again.
        friendlyByteBuf.readerIndex(1);
        containerId = friendlyByteBuf.readInt();
        buttonId = friendlyByteBuf.readInt();
    }

    @WrapMethod(method = "write")
    private void wrapWrite(FriendlyByteBuf friendlyByteBuf, Operation<Void> original) {
        friendlyByteBuf.writeInt(containerId);
        friendlyByteBuf.writeInt(buttonId);
    }

}
