package codebots.actions;

import codebots.Bot;

public class FlagAction extends Action<FlagAction> {
    public final Bot owner;
    public FlagAction(Bot original){
        owner = original;
    }
    @Override
    public boolean equals(FlagAction action) {
        return owner == action.owner;
    }

    @Override
    public void act(Bot b) {}
}
