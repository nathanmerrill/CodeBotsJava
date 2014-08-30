package codebots.actions.arguments;

import codebots.Bot;

public abstract class IntArgument extends Argument<Integer>{

    public static Argument<Integer> parse(String argument){
        if (argument.contains("+")){
            return new AddArgument(argument.split("\\+"));
        }
        for (char c = 'A'; c <= 'E'; c++) {
            if (argument.lastIndexOf(c) != -1) {
                return new VarArgument(argument);
            }
        }
        return new NumArgument(argument);
    }

    @Override
    public boolean equalsExact(Argument other, Bot current) {
        return other.getValue(current) == this.getValue(current);
    }

    @Override
    public boolean equals(Argument other, Bot current) {
        return equalsExact(other, current);
    }

    @Override
    public boolean isTrue(Bot current) {
        return getValue(current) != 0;
    }
}
