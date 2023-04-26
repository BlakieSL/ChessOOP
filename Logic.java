
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.stream.IntStream;

public class Logic {
    private final Board board;
    private Player currentPlayer;
    private final History history;
    public Logic(Player currentPlayer, Board board, History history){
        this.board = board;
        this.currentPlayer = currentPlayer;
        this.history = history;
    }
    public void setCurrentPlayer(Player player){
        this.currentPlayer = player;
    }
    public Player getCurrentPlayer(){
        return currentPlayer;
    }
    public boolean check() {
        King king = board.getKingOnTheBoard(currentPlayer.color());
        return IntStream.range(0, 8)
                .boxed()
                .flatMap(i -> IntStream.range(0, 8)
                        .mapToObj(j -> new Positions(i, j))
                        .filter(position -> {
                            Piece piece = board.getPieceAtPosition(position);
                            return piece != null && piece.getColor() == currentPlayer.getOpponent().color()
                                    && piece.isLegal(position, board.getPosition(king), board, history);
                        })
                        .findFirst().stream())
                .findAny()
                .isPresent();
    }
    public boolean checkmate() {
        if (!check()) {
            return false;
        }
        King king = board.getKingOnTheBoard(currentPlayer.color());
        Positions kingPosition = board.getPosition(king);

        for (int i =0; i<8; i++){
            for(int j =0; j<8; j++){
                Positions newpos = new Positions(i,j);
                if(king.isLegal(kingPosition,newpos, board, history)){
                    Move move = new Move(kingPosition, newpos, board);
                    move.makeMove();
                    if(!check()){
                        move.undoMove();
                        System.out.println("No checkmate! Possible move for king: " + king.getPieceType() + " " + king.getColor() + " " + kingPosition.x() +" " + kingPosition.y()
                                + " " + newpos.x() + " " + newpos.y());
                        return false;
                    }
                    move.undoMove();
                }
            }
        }
        for(int i = 0; i<8; i++){
            for(int j = 0; j<8; j++){
                Positions oldpos = new Positions(i, j);
                Piece piece = board.getPieceAtPosition(oldpos);
                if ( piece != null && piece.getColor() == currentPlayer.color()) {
                    for (int x = 0; x < 8; x++) {
                        for (int y = 0; y < 8; y++) {
                            Positions newpos = new Positions(x, y);
                            if (piece.isLegal(oldpos, newpos, board,history) ){
                                Move move = new Move(oldpos, newpos, board);
                                move.makeMove();
                                if(!check()){
                                    move.undoMove();
                                    System.out.println("No checkmate! Possible move for other pieces to protect king: " + piece.getPieceType() + " " + piece.getColor() + " " + oldpos.x() +" " + oldpos.y()
                                            + " " + newpos.x() + " " + newpos.y());
                                    return false;
                                }
                                move.undoMove();
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
    /*public boolean stalemate(){
        if(check()){
            return false;
        }
        for(int i=0; i<8; i++){
            for(int j = 0; j<8; j++){
                Positions oldpos = new Positions(i, j);
                Piece piece = board.getPieceAtPosition(oldpos);

                if(piece != null && piece.getColor() == currentPlayer.color()){
                    for(int x =0; x<8; x++){
                        for(int y =0; y<8; y++){
                            Positions newpos = new Positions(x, y);

                            if(piece.isLegal(oldpos, newpos, board, history)){

                                Move move = new Move(oldpos,newpos, board);
                                move.makeMove();
                                if(!check()){

                                    move.undoMove();
                                    System.out.println("No stalemate! Possible move: " + piece.getPieceType() + " " + piece.getColor() + " " + oldpos.x() +" " + oldpos.y()
                                            + " " + newpos.x() + " " + newpos.y());
                                    return false;
                                }
                                move.undoMove();
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

     */
    public boolean draw(int a){
        return switch (a) {
            case 1 -> true;
            case 2 -> false;
            default -> throw new InputMismatchException();
        };
    }
    public void enPassantDeleting(){
        ArrayList<Move> moves = history.getMoves();
        if(moves.size()>2) {
            Move move1 = moves.get(moves.size() - 1);
            Piece piece1 = move1.getLastPiece();
            Positions oldPos1 = move1.getLastOldPos();
            Positions newPos1 = move1.getLastNewPos();

            Move move2 = moves.get(moves.size() - 2);
            Piece piece2 = move2.getLastPiece();
            Positions oldPos2 = move2.getLastOldPos();
            Positions newPos2 = move2.getLastNewPos();

            if (piece2.getPieceType() == PieceType.P && piece1.getPieceType() == PieceType.P) {
                if(piece1.getColor() == Color.W) {
                    System.out.println();
                    if (oldPos2.x() == 1 && newPos2.x() == 3 && oldPos2.y() == newPos2.y()
                            && oldPos1.x() == 3 && newPos1.x() == 2 && Math.abs(oldPos1.y() - newPos1.y()) == 1
                            && newPos1.y() == oldPos2.y()) {
                        board.setPieceAtPosition(newPos2, null);
                        System.out.println("EN PASSANT DELETING, PAWN AT " + newPos2.x() + " " + newPos2.y() );
                    }
                }else if(piece1.getColor() == Color.B){
                    if(oldPos2.x() == 6 && newPos2.x() == 4 && oldPos2.y() == newPos2.y()
                            && oldPos1.x() == 4 && newPos1.x() == 5 && Math.abs(oldPos1.y() - newPos1.y())==1
                            && newPos1.y() == oldPos2.y()){
                        board.setPieceAtPosition(newPos2, null);
                        System.out.println("EN PASSANT DELETING, PAWN AT " + newPos2.x() + " " + newPos2.y() );
                    }
                }
            }
        }
    }
    public boolean promotionLegal(Positions positions){
        Piece piece = board.getPieceAtPosition(positions);
        if(piece.getPieceType() != PieceType.P){
            return false;
        }
        return (piece.getColor() == Color.W && positions.x() == 0)||
                (piece.getColor() == Color.B && positions.x() == 7);
    }
    public void setPromotedPiece(Positions positions, int a){
        Piece piece = null;
        switch (a){
            case 1 -> piece = new Queen(currentPlayer.color());
            case 2 -> piece = new Rook(currentPlayer.color());
            case 3 -> piece = new Bishop(currentPlayer.color());
            case 4 -> piece = new Knight(currentPlayer.color());
        }
        board.setPieceAtPosition(positions, piece);
    }
    public boolean longCastlingLegal(){
        ArrayList<Move> moves = history.getMoves();
        Piece king = board.getKingOnTheBoard(currentPlayer.color());
        Positions kingpos = board.getPosition(king);
        int a = currentPlayer.color() == Color.W? 7:0;//temp val to check if king or rook is in correct row

        if(kingpos.x() != a || kingpos.y() != 4 ){
            return false;// if king isn't on start pos
        }

        Piece lrook = board.getPieceAtPosition(new Positions(kingpos.x(), 0));
        if(lrook == null || lrook.getColor()!=currentPlayer.color()){
            return false;//if there is no rook on position or if it is not currentPlayer color
        }

        for(int i =0; i<moves.size(); i++){
            Positions oldpos = moves.get(i).getLastOldPos();
            if(( oldpos.x() == a && oldpos.y() ==4) ||
                    (oldpos.x() == a && oldpos.y()==0 )){

                return false;//check if there were moves from king position or left rook
            }
        }
        for(int i = 1; i<4; i++){
            if(board.getPieceAtPosition(new Positions(kingpos.x(), i)) != null){
                return false;//check if there are no figures between rook and king
            }
        }

        if(check()){
            return false;//can't do castling while check
        }
        for(int i = 3; i>1; i--) {
            Move move = new Move(kingpos,new Positions(kingpos.x(), i), board);
            move.makeMove();
            if(check()){
                move.undoMove();
                return false;//check if there are no check on king's way
            }
            move.undoMove();
        }

        return true;
    }
    public boolean shortCastlingLegal(){
        ArrayList<Move> moves = history.getMoves();
        Piece king = board.getKingOnTheBoard(currentPlayer.color());
        Positions kingpos = board.getPosition(king);
        int a = currentPlayer.color() == Color.W? 7:0;//temp val to check if king or rook is in correct row

        if(kingpos.x() != a || kingpos.y() != 4 ){
            return false;// if king isn't on start pos
        }

        Piece lrook = board.getPieceAtPosition(new Positions(kingpos.x(), 7));
        if(lrook == null || lrook.getColor()!=currentPlayer.color()){
            return false;//if there is no rook on position or if it is not currentPlayer color
        }

        for(int i =0; i<moves.size(); i++){
            Positions oldpos = moves.get(i).getLastOldPos();
            if(( oldpos.x() == a && oldpos.y() ==4) ||
                    (oldpos.x() == a && oldpos.y()==7 )){

                return false;//check if there were moves from king position or left rook
            }
        }
        for(int i = 5; i<7; i++){
            if(board.getPieceAtPosition(new Positions(kingpos.x(), i)) != null){
                return false;//check if there are no figures between rook and king
            }
        }
        if(check()){
            return false;//can't do castling while check
        }
        for(int i = 5; i<7; i++) {
            Move move = new Move(kingpos,new Positions(kingpos.x(), i), board);
            move.makeMove();
            if(check()){
                move.undoMove();
                return false;//check if there are no check on king's way
            }
            move.undoMove();
        }

        return true;
    }
    public void setLongCastling(){
        int a = currentPlayer.color() == Color.W? 7:0;
        board.setPieceAtPosition(new Positions(a,2), new King(currentPlayer.color()));
        board.setPieceAtPosition(new Positions(a,3), new Rook(currentPlayer.color()));
        board.setPieceAtPosition(new Positions(a,0),null);
        board.setPieceAtPosition(new Positions(a,4), null);
    }
    public void  setShortCastling(){
        int a = currentPlayer.color() == Color.W? 7:0;
        board.setPieceAtPosition(new Positions(a,6), new King(currentPlayer.color()));
        board.setPieceAtPosition(new Positions(a,5), new Rook(currentPlayer.color()));
        board.setPieceAtPosition(new Positions(a,7),null);
        board.setPieceAtPosition(new Positions(a,4), null);
    }
}
