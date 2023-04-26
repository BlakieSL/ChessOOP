import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
public class Read {
    public void read(String filename, Board board){
        try(InputStream is = new FileInputStream(filename)){
            while (true) {
                int value = 0;
                for (int x = 0; x < 2; x++) {
                    int a = is.read();
                    if (a == -1) {
                        return;
                    }
                    value = (value << 8) | a;
                }

                int pieceType = (value) & 0b111;
                int horizontalY = (value >> 3) & 0b1111;
                int verticalX = (value >> 7) & 0b1111;
                //System.out.println(verticalX + " " + horizontalY);
                int color = (value >> 11) & 0b1;
                Color color1 = (color == 1) ? Color.W : Color.B;

                if(horizontalY == 0 && verticalX ==0){
                    board.addPiecesThatAreNot(createPieceOnEnumType(pieceType, color1));
                }
                else{
                    Piece piece = createPieceOnEnumType(pieceType, color1);
                    Positions positions = new Positions(converterCoordinates(verticalX), converterCoordinates(horizontalY));
                    board.setPieceAtPosition(positions, piece);
                }
            }
        } catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }
    public Piece createPieceOnEnumType(int a, Color color){
        return switch (a){
            case 1 -> new King(color);
            case 2 ->  new Queen(color);
            case 3 -> new Rook(color);
            case 4 -> new Bishop(color);
            case 5 ->  new Knight(color);
            case 0 ->  new Pawn(color);
            default -> throw new IllegalStateException("Unexpected value12321321: " + a);
        };
    }
    public int converterCoordinates(int a ){
        return a-1;
    }
}
