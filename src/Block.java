import java.util.Date;

public class Block
{
    public String hash;
    public String previousHash;
    // Let's say our data is stored in as a simple string
    private String m_data;
    private long m_timeStamp;


    // Block Constructor
    public Block(String _data, String _previousHash)
    {
        this.m_data = _data;
        this.previousHash = _previousHash;
        this.m_timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    public String calculateHash()
    {
        return StringUtils.applySHA256(previousHash + Long.toString(m_timeStamp) + m_data);
    }
}
