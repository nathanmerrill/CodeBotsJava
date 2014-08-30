package codebots.actions;

import codebots.Bot;
import codebots.actions.arguments.Argument;

public class BlockAction extends Action<BlockAction>{
    private Argument<?> toBlock;
    public BlockAction(String toBlock){
        this.toBlock = Argument.parseArgument(toBlock);
    }
    @Override
    public void act(Bot bot) {
        toBlock.blockValue(bot, bot.getVariable('C'));
    }
}
