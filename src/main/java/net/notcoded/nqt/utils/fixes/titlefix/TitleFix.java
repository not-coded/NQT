package net.notcoded.nqt.utils.fixes.titlefix;

import net.fabricmc.loader.api.FabricLoader;
import net.notcoded.nqt.NQT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TitleFix {

    private static final List<String> incompatibleMods = Arrays.asList(
            "immediatelyfast",
            "uglyscoreboardfix",
            "exordium"
    );

    public static boolean canRun() {
        boolean hasIncompatibleMods = false;

        for(String mod : incompatibleMods) {
            if(FabricLoader.getInstance().isModLoaded(mod)) {
                hasIncompatibleMods = true;
                break;
            }
        }

        return NQT.clientModConfig.isEnabled && NQT.clientModConfig.fixes.titleFix && hasIncompatibleMods;
    }
}
