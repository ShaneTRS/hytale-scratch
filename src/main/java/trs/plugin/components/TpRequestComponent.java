package trs.plugin.components;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import trs.plugin.MainPlugin;

import java.util.UUID;

public class TpRequestComponent implements Component<EntityStore> {
	private Float tpLifetime = 60f;
	private Float tpDelay = 10f;
	private UUID tpTarget;
	private boolean tpAccepted = false;
	
	public static final BuilderCodec<TpRequestComponent> CODEC = BuilderCodec
		.builder(TpRequestComponent.class, TpRequestComponent::new)
		.append(
			new KeyedCodec<>("Lifetime", BuilderCodec.FLOAT),
			TpRequestComponent::setTpLifetime,
			TpRequestComponent::getTpLifetime
		)
		.add()
		.append(
			new KeyedCodec<>("Delay", BuilderCodec.FLOAT),
			TpRequestComponent::setTpDelay,
			TpRequestComponent::getTpDelay
		)
		.add()
		.append(
			new KeyedCodec<>("Target", BuilderCodec.UUID_STRING),
			TpRequestComponent::setTpTarget,
			TpRequestComponent::getTpTarget
		)
		.add()
		.append(
			new KeyedCodec<>("Accepted", BuilderCodec.BOOLEAN),
			TpRequestComponent::setTpAccepted,
			TpRequestComponent::getTpAccepted
		)
		.add()
		.build();
	
	public static ComponentType<EntityStore, TpRequestComponent> getComponentType() {
		return MainPlugin.get().getTpRequestComponentType();
	}
	
	public void setTpLifetime(float duration) { this.tpLifetime = duration; }
	public Float getTpLifetime() { return this.tpLifetime; }
	
	public void setTpDelay(float duration) { this.tpDelay = duration; }
	public Float getTpDelay() { return this.tpDelay; }
	
	public void setTpTarget(UUID target) { this.tpTarget = target; }
	public UUID getTpTarget() { return this.tpTarget; }
	
	public void setTpAccepted(boolean state) { this.tpAccepted = state; }
	public boolean getTpAccepted() { return this.tpAccepted; }
	
	public void decrementLifetime(float seconds) { this.tpLifetime -= seconds; }
	public void decrementTpDelay(float seconds) { this.tpDelay -= seconds; }
	
	@NullableDecl
	@Override
	public Component<EntityStore> clone() {
		TpRequestComponent cloned = new TpRequestComponent();
		cloned.tpLifetime = this.tpLifetime;
		cloned.tpDelay = this.tpDelay;
		cloned.tpTarget = this.tpTarget;
		cloned.tpAccepted = this.tpAccepted;
		return cloned;
	}
}