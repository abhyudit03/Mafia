package mafia;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Detective extends Player {
    private int hitpoint;




    public Detective(String ID){
        super(ID);
        hitpoint=800;

    }
    @Override
    public int voteout(HashMap<Integer, Player> input, int vote) {
        Scanner sc = new Scanner(System.in);
        while (!input.containsKey(vote)) {
            System.out.println("Player" + vote + "cannot be voted out");
            vote = sc.nextInt();
        }
        ArrayList<Integer> votes = new ArrayList<>();
        votes.add(vote);
        for (int i = 1; i < input.size(); i++) {
            votes.add(ThreadLocalRandom.current().nextInt(1, input.size() + 1));
        }
        int max = 0;
        int curr = 0;
        int currKey = -1;
        Set<Integer> unique = new HashSet<Integer>(votes);

        for (Integer key : unique) {
            curr = Collections.frequency(votes, key);

            if (max < curr) {
                max = curr;
                currKey = key;
            }
        }
        if (input.containsKey(currKey)) {
            return currKey;
        } while (!input.containsKey(currKey)){
            currKey = ThreadLocalRandom.current().nextInt(1,input.size()+1);
        }
        return currKey;
    }

    @Override
    public int getHitpoint() {
        return hitpoint;
    }

    @Override
    public void setHitpoint(int hitpoint) {
        this.hitpoint = hitpoint;
    }


    public static int detect(int no_of_players,HashMap<Integer,Player>input) {
        int rd =ThreadLocalRandom.current().nextInt(1, no_of_players + 1);
        while (!input.containsKey(rd)){
            rd= ThreadLocalRandom.current().nextInt(1,no_of_players+1);
        }
        while ((input.get(rd) instanceof Detective)){
            rd = ThreadLocalRandom.current().nextInt(1,no_of_players+1);
            while (!input.containsKey(rd)){
                rd = ThreadLocalRandom.current().nextInt(1, no_of_players + 1);
            }
        }
        return rd;
    }


    public  static int detect(HashMap<Integer,Player>input,int no_of_players, int vote) {
        Scanner sc = new Scanner(System.in);

        while (!input.containsKey(vote)) {
            System.out.println("Invalid player vote");
            vote = sc.nextInt();
        }
        while (input.get(vote).getClass().equals(Detective.class)){
            System.out.println("You can not test a detective. Choose a player to test");
            vote = sc.nextInt();
            while (!input.containsKey(vote)) {
                System.out.println("Invalid player vote");
                vote = sc.nextInt();
            }
        }
        return vote;
    }
}
