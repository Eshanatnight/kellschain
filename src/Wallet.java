import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Wallet
{
    public PrivateKey privateKey;
    public PublicKey publicKey;

    //only UTXOs owned by this wallet.
    public HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();


    public Wallet()
    {
        generateKeys();
    }


    public void generateKeys()
    {
        try
        {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");

            // Initialize the key generator and generate a KeyPair
            // 256 bytes provides an acceptable security level
            keyGen.initialize(ecSpec, random);
            KeyPair keyPair = keyGen.genKeyPair();

            // Set the public and private keys from the keyPair
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        }

        catch (Exception e)
        {
            ErrorHandle.handleException(e);
        }
    }


    //returns balance and stores the UTXO's owned by this wallet in this.UTXOs
    public float getBalance() {
        float total = 0;
        for (Map.Entry<String, TransactionOutput> item: Kellschain.UTXOs.entrySet()){
            TransactionOutput UTXO = item.getValue();
            if(UTXO.isMine(publicKey))
            {
                // if output belongs to me ( if coins belong to me )
                // add it to our list of unspent transactions.
                UTXOs.put(UTXO.id,UTXO);
                total += UTXO.value ;
            }
        }
        return total;
    }


    //Generates and returns a new transaction from this wallet.
    public Transaction sendFunds(PublicKey _recipient, float value )
    {
        if(getBalance() < value)
        {
            //gather balance and check funds.
            System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
            return null;
        }

        //create array list of inputs
        ArrayList<TransactionInput> inputs = new ArrayList<>();

        float total = 0;
        for (Map.Entry<String, TransactionOutput> item: UTXOs.entrySet())
        {
            TransactionOutput UTXO = item.getValue();
            total += UTXO.value;
            inputs.add(new TransactionInput(UTXO.id));
            if(total > value)
                break;
        }

        Transaction newTransaction = new Transaction(publicKey, _recipient , value, inputs);
        newTransaction.generateSignature(privateKey);

        for(TransactionInput input: inputs)
        {
            UTXOs.remove(input.transactionOutputID);
        }

        return newTransaction;
    }
}
