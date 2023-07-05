package net.notcoded.nqt.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Tooltip;

@Config(name = "nqt")
public class ServerModConfig implements ConfigData {
    @Tooltip
    public boolean isEnabled = true;

    public Fixes fixes = new Fixes();

    public static class Fixes {
        @Tooltip
        public boolean entityCrashFix = false;
    }
}
