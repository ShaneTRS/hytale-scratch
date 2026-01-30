package trs.scratch.components;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.NPCPlugin;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import trs.scratch.MainPlugin;
import trs.scratch.assets.MaturityAsset;

public class MaturityComponent implements Component<EntityStore> {
	private Float age = 0f;
	private Float childhood = 0f;
	private int adultRole;
	
	public static final BuilderCodec<MaturityComponent> CODEC = BuilderCodec
		.builder(MaturityComponent.class, MaturityComponent::new)
		.append(
			new KeyedCodec<>("Age", BuilderCodec.FLOAT),
			MaturityComponent::setAge,
			MaturityComponent::getAge
		)
		.add()
		.append(
			new KeyedCodec<>("Childhood", BuilderCodec.FLOAT),
			MaturityComponent::setChildhood,
			MaturityComponent::getChildhood
		)
		.add()
		.append(
			new KeyedCodec<>("AdultRole", BuilderCodec.INTEGER),
			MaturityComponent::setCreatureAdultRoleId,
			MaturityComponent::getAdultRole
		)
		.add()
		.build();
	
	public static ComponentType<EntityStore, MaturityComponent> getComponentType() {
		return MainPlugin.get().getMaturityComponentType();
	}
	
	public Float getAge() { return this.age; }
	public void setAge(Float seconds) { this.age = seconds; }
	
	public Float getChildhood() { return this.childhood; }
	public void setChildhood(Float seconds) { this.childhood = seconds; }
	
	public int getAdultRole() { return this.adultRole; }
	public void setAdultRole(String roleName) {
		this.adultRole = NPCPlugin.get().getIndex(roleName);
	}
	public void setCreatureAdultRoleId(int roleId) { this.adultRole = roleId; }
	
	public void incrementAge(Float seconds) { this.age += seconds; }
	public boolean completedChildhood() { return this.age > this.childhood; }
	
	public static MaturityComponent fromAsset(MaturityAsset maturityAsset) {
		MaturityComponent component = new MaturityComponent();
		component.setAdultRole(maturityAsset.getAdultRole());
		component.setChildhood(maturityAsset.getChildhood());
		return component;
	}
	
	@NullableDecl
	@Override
	public Component<EntityStore> clone() {
		MaturityComponent cloned = new MaturityComponent();
		cloned.age = this.age;
		cloned.childhood = this.childhood;
		cloned.adultRole = this.adultRole;
		return cloned;
	}
}
