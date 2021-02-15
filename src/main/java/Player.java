public class Player {
    private int[] x;
    private int[] y;
    private char symbol;

    private int[] previousX;
    private int[] previousY;

    public Player(int[] x, int[] y, char symbol) {
        this.x = x;
        this.y = y;
        this.symbol = symbol;
    }

    public void moveLeft(){
        for(int i = 0; i < x.length; i++){
            previousX[i] = x[i];
            x[i] = x[i] - 1;
        }
    }

    public void moveRight(){
        for(int i = 0; i < x.length; i++){
            previousX[i] = x[i];
            x[i] = x[i] + 1;
        }
    }

    public int[] getX() {
        return x;
    }

    public int getX1() {
        return x[0];
    }

    public int getX2() {
        return x[1];
    }

    public int[] getY() {
        return y;
    }

   public char getSymbol() {
        return symbol;
    }

    public int[] getPreviousX() {
        return previousX;
    }

    public void setPreviousX(int[] previousX) {
        this.previousX = previousX;
    }

    public int[] getPreviousY() {
        return previousY;
    }

    public void setPreviousY(int[] previousY) {
        this.previousY = previousY;
    }
}
