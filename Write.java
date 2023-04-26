import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class Write {
    public void write(String filename, Board board){
        try(OutputStream os = new FileOutputStream(filename)) {
            Piece[][] pieces = board.getFigures();
            for(int i =0; i<pieces.length; i++){
                for (int j =0; j<pieces[i].length; j++){
                    Piece piece = pieces[i][j];

                    if(piece!=null){
                        int pieceType = translateTypes(piece);
                        int horizontalY = j+1;
                        int verticalX = i+1;
                        int color = piece.getColor() == Color.W? 1:0;

                        int value = pieceType | horizontalY<<3 | verticalX << 7 | color<<11;
                        for(int x = 1; x>=0; x--){
                            os.write((int)( value>>x*8));
                        }
                    }
                }
            }
            ArrayList<Piece> deletedPieces = board.getPiecesThatAreNotOnTheBoard();
            for(int i = 0; i<deletedPieces.size(); i++){
                if(deletedPieces.get(i)!=null){
                    Piece piece = deletedPieces.get(i);
                    int pieceType = translateTypes(piece);
                    int horizontalY = 0;
                    int verticalX = 0;
                    int color = piece.getColor() == Color.W? 1:0;

                    int value = pieceType | horizontalY<<3 | verticalX << 7 | color<<11;
                    for(int x = 1; x>=0; x--){
                        os.write((int)( value>>x*8));
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }
    public int translateTypes(Piece piece){
        return switch (piece.getPieceType()){
            case Kg -> 1;
            case Q ->  2;
            case R ->  3;
            case B -> 4;
            case Kn ->  5;
            case P ->  0;
        };
    }

}
