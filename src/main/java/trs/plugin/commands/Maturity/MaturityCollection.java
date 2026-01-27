package trs.plugin.commands.Maturity;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;
import com.hypixel.hytale.server.npc.NPCPlugin;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import trs.plugin.components.MaturityComponent;

public class MaturityCollection extends AbstractCommandCollection {

    public MaturityCollection() {
        super("maturity", "Commands to interact with the Maturity component.");
        addSubCommand(new QueryCommand("query", "Check the maturity of an entity", false));
        addSubCommand(new SetCommand("set", "Modify the maturity of an entity", false));
    }

    public static Message maturityDebugMessage(MaturityComponent maturity, NPCEntity npc) {
        String adultRole = NPCPlugin.get().getName(maturity.getCreatureAdultRole());
        return Message.raw(String.format(
                "%s will grow up into %s. %.3f/%.3f",
                npc.getRoleName(),
                adultRole,
                maturity.getCreatureAge(),
                maturity.getCreatureChildhood()
        ));
    }
}
