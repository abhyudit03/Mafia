package mafia;

import java.util.ArrayList;

public class generic_class<T1> {
    public void print (ArrayList<T1> arr, String str){

        for(T1 element: arr){

            System.out.print(element.toString() + " ");
//            System.out.println(element.equals((element.getClass())player));


        }
        System.out.println(str);
    }
}
