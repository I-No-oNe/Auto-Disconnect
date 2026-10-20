package net.i_no_am.auto_disconnect.mixin;

import com.mojang.authlib.GameProfile;
import net.i_no_am.auto_disconnect.Global;
import net.i_no_am.auto_disconnect.config.Config;
import net.i_no_am.auto_disconnect.utils.Utils;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Mixin(ClientPacketListener.class)
public abstract class MixinClientPlayNetworkHandler implements Global {

    @Unique private final Set<UUID> knownPlayerIds = new HashSet<>();

    @Inject(method = "handlePlayerInfoUpdate", at = @At("TAIL"))
    private void onListUpdate(ClientboundPlayerInfoUpdatePacket packet, CallbackInfo ci) {
        if (!Config.enable) return;

        for (ClientboundPlayerInfoUpdatePacket.Entry entry : packet.entries()) {
            GameProfile profile = entry.profile();
            if (profile == null) continue;
            UUID uuid = profile.id();

            // Skip self
            if (mc.player != null && uuid.equals(mc.player.getUUID())) continue;

            String playerName = profile.name();

            if (Config.checkNewPlayers && !knownPlayerIds.contains(uuid)) {
                knownPlayerIds.add(uuid);
                Utils.disconnect("New player joined: " + playerName);
                return;
            }

            if (Utils.isPlayerInList(playerName)) {
                Utils.disconnect("Blocked player joined: " + playerName);
                return;
            }

            knownPlayerIds.add(uuid);
        }
    }

    @Inject(method = "handleLogin", at = @At("TAIL"))
    private void onGameJoin(ClientboundLoginPacket packet, CallbackInfo ci) {
        knownPlayerIds.clear();
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void onTick(CallbackInfo ci) {

        if (Utils.isInvalid() || !Config.enable || !Config.checkPlayersInRenderDistance) return;

        Vec3 selfPos = mc.player.position();
        double rangeSquared = Config.range * Config.range;

        var player = Utils.findEntity(EntityTypes.PLAYER, (int) Config.range);
        double distSq = player != mc.player ? player.position().distanceToSqr(selfPos) : Double.MAX_VALUE;

        if (distSq <= rangeSquared) {
            Utils.disconnect("Player in render range");
        }
    }
}
