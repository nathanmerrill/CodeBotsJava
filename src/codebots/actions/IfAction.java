package codebots.actions;

import codebots.Bot;
import codebots.actions.arguments.Argument;
import codebots.actions.arguments.LineArgument;
import codebots.exceptions.BadFormatException;

import java.util.HashSet;

public class IfAction extends Action<IfAction>{
    private enum ConditionType{Var, Equals, EqualsExact}
    private final ConditionType conditionType;
    private final Argument leftComparison;
    private final Argument rightComparison;
    private final LineArgument falseLine;
    private final LineArgument trueLine;
    public final static HashSet<String> recursionBlocker = new HashSet<>();
    public IfAction(String condition, String trueLine, String falseLine){
        if (condition.contains("==")){
            conditionType = ConditionType.EqualsExact;
            String[] parts = condition.split("==");
            if (parts.length != 2){
                throw new BadFormatException("Only 1 set of double equals allowed");
            }
            leftComparison = Argument.parseArgument(parts[0]);
            rightComparison = Argument.parseArgument(parts[1]);
        } else if (condition.contains("=")){
            conditionType = ConditionType.Equals;
            String[] parts = condition.split("=");
            if (parts.length != 2){
                throw new BadFormatException("Only 1 equals sign allowed");
            }
            leftComparison = Argument.parseArgument(parts[0]);
            rightComparison = Argument.parseArgument(parts[1]);
        } else {
            leftComparison = Argument.parseArgument(condition);
            rightComparison = null;
            conditionType = ConditionType.Var;
        }
        this.falseLine = (LineArgument) Argument.parseArgument(falseLine);
        this.trueLine = (LineArgument) Argument.parseArgument(trueLine);
    }

    @Override
    public void act(Bot b) {
        if (recursionBlocker.contains(this.command))
            return;
        recursionBlocker.add(this.command);
        if (this.isTrue(b)){
            this.trueLine.getValue(b).act(b);
        } else {
            this.falseLine.getValue(b).act(b);
        }
    }
    private boolean isTrue(Bot b){
        switch (conditionType){
            case Equals:
                return leftComparison.isSimilarTo(rightComparison, b);
            case EqualsExact:
                return leftComparison.equalsExact(rightComparison, b);
            case Var:
            default:
                return leftComparison.isTrue(b);
        }
    }


}
