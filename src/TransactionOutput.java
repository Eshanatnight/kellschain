import java.security.PublicKey;

public class TransactionOutput
{
    public String id;

    // Also the new owner of these coins.
    public PublicKey recipient;

    // Value of the coin
    public float value;
    // The id of the transaction this output was created in
    public String parentTransactionID;

    // Constructor
    public TransactionOutput(PublicKey recipient, float value, String parentTransactionID)
    {
        this.recipient = recipient;
        this.value = value;
        this.parentTransactionID = parentTransactionID;
        this.id = StringUtils.applySHA256(StringUtils.getStringFromKey(recipient) +
                Float.toString(value) +
                parentTransactionID
        );
    }


    // Check if the coins belongs to You
    public boolean isMine(PublicKey publicKey)
    {
        return (publicKey == recipient);
    }
}
