import java.util.*;

class Game implements Displayer{
    Scanner scanner = new Scanner(System.in);
    private final Board board;
    private final Logic logic;
    private Piece[][] pieces;
    private final ArrayList<Move> moves;
    private final History history;
    public Game(Player playerwhite) {

        board = new Board();
        board.createBoard();
        pieces = board.getFigures();

        history = new History();
        moves = history.getMoves();
        logic = new Logic(playerwhite, board, history);


    }
    public void play()  {
        int a = 2;
        while (!logic.checkmate() && !logic.draw(a)) {
            displayboard();
            menuInteraction();
            System.out.println("Want draw? Press 1, press 2 to continue game");
            a = scanner.nextInt();
            scanner.nextLine();
        }
        System.out.println( logic.checkmate()?  "Checkmate: " + logic.getCurrentPlayer().color() + " lose ":  " ");
        System.out.println(logic.draw(a)? "Draw": " ");
        displayboard();
        displayHistoryOfMoves();
    }
    public void apply1(){
        boolean flag = false;
        do{
            try {
                if (logic.longCastlingLegal() && logic.shortCastlingLegal()) {
                    System.out.println("You can make castling: 1 for regular move, 2 for long, 3 for short");
                    int a = scanner.nextInt();
                    scanner.nextLine();
                    switch (a) {
                        case 1 -> {
                            regularInteraction();
                            flag = true;
                        }
                        case 2 -> {
                            logic.setLongCastling();
                            logic.setCurrentPlayer(logic.getCurrentPlayer().getOpponent());
                            flag = true;
                        }
                        case 3 -> {
                            logic.setShortCastling();
                            logic.setCurrentPlayer(logic.getCurrentPlayer().getOpponent());
                            flag = true;
                        }
                    }

                }
                else if (logic.shortCastlingLegal()) {
                    System.out.println("You can make a short castling: 1 for regular move, 2 for castling");
                    int a = scanner.nextInt();
                    scanner.nextLine();
                    switch (a) {
                        case 1 -> {
                            regularInteraction();
                            flag = true;
                        }
                        case 2 -> {
                            logic.setShortCastling();
                            logic.setCurrentPlayer(logic.getCurrentPlayer().getOpponent());
                            flag = true;
                        }
                    }
                }
                else if (logic.longCastlingLegal()) {
                    System.out.println("You can make a long castling: 1 for regular move, 2 for castling");
                    int a = scanner.nextInt();
                    scanner.nextLine();
                    switch (a) {
                        case 1 -> {
                            regularInteraction();
                            flag = true;
                        }
                        case 2 -> {
                            logic.setLongCastling();
                            logic.setCurrentPlayer(logic.getCurrentPlayer().getOpponent());
                            flag = true;
                        }
                    }
                }
                else{
                    regularInteraction();
                    flag = true;
                }
            }
            catch (InputMismatchException  e){
                System.out.println("Invalid input!");
                scanner.nextLine();
            }
        }while (!flag);

    }
    public void regularInteraction(){
        boolean flag = false;
        do{
            try {
                System.out.println("Choose coordinates of piece, first a-h, then 1-8: ");
                String e = scanner.nextLine();
                int oldy = Calculator.translateSymbol(e);
                int x = scanner.nextInt();
                int oldx = Calculator.translateNum(x);
                scanner.nextLine();

                System.out.println("Choose coordinates to move the piece, first a-h, then 1-8: ");
                String e1 = scanner.nextLine();
                int newy =Calculator.translateSymbol(e1);
                int x1 = scanner.nextInt();
                int newx = Calculator.translateNum(x1);
                scanner.nextLine();

                Positions oldP = new Positions(oldx, oldy);
                Positions newP = new Positions(newx, newy);
                Piece piece = board.getPieceAtPosition(oldP);

                if(piece != null && piece.isLegal(oldP, newP, board, history) && piece.getColor() == logic.getCurrentPlayer().color()){
                    int counter = 0;
                    Move move = new Move(oldP, newP, board);
                    move.makeMove();
                    if(logic.check()){
                        move.undoMove();
                        System.out.println("You cannot move to the check! ");
                        counter++;
                    }
                    if(counter == 0){
                        history.addMove(move);
                        board.addPiecesThatAreNot(move.getCapturedPiece());
                        logic.enPassantDeleting();

                        if(logic.promotionLegal(newP)){
                            System.out.println("You can promote pawn, 1 for queen, 2 for rook, 3 for bishop, 4 for knight: ");
                            int a = scanner.nextInt();
                            logic.setPromotedPiece(newP, a);
                            scanner.nextLine();
                        }
                        logic.setCurrentPlayer(logic.getCurrentPlayer().getOpponent());
                        flag = true;
                    }

                }else {
                    System.out.println("Invalid move!");
                }
            }catch (InputMismatchException e){
                System.out.println("Invalid input!");
            }

        }while (!flag);
    }
    public void menuInteraction(){
        boolean flag = false;
        do {
            try {
                System.out.println("Press 1 to play, press 2 to save game, press 3 to load game: ");
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1 -> {
                        apply1();
                        flag = true;
                    }
                    case 2 -> {
                        System.out.println("Enter file name: ");
                        String filenameW = scanner.nextLine();
                        board.saveBoardToFile(filenameW);
                        flag = true;
                    }
                    case 3 -> {
                        System.out.println("Enter file name: ");
                        String filenameR = scanner.nextLine();
                        board.createBoardFromFile(filenameR);
                        pieces = board.getFigures();
                        flag = true;
                    }
                    default -> System.out.println("There is no such option");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid choice!");
                scanner.nextLine();
            }
        }while (!flag);
    }
    @Override
    public void displayboard() {
        String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h"};
        String figure;
        Arrays.stream(letters).map(s -> "    " + s).forEach(System.out::print);
        System.out.println();

        System.out.println(" +----+----+----+----+----+----+----+----+");
        for (int i = 0; i < pieces.length; i++) {
            System.out.print((8 - i) + "|");
            for (int j = 0; j < pieces[i].length; j++) {
                if(pieces[i][j]== null){
                    figure = " ";
                }else{
                    figure = pieces[i][j].getColor().toString()+ (pieces[i][j]) ;
                }
                System.out.printf("%4s|", figure);
            }
            System.out.println(" " + (8 - i));
            System.out.println(" +----+----+----+----+----+----+----+----+");
        }

        Arrays.stream(letters).map(letter -> "    " + letter).forEach(System.out::print);
        System.out.println();
    }
    @Override
    public void displayHistoryOfMoves(){
        moves.stream().map(Move::toString).forEach(System.out::println);
    }
}
