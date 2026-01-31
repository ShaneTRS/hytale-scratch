package trs.scratch.systems;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.RefSystem;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import com.hypixel.hytale.server.npc.systems.RoleChangeSystem;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import trs.scratch.assets.MaturityAsset;
import trs.scratch.components.MaturityComponent;

import static com.hypixel.hytale.math.vector.Vector3d.formatShortString;

public class MaturitySystems {
	private static final ComponentType<EntityStore, MaturityComponent> MATURITY_COMPONENT = MaturityComponent.getComponentType();
	private static final ComponentType<EntityStore, NPCEntity> NPC_COMPONENT = NPCEntity.getComponentType();
	
	public static class MaturityTicking extends EntityTickingSystem<EntityStore> {
		@Override
		public void tick(
			float dt,
			int index,
			@NonNullDecl ArchetypeChunk<EntityStore> archetypeChunk,
			@NonNullDecl Store<EntityStore> store,
			@NonNullDecl CommandBuffer<EntityStore> commandBuffer
		) {
			MaturityComponent maturityComp = archetypeChunk.getComponent(index, MATURITY_COMPONENT);
			maturityComp.incrementAge(dt);
			
			if (!maturityComp.completedChildhood()) return;
			
			NPCEntity npcComp = archetypeChunk.getComponent(index, NPC_COMPONENT);
			Ref<EntityStore> ref = archetypeChunk.getReferenceTo(index);
			
			RoleChangeSystem.requestRoleChange(ref, npcComp.getRole(), maturityComp.getAdultRole(), true, store);
			HytaleLogger.forEnclosingClass().atInfo().log(String.format(
				"Requested role change for %s at position %s", npcComp.getRoleName(), formatShortString(npcComp.getOldPosition())
			));
			commandBuffer.removeComponent(ref, MATURITY_COMPONENT);
		}
		
		@Override
		public Query<EntityStore> getQuery() { return Query.and(MATURITY_COMPONENT, NPC_COMPONENT); }
	}
	
	public static class MaturityRef extends RefSystem<EntityStore> {
		@Override
		public void onEntityAdded(
			@NonNullDecl Ref<EntityStore> ref,
			@NonNullDecl AddReason addReason,
			@NonNullDecl Store<EntityStore> store,
			@NonNullDecl CommandBuffer<EntityStore> commandBuffer
		) {
			NPCEntity npcComp = store.getComponent(ref, NPC_COMPONENT);
			MaturityAsset maturityAsset = MaturityAsset.getAssetMap().getAsset(npcComp.getRoleName());
			if (maturityAsset == null) return;
			
			commandBuffer.addComponent(ref, MATURITY_COMPONENT, MaturityComponent.fromAsset(maturityAsset));
		}
		
		@Override
		public void onEntityRemove(
			@NonNullDecl Ref<EntityStore> ref,
			@NonNullDecl RemoveReason removeReason,
			@NonNullDecl Store<EntityStore> store,
			@NonNullDecl CommandBuffer<EntityStore> commandBuffer
		) { }
		
		@NullableDecl
		@Override
		public Query<EntityStore> getQuery() { return Query.and(NPC_COMPONENT, Query.not(MATURITY_COMPONENT)); }
	}
}