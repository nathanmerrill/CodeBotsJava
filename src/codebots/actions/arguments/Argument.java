package codebots.actions.arguments;

import codebots.Bot;
import codebots.exceptions.BadFormatException;
import codebots.exceptions.NoOpponent;

public abstract class Argument<T> {
    public abstract T getValue(Bot current);
    public abstract void setValue(Bot current, T value);
    public abstract void blockValue(Bot current, int hostLine);
    public abstract boolean isTrue(Bot current);
    public abstract boolean equals(Argument other, Bot current);
    public abstract boolean equalsExact(Argument other, Bot current);
    public Bot followOpponent(Bot bot, int num_opponents){
        for (int i = 0; i < num_opponents; i++){
            bot = bot.getOpponent();
            if (bot == null){
                throw new NoOpponent();
            }
        }
        return bot;
    }
    public int getNumOpponents(String argument){
        int numOpponents = argument.lastIndexOf("*")+1;
        for (char c: argument.substring(0,numOpponents).toCharArray()){
            if (c != '*'){
                throw new BadFormatException("Asterisks cannot be separated");
            }
        }
        return numOpponents;
    }
    public static Argument parseArgument(String argument){
        if (argument.contains("#")){
            String[] parts = argument.split("\\#");
            if (parts.length != 2){
                throw new BadFormatException("Only 1 # allowed in argument");
            }
            return new LineArgument(parts[0], parts[1]);
        }
        return IntArgument.parse(argument);
    }
}
