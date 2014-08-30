package codebots.actions.arguments;

import codebots.Bot;
import codebots.exceptions.BadFormatException;

public class VarArgument extends IntArgument{
    private final char var;
    private final int numOpponents;
    public VarArgument(String argument){
        numOpponents = getNumOpponents(argument);
        argument = argument.substring(numOpponents);
        if (argument.length() != 1){
            throw new BadFormatException("Cannot parse variable "+argument.substring(argument.length()-1));
        }
        var = argument.charAt(0);
    }


    @Override
    public Integer getValue(Bot current) {
        return followOpponent(current, numOpponents).getVariable(var);
    }

    @Override
    public void setValue(Bot current, Integer value) {
        followOpponent(current, numOpponents).setVariable(var, value);
    }

    @Override
    public void blockValue(Bot current, int hostLine) {
        this.followOpponent(current, numOpponents).blockVar(var, hostLine);
    }

    @Override
    public boolean equals(Argument other, Bot current) {
        if (var != 'D'){
            return equalsExact(other, current);
        }
        if (other instanceof VarArgument) {
            VarArgument va = (VarArgument) other;
            if (va.var == 'D'){
                return getValue(current)%4 == this.getValue(current)%4;
            }
        }
        return equalsExact(other, current);
    }

    @Override
    public boolean isTrue(Bot current) {
        switch (var){
            case 'D':
                return current.getOpponent() != null;
            case 'E':
                return 'E'%2 == 0;
            default:
                return super.isTrue(current);
        }
    }
}
