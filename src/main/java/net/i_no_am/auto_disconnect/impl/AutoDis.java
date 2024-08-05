package net.i_no_am.auto_disconnect.impl;

import net.i_no_am.auto_disconnect.client.Global;
import net.i_no_am.auto_disconnect.config.Config;
import net.i_no_am.auto_disconnect.utils.ConfigUtils;
import net.i_no_am.auto_disconnect.utils.NetworkUtils;
import net.i_no_am.auto_disconnect.utils.PlayerUtils;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class AutoDis implements Global {

    public static void impl() {
        if (PlayerUtils.invalid()) {
            return;
        }

        List<Integer> rangeList = PlayerUtils.getRangeList(Config.range.getVal());
        if (ConfigUtils.isOn()) {
            if (Config.checkPlayerHealth.getVal()) {
                if (PlayerUtils.player().getHealth() < Config.selfPlayerHealth.getVal()) {
                    NetworkUtils.disconnectPlayer("Player health is below " + Config.selfPlayerHealth.getVal() + "%");
                    return;
                }
            }

            if (Config.checkAnchors.getVal()) {
                for (Integer r : rangeList) {
                    BlockPos anchorPos = PlayerUtils.getNearestBlock(r, state -> state.getBlock() == Blocks.RESPAWN_ANCHOR);
                    if (anchorPos != null) {
                        NetworkUtils.disconnectPlayer("Respawn Anchor is in range");
                        return;
                    }
                }
            }

            if (Config.checkCrystals.getVal()) {
                for (Integer r : rangeList) {
                    Entity crystal = PlayerUtils.getNearestEntity(r, entity -> entity instanceof EndCrystalEntity);
                    if (crystal != null) {
                        NetworkUtils.disconnectPlayer("End Crystal is in range");
                        return;
                    }
                }
            }

            if (Config.checkNewPlayers.getVal()) {
                for (PlayerEntity player : mc.world.getPlayers()) {
                    if (player != mc.player && mc.player.squaredDistanceTo(player) <= mc.options.getViewDistance().getValue() * mc.options.getViewDistance().getValue()) {
                        NetworkUtils.disconnectPlayer("New player detected within render distance");
                        return;
                    }
                }
            }
        }
    }
}