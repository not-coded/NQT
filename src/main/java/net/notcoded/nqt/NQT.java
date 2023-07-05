package net.notcoded.nqt;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.server.MinecraftServer;
import net.notcoded.nqt.config.ClientModConfig;
import net.notcoded.nqt.config.ServerModConfig;
import org.lwjgl.glfw.GLFW;

public class NQT implements ModInitializer {
	public static MinecraftServer server;

	public static MinecraftClient client;
	public static EnvType type = EnvType.CLIENT;
	public static KeyBinding keyBinding;

	public static ClientModConfig clientModConfig;
	public static ServerModConfig serverModConfig;
	@Override
	public void onInitialize() {
		try {
			loadClient();
		} catch(NoClassDefFoundError | Exception ignored){
			loadServer();
		}
	}

	private void loadClient(){
		NQT.client = MinecraftClient.getInstance();

		AutoConfig.register(ClientModConfig.class, GsonConfigSerializer::new);
		clientModConfig = AutoConfig.getConfigHolder(ClientModConfig.class).getConfig();

		keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"nqt.keybinds.menu",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_B,
				"nqt.title"
		));
	}

	private void loadServer() {
		AutoConfig.register(ServerModConfig.class, GsonConfigSerializer::new);
		serverModConfig = AutoConfig.getConfigHolder(ServerModConfig.class).getConfig();

		type = EnvType.SERVER;
	}
}
