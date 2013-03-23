
public abstract class Player
{
    protected int playerNum;
    
    public Player(int playerNum)
    {
        this.playerNum = playerNum;
    }
    
    public abstract Move getMove();
    public abstract void OpponentMove(Move m);
    
    public abstract void printBoard();
    public abstract boolean hasWon(int playerNum);

}