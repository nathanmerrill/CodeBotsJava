package codebots.actions.arguments;

import codebots.Bot;
import codebots.CodeBots;
import codebots.exceptions.BadFormatException;

import java.util.ArrayList;
import java.util.List;

public class AddArgument extends IntArgument{
    private final List<Argument<Integer>> addends;
    public AddArgument(String[] addends){
        this.addends = new ArrayList<>(addends.length);
        for (String addend: addends){
            this.addends.add(parse(addend));
        }
    }

    @Override
    public Integer getValue(Bot current) {
        int sum = 0;
        for (Argument<Integer> addend: addends){
            sum += addend.getValue(current);
        }
        return sum % CodeBots.numLines;
    }

    @Override
    public void setValue(Bot current, Integer value) {
        throw new BadFormatException("Cannot set to an addend");
    }

    @Override
    public void blockValue(Bot current, int hostLine) {
        throw new BadFormatException("Cannot block number");
    }

}
