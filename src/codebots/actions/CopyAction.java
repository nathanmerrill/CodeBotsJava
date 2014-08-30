package codebots.actions;


import codebots.Bot;
import codebots.actions.arguments.Argument;

public class CopyAction extends Action<CopyAction>{

    private final Argument copyTo;
    private final Argument copyFrom;
    public CopyAction(String from, String to){
        copyTo = Argument.parseArgument(to);
        copyFrom = Argument.parseArgument(from);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void act(Bot b) {
        copyTo.setValue(b, copyFrom.getValue(b));
    }
}
