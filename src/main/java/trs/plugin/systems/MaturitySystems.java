package trs.plugin.systems;

import com.hypixel.hytale.builtin.tagset.TagSetPlugin;
import com.hypixel.hytale.builtin.tagset.config.NPCGroup;
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
import trs.plugin.components.MaturityComponent;

import java.util.logging.Level;

public class MaturitySystems {
    private static final ComponentType<EntityStore, MaturityComponent> maturityCompType = MaturityComponent.getComponentType();
    private static final ComponentType<EntityStore, NPCEntity> npcCompType = NPCEntity.getComponentType();

    public static class MaturityTicking extends EntityTickingSystem<EntityStore> {

        @Override
        public void tick(float dt, int index, @NonNullDecl ArchetypeChunk<EntityStore> archetypeChunk, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> commandBuffer) {

            MaturityComponent maturityComponent = archetypeChunk.getComponent(index, maturityCompType);
            if (maturityComponent.getCreatureDone()) return;
            maturityComponent.incrementAge(dt);

            if (!maturityComponent.completedChildhood()) return;

            NPCEntity npcComponent = archetypeChunk.getComponent(index, npcCompType);

            maturityComponent.setCreatureDone(true);
            RoleChangeSystem.requestRoleChange(archetypeChunk.getReferenceTo(index), npcComponent.getRole(), maturityComponent.getCreatureAdultRole(), true, store);

            HytaleLogger.forEnclosingClass().at(Level.INFO).log(String.format("Maturity ticked for %s at %s", npcComponent.getRoleName(), npcComponent.getOldPosition()));
        }

        @Override
        public Query<EntityStore> getQuery() {
            return Query.and(maturityCompType, npcCompType);
        }
    }

    public static class MaturityRef extends RefSystem<EntityStore> {

        @Override
        public void onEntityAdded(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl AddReason addReason, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> commandBuffer) {
            NPCEntity npcEntity = store.getComponent(ref, npcCompType);
            int immatureGroup = NPCGroup.getAssetMap().getIndex("TRS_Immature");
            int roleIndex = npcEntity.getRoleIndex();

            TagSetPlugin.TagSetLookup tagSetPlugin = TagSetPlugin.get(NPCGroup.class);
            if (!tagSetPlugin.tagInSet(immatureGroup, roleIndex)) return;

            store.getExternalData().getWorld().execute(() -> {
                store.addComponent(ref, MaturityComponent.getComponentType(), MaturityComponent.fromNPC(npcEntity));
            });
        }

        @Override
        public void onEntityRemove(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl RemoveReason removeReason, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> commandBuffer) {
        }

        @NullableDecl
        @Override
        public Query<EntityStore> getQuery() {
            return Query.and(npcCompType, Query.not(maturityCompType));
        }

    }
}