import java.security.MessageDigest;

public class StringUtils
{
    public static String applySHA256(String input)
    {
        try
        {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // Apply the sha256 encryption.
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            // Save the hash as a hex string
            StringBuffer hexString = new StringBuffer();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');

                hexString.append(hex);
            }

            return hexString.toString();
        }
        catch(Exception e)
        {
            System.err.println(e.getLocalizedMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
