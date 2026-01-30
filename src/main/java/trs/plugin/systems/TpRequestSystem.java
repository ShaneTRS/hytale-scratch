package trs.plugin.systems;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.modules.physics.component.Velocity;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import trs.plugin.components.TpRequestComponent;

	public class TpRequestSystem extends EntityTickingSystem<EntityStore> {
		private static final ComponentType<EntityStore, TpRequestComponent> TP_REQUEST_COMPONENT = TpRequestComponent.getComponentType();
		private static final ComponentType<EntityStore, TransformComponent> TRANSFORM_COMPONENT = TransformComponent.getComponentType();
		private static final ComponentType<EntityStore, Velocity> VELOCITY_COMPONENT = Velocity.getComponentType();
		private static final ComponentType<EntityStore, Teleport> TELEPORT_COMPONENT = Teleport.getComponentType();
		private static final ComponentType<EntityStore, PlayerRef> PLAYER_REF_COMPONENT = PlayerRef.getComponentType();
		
		@Override
		public void tick(
			float dt,
			int index,
			@NonNullDecl ArchetypeChunk<EntityStore> archetypeChunk,
			@NonNullDecl Store<EntityStore> store,
			@NonNullDecl CommandBuffer<EntityStore> commandBuffer
		) {
			TpRequestComponent tpRequestComp = archetypeChunk.getComponent(index, TP_REQUEST_COMPONENT);
			if (!tpRequestComp.getAccepted()) {
				tpRequestComp.decrementLifetime(dt);
				if (tpRequestComp.getLifetime() > 0) return;
				commandBuffer.removeComponent(archetypeChunk.getReferenceTo(index), TP_REQUEST_COMPONENT);
				return;
			}
			tpRequestComp.decrementDelay(dt);
			
			float tpRequestDelay = tpRequestComp.getDelay();
			if (tpRequestDelay > 3) return;
			if (archetypeChunk.getComponent(index, VELOCITY_COMPONENT).getSpeed() > 1) {
				commandBuffer.removeComponent(archetypeChunk.getReferenceTo(index), TP_REQUEST_COMPONENT);
				archetypeChunk.getComponent(index, PLAYER_REF_COMPONENT)
					.sendMessage(Message.raw("Your teleport request was cancelled because you were moving!"));
				return;
			} else if (!tpRequestComp.getNotified()) {
				archetypeChunk.getComponent(index, PLAYER_REF_COMPONENT)
					.sendMessage(Message.raw("Teleporting..."));
				tpRequestComp.setNotified(true);
			}
			if (tpRequestDelay > 0) return;
			
			Ref<EntityStore> targetRef = commandBuffer.getExternalData().getRefFromUUID(tpRequestComp.getTarget());
			if (targetRef == null) return;
			
			Ref<EntityStore> ref = archetypeChunk.getReferenceTo(index);
			TransformComponent transformComp = commandBuffer.getComponent(targetRef, TRANSFORM_COMPONENT);
			
			commandBuffer.addComponent(ref, TELEPORT_COMPONENT,
				Teleport.createForPlayer(transformComp.getTransform()).withoutVelocityReset());
			commandBuffer.removeComponent(ref, TP_REQUEST_COMPONENT);
		}
		
		@Override
		public Query<EntityStore> getQuery() { return Query.and(TP_REQUEST_COMPONENT, VELOCITY_COMPONENT, PLAYER_REF_COMPONENT); }
	}
