package mafia;
import java.lang.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

abstract class Player {
    private int hitpoint;
    private final String Name;

    public Player(String ID) {
        this.Name = ID;
    }

    public String getID() {
        return Name;
    }


    public int getHitpoint() {
        return hitpoint;
    }

    public void setHitpoint(int hitpoint) {
        this.hitpoint = hitpoint;
    }

    public abstract int voteout(HashMap<Integer, Player> input, int vote);

    @Override
    public String toString() {

        return this.getID();
    }

    public static int mafia_hp(HashMap<Integer, Player> mafia) {
        Set<Integer> setkey = mafia.keySet();
        int num = 0;

        for (Integer key : setkey) {
//        System.out.println("hh"+mafia.get(key).getHitpoint());
            if (mafia.get(key).getHitpoint() > 0) {
                num++;
            }
        }
        return num;
    }

    public static void dec_mafia(HashMap<Integer, Player> input, int x, int y, HashMap<Integer, Player> mafia) {
        int local = x / y;
//    System.out.println("HX"+x);
//    System.out.println("HY"+y);
        List<Map.Entry<Integer, Player>> list =
                new LinkedList<Map.Entry<Integer, Player>>(mafia.entrySet());
        list.sort(new Comparator<Map.Entry<Integer, Player>>() {
            public int compare(Map.Entry<Integer, Player> o1,
                               Map.Entry<Integer, Player> o2) {
                return Integer.compare(o1.getValue().getHitpoint() - o2.getValue().getHitpoint(), 0);
            }
        });
        HashMap<Integer, Player> temp = new LinkedHashMap<>();
        for (Map.Entry<Integer, Player> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        input = temp;

        Set<Integer> setkeyy = mafia.keySet();
        for (Integer key : setkeyy) {
            local = x / y;
//        System.out.println("LOc"+local);
//        System.out.println("XX"+x);
//        System.out.println("YY"+y);
            if (input.get(key).getHitpoint() > 0) {
                if (local >= input.get(key).getHitpoint()) {
                    x -= input.get(key).getHitpoint();
                    input.get(key).setHitpoint(0);

                    y--;

                } else {
                    x -= local;
//                System.out.println(local);
//                System.out.println(input.get(key).getHitpoint()+"P");
                    input.get(key).setHitpoint(input.get(key).getHitpoint() - local);
//                System.out.println(input.get(key).getHitpoint()+"O");
                    y--;
                }
            }
        }
        mafia = input;
    }

    public static void round(HashMap<Integer, Player> input, int no_of_player, int character, int player) {
        Scanner sc = new Scanner(System.in);
        HashMap<Integer, Player> mafia = new HashMap<>();
        HashMap<Integer,Player> dett = new HashMap<>();
        for(int i =0;i<input.size();i++){
            if(input.get(i+1) instanceof Detective){
                dett.put(i+1,mafia.get(i+1));
            }
        }

        for (int i = 0; i < no_of_player; i++) {
            if (input.get(i + 1).getClass().equals(Mafia.class)) {
                mafia.put(i + 1, input.get(i + 1));
            }

        }
        ArrayList<Mafia> maafi = new ArrayList<>();
        ArrayList<Healer> hh = new ArrayList<>();
        ArrayList<Detective> det = new ArrayList<>();
        ArrayList<commoner> com = new ArrayList<>();
        for (int ii = 0; ii < input.size(); ii++) {
            if (input.get(ii + 1) instanceof Mafia) {
                maafi.add((Mafia) input.get(ii + 1));
            }
        }
        for (int ii = 0; ii < input.size(); ii++) {
            if (input.get(ii + 1) instanceof Detective) {
                det.add((Detective) input.get(ii + 1));
            }
        }
        for (int ii = 0; ii < input.size(); ii++) {
            if (input.get(ii + 1) instanceof Healer) {
                hh.add((Healer) input.get(ii + 1));
            }
        }
        for (int ii = 0; ii < input.size(); ii++) {
            if (input.get(ii + 1) instanceof commoner) {
                com.add((commoner) input.get(ii + 1));
            }
        }


        boolean flag = true;
        int Round = 1;
        while (flag) {
            System.out.println("Round: " + Round);
            System.out.print(input.size() + " Players are remaining: ");
            Set<Integer> setkeyyy = input.keySet();
            for (Integer key : setkeyyy) {
                System.out.print("Player" + key + ", ");
            }
//            System.out.print("Player"+input.size());
            System.out.println(" are alive ");

            switch (character) {
                case 1:
                    if (input.containsKey(player)) {
                        System.out.println("Choose a target");
                        int target = sc.nextInt();
                        int vote = Mafia.choose_target(input, no_of_player, target);
                        int detect = Detective.detect(no_of_player, input);
                        int heal = Healer.heal(no_of_player, input);
//                        System.out.println(heal);
                        System.out.println("Detectives have choosen a player to test");
                        System.out.println("Healers have choosen a player to heal");
                        System.out.println("--End of actions--");
                        int hp_mafia = 0;
                        Set<Integer> setkey = mafia.keySet();
                        int x = 0;
                        int y = mafia_hp(mafia);
//                        System.out.println("y"+y);
                        for (Integer key : setkey) {
                            hp_mafia += input.get(key).getHitpoint();
                        }
                        if (hp_mafia < input.get(vote).getHitpoint()) {
                            input.get(vote).setHitpoint(input.get(vote).getHitpoint() - hp_mafia);
                            Set<Integer> setkeyy = mafia.keySet();
                            for (Integer key : setkeyy) {
                                input.get(key).setHitpoint(0);
                                mafia.get(key).setHitpoint(0);
                            }
                        } else {
//                            System.out.println("HPMAF"+hp_mafia);
                            x = input.get(vote).getHitpoint();
//                            System.out.println("KPX"+x);
                            input.get(vote).setHitpoint(0);
                            dec_mafia(input, x, y, mafia);
                        }
                        input.get(heal).setHitpoint(input.get(heal).getHitpoint() + 500);
//                        System.out.println(input.get(vote).getHitpoint());
                        if (input.get(vote).getHitpoint() == 0) {
                            System.out.println("Player" + vote + " has died");
                            input.remove(vote);

                                mafia.remove(vote);


                        } else {
                            System.out.println("No one died");
                        }


                        Mafia oo = new Mafia("-1");
                        if (input.get(detect) instanceof Mafia ) {
                            input.remove(detect);
                            mafia.remove(detect);
                            System.out.println("Player" + detect + " has been voted out ");
                            System.out.println("--End of Round " + Round);
                            Round++;
                        } else {
                            System.out.println("select a person to voteout");
                            int voteout = sc.nextInt();
                            voteout = oo.voteout(input, voteout);
                            if (input.get(voteout) instanceof Mafia) {
                                mafia.remove(voteout);
                            }

                            input.remove(voteout);

                            System.out.println("Player" + voteout + " has been voted out ");
                            System.out.println("--End of Round " + Round);
                            Round++;
                        }
                    } else {
                        int vote = Mafia.choose_target(no_of_player, input);
                        int detect = Detective.detect(no_of_player, input);
                        int heal = Healer.heal(no_of_player, input);
                        System.out.println("Mafias have choosen their target");
                        System.out.println("Detectives have choosen a player to test");
                        System.out.println("Healers have choosen a player to heal");
                        System.out.println("--End of actions--");
                        int hp_mafia = 0;
                        Set<Integer> setkey = mafia.keySet();
                        int x = 0;
                        int y = mafia_hp(mafia);
//                        System.out.println("y"+y);
                        for (Integer key : setkey) {
                            hp_mafia += input.get(key).getHitpoint();
                        }
                        if (hp_mafia < input.get(vote).getHitpoint()) {
                            input.get(vote).setHitpoint(input.get(vote).getHitpoint() - hp_mafia);
                            Set<Integer> setkeyy = mafia.keySet();
                            for (Integer key : setkeyy) {
                                input.get(key).setHitpoint(0);
                                mafia.get(key).setHitpoint(0);
                            }
                        } else {
//                            System.out.println("HPMAF"+hp_mafia);
                            x = input.get(vote).getHitpoint();
//                            System.out.println("KPX"+x);
                            input.get(vote).setHitpoint(0);
                            dec_mafia(input, x, y, mafia);
                        }
                        input.get(heal).setHitpoint(input.get(heal).getHitpoint() + 500);
//                        System.out.println(input.get(vote).getHitpoint());
                        if (input.get(vote).getHitpoint() == 0) {
                            System.out.println("Player" + vote + " has died");
                            input.remove(vote);

                                mafia.remove(vote);


                        } else {
                            System.out.println("No one died");
                        }
//                        System.out.println("select a person to voteout");

                        Mafia oo = new Mafia("-2");
                        if (input.get(detect) instanceof Mafia) {
                            input.remove(detect);
                            mafia.remove(detect);
                            System.out.println("Player" + detect + " has been voted out ");
                            System.out.println("--End of Round " + Round);
                            Round++;
                        } else {
                            int voteout = ThreadLocalRandom.current().nextInt(1, no_of_player + 1);
                            while (!input.containsKey(voteout)) {
                                voteout = ThreadLocalRandom.current().nextInt(1, no_of_player + 1);
                            }
                            voteout = oo.voteout(input, voteout);
                            if (input.get(voteout) instanceof Mafia) {
                                mafia.remove(voteout);
                            }

                            input.remove(voteout);

                            System.out.println("Player" + voteout + " has been voted out ");
                            System.out.println("--End of Round " + Round);
                            Round++;
                        }
                    }
                    break;
                case 2:
                    if (input.containsKey(player)) {
                        System.out.println("Mafias have choosen their target");
                        System.out.println("Choose a player to test");
                        int target = sc.nextInt();
                        int vote = Mafia.choose_target(no_of_player, input);
                        int detect = Detective.detect(input, no_of_player, target);
                        int heal = Healer.heal(no_of_player, input);
//                        System.out.println(heal);

                        System.out.println("Healers have choosen a player to heal");
                        System.out.println("--End of actions--");
                        int hp_mafia = 0;
                        Set<Integer> sekey = mafia.keySet();
                        int x;
                        int y = mafia_hp(mafia);
//                        System.out.println("y"+y);
                        for (Integer key : sekey) {
//                            System.out.println(key);
                            hp_mafia += input.get(key).getHitpoint();
                        }
                        if (hp_mafia < input.get(vote).getHitpoint()) {
                            input.get(vote).setHitpoint(input.get(vote).getHitpoint() - hp_mafia);
                            Set<Integer> setkeyy = mafia.keySet();
                            for (Integer key : setkeyy) {
                                input.get(key).setHitpoint(0);
                                mafia.get(key).setHitpoint(0);
                            }
                        } else {
//                            System.out.println("HPMAF"+hp_mafia);
                            x = input.get(vote).getHitpoint();
//                            System.out.println("KPX"+x);
                            input.get(vote).setHitpoint(0);
                            dec_mafia(input, x, y, mafia);
                        }
                        input.get(heal).setHitpoint(input.get(heal).getHitpoint() + 500);
//                        System.out.println(input.get(vote).getHitpoint());
                        if (input.get(vote).getHitpoint() == 0) {
                            System.out.println("Player" + vote + " has died");
                            input.remove(vote);
                            mafia.remove(vote);
                        } else {
                            System.out.println("No one died");
                        }


                        Mafia oo = new Mafia("-3");
                        if (input.get(detect) instanceof Mafia ) {
                            input.remove(detect);

                                mafia.remove(detect);


                            System.out.println("Player" + detect + " has been voted out ");
                            System.out.println("--End of Round " + Round);
                            Round++;
                        } else {
                            System.out.println("select a person to voteout");
                            int voteout = sc.nextInt();
                            voteout = oo.voteout(input, voteout);
                            if (input.get(voteout) instanceof Mafia) {
                                mafia.remove(voteout);
                            }

                            input.remove(voteout);

                            System.out.println("Player" + voteout + " has been voted out ");
                            System.out.println("--End of Round " + Round);
                            Round++;
                        }
                    } else {
                        int vote = Mafia.choose_target(no_of_player, input);
                        int detect = Detective.detect(no_of_player, input);
                        int heal = Healer.heal(no_of_player, input);
                        System.out.println("Mafias have choosen their target");
                        System.out.println("Detectives have choosen a player to test");
                        System.out.println("Healers have choosen a player to heal");
                        System.out.println("--End of actions--");
                        int hp_mafia = 0;
                        Set<Integer> setkey = mafia.keySet();
                        int x;
                        int y = mafia_hp(mafia);
//                        System.out.println("y"+y);
                        for (Integer key : setkey) {
                            hp_mafia += input.get(key).getHitpoint();
                        }
                        if (hp_mafia < input.get(vote).getHitpoint()) {
                            input.get(vote).setHitpoint(input.get(vote).getHitpoint() - hp_mafia);
                            Set<Integer> setkeyy = mafia.keySet();
                            for (Integer key : setkeyy) {
                                input.get(key).setHitpoint(0);
                                mafia.get(key).setHitpoint(0);
                            }
                        } else {
//                            System.out.println("HPMAF"+hp_mafia);
                            x = input.get(vote).getHitpoint();
//                            System.out.println("KPX"+x);
                            input.get(vote).setHitpoint(0);
                            dec_mafia(input, x, y, mafia);
                        }
                        input.get(heal).setHitpoint(input.get(heal).getHitpoint() + 500);
//                        System.out.println(input.get(vote).getHitpoint());
                        if (input.get(vote).getHitpoint() == 0) {
                            System.out.println("Player" + vote + " has died");
                            input.remove(vote);

                                mafia.remove(vote);


                        } else {
                            System.out.println("No one died");
                        }
//                        System.out.println("select a person to voteout");

                        Mafia oo = new Mafia("-4");
                        if (input.get(detect) instanceof Mafia ) {
                            input.remove(detect);
                            mafia.remove(detect);
                            System.out.println("Player" + detect + " has been voted out ");
                            System.out.println("--End of Round " + Round);
                            Round++;
                        } else {
                            int voteout = ThreadLocalRandom.current().nextInt(1, no_of_player + 1);
                            while (!input.containsKey(voteout)) {
                                voteout = ThreadLocalRandom.current().nextInt(1, no_of_player + 1);
                            }
                            voteout = oo.voteout(input, voteout);
                            if (input.get(voteout) instanceof Mafia) {
                                mafia.remove(voteout);
                            }
                            input.remove(voteout);

                            System.out.println("Player" + voteout + " has been voted out ");
                            System.out.println("--End of Round " + Round);
                            Round++;
                        }
                    }
                    break;
                case 3:
                    if (input.containsKey(player)) {
                        System.out.println("Mafias have choosen their target");
                        System.out.println("Detectives have choosen a player to test");
                        System.out.println("Choose a player to heal");
                        int target = sc.nextInt();
                        int vote = Mafia.choose_target(no_of_player, input);
                        int detect = Detective.detect(no_of_player, input);
                        int heal = Healer.heal(target, no_of_player, input);
//                        System.out.println(heal);


                        System.out.println("--End of actions--");
                        int hp_mafia = 0;
                        Set<Integer> sekey = mafia.keySet();
                        int x;
                        int y = mafia_hp(mafia);
//                        System.out.println("y"+y);
                        for (Integer key : sekey) {
//                            System.out.println(key);
                            hp_mafia += input.get(key).getHitpoint();
                        }
                        if (hp_mafia < input.get(vote).getHitpoint()) {
                            input.get(vote).setHitpoint(input.get(vote).getHitpoint() - hp_mafia);
                            Set<Integer> setkeyy = mafia.keySet();
                            for (Integer key : setkeyy) {
                                input.get(key).setHitpoint(0);
                                mafia.get(key).setHitpoint(0);
                            }
                        } else {
//                            System.out.println("HPMAF"+hp_mafia);
                            x = input.get(vote).getHitpoint();
//                            System.out.println("KPX"+x);
                            input.get(vote).setHitpoint(0);
                            dec_mafia(input, x, y, mafia);
                        }
                        input.get(heal).setHitpoint(input.get(heal).getHitpoint() + 500);
//                        System.out.println(input.get(vote).getHitpoint());
                        if (input.get(vote).getHitpoint() == 0) {
                            System.out.println("Player" + vote + " has died");
                            input.remove(vote);
                            mafia.remove(vote);
                        } else {
                            System.out.println("No one died");
                        }


                        Mafia oo = new Mafia("-5");
                        if (input.get(detect) instanceof Mafia ) {
                            input.remove(detect);
                            mafia.remove(detect);
                            System.out.println("Player" + detect + " has been voted out ");
                            System.out.println("--End of Round " + Round);
                            Round++;
                        } else {
                            System.out.println("select a person to voteout");
                            int voteout = sc.nextInt();
                            voteout = oo.voteout(input, voteout);
                            if (input.get(voteout) instanceof Mafia) {
                                mafia.remove(voteout);
                            }
//                            System.out.println(input.containsKey(voteout));
                            input.remove(voteout);
//                            System.out.println(input.containsKey(voteout));


                            System.out.println("Player" + voteout + " has been voted out ");
                            System.out.println("--End of Round " + Round);
                            Round++;
                        }
                    } else {
                        int vote = Mafia.choose_target(no_of_player, input);
                        int detect = Detective.detect(no_of_player, input);
                        int heal = Healer.heal(no_of_player, input);
                        System.out.println("Mafias have choosen their target");
                        System.out.println("Detectives have choosen a player to test");
                        System.out.println("Healers have choosen a player to heal");
                        System.out.println("--End of actions--");
                        int hp_mafia = 0;
                        Set<Integer> setkey = mafia.keySet();
                        int x;
                        int y = mafia_hp(mafia);
//                        System.out.println("y"+y);
                        for (Integer key : setkey) {
                            hp_mafia += input.get(key).getHitpoint();
                        }
                        if (hp_mafia < input.get(vote).getHitpoint()) {
                            input.get(vote).setHitpoint(input.get(vote).getHitpoint() - hp_mafia);
                            Set<Integer> setkeyy = mafia.keySet();
                            for (Integer key : setkeyy) {
                                input.get(key).setHitpoint(0);
                                mafia.get(key).setHitpoint(0);
                            }
                        } else {
//                            System.out.println("HPMAF"+hp_mafia);
                            x = input.get(vote).getHitpoint();
//                            System.out.println("KPX"+x);
                            input.get(vote).setHitpoint(0);
                            dec_mafia(input, x, y, mafia);
                        }
                        input.get(heal).setHitpoint(input.get(heal).getHitpoint() + 500);
//                        System.out.println(input.get(vote).getHitpoint());
                        if (input.get(vote).getHitpoint() == 0) {
                            System.out.println("Player" + vote + " has died");
                            input.remove(vote);
                            mafia.remove(vote);
                        } else {
                            System.out.println("No one died");
                        }
//                        System.out.println("select a person to voteout");

                        Mafia oo = new Mafia("-6");
                        if (input.get(detect) instanceof Mafia) {
                            input.remove(detect);
                            mafia.remove(detect);
                            System.out.println("Player" + detect + " has been voted out ");
                            System.out.println("--End of Round " + Round);
                            Round++;
                        } else {
                            int voteout = ThreadLocalRandom.current().nextInt(1, no_of_player + 1);
                            while (!input.containsKey(voteout)) {
                                voteout = ThreadLocalRandom.current().nextInt(1, no_of_player + 1);
                            }
                            voteout = oo.voteout(input, voteout);
                            if (input.get(voteout) instanceof Mafia) {
                                mafia.remove(voteout);
                            }
                            input.remove(voteout);

                            System.out.println("Player" + voteout + " has been voted out ");
                            System.out.println("--End of Round " + Round);
                            Round++;
                        }
                    }
                    break;
                case 4:
                    if (input.containsKey(player)) {
                        int vote = Mafia.choose_target(no_of_player, input);
                        int detect = Detective.detect(no_of_player, input);
                        int heal = Healer.heal(no_of_player, input);
                        System.out.println("Mafias have choosen their target");
                        System.out.println("Detectives have choosen a player to test");
                        System.out.println("Healers have choosen a player to heal");
                        System.out.println("--End of actions--");
                        int hp_mafia = 0;
                        Set<Integer> setkey = mafia.keySet();
                        int x = 0;
                        int y = mafia_hp(mafia);
//                        System.out.println("y"+y);
                        for (Integer key : setkey) {
                            hp_mafia += input.get(key).getHitpoint();
                        }
                        if (hp_mafia < input.get(vote).getHitpoint()) {
                            input.get(vote).setHitpoint(input.get(vote).getHitpoint() - hp_mafia);
                            Set<Integer> setkeyy = mafia.keySet();
                            for (Integer key : setkeyy) {
                                input.get(key).setHitpoint(0);
                                mafia.get(key).setHitpoint(0);
                            }
                        } else {
//                            System.out.println("HPMAF"+hp_mafia);
                            x = input.get(vote).getHitpoint();
//                            System.out.println("KPX"+x);
                            input.get(vote).setHitpoint(0);
                            dec_mafia(input, x, y, mafia);
                        }
                        input.get(heal).setHitpoint(input.get(heal).getHitpoint() + 500);
//                        System.out.println(input.get(vote).getHitpoint());
                        if (input.get(vote).getHitpoint() == 0) {
                            System.out.println("Player" + vote + " has died");
                            input.remove(vote);
                            mafia.remove(vote);
                        } else {
                            System.out.println("No one died");
                        }
//                        System.out.println("select a person to voteout");

                        Mafia oo = new Mafia("-7");
                        if (input.get(detect) instanceof Mafia ) {
                            input.remove(detect);
                            mafia.remove(detect);
                            System.out.println("Player" + detect + " has been voted out ");
                            System.out.println("--End of Round " + Round);
                            Round++;
                        } else {
                            System.out.println("select a person to voteout");
                            int voteout = sc.nextInt();
                            voteout = oo.voteout(input, voteout);
                            if (input.get(voteout) instanceof Mafia) {
                                mafia.remove(voteout);
                            }
                            input.remove(voteout);

                            System.out.println("Player" + voteout + " has been voted out ");
                            System.out.println("--End of Round " + Round);
                            Round++;
                        }

                    } else {
                        int vote = Mafia.choose_target(no_of_player, input);
                        int detect = Detective.detect(no_of_player, input);
                        int heal = Healer.heal(no_of_player, input);
                        System.out.println("Mafias have choosen their target");
                        System.out.println("Detectives have choosen a player to test");
                        System.out.println("Healers have choosen a player to heal");
                        System.out.println("--End of actions--");
                        int hp_mafia = 0;
                        Set<Integer> setkey = mafia.keySet();
                        int x = 0;
                        int y = mafia_hp(mafia);
//                        System.out.println("y"+y);
                        for (Integer key : setkey) {
                            hp_mafia += input.get(key).getHitpoint();
                        }
                        if (hp_mafia < input.get(vote).getHitpoint()) {
                            input.get(vote).setHitpoint(input.get(vote).getHitpoint() - hp_mafia);
                            Set<Integer> setkeyy = mafia.keySet();
                            for (Integer key : setkeyy) {
                                input.get(key).setHitpoint(0);
                                mafia.get(key).setHitpoint(0);
                            }
                        } else {
//                            System.out.println("HPMAF"+hp_mafia);
                            x = input.get(vote).getHitpoint();
//                            System.out.println("KPX"+x);
                            input.get(vote).setHitpoint(0);
                            dec_mafia(input, x, y, mafia);
                        }
                        input.get(heal).setHitpoint(input.get(heal).getHitpoint() + 500);
//                        System.out.println(input.get(vote).getHitpoint());
                        if (input.get(vote).getHitpoint() == 0) {
                            System.out.println("Player" + vote + " has died");
                            input.remove(vote);
                            mafia.remove(vote);
                        } else {
                            System.out.println("No one died");
                        }
//                        System.out.println("select a person to voteout");

                        Mafia oo = new Mafia("-8");
                        if (input.get(detect) instanceof Mafia ) {
                            input.remove(detect);
                            mafia.remove(detect);
                            System.out.println("Player" + detect + " has been voted out ");
                            System.out.println("--End of Round " + Round);
                            Round++;
                        } else {
                            int voteout = ThreadLocalRandom.current().nextInt(1, no_of_player + 1);
                            while (!input.containsKey(voteout)) {
                                voteout = ThreadLocalRandom.current().nextInt(1, no_of_player + 1);
                            }
                            voteout = oo.voteout(input, voteout);
                            if (input.get(voteout) instanceof Mafia) {
                                mafia.remove(voteout);
                            }
                            input.remove(voteout);

                            System.out.println("Player" + voteout + " has been voted out ");
                            System.out.println("--End of Round " + Round);
                            Round++;
                        }
                    }
                    break;

            }

            int no_of_mafia = 0;
            int mafia_flag = 0;
            Set<Integer> setke = input.keySet();
            for (Integer key : setke) {
                if (input.get(key).getClass().equals(Mafia.class)) {
                    no_of_mafia++;
                    mafia_flag = 1;
                }
            }
            if (mafia_flag == 0) {
                System.out.println("Game over");
                System.out.println("Mafias have lost");
                generic_class<Mafia> g = new generic_class<>();
                generic_class<Detective> gg = new generic_class<>();
                generic_class<Healer> ggg = new generic_class<>();
                generic_class<commoner> gggg = new generic_class<>();

                g.print(maafi, "were mafia");
                gg.print(det, "were Detective");
                ggg.print(hh, "were Healer");
                gggg.print(com, "were commoner");

                flag = false;
            }
            if (no_of_mafia == input.size() - no_of_mafia) {

                System.out.println("Game over");
                System.out.println("Mafias have won");
                generic_class<Mafia> g = new generic_class<>();
                generic_class<Detective> gg = new generic_class<>();
                generic_class<Healer> ggg = new generic_class<>();
                generic_class<commoner> gggg = new generic_class<>();
                g.print(maafi, "were mafia");
                gg.print(det, "were Detective");
                ggg.print(hh, "were Healer");
                gggg.print(com, "were commoner");

                System.out.println("were commoners");
                System.out.println("End of Sample case");
                System.out.println("End of sample case");
                flag = false;
            }

        }
    }
}

