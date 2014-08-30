package codebots.actions.arguments;

import codebots.Bot;
import codebots.actions.Action;
import codebots.actions.FlagAction;
import codebots.exceptions.BadFormatException;

public class LineArgument extends Argument<Action<?>> {
    private final Argument<Integer> lineNumber;
    private final int numOpponents;
    public LineArgument(String before, String after){
        lineNumber = IntArgument.parse(after);
        numOpponents = getNumOpponents(before);
        if (numOpponents != before.length()){
            throw new BadFormatException("Cannot have anything but asterisks before #");
        }
    }

    @Override
    public void setValue(Bot current, Action<?> value) {
        this.followOpponent(current, numOpponents).setLine(lineNumber.getValue(current), value);
    }

    @Override
    public Action<?> getValue(Bot current) {
        return this.followOpponent(current, numOpponents).getLine(lineNumber.getValue(current));
    }

    @Override
    public void blockValue(Bot current, int hostLine) {
        this.followOpponent(current, numOpponents).blockLine(lineNumber.getValue(current), hostLine);
    }

    @Override
    public boolean equals(Argument other, Bot current) {
        if (other instanceof LineArgument) {
            LineArgument lineArgument = (LineArgument) other;
            return lineArgument.getValue(current).isSimilarTo(getValue(current));
        }
        return false;
    }

    @Override
    public boolean equalsExact(Argument other, Bot current) {
        if (other instanceof LineArgument) {
            LineArgument lineArgument = (LineArgument) other;
            return lineArgument.getValue(current).equals(getValue(current));
        }
        return false;
    }

    @Override
    public boolean isTrue(Bot current) {
        if (getValue(current) instanceof FlagAction) {
            return true;
        }
        return false;
    }
}
