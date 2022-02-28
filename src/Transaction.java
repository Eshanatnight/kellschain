import java.security.*;
import java.util.ArrayList;

public class Transaction
{
    // Also the hash  of the transaction.
    public String transactionID;
    // Sender address (public key)
    public PublicKey sender;
    // Recipient address (public key)
    public PublicKey reciepient;
    public float value;
    // This is to prevent anybody else from spending funds in our wallet
    public byte[] signature;

    public ArrayList<TransactionInput> inputs = new ArrayList<>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<>();

    // a rough count of how many transactions have been generated.
    private static int sequence = 0;


    // Constructor
    public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs)
    {
        this.sender = from;
        this.reciepient = to;
        this.value = value;
        this.inputs = inputs;
    }


    // Calculates the transaction hash which is used as it's ID
    public String calculateHash()
    {
        sequence++;
        return StringUtils.applySHA256(
                StringUtils.getStringFromKey(sender) +
                        StringUtils.getStringFromKey(reciepient) +
                        Float.toString(value) + sequence
        );
    }


    // Sign all the data that we do not wish to be tampered with
    public void generateSignature(PrivateKey privatekey)
    {
        var data = StringUtils.getStringFromKey(sender) +
                StringUtils.getStringFromKey(reciepient) +
                Float.toString(value);

        signature = StringUtils.applyECSDASignature(privatekey, data);
    }


    // Verifies the data we signed has not been tampered with
    public boolean verifySignature()
    {
        var data = StringUtils.getStringFromKey(sender) +
                StringUtils.getStringFromKey(reciepient) +
                Float.toString(value);

        return StringUtils.verifyECSDASignature(sender, data, signature);
    }


    // Return true if the transaction is created
    public boolean processTransaction()
    {
        if (!verifySignature())
        {
            System.err.println("#Transaction Signature failed to verify");
            return false;
        }

        for(TransactionInput i : inputs)
        {
            i.UTXO = Kellschain.UTXOs.get(i.transactionOutputID);
        }

        // Generate the transaction outputs
        //get value of inputs then the left over change:
        float leftOver = getInputsValue() - value;
        transactionID = calculateHash();

        //send value to recipient
        outputs.add(new TransactionOutput(this.reciepient, value, transactionID));
        //send the left over 'change' back to sender
        outputs.add(new TransactionOutput(this.sender, leftOver, transactionID));

        //add outputs to Unspent list
        for(TransactionOutput o : outputs)
        {
            Kellschain.UTXOs.put(o.id , o);
        }

        //remove transaction inputs from UTXO lists as spent:
        for(TransactionInput i : inputs)
        {
            //if Transaction can't be found skip it
            if(i.UTXO == null)
                continue;
            Kellschain.UTXOs.remove(i.UTXO.id);
        }

        return true;
    }


    // returns sum of inputs(UTXOs) values
    public float getInputsValue()
    {
        float total = 0;
        for(TransactionInput i : inputs)
        {
            //if Transaction can't be found skip it
            if(i.UTXO == null)
                continue;

            total += i.UTXO.value;
        }

        return total;
    }


    // returns sum of outputs:
    public float getOutputsValue()
    {
        float total = 0;
        for(TransactionOutput o : outputs)
        {
            total += o.value;
        }

        return total;
    }
}
