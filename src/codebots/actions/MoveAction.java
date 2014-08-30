package codebots.actions;


import codebots.Bot;
import codebots.CodeBots;

public class MoveAction extends Action<MoveAction> {
    @Override
    public void act(Bot b) {
        CodeBots.Direction direction = CodeBots.Direction.values()[b.getVariable('D')%4];
        int newX = b.x + direction.x;
        int newY = b.y + direction.y;
        if (CodeBots.getBot(newX, newY) != null) {
            return;
        }
        CodeBots.addBot(null, b.x, b.y);
        b.x = newX;
        b.y = newY;
        if (b.x < 0){
            b.x += CodeBots.getWidth();
        } else if (b.x >= CodeBots.getWidth()){
            b.x -= CodeBots.getWidth();
        }
        if (b.y < 0){
            b.y += CodeBots.getHeight();
        } else if (b.y >= CodeBots.getHeight()){
            b.y -= CodeBots.getHeight();
        }
        CodeBots.addBot(b, b.x, b.y);
    }
}