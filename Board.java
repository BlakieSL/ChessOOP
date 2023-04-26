import java.util.ArrayList;
import java.util.Arrays;

class Board {
    private final Piece[][] figures;
    private final ArrayList<Piece> piecesThatAreNotOnTheBoard;
    Board() {
        this.figures = new Piece[8][8];
        this.piecesThatAreNotOnTheBoard = new ArrayList<>();
    }
    public void createBoard() {
        for (int i = 0; i < 8; i++) {
            figures[1][i] = new Pawn(Color.B);
            figures[6][i] = new Pawn(Color.W);
        }
        figures[0][0] = new Rook(Color.B);
        figures[0][1] = new Knight(Color.B);
        figures[0][2] = new Bishop(Color.B);
        figures[0][3] = new Queen(Color.B);
        figures[0][4] = new King(Color.B);
        figures[0][5] = new Bishop(Color.B);
        figures[0][6] = new Knight(Color.B);
        figures[0][7] = new Rook(Color.B);

        figures[7][0] = new Rook(Color.W);
        figures[7][1] = new Knight(Color.W);
        figures[7][2] = new Bishop(Color.W);
        figures[7][3] = new Queen(Color.W);
        figures[7][4] = new King(Color.W);
        figures[7][5] = new Bishop(Color.W);
        figures[7][6] = new Knight(Color.W);
        figures[7][7] = new Rook(Color.W);



    }
    public void createBoardFromFile(String  filename) {
        for (Piece[] figure : figures) {
            Arrays.fill(figure, null);// clean board/array contains nulls
        }
        Read reader = new Read();

        reader.read(filename, this);

    }
    public void saveBoardToFile(String filename){
        Write write = new Write();
        write.write(filename, this);
    }
    public Piece getPieceAtPosition(Positions position) {
        if(isValidPosition(position.x(),position.y())) {
            return figures[position.x()][position.y()];
        }
        return  null;
    }
    public Positions getPosition(Piece piece) {
        if (piece == null) {
            throw new IllegalArgumentException("there is no such piece on the board");
        }
        for (int i = 0; i < figures.length; i++) {
            for (int j = 0; j < figures[i].length; j++) {
                if (figures[i][j] == piece) {
                    return new Positions(i, j);
                }
            }
        }
        throw new IllegalArgumentException("there is no such piece on the board");
    }
    public Piece[][] getFigures(){
        return figures;
    }
    public void setPieceAtPosition(Positions position, Piece piece) {
        figures[position.x()][position.y()] = piece;
    }
    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < figures.length && y >= 0 && y < figures[1].length;
    }
    public King getKingOnTheBoard(Color color) {
        return (King) Arrays.stream(figures).
                flatMap(Arrays::stream).
                filter(piece -> piece instanceof King && piece.getColor() == color).
                findFirst().
                orElse(null);
    }
    public void addPiecesThatAreNot(Piece piece){
        piecesThatAreNotOnTheBoard.add(piece);
    }
    public ArrayList<Piece> getPiecesThatAreNotOnTheBoard(){
        return piecesThatAreNotOnTheBoard;
    }
}
