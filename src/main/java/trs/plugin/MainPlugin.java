package trs.plugin;

import com.hypixel.hytale.assetstore.map.DefaultAssetMap;
import com.hypixel.hytale.component.ComponentRegistryProxy;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.asset.HytaleAssetStore;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import trs.plugin.assets.MaturityAsset;
import trs.plugin.commands.TrsCollection;
import trs.plugin.components.MaturityComponent;
import trs.plugin.systems.MaturitySystems;

import javax.annotation.Nonnull;

public class MainPlugin extends JavaPlugin {

    protected static MainPlugin instance;
    private ComponentType<EntityStore, MaturityComponent> maturityComponentType;

    public static MainPlugin get() {
        return instance;
    }

    public MainPlugin(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        instance = this;
        ComponentRegistryProxy<EntityStore> storeRegistry = this.getEntityStoreRegistry();

        this.getCommandRegistry().registerCommand(new TrsCollection());
        this.maturityComponentType = storeRegistry.registerComponent(MaturityComponent.class, "Maturity", MaturityComponent.CODEC);
        storeRegistry.registerSystem(new MaturitySystems.MaturityTicking());
        storeRegistry.registerSystem(new MaturitySystems.MaturityRef());

        HytaleAssetStore.Builder<String, MaturityAsset, DefaultAssetMap<String, MaturityAsset>> maturityAssetBuilder = HytaleAssetStore.builder(MaturityAsset.class, new DefaultAssetMap<>());
        this.getAssetRegistry().register(maturityAssetBuilder.setPath("NPC/Maturity").setCodec(MaturityAsset.CODEC).setKeyFunction(MaturityAsset::getId).build());
    }

    public ComponentType<EntityStore, MaturityComponent> getMaturityComponentType() {
        return this.maturityComponentType;
    }
}
