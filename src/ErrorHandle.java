public class ErrorHandle
{
    // Prints a LocalizedMessage to the console.
    // Then Prints the stack trace to the console.
    // Finally throws a new RuntimeException.
    public static void handleException(Exception e)
    {
        System.out.println("Error: " + e.getLocalizedMessage());
        e.printStackTrace();
        throw new RuntimeException(e);
    }
}
