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
	private Float lifetime = 60f;
	private Float delay = 10f;
	private UUID target;
	private boolean accepted = false;
	private boolean notified = false;
	
	public static final BuilderCodec<TpRequestComponent> CODEC = BuilderCodec
		.builder(TpRequestComponent.class, TpRequestComponent::new)
		.append(
			new KeyedCodec<>("Lifetime", BuilderCodec.FLOAT),
			TpRequestComponent::setLifetime,
			TpRequestComponent::getLifetime
		)
		.add()
		.append(
			new KeyedCodec<>("Delay", BuilderCodec.FLOAT),
			TpRequestComponent::setDelay,
			TpRequestComponent::getDelay
		)
		.add()
		.append(
			new KeyedCodec<>("Target", BuilderCodec.UUID_STRING),
			TpRequestComponent::setTarget,
			TpRequestComponent::getTarget
		)
		.add()
		.append(
			new KeyedCodec<>("Accepted", BuilderCodec.BOOLEAN),
			TpRequestComponent::setAccepted,
			TpRequestComponent::getAccepted
		)
		.add()
		.append(
			new KeyedCodec<>("Notified", BuilderCodec.BOOLEAN),
			TpRequestComponent::setNotified,
			TpRequestComponent::getNotified
		)
		.add()
		.build();
	
	public static ComponentType<EntityStore, TpRequestComponent> getComponentType() {
		return MainPlugin.get().getTpRequestComponentType();
	}
	
	public void setLifetime(float duration) { this.lifetime = duration; }
	public Float getLifetime() { return this.lifetime; }
	public void setDelay(float duration) { this.delay = duration; }
	public Float getDelay() { return this.delay; }
	
	public void setTarget(UUID target) { this.target = target; }
	public UUID getTarget() { return this.target; }
	
	public void setAccepted(boolean state) { this.accepted = state; }
	public boolean getAccepted() { return this.accepted; }
	public void setNotified(boolean state) { this.notified = state; }
	public boolean getNotified() { return this.notified; }
	
	public void decrementLifetime(float seconds) { this.lifetime -= seconds; }
	public void decrementDelay(float seconds) { this.delay -= seconds; }
	
	@NullableDecl
	@Override
	public Component<EntityStore> clone() {
		TpRequestComponent cloned = new TpRequestComponent();
		cloned.lifetime = this.lifetime;
		cloned.delay = this.delay;
		cloned.target = this.target;
		cloned.accepted = this.accepted;
		return cloned;
	}
}