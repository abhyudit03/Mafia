package mafia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to mafia");
        System.out.println("Choose no of Players");
        int no_of_players = sc.nextInt();
        while (no_of_players<6) {
            System.out.println("Minimum 6 players are required to play the game");
            no_of_players = sc.nextInt();
        }
        int no_of_detectives = no_of_players/5;
        int no_of_mafias = no_of_players/5;
        int no_of_healers = Math.max(1,no_of_players/10);

        HashMap <Integer,Player> List_of_players = new HashMap<>(no_of_players);

        System.out.println("Choose a character :");
        System.out.println("1) Mafia");
        System.out.println("2) Detective");
        System.out.println("3) Healer");
        System.out.println("4) Commoner");
        System.out.println("5) Assign Randomly");
        int character = sc.nextInt();
        int Rando= ThreadLocalRandom.current().nextInt(1, no_of_players + 1);
        if(character==5){
            character = ThreadLocalRandom.current().nextInt(1, 5);
        }
        switch (character){
            case 1:
                System.out.println("You are Playerr"+Rando);
                Mafia ob = new Mafia("Player"+Rando+"[User]");
                List_of_players.put(Rando,ob);
                allotment(List_of_players,no_of_detectives,no_of_mafias-1,no_of_healers,no_of_players);
                ArrayList<String> arr = new ArrayList<>(no_of_mafias);
                for (int i=1;i<List_of_players.size()+1;i++){
                    if (List_of_players.get(i) instanceof Mafia && i!=Rando){
                        arr.add("Player"+(i));
                    }
                }
                System.out.println("You are mafia. Other mafias are: "+arr);
                break;
            case 2:
                System.out.println("You are Player"+Rando);
                Detective ob1 = new Detective("Player"+Rando+"[User]");
                List_of_players.put(Rando,ob1);
                allotment(List_of_players,no_of_detectives-1,no_of_mafias,no_of_healers,no_of_players);
                ArrayList<String> arr1 = new ArrayList<>(no_of_detectives);
                for (int i=1;i<List_of_players.size()+1;i++){
                    if (List_of_players.get(i) instanceof Detective && i!=Rando){
                        arr1.add("Player"+(i));
                    }
                }
                System.out.println("You are Detective. Other Detective are: "+arr1);
                break;
            case 3 :
                System.out.println("You are Player"+Rando);
                Healer ob2 = new Healer("Player"+Rando+"[User]");
                List_of_players.put(Rando,ob2);
                allotment(List_of_players,no_of_detectives,no_of_mafias,no_of_healers-1,no_of_players);
                ArrayList<String> arr2 = new ArrayList<>(no_of_healers);
                for (int i=1;i<List_of_players.size()+1;i++){
                    if (List_of_players.get(i) instanceof Healer && i!=Rando){
                        arr2.add("Player"+(i));
                    }
                }
                System.out.println("You are Healer. Other Healer are: "+arr2);
                break;
            case 4 :
                System.out.println("You are Player"+Rando);
                commoner ob3 = new commoner("Player"+Rando+"[User]");
                List_of_players.put(Rando,ob3);
                allotment(List_of_players,no_of_detectives,no_of_mafias,no_of_healers,no_of_players);
                ArrayList<String> arr3 = new ArrayList<>(no_of_players);
                for (int i=1;i<List_of_players.size()+1;i++){
                    if (List_of_players.get(i) instanceof commoner && i!=Rando){
                        arr3.add("Player"+(i));
                    }
                }
                System.out.println("You are Commoner. Other Commoner are: "+arr3);
                break;

        }
        Player.round(List_of_players,no_of_players,character,Rando);


    }
    public static void allotment(HashMap<Integer,Player> input, int det, int mafia, int healer,int no){
        for(int i=0;i<det;i++){

            int Random = ThreadLocalRandom.current().nextInt(1, no + 1);
            while (!(input.get(Random)==null)){
                Random = ThreadLocalRandom.current().nextInt(1, no + 1);
            }
            Detective ob = new Detective("Player"+Random);
            input.put(Random,ob);
        }
        for(int i=0;i<mafia;i++){

            int Random = ThreadLocalRandom.current().nextInt(1, no + 1);
            while (!(input.get(Random)==null)){
                Random = ThreadLocalRandom.current().nextInt(1, no + 1);
            }
            Mafia ob = new Mafia("Player"+Random);
            input.put(Random,ob);
        }
        for(int i=0;i<healer;i++){

            int Random = ThreadLocalRandom.current().nextInt(1, no + 1);
            while (!(input.get(Random)==null)){
                Random = ThreadLocalRandom.current().nextInt(1, no + 1);
            }
            Healer ob = new Healer("Player"+Random);
            input.put(Random,ob);
        }
        for (int i=1;i<no+1;i++){
            if(input.get(i)==null){
                commoner ob = new commoner("Player"+i);
                input.put(i,ob);
            }

        }
//        System.out.println(input);
    }
}
