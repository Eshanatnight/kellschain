import java.util.ArrayList;
import com.google.gson.GsonBuilder;

public class Kellschain
{
    public static ArrayList<Block> blockChain = new ArrayList<Block>();
    public static int difficulty = 5;

    public static void main(String[] args) {
        //add our blocks to the blockChain ArrayList:

        blockChain.add(new Block("Hi im the first block", "0"));
        System.out.println("Trying to Mine block 1... ");
        blockChain.get(0).mineBlock(difficulty);

        blockChain.add(new Block("Yo im the second block",blockChain.get(blockChain.size()-1).hash));
        System.out.println("Trying to Mine block 2... ");
        blockChain.get(1).mineBlock(difficulty);

        blockChain.add(new Block("Hey im the third block",blockChain.get(blockChain.size()-1).hash));
        System.out.println("Trying to Mine block 3... ");
        blockChain.get(2).mineBlock(difficulty);

        System.out.println("\nBlockchain is Valid: " + isChainValid());

        String blockChainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockChain);
        System.out.println("\nThe block chain: ");
        System.out.println(blockChainJson);
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
