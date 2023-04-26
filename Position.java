record Positions(int x, int y) {
    @Override
    public String toString() {
        return   Calculator.translateIntoNum(x()) + Calculator.translateIntoSym(y());
    }
}
