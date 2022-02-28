import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.ArrayList;
import java.util.Base64;

public class StringUtils
{
    public static String applySHA256(String input)
    {
        StringBuffer hexString = new StringBuffer();
        try
        {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // Apply the sha256 encryption.
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');

                hexString.append(hex);
            }

        }

        catch(Exception e)
        {
            ErrorHandle.handleException(e);
        }

        return hexString.toString();
    }


    // Applies ECDSA Signature and return the result as bytes
    public static byte[] applyECSDASignature(PrivateKey privateKey, String input)
    {
        Signature dsa;

        byte[] output = new byte[0];

        try
        {
            dsa = Signature.getInstance("ECDSA", "BC");
            dsa.initSign(privateKey);

            byte[] strBytes = input.getBytes();
            dsa.update(strBytes);

            output = dsa.sign();
        }
        catch(Exception e)
        {
            ErrorHandle.handleException(e);
        }

        return output;
    }


    public static boolean verifyECSDASignature(PublicKey publicKey, String data, byte[] signature)
    {
        boolean result = false;
        try
        {
            Signature ecdsaVerify =  Signature.getInstance("ECDSA", "BC");
            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(data.getBytes());

            result = ecdsaVerify.verify(signature);
        }
        catch(Exception e)
        {
            ErrorHandle.handleException(e);
        }

        return result;

    }


    public static String getStringFromKey(Key key)
    {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }


    public static String getMerkleRoot(ArrayList<Transaction> transactions)
    {
        int count = transactions.size();
        ArrayList<String> previousTreeLayer = new ArrayList<>();

        for(Transaction transaction : transactions)
        {
            previousTreeLayer.add(transaction.transactionID);
        }

        ArrayList<String> treeLayer = previousTreeLayer;

        while(count > 1)
        {
            treeLayer = new ArrayList<>();
            
            for(int i = 1; i < previousTreeLayer.size(); ++i)
            {
                treeLayer.add(applySHA256(previousTreeLayer.get(i - 1) + previousTreeLayer.get(i)));
            }

            count = treeLayer.size();
            previousTreeLayer = treeLayer;
        }

        return (treeLayer.size() == 1) ? treeLayer.get(0) : "";
    }
}
