import java.util.ArrayList;

public class History {
    private final ArrayList<Move> moves;
    public History(){
        this.moves = new ArrayList<>();
    }
    public void addMove(Move move){
        moves.add(move);
    }
    public Move getLastMove(){
        return moves.get(moves.size()-1);
    }
    public ArrayList<Move> getMoves(){
        return  moves;
    }

}
