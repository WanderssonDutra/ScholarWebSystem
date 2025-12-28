package Project.SchoolWebApp.factory;

import lombok.ToString;

import java.util.Random;

/**
 * Class that creates a code of 6 digits
 */
public class UserCodeFactory {

    public static String generate(String str){

        String code = "";

        if(str.equalsIgnoreCase("student")) {

            code = "std";
            Random random = new Random();
            code += Long.toString(random.nextLong(000000, 999999));
        }

        if(str.equalsIgnoreCase("professor")){

            code = "pfr";
            Random random = new Random();
            code += Long.toString(random.nextLong(000000, 999999));
        }

        return code;
    }
}
