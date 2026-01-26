package trs.plugin.components;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.NPCPlugin;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import trs.plugin.MainPlugin;
import trs.plugin.assets.MaturityAsset;

public class MaturityComponent implements Component<EntityStore> {

    private Float creatureAge = 0f;
    private Float creatureChildhood;
    private int creatureAdultRole;
    private boolean creatureDone = false;

    public static final BuilderCodec<MaturityComponent> CODEC = BuilderCodec.builder(MaturityComponent.class, MaturityComponent::new)
            .append(
                    new KeyedCodec<>("Age", BuilderCodec.FLOAT),
                    MaturityComponent::setCreatureAge,
                    MaturityComponent::getCreatureAge
            )
            .add()
            .append(
                    new KeyedCodec<>("Childhood", BuilderCodec.FLOAT),
                    MaturityComponent::setCreatureChildhood,
                    MaturityComponent::getCreatureChildhood
            )
            .add()
            .append(
                    new KeyedCodec<>("AdultRole", BuilderCodec.INTEGER),
                    MaturityComponent::setCreatureAdultRoleId,
                    MaturityComponent::getCreatureAdultRole
            )
            .add()
            .append(
                    new KeyedCodec<>("Done", BuilderCodec.BOOLEAN),
                    MaturityComponent::setCreatureDone,
                    MaturityComponent::getCreatureDone
            )
            .add()
            .build();

    public static ComponentType<EntityStore, MaturityComponent> getComponentType() {
        return MainPlugin.get().getMaturityComponentType();
    }

    public void incrementAge(Float seconds) { this.creatureAge += seconds; }
    public boolean completedChildhood() { return this.creatureAge > this.creatureChildhood; }

    public void setCreatureAge(Float seconds) { this.creatureAge = seconds; }
    public void setCreatureChildhood(Float seconds) { this.creatureChildhood = seconds; }
    public void setCreatureAdultRole(String roleName) {
        this.creatureAdultRole = NPCPlugin.get().getIndex(roleName);
    }
    public void setCreatureAdultRoleId(int roleId) { this.creatureAdultRole = roleId; }
    public void setCreatureDone(boolean done) { this.creatureDone = done; }

    public Float getCreatureAge() { return this.creatureAge; }
    public Float getCreatureChildhood() { return this.creatureChildhood; }
    public int getCreatureAdultRole() { return this.creatureAdultRole; }
    public boolean getCreatureDone() { return this.creatureDone; }

    @NullableDecl
    @Override
    public Component<EntityStore> clone() {
        MaturityComponent cloned = new MaturityComponent();
        cloned.creatureAge = this.creatureAge;
        cloned.creatureChildhood = this.creatureChildhood;
        cloned.creatureAdultRole = this.creatureAdultRole;
        cloned.creatureDone = this.creatureDone;
        return cloned;
    }

    public static MaturityComponent fromNPC(NPCEntity npcEntity) {
        MaturityComponent component = new MaturityComponent();
        MaturityAsset maturityAsset = MaturityAsset.getAssetMap().getAsset(npcEntity.getRoleName());
        if (maturityAsset != null) {
            component.setCreatureAdultRole(maturityAsset.getAdultRole());
            component.setCreatureChildhood(maturityAsset.getChildhood());
        } else {
            component.setCreatureAdultRoleId(npcEntity.getRoleIndex());
        }
        return component;
    }
}
