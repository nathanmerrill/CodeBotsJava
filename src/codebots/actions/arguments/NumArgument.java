package codebots.actions.arguments;

import codebots.Bot;
import codebots.exceptions.BadFormatException;

public class NumArgument extends IntArgument {
    private final Integer num;
    public NumArgument(String argument){
        try {
            num = Integer.parseInt(argument);
        }catch (NumberFormatException e){
            throw new BadFormatException("Cannot parse argument");
        }
    }

    @Override
    public Integer getValue(Bot current) {
        return num;
    }

    @Override
    public void setValue(Bot current, Integer value) {
        throw new BadFormatException("Cannot set to a number");
    }

    public void blockValue(Bot current, int hostLine){
        throw new BadFormatException("Cannot block number");
    }


}
