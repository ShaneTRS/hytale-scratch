package trs.scratch.assets;

import com.hypixel.hytale.assetstore.AssetExtraInfo.Data;
import com.hypixel.hytale.assetstore.AssetRegistry;
import com.hypixel.hytale.assetstore.AssetStore;
import com.hypixel.hytale.assetstore.codec.AssetBuilderCodec;
import com.hypixel.hytale.assetstore.map.DefaultAssetMap;
import com.hypixel.hytale.assetstore.map.JsonAssetWithMap;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;

public class MaturityAsset implements JsonAssetWithMap<String, DefaultAssetMap<String, MaturityAsset>> {
	private static AssetStore<String, MaturityAsset, DefaultAssetMap<String, MaturityAsset>> ASSET_STORE;

	public static final AssetBuilderCodec<String, MaturityAsset> CODEC = AssetBuilderCodec
		.builder(MaturityAsset.class, MaturityAsset::new,
			Codec.STRING,
			(asset, id) -> asset.id = id,
			asset -> asset.id,
			(asset, data) -> asset.data = data,
			asset -> asset.data
		)
		.append(
			new KeyedCodec<>("AdultRole", AssetBuilderCodec.STRING),
			MaturityAsset::setAdultRole,
			MaturityAsset::getAdultRole
		)
		.add()
		.append(
			new KeyedCodec<>("Childhood", AssetBuilderCodec.FLOAT),
			MaturityAsset::setChildhood,
			MaturityAsset::getChildhood
		)
		.add()
		.build();
	
	protected String id;
	protected Data data;
	protected String adultRole;
	protected Float childhood = 900f;
	
	public String getAdultRole() { return this.adultRole; }
	public void setAdultRole(String role) { this.adultRole = role; }
	
	public Float getChildhood() { return this.childhood; }
	public void setChildhood(Float seconds) { this.childhood = seconds; }
	
	@Override
	public String getId() { return this.id; }
	
	public static AssetStore<String, MaturityAsset, DefaultAssetMap<String, MaturityAsset>> getAssetStore() {
		if (ASSET_STORE == null) {
			ASSET_STORE = AssetRegistry.getAssetStore(MaturityAsset.class);
		}
		return ASSET_STORE;
	}
	
	public static DefaultAssetMap<String, MaturityAsset> getAssetMap() {
		return getAssetStore().getAssetMap();
	}
}
