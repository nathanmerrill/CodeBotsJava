package codebots;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.*;

public class CodeBots {

    private static int width, height;
    private static Map<Integer, Bot> bots;
    public final static int numLines = 24;
    public final static int totalTurns = 5000;
    public final static int totalGames = 10;
    public final static int numBotCopies = 50;

    public enum Direction{
        North(0, -1), East(-1, 0), South(0, 1), West(1, 0);
        public int x, y;
        Direction(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

    public static int getWidth(){
        return width;
    }
    public static int getHeight(){
        return height;
    }
    public static void addBot(Bot b, int x, int y){
        bots.put(toCoordinates(x, y), b);
    }
    private static int toCoordinates(int x, int y){
        return x+y*(10+width);
    }
    public static Bot getBot(int x, int y){
        if (bots.containsKey(toCoordinates(x, y))) {
            return bots.get(toCoordinates(x, y));
        }
        return null;
    }

    public static File[] readBotFolder() {
        File dir = new File("src/codebots/bots");
        return dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".txt");
            }
        });
    }
    public static void main(String[] args) throws FileNotFoundException{
        bots = new HashMap<>();
        run();
        print();
    }
    public static void readBots() throws FileNotFoundException{
        bots.clear();
        File[] files = readBotFolder();
        ArrayList<Bot> botList = new ArrayList<>(files.length*50);
        for (File f:readBotFolder()) {
            for (int i = 0; i < numBotCopies; i++) {
                botList.add(new Bot(f));
            }
        }
        for (Bot b: botList){
            Scorer.addBot(b);
        }
        placeBots(botList);
    }
    public static void placeBots(List<Bot> bots){
        Collections.shuffle(bots);
        int maxX = (int) Math.sqrt(bots.size()*4)+1;
        int curY = 0;
        int curX = 0;
        for (Bot b : bots) {
            b.x = curX;
            b.y = curY;
            curX += 4;
            if (curX > maxX) {
                curY++;
                curX = curY % 2 * 2;
            }
            CodeBots.bots.put(toCoordinates(b.x, b.y), b);
        }
        width = maxX;
        height = curY;
    }
    public static void run() throws FileNotFoundException{
        readBots();
        for (int i = 0; i < totalGames; i++){
            System.out.println("Game:"+i);
            for (int j = 0; j < totalTurns; j++){
                Bot[] allBots = bots.values().toArray(new Bot[bots.size()]);
                for (Bot b: allBots){
                    if (b != null) {
                        b.takeTurn();
                    }
                }
            }
            count();
            reset();
        }
    }
    public static void reset(){
        for (Bot b: bots.values()){
            b.reset();
        }
        ArrayList<Bot> temp = new ArrayList<>(bots.values());
        bots.clear();
        placeBots(temp);
    }
    public static void count(){
        Scorer.scoreBots();
    }
    public static void print(){
        for (Scorer b: Scorer.getScores()){
            System.out.println(b.name+" got a score of "+b.getScore());
        }
    }
}
