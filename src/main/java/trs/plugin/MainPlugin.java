package trs.plugin;

import com.hypixel.hytale.assetstore.map.DefaultAssetMap;
import com.hypixel.hytale.component.ComponentRegistryProxy;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.asset.HytaleAssetStore;
import com.hypixel.hytale.server.core.command.system.CommandRegistry;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.plugin.registry.AssetRegistry;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import trs.plugin.assets.MaturityAsset;
import trs.plugin.commands.TpRequestCommands.TpRequestCommand;
import trs.plugin.commands.TpRequestCommands.TpAcceptAllCommand;
import trs.plugin.commands.TrsCollection;
import trs.plugin.components.MaturityComponent;
import trs.plugin.components.TpRequestComponent;
import trs.plugin.systems.MaturitySystems;
import trs.plugin.systems.TpRequestSystems;

import javax.annotation.Nonnull;

public class MainPlugin extends JavaPlugin {
	protected static MainPlugin instance;
	private ComponentType<EntityStore, MaturityComponent> MATURITY_COMPONENT;
	private ComponentType<EntityStore, TpRequestComponent> TP_REQUEST_COMPONENT;
	
	public MainPlugin(@Nonnull JavaPluginInit init) { super(init); }
	
	@Override
	protected void setup() {
		instance = this;
		CommandRegistry commandRegistry = this.getCommandRegistry();
		ComponentRegistryProxy<EntityStore> entityStoreRegistry = this.getEntityStoreRegistry();
		AssetRegistry assetRegistry = this.getAssetRegistry();
		
		commandRegistry.registerCommand(new TrsCollection());
		commandRegistry.registerCommand(new TpRequestCommand("tpa", "Request to teleport to a player", false));
		commandRegistry.registerCommand(new TpAcceptAllCommand(
			"tpaccept", "Accept incoming teleportation request", false,
			"Accept a specific teleportation request"
		));
		
		this.MATURITY_COMPONENT = entityStoreRegistry.registerComponent(MaturityComponent.class, "Maturity", MaturityComponent.CODEC);
		this.TP_REQUEST_COMPONENT = entityStoreRegistry.registerComponent(TpRequestComponent.class, "TpRequest", TpRequestComponent.CODEC);
		
		entityStoreRegistry.registerSystem(new MaturitySystems.MaturityTicking());
		entityStoreRegistry.registerSystem(new MaturitySystems.MaturityRef());
		entityStoreRegistry.registerSystem(new TpRequestSystems.TpRequestTicking());
		
		HytaleAssetStore.Builder<String, MaturityAsset, DefaultAssetMap<String, MaturityAsset>> maturityAssetBuilder =
			HytaleAssetStore.builder(MaturityAsset.class, new DefaultAssetMap<>());
		assetRegistry.register(maturityAssetBuilder
			.setPath("NPC/Maturity")
			.setCodec(MaturityAsset.CODEC)
			.setKeyFunction(MaturityAsset::getId)
			.build()
		);
	}
	
	public static MainPlugin get() { return instance; }
	
	public ComponentType<EntityStore, MaturityComponent> getMaturityComponentType() { return this.MATURITY_COMPONENT; }
	public ComponentType<EntityStore, TpRequestComponent> getTpRequestComponentType() { return this.TP_REQUEST_COMPONENT; }
}
