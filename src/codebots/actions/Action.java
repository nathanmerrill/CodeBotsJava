package codebots.actions;

import codebots.Bot;
import codebots.exceptions.BadFormatException;

import java.util.HashMap;

public abstract class Action<T extends Action<T>> {
    private static HashMap<String, Action> memoizer = new HashMap<>();
    public abstract void act(Bot bot);
    public boolean equalsExact(Action action){
        return action.command.equals(this.command);
    }
    @Override
    public String toString(){
        return this.command;
    }
    protected String command;
    public boolean isSimilarTo(Action action){
        return action.getClass().equals(this.getClass());
    }
    public static Action<?> createAction(String line, Bot creator){
        if (line.length()==0){
            throw new BadFormatException("Line needed");
        }
        String name;
        if (line.contains(" ")){
            name = line.substring(0, line.indexOf(" "));
        } else {
            name = line;
        }
        line = line.substring(name.length());
        if (line.contains("//")) {
            line = line.substring(0, line.indexOf("//"));
        }
        line = line.trim();
        String[] parts = line.split(" ");
        String fullLine = name+" "+line;
        if (name.equals("Flag")){
            fullLine+=" "+creator.name;
        }
        if (memoizer.containsKey(fullLine)){
            return memoizer.get(fullLine);
        }
        Action<?> action = null;
        if (name.equals("Move")){
            if (!line.isEmpty()){
                throw new BadFormatException("Move should not have any arguments");
            }
            action = new MoveAction();
        } else if (name.equals("Copy")){
            if (parts.length != 2){
                throw new BadFormatException("Copy requires exactly 2 arguments");
            }
            action = new CopyAction(parts[0], parts[1]);
        } else if (name.equals("Flag")){
            if (!line.isEmpty()){
                throw new BadFormatException("Flag should not have any arguments");
            }
            action = new FlagAction(creator);
        } else if (name.equals("If")){
            if (parts.length != 3){
                throw new BadFormatException("If should have exactly 3 arguments");
            }
            action = new IfAction(parts[0], parts[1], parts[2]);
        } else if (name.equals("Block")){
            if (parts.length != 1){
                throw new BadFormatException("Block should have exactly 1 argument");
            }
            action = new BlockAction(parts[0]);
        }
        if (action != null)
            action.command = fullLine;
        memoizer.put(fullLine, action);
        return action;
    }
}
