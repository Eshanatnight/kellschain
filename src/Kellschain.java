import java.util.ArrayList;
import java.security.Security;
import java.util.HashMap;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import com.google.gson.GsonBuilder;

public class Kellschain
{
    public static ArrayList<Block> blockChain = new ArrayList<Block>();
    public static HashMap<String, TransactionOutput> UTXOs = new HashMap<>();
    public static int difficulty = 0;

    public static Wallet wallet_A;
    public static Wallet wallet_B;


    public static void main(String[] args)
    {
        //Setup Bouncey castle as a Security Provider
        Security.addProvider(new BouncyCastleProvider());

        // Initialize the wallets
        wallet_A = new Wallet();
        wallet_B = new Wallet();

        //Test public and private keys
        System.out.println("Private and public keys:");
        System.out.println(StringUtils.getStringFromKey(wallet_A.privateKey));
        System.out.println(StringUtils.getStringFromKey(wallet_A.publicKey));

        // Create a test Transaction
        Transaction transaction = new Transaction(wallet_A.publicKey, wallet_B.publicKey, 10.0f, null);
        transaction.generateSignature(wallet_A.privateKey);

        // Verify the signature
        System.out.println("Signature Verification: " + transaction.verifySignature());
    }


    public static Boolean isChainValid()
    {
        Block currentBlock;
        Block previousBlock;

        // Loop through the blockchain to check hashes
        for (int i = 1; i < blockChain.size(); i++)
        {
            currentBlock = blockChain.get(i);
            previousBlock = blockChain.get(i - 1);

            // Compare the registered hash and the calculated hash
            if (!currentBlock.hash.equals(currentBlock.calculateHash()))
            {
                System.out.println("Current Hashes are not Equal");
                return false;
            }

            // Compare the previous hash and the previous block's hash
            if (!previousBlock.hash.equals(currentBlock.previousHash))
            {
                System.out.println("Previous Hashes are not Equal");
                return false;
            }
        }

        return true;
    }

}
