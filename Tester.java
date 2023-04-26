
class Tester {
    public static void main(String[] args)  {
        Player wj = new Player(Color.W);
        Player bl = new Player(Color.B);

        Game game = new Game(wj);
        game.play();

    }
}
