package net.notcoded.nqt.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.CollapsibleObject;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Tooltip;

@Config(name = "nqt")
public class ClientModConfig implements ConfigData {
    @Tooltip
    public boolean isEnabled = true;

    @Tooltip
    public boolean optionsMenu = true;

    @CollapsibleObject
    public QOL qol = new QOL();

    @CollapsibleObject
    public Performance performance = new Performance();

    @CollapsibleObject
    public Fog fog = new Fog();

    @CollapsibleObject
    public Fixes fixes = new Fixes();

    public static class Fog {
        @Tooltip
        public boolean disableAllFog = false;

        @Tooltip
        public boolean disableSkyFog = false;

        @Tooltip
        public boolean disableTerrainFog = false;
        @Tooltip
        public boolean disableOverworldFog = false;
        @Tooltip
        public boolean disableNetherFog = false;
        @Tooltip
        public boolean disableEndFog = false;
    }

    public static class Performance {
        @Tooltip
        public boolean noClientSideEntityCollisionChecks = false;

    }

    public static class Fixes {
        @Tooltip
        public boolean toolTipFix = false;

        @Tooltip
        public boolean cursorCenteredFix = false;

        @Tooltip
        public boolean chatLagFix = false;

        @Tooltip
        public boolean entityCrashFix = false;

        @Tooltip
        public boolean titleFix = false;
    }

    public static class QOL {
        @Tooltip
        public boolean fullBright = false;

        @Tooltip
        public boolean gamemodeSwitcherBypass = false;

        @Tooltip
        public boolean lowerCaseCommands = false;

        @Tooltip
        public boolean dontClearChatHistory = false;
    }
}
