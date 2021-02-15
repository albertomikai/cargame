import com.googlecode.lanterna.TextColor;

import java.util.concurrent.ThreadLocalRandom;

public class Opponent {
    private int[] x;
    private int[] y;
    private char symbol;
    private int[] previousX;
    private int[] previousY;
    private TextColor color;

    public Opponent(int[] x, int[] y, char symbol) {
        this.x = x;
        this.y = y;
        this.symbol = symbol;
        this.color = assignRandomColor();
    }

    public TextColor getColor() {
        return color;
    }

    public void move(){
        for(int i = 0; i < y.length; i++){
            previousY[i] = y[i];
            y[i] = y[i] + 1;

        }
    }


    private TextColor assignRandomColor() {
        int colorId = ThreadLocalRandom.current().nextInt(1, 7);
        TextColor output;
        switch (colorId) {
            case 1 -> {
                output = TextColor.ANSI.YELLOW;
            }
            case 2 -> {
                output = TextColor.ANSI.CYAN;
            }
            case 3 -> {
                output = TextColor.ANSI.WHITE;
            }
            case 4 -> {
                output = TextColor.ANSI.MAGENTA;
            }
            case 5 -> {
                output = TextColor.ANSI.GREEN;
            }
            default -> output = TextColor.ANSI.BLUE;
        }
        return output;
    }

    public int[] getX() {
        return x;
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
