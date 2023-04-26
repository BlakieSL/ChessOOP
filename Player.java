public record Player(Color color) {
    public Player getOpponent() {
        if (this.color == Color.W) {
            return new Player(Color.B);
        } else {
            return new Player(Color.W);
        }
    }
}