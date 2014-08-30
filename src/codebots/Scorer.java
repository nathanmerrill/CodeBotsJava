package codebots;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Scorer implements Comparable<Scorer> {
    public static HashMap<String, Scorer> scorers = new HashMap<String, Scorer>();
    public static void scoreBots(){
        for (Scorer s: scorers.values()) {
            for (Bot b: s.bots) {
                String flag = b.declareFlag();
                scorers.get(flag).score++;
            }
        }
    }
    public static void addBot(Bot b){
        if (scorers.containsKey(b.name)){
            scorers.get(b.name).bots.add(b);
        }
        scorers.put(b.name, new Scorer(b));
    }
    private final List<Bot> bots;
    private int score;
    public final String name;
    private Scorer(Bot b){
        bots = new ArrayList<>();
        score = 0;
        name = b.name;
    }
    public static List<Scorer> getScores(){
        List<Scorer> scores = new ArrayList<>(scorers.values());
        Collections.sort(scores);
        return scores;
    }
    public int getScore(){
        return score;
    }
    @Override
    public int compareTo(Scorer o) {
        return this.score - o.score;
    }
}
