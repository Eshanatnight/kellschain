public class TransactionInput
{
    // References TransactionOutputs -> TransactionID
    public String transactionOutputID;

    // Contains the unspent transaction output
    public TransactionOutput UTXO;


    // Constructor
    public TransactionInput(String transactionOutputID)
    {
        this.transactionOutputID = transactionOutputID;
    }
}
