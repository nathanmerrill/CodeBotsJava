package codebots;

import codebots.actions.Action;
import codebots.actions.FlagAction;
import codebots.actions.IfAction;
import codebots.exceptions.NoOpponent;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Bot {
    private final Action[] actions;
    private final Action[] initialActions;
    private final List<Queue<Integer>> actionBlocks;
    private final int[] vars;
    private final List<Queue<Integer>> blocks;
    public final String name;
    public int x, y;
    private static Random random = new Random(System.currentTimeMillis());

    public Bot(File botFile) throws FileNotFoundException{
        String filename = botFile.getName();
        name = filename.substring(0,filename.indexOf("."));
        Scanner scanner = new Scanner(botFile);
        String[] lines = scanner.useDelimiter("\\Z").next().replace("\r","").split("\n");
        scanner.close();
        actions = new Action[CodeBots.numLines];
        initialActions = new Action[CodeBots.numLines];
        int action_index = 0;
        for (int i = 0; action_index < CodeBots.numLines; i++){
            if (i < lines.length) {
                actions[action_index] = Action.createAction(lines[i], this);
            } else {
                actions[action_index] = Action.createAction("Flag", this);
            }
            if (actions[action_index]!= null) {
                initialActions[action_index] = actions[action_index];
                action_index++;
            }
        }
        vars = new int[5];
        setVars();
        blocks = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            blocks.add(new LinkedList<>());
        }
        actionBlocks = new ArrayList<>();
        for (int i = 0; i < CodeBots.numLines; i++){
            actionBlocks.add(new LinkedList<>());
        }
    }
    public Bot getOpponent(){
        CodeBots.Direction direction = CodeBots.Direction.values()[vars['D'-'A']%4];
        return CodeBots.getBot(x+direction.x, y+direction.y);
    }
    public Action getLine(int lineNumber){
        return actions[lineNumber%24];
    }
    public void setLine(int lineNumber, Action action){
        if (actionBlocks.get(lineNumber%24).size() > 0){
            actionBlocks.get(lineNumber%24).poll();
            return;
        }
        actions[lineNumber%24] =  action;
    }
    public int getVariable(char varName){
        return vars[varName-'A'];
    }
    public void setVariable(char varName, int value){
        if (blocks.get(varName-'A').size() > 0){
            blocks.get(varName-'A').poll();
            return;
        }
        vars[varName-'A'] = value;
    }
    public void blockLine(int lineNumber, int hostLine){
        if (!actionBlocks.get(lineNumber%24).contains(hostLine)){
            actionBlocks.get(lineNumber%24).add(lineNumber);
        }
    }
    public void blockVar(char var, int hostLine){
        if (!blocks.get(var-'A').contains(hostLine)){
            blocks.get(var-'A').add(hostLine);
        }
    }
    public String declareFlag(){
        HashMap<String, Integer> count = new HashMap<>();
        for (Action a: actions){
            if (a instanceof FlagAction) {
                FlagAction fl = (FlagAction) a;
                if (count.containsKey(fl.owner.name)){
                    count.put(fl.owner.name, count.get(fl.owner.name)+1);
                } else{
                    count.put(fl.owner.name, 1);
                }
            }
        }
        int maxCount = 0;
        String maxName = "";
        for (String name: count.keySet()){
            if (count.get(name) > maxCount){
                maxCount = count.get(name);
                maxName = name;
            }else if (count.get(name) == maxCount){
                maxName = "";
            }
        }
        return maxName;
    }
    public void takeTurn(){
        IfAction.recursionBlocker.clear();
        vars['E'-'A'] = Math.abs(random.nextInt()%CodeBots.numLines);
        try {
            getLine(vars['C' - 'A']).act(this);
        } catch(NoOpponent e){
        } catch (Exception e){
            System.out.println(getLine(vars['C' - 'A']).toString());
            throw e;
        }
        vars['C'-'A'] = (vars['C'-'A']+1)%CodeBots.numLines;
    }
    private void setVars(){
        for (int i = 0; i < 5; i++){
            vars[i] = 0;
        }
        vars['D'-'A'] = Math.abs(random.nextInt()%CodeBots.numLines);
    }
    public void reset(){
        System.arraycopy(actions, 0, initialActions, 0, actions.length);
        setVars();
        for (Queue<Integer> block: blocks){
            block.clear();
        }
        for (Queue<Integer> block: actionBlocks){
            block.clear();
        }
    }
}
