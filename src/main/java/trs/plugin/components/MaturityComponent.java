package trs.plugin.components;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.NPCPlugin;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import trs.plugin.MainPlugin;
import trs.plugin.assets.MaturityAsset;

public class MaturityComponent implements Component<EntityStore> {

    private Float creatureAge = 0f;
    private Float creatureChildhood;
    private int creatureAdultRole;

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

    public Float getCreatureAge() { return this.creatureAge; }
    public Float getCreatureChildhood() { return this.creatureChildhood; }
    public int getCreatureAdultRole() { return this.creatureAdultRole; }

    @NullableDecl
    @Override
    public Component<EntityStore> clone() {
        MaturityComponent cloned = new MaturityComponent();
        cloned.creatureAge = this.creatureAge;
        cloned.creatureChildhood = this.creatureChildhood;
        cloned.creatureAdultRole = this.creatureAdultRole;
        return cloned;
    }

    public static MaturityComponent fromAsset(MaturityAsset maturityAsset) {
        MaturityComponent component = new MaturityComponent();
        component.setCreatureAdultRole(maturityAsset.getAdultRole());
        component.setCreatureChildhood(maturityAsset.getChildhood());
        return component;
    }
}
