import java.util.ArrayList;
import java.security.Security;
import java.util.HashMap;
import org.bouncycastle.jce.provider.BouncyCastleProvider;


public class Kellschain
{
    public static ArrayList<Block> blockchain = new ArrayList<>();
    public static HashMap<String, TransactionOutput> UTXOs = new HashMap<>();

    public static int difficulty = 1;
    public static float minimumTransaction = 0.1f;
    public static Wallet wallet_A;
    public static Wallet wallet_B;
    public static Transaction genesisTransaction;


    public static void main(String[] args)
    {
        // add our blocks to the blockchain ArrayList:

        // Setup Bouncey castle as a Security Provider
        Security.addProvider(new BouncyCastleProvider());

        // Initialize the wallets
        wallet_A = new Wallet();
        wallet_B = new Wallet();
        Wallet coinbase = new Wallet();

        // Create genesis transaction, which sends 100 KellsCoin to wallet A
        genesisTransaction = new Transaction(coinbase.publicKey, wallet_A.publicKey, 100f, null);

        // Manually sign the genesis transaction
        genesisTransaction.generateSignature(coinbase.privateKey);
        // Manually set the transaction id
        genesisTransaction.transactionID = "0";
        // Manually add the Transaction Output
        genesisTransaction.outputs.add(new TransactionOutput(
                genesisTransaction.reciepient,
                genesisTransaction.value,
                genesisTransaction.transactionID)
        );

        // it's important to store our first transaction in the UTXOs list.
        UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));

        System.out.println("Creating and Mining Genesis block... ");
        Block genesis = new Block("0");
        genesis.addTransaction(genesisTransaction);
        addBlock(genesis);

        //testing
        Block block1 = new Block(genesis.hash);
        System.out.println("\nWalletA's balance is: " + wallet_A.getBalance());
        System.out.println("\nWalletA is Attempting to send funds (40) to WalletB...");
        block1.addTransaction(wallet_A.sendFunds(wallet_B.publicKey, 40f));
        addBlock(block1);
        System.out.println("\nWalletA's balance is: " + wallet_A.getBalance());
        System.out.println("WalletB's balance is: " + wallet_B.getBalance());

        Block block2 = new Block(block1.hash);
        System.out.println("\nWalletA Attempting to send more funds (1000) than it has...");
        block2.addTransaction(wallet_A.sendFunds(wallet_B.publicKey, 1000f));
        addBlock(block2);
        System.out.println("\nWalletA's balance is: " + wallet_A.getBalance());
        System.out.println("WalletB's balance is: " + wallet_B.getBalance());

        Block block3 = new Block(block2.hash);
        System.out.println("\nWalletB is Attempting to send funds (20) to WalletA...");
        block3.addTransaction(wallet_B.sendFunds( wallet_A.publicKey, 20));
        System.out.println("\nWalletA's balance is: " + wallet_A.getBalance());
        System.out.println("WalletB's balance is: " + wallet_B.getBalance());

        isChainValid();
    }


    public static void addBlock(Block newBlock)
    {
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }


    public static Boolean isChainValid()
    {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = StringUtils.getDifficultyString(difficulty);

        //a temporary working list of unspent transactions at a given block state.
        HashMap<String,TransactionOutput> tempUTXOs = new HashMap<>();
        tempUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));

        //loop through blockchain to check hashes:
        for(int i=1; i < blockchain.size(); ++i)
        {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i-1);
            //compare registered hash and calculated hash:
            if(!currentBlock.hash.equals(currentBlock.calculateHash()) )
            {
                System.out.println("#Current Hashes not equal");
                return false;
            }

            //compare previous hash and registered previous hash
            if(!previousBlock.hash.equals(currentBlock.previousHash) )
            {
                System.out.println("#Previous Hashes not equal");
                return false;
            }

            //check if hash is solved
            if(!currentBlock.hash.substring( 0, difficulty).equals(hashTarget))
            {
                System.out.println("#This block hasn't been mined");
                return false;
            }

            //loop through blockchains transactions:
            TransactionOutput tempOutput;

            for(int t = 0; t < currentBlock.transactions.size(); ++t)
            {
                Transaction currentTransaction = currentBlock.transactions.get(t);

                if(!currentTransaction.verifySignature())
                {
                    System.out.println("#Signature on Transaction(" + t + ") is Invalid");
                    return false;
                }

                if(currentTransaction.getInputsValue() != currentTransaction.getOutputsValue())
                {
                    System.out.println("#Inputs are note equal to outputs on Transaction(" + t + ")");
                    return false;
                }

                for(TransactionInput input: currentTransaction.inputs)
                {
                    tempOutput = tempUTXOs.get(input.transactionOutputID);

                    if(tempOutput == null)
                    {
                        System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
                        return false;
                    }

                    if(input.UTXO.value != tempOutput.value)
                    {
                        System.out.println("#Referenced input Transaction(" + t + ") value is Invalid");
                        return false;
                    }

                    tempUTXOs.remove(input.transactionOutputID);
                }

                for(TransactionOutput output: currentTransaction.outputs)
                {
                    tempUTXOs.put(output.id, output);
                }

                if( currentTransaction.outputs.get(0).recipient != currentTransaction.reciepient)
                {
                    System.out.println("#Transaction(" + t + ") output recipient is not who it should be");
                    return false;
                }

                if( currentTransaction.outputs.get(1).recipient != currentTransaction.sender)
                {
                    System.out.println("#Transaction(" + t + ") output 'change' is not sender.");
                    return false;
                }
            }
        }

        System.out.println("Blockchain is valid");
        return true;
    }
}
