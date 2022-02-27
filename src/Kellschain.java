public class Kellschain
{

    public static void main(String[] args)
    {
        Block genesisBlock = new Block(" Hi I am the first block in the chain", "0");
        System.out.println("Hash for Block 1: " + genesisBlock.hash);

        Block secondBlock = new Block(" Hi I am the second block in the chain", genesisBlock.hash);
        System.out.println("Hash for Block 2: " + secondBlock.hash);

        Block thirdBlock = new Block(" Hi I am the third block in the chain", secondBlock.hash);
        System.out.println("Hash for Block 3: " + thirdBlock.hash);
    }
}
