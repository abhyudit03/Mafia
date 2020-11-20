package mafia;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class commoner extends Player{
    private int hitpoint;

    public commoner(String id){
        super(id);
        hitpoint=1000;

    }
    @Override
    public int getHitpoint() {
        return hitpoint;
    }
    @Override
    public void setHitpoint(int hitpoint) {
        this.hitpoint = hitpoint;
    }

    @Override
    public int voteout(HashMap<Integer, Player> input, int vote) {
        Scanner sc = new Scanner(System.in);
        while (!input.containsKey(vote)){
            System.out.println("Player"+vote +"cannot be voted out");
            vote = sc.nextInt();
        }
        ArrayList<Integer> votes = new ArrayList<>();
        votes.add(vote);
        for(int i=1;i<input.size();i++){
            votes.add(ThreadLocalRandom.current().nextInt(1, input.size() + 1));
        }
        int max = 0;
        int curr;
        int currKey =  -1;
        Set<Integer> unique = new HashSet<Integer>(votes);

        for (Integer key : unique) {
            curr = Collections.frequency(votes, key);

            if(max < curr){
                max = curr;
                currKey = key;
            }
        }
        if(input.containsKey(currKey)){
            return currKey;
        }
        while (!input.containsKey(currKey)){
            currKey = ThreadLocalRandom.current().nextInt(1,input.size()+1);
        }

        return currKey;
    }
}
