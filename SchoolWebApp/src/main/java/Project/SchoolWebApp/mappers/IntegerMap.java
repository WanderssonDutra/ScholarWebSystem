package Project.SchoolWebApp.mappers;

public class IntegerMap {

    public static Boolean isInteger(String str){


        try {
                Integer.parseInt(str);
        }
        catch (NumberFormatException ex){
            return false;
        }

        return true;
    }
}
