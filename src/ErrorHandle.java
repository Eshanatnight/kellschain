public class ErrorHandle
{
    public static void handleException(Exception e)
    {
        System.out.println("Error: " + e.getLocalizedMessage());
        e.printStackTrace();
        throw new RuntimeException(e);
    }
}
