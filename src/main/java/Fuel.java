import com.googlecode.lanterna.TextColor;


public class Fuel {
    private int x;
    private int y;
    private char symbol;
    private int previousX;
    private int previousY;
    private TextColor color;

    public Fuel(int x, int y) {
        this.x = x;
        this.y = y;
        this.symbol = '\u26FD';
        this.color = TextColor.ANSI.RED;
    }

    public TextColor getColor() {
        return color;
    }

    public void move() {
        previousY = y;
        y = y + 1;
    }

    public char getSymbol() {
        return symbol;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getPreviousY() {
        return previousY;
    }

    }
