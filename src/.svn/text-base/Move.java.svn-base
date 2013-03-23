/**
 * Representation of a Move in Network.  Each move is either a PLACE_PIECE move or a
 * MOVE_PIECE move.  If it is a PLACE_PIECE move, then the toRow and toCol fields are the 
 * row and column to place the piece, and the fromRow and fromCol fields are ignored.
 * If this is a MOVE_PIECE move, then fromRow and fromCol are the row and column to
 * move the piece from, and toRow and toCol are the row and column to move the piece to
 */

public class Move
{
    public enum MoveType {PLACE_PIECE, MOVE_PIECE};
    
    public char toRow;
    public int toCol;
    
    public char fromRow;
    public int fromCol;
    
    public MoveType moveType;
    
  
    public Move(char row, int col)
    {
        moveType = MoveType.PLACE_PIECE;
        toRow = row;
        toCol = col;
    }
    
    public Move(char fromRow, int fromCol, char toRow, int toCol)
    {
        moveType = MoveType.MOVE_PIECE;
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
    }    
    
}