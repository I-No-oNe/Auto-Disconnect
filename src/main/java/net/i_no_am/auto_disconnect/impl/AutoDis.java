package net.i_no_am.auto_disconnect.impl;

import net.i_no_am.auto_disconnect.client.Global;
import net.i_no_am.auto_disconnect.config.Config;
import net.i_no_am.auto_disconnect.utils.NetworkUtils;
import net.i_no_am.auto_disconnect.utils.PlayerUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import java.util.List;

public class AutoDis implements Global {

    public static void impl() {
        if (PlayerUtils.invalid()) return;
        List<Integer> rangeList = PlayerUtils.getRangeList(Config.range.getVal());
            /*A check if the mod is actually enabled*/
        if (Config.enable.getVal()) {
            /*Player Health -> Still in progress*/
            if (Config.checkPlayerHealth.getVal() && PlayerUtils.player().isAlive()) {
                float healthThreshold = (Config.selfPlayerHealth.getVal() / 100.0f) * PlayerUtils.player().getMaxHealth();
                if (PlayerUtils.player().getHealth() < healthThreshold) {
                    NetworkUtils.disconnectPlayer(String.format("Player health is below %d%%", Config.selfPlayerHealth.getVal()));
                    return;
                }
            }
            /*Check Anchors -> Check For Anchors In range*/
            if (Config.checkAnchors.getVal()) {
                for (Integer r : rangeList) {
                    BlockPos anchorPos = PlayerUtils.getNearestBlock(r, state -> {
                        if (state.getBlock() == Blocks.RESPAWN_ANCHOR) {
            /*Check Glowstone -> Check For Anchors with glowstone inside them In range*/
                            if (Config.checkGlowstone.getVal()) {
                                BlockState blockState = state.getBlock().getStateWithProperties(state);
                                int charges = blockState.get(RespawnAnchorBlock.CHARGES);
                                return charges > 0;
                            } else {
                                return true;
                            }
                        }
                        return false;
                    });
                    if (anchorPos != null) {
                        NetworkUtils.disconnectPlayer("Respawn Anchor is in range");
                        return;
                    }
                }
            }
            /*Check Crystals -> Check For End Crystals Entities In range*/
            if (Config.checkCrystals.getVal()) {
                for (Integer r : rangeList) {
                    Entity crystal = PlayerUtils.getNearestEntity(r, entity -> entity instanceof EndCrystalEntity);
                    if (crystal != null) {
                        NetworkUtils.disconnectPlayer("End Crystal is in range");
                        return;
                    }
                }
            }
            /*Check New Players -> Check For New Players In range, Still in progress*/
            if (Config.checkNewPlayers.getVal()) {
                for (PlayerEntity player : mc.world.getPlayers()) {
                    if (player != mc.player && mc.player.squaredDistanceTo(player) <= mc.options.getViewDistance().getValue() * 16 * mc.options.getViewDistance().getValue() * 16) {
                        NetworkUtils.disconnectPlayer("New player detected within render distance");
                        return;
                    }
                }
            }
        }
    }
}
