import java.util.ArrayList;
public class Move {
    private final Positions oldPosition;
    private final Positions newPosition;
    private final Board board;
    private final Piece piece;
    private Piece capturedPiece;
    public Move(Positions oldPosition, Positions newPosition, Board board ) {
        this.oldPosition = oldPosition;
        this.newPosition = newPosition;
        this.board = board;
        this.piece = board.getPieceAtPosition(oldPosition);
    }
    public void makeMove(){
        this.capturedPiece = board.getPieceAtPosition(newPosition);
        board.setPieceAtPosition(newPosition, piece);
        board.setPieceAtPosition(oldPosition, null);

    }
    public void undoMove() {
        board.setPieceAtPosition(oldPosition, piece);
        board.setPieceAtPosition(newPosition, capturedPiece);
    }
    public Piece getCapturedPiece(){
        return capturedPiece;
    }
    public Piece getLastPiece(){
        return piece;
    }
    public Positions getLastOldPos(){
        return oldPosition;
    }
    public Positions getLastNewPos(){
        return newPosition;
    }
    @Override
    public String toString(){
        return getLastPiece().getColor()+ String.valueOf(getLastPiece())+ " " + getLastOldPos() + " " + getLastNewPos() + ";";
    }
}
