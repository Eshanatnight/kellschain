import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Block
{
    public String hash;
    public String previousHash;
    public String merkleRoot;
    // Say our data is a simple String
    public ArrayList<Transaction> transactions = new ArrayList<>();
    private long m_timeStamp;
    private int m_nonce;


    // Block Constructor
    public Block(String _previousHash)
    {
        this.previousHash = _previousHash;
        this.m_timeStamp = new Date().getTime();

        //Making sure we do this after we set the other values.
        this.hash = calculateHash();
    }


    public String calculateHash()
    {
        return StringUtils.applySHA256(previousHash + Long.toString(m_timeStamp) +
                Integer.toString(m_nonce) +
                merkleRoot);
    }


    public void mineBlock(int _difficulty)
    {
        merkleRoot = StringUtils.getMerkleRoot(transactions);

        //Create a string with difficulty * "0"
        String target = StringUtils.getDifficultyString(_difficulty);
        // new String(new char [_difficulty]).replace('\0', '0');

        while(!hash.substring(0, _difficulty).equals(target))
        {
            m_nonce++;
            hash = calculateHash();
        }

        System.out.println("Block Mined!!! : " + hash);
    }


    // Add Transactions to this block
    public boolean addTransaction(Transaction _transaction)
    {
        // process transaction and check if valid, unless block is genesis block then ignore.

        if(_transaction == null)
            return false;

        if(!Objects.equals(previousHash, "0"))
        {
            if(!_transaction.processTransaction())
            {
                System.out.println("Transaction failed to process. Discarded.");
                return false;
            }
        }

        transactions.add(_transaction);
        System.out.println("Transaction Successfully added to Block");
        return true;
    }



}
