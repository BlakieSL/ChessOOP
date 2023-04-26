import java.util.ArrayList;

enum PieceType{
    Kg,
    Q,
    P,
    Kn,
    B,
    R;
}
enum Color{
    W,
    B;
}
abstract class Piece{
    private final Color color;
    private final PieceType pieceType;
    Piece(Color color, PieceType pieceType){
        this.color = color;
        this.pieceType = pieceType;
    }
    public Color getColor(){
        return color;
    }
    public PieceType getPieceType(){
        return pieceType;
    }
    @Override
    public String toString(){
        return pieceType + " ";
    }
    public abstract boolean isLegal(Positions start, Positions end, Board board, History history);
    public boolean verticalLegal(Positions start, Positions end, Board board){
        int startX = start.x();
        int startY = start.y();
        int endX = end.x();
        int endY = end.y();

        if (startY == endY) {
            checkForPiecesBetween(start, end, board);
            return board.getPieceAtPosition(end) == null || board.getPieceAtPosition(end).getColor() != getColor();
        }
        return false;
    }
    public boolean horizontalLegal(Positions start, Positions end, Board board ){
        int startX = start.x();
        int startY = start.y();
        int endX = end.x();
        int endY = end.y();
        if (startX == endX) {
            checkForPiecesBetween(start, end, board);
            return board.getPieceAtPosition(end) == null || board.getPieceAtPosition(end).getColor() != getColor();
        }
        return false;
    }
    public boolean diagonal(Positions start, Positions end, Board board){
        int startX = start.x();
        int startY = start.y();
        int endX = end.x();
        int endY = end.y();
        if (Math.abs(startX - endX) == Math.abs(startY - endY)) {
            int xStep = (startX < endX) ? 1 : -1;
            int yStep = (startY < endY) ? 1 : -1;
            int i = startX + xStep;
            int j = startY + yStep;
            while (i != endX && j != endY) {
                if (board.getPieceAtPosition(new Positions(i, j)) != null) {
                    return false;
                }
                i += xStep;
                j += yStep;
            }
            return board.getPieceAtPosition(end) == null || board.getPieceAtPosition(end).getColor() != getColor();
        }
        return false;
    }
    public boolean checkForPiecesBetween(Positions start, Positions end, Board board){
        int startX = start.x();
        int startY = start.y();
        int endX = end.x();
        int endY = end.y();
        int step = (startY < endY) ? 1 : -1;
        for (int i = startY + step; i != endY; i += step) {
            if (board.getPieceAtPosition(new Positions(startX, i)) != null) {
                return false;
            }
        }
        return true;
    }
}
class Pawn extends Piece {
    Pawn(Color color) {
        super(color, PieceType.P);

    }
    public boolean isLegal(Positions start, Positions end, Board board, History history) {
        int startX = start.x();
        int startY = start.y();
        int endX = end.x();
        int endY = end.y();

        if (!board.isValidPosition(endX, endY)) {
            return false;
        }


        if (getColor() == Color.B) {
            if (startX == 1 && endX == 3 && startY == endY && board.getPieceAtPosition(new Positions(2, startY)) == null && board.getPieceAtPosition(end) ==null) {
                return true;//move from beginning two positions forward
            } else if (endX == startX + 1 && startY == endY && board.getPieceAtPosition(end) == null) {
                return true;//default move one position forward
            } else if (endX == startX + 1 && Math.abs(startY - endY)==1 && board.getPieceAtPosition(end) != null && board.getPieceAtPosition(end).getColor() != getColor()) {
                return true;//to attack diagonally
            }else if(startX == 4 && endX == 5 && Math.abs(startY - endY)==1 && board.getPieceAtPosition(end) == null) {
                Move lastMove = history.getLastMove();
                Piece lastPiece = lastMove.getLastPiece();
                Positions lastOldPos = lastMove.getLastOldPos();
                Positions lastNewPos = lastMove.getLastNewPos();
                return lastPiece != null && lastPiece.getPieceType() == PieceType.P &&
                        lastOldPos.x() == 6 && lastNewPos.x() == 4 && lastOldPos.y() == lastNewPos.y();//en passant
            }
        } else {
            if (startX == 6 && endX == 4 && startY == endY && board.getPieceAtPosition(new Positions(5, startY)) == null && board.getPieceAtPosition(end) == null) {
                return true;
            } else if (endX == startX - 1 && startY == endY && board.getPieceAtPosition(end) == null) {
                return true;
            } else if (endX == startX - 1 && Math.abs(startY - endY)==1 && board.getPieceAtPosition(end) != null && board.getPieceAtPosition(end).getColor() != getColor()) {
                return true;
            }else if(startX == 3 && endX == 2 && Math.abs(startY - endY)==1 && board.getPieceAtPosition(end) == null) {
                Move lastMove = history.getLastMove();
                Piece lastPiece = lastMove.getLastPiece();
                Positions lastOldPos = lastMove.getLastOldPos();
                Positions lastNewPos = lastMove.getLastNewPos();
                return lastPiece != null && lastPiece.getPieceType() == PieceType.P &&
                        lastOldPos.x() == 1 && lastNewPos.x() == 3 && lastOldPos.y() == lastNewPos.y();
            }
        }

        return false;
    }
}
class King extends Piece{
    King(Color color){
        super(color, PieceType.Kg);
    }
    public boolean isLegal(Positions start, Positions end, Board board, History history) {
        int startX = start.x();
        int startY = start.y();
        int endX = end.x();
        int endY = end.y();


        if ((Math.abs(endX - startX) <= 1) && (Math.abs(endY - startY) <= 1)) {
            return board.getPieceAtPosition(end) == null || board.getPieceAtPosition(end).getColor() != getColor();
        }
        return false;
    }
}
class Queen extends Piece{
    Queen(Color color){
        super(color, PieceType.Q);
    }
    public boolean isLegal(Positions start, Positions end, Board board, History history) {

        return verticalLegal(start, end, board)||horizontalLegal(start, end, board)|| diagonal(start, end, board);
    }
}
class Bishop extends Piece{
    Bishop(Color color){
        super(color, PieceType.B);
    }
    public boolean isLegal(Positions start, Positions end, Board board, History history) {
        return diagonal(start, end, board);
    }

}
class Knight extends Piece{
    Knight(Color color){
        super(color, PieceType.Kn);
    }
    public boolean isLegal(Positions start, Positions end, Board board,  History history) {
        int startX = start.x();
        int startY = start.y();
        int endX = end.x();
        int endY = end.y();


        int dx = Math.abs(startX - endX);
        int dy = Math.abs(startY - endY);
        if (!((dx == 1 && dy == 2) || (dx == 2 && dy == 1))) {
            return false;
        }


        return board.getPieceAtPosition(end) == null || board.getPieceAtPosition(end).getColor() != getColor();
    }
}
class Rook extends Piece{
    Rook(Color color){
        super(color, PieceType.R);
    }
    public boolean isLegal( Positions start, Positions end, Board board,  History history) {
        return verticalLegal(start, end, board)||horizontalLegal(start, end, board);
    }
}
