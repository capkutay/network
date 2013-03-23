import java.util.Scanner;


public class SingletonScanner
{
    private static Scanner s = null;
    
    private SingletonScanner() { }

    public static Scanner getInstance()
    {
        if (s == null)
        {
            s = new Scanner(System.in);
        }
        return s;
    }

}