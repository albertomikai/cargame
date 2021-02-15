import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static int score = 0;
    public static int roadSideWidth = 20;

    public static void main(String[] args) {

        try {
            startGame();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
        }
    }

    private static void startGame() throws IOException, InterruptedException {
        Terminal terminal = createTerminal();
        drawRoadside(terminal, roadSideWidth);
        int[] playerX = {40, 41};
        int[] playerY = {20, 21, 22};
        int[] playerPreviousX = {40, 41};
        int[] playerPreviousY = {20, 21, 22};
        Player player = new Player(playerX, playerY, '\u2588');
        player.setPreviousX(playerPreviousX);
        player.setPreviousY(playerPreviousY);

        List<Opponent> opponents = new ArrayList<>();
        opponents.add(createOpponent());

        List<Opponent> opponentsForRemoval = new ArrayList<>();

        List<Fuel> fuelObjects = new ArrayList<>();
        fuelObjects.add(new Fuel(35,0));

        List<Fuel> fuelObjectsForRemoval = new ArrayList<>();

        drawCars(terminal, player, opponents, fuelObjects);

        int carSpawnFactor = 50;
        int speedFactor = 50;
        double fuel = 100;

        do {
            score++;
            Thread.sleep(speedFactor);

            KeyStroke keyStroke = getUserKeyStroke(terminal);

            if (keyStroke != null) {
                movePlayer(player, keyStroke, roadSideWidth);
            }

            if (score % 5 == 0) {
                for (Opponent o : opponents) {
                    o.move();
                    if(o.getY()[0] == 30){
                        opponentsForRemoval.add(o);
                    }
                }

                for (Fuel fuelObject : fuelObjects) {
                    fuelObject.move();
                    if(fuelObject.getY() == 30) {
                        fuelObjectsForRemoval.add(fuelObject);
                    }
                }

                opponents.removeAll(opponentsForRemoval);
                fuelObjects.removeAll(fuelObjectsForRemoval);
            }

            if (score % carSpawnFactor == 0) {
                opponents.add(createOpponent());
            }

            if (score % 100 == 0 && carSpawnFactor > 20) {
                carSpawnFactor = carSpawnFactor - 5;
            }

            if (score % 200 == 0) {
                fuelObjects.add(createFuelObject());
            }

            if (score % 100 == 0 && speedFactor > 15) {
                speedFactor = speedFactor - 2;
            }

            fuel = fuel - 0.1;
            drawCars(terminal, player, opponents, fuelObjects);
            printScore(terminal,0,0, score);
            printFuel(terminal, fuel);

            for (Fuel fuelObject : fuelObjects) {
                if (fuelObject.getY() == player.getY()[0] || fuelObject.getY() == player.getY()[1] || fuelObject.getY() == player.getY()[2]) {
                    if (fuelObject.getX() == player.getX()[0] || fuelObject.getX() == player.getX()[1]) {
                        fuel = fuel + 20;
                        fuelObjectsForRemoval.add(fuelObject);
                    }
                }
            }

            fuelObjects.removeAll(fuelObjectsForRemoval);

        } while (isPlayerAlive(player, opponents, fuel));

        gameOver(terminal, fuel);

    }

    private static KeyStroke getUserKeyStroke(Terminal terminal) throws InterruptedException, IOException {
        KeyStroke keyStroke;
        keyStroke = terminal.pollInput();
        return keyStroke;
    }

    private static Opponent createOpponent() {
        int x = ThreadLocalRandom.current().nextInt(roadSideWidth, 79 - roadSideWidth);
        int[] opponentX = {x, x + 1};
        int[] opponentY = {-3, -2, -1};
        int[] opponentPreviousX = {x, x + 1};
        int[] opponentPreviousY = {-3, -2, -1};
        Opponent opponent = new Opponent(opponentX, opponentY, '\u2588');
        opponent.setPreviousX(opponentPreviousX);
        opponent.setPreviousY(opponentPreviousY);
        return opponent;
    }

    private static Fuel createFuelObject() {
        int x = ThreadLocalRandom.current().nextInt(roadSideWidth, 79 - roadSideWidth);
        int y = 0;
        return new Fuel (x,y);
    }

    private static Terminal createTerminal() throws IOException {
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Terminal terminal = terminalFactory.createTerminal();
        terminal.setCursorVisible(false);
        return terminal;
    }

    private static void movePlayer(Player player, KeyStroke keyStroke, int roadSideWidth) {
        switch (keyStroke.getKeyType()) {
            case ArrowLeft:
                if (player.getX1() > roadSideWidth) {
                    player.moveLeft();
                }
                break;
            case ArrowRight:
                if (player.getX2() < (79 - roadSideWidth)) {
                    player.moveRight();
                }
                break;
        }
    }

    private static boolean isPlayerAlive(Player player, List<Opponent> opponents, double fuel) {
        for (Opponent o : opponents) {
            if (o.getY()[2] >= player.getY()[0] && o.getY()[0] <= player.getY()[2]) {
                if (o.getX()[0] == player.getX()[0] || o.getX()[1] == player.getX()[0] || o.getX()[0] == player.getX()[1] || o.getX()[1] == player.getX()[1]) {
                    return false;
                }
            }
        }
        if (fuel <= 0.0) {
            return false;
        }
        return true;
    }

    private static void printScore(Terminal terminal, int x, int y, int score) throws IOException {
        String scoreString = "Score: " + score;
        char[] scoreCharArray = scoreString.toCharArray();
        terminal.setForegroundColor(TextColor.ANSI.WHITE);
        for (int i = 0; i < scoreCharArray.length; i++) {
            terminal.setCursorPosition(x+i, y);
            terminal.putCharacter(scoreCharArray[i]);
        }
    }

    private static void printFuel(Terminal terminal, double fuel) throws IOException {
        fuel = (int) (fuel);
        String fuelString = "Fuel: ";

        if (fuel > 80) {
            fuelString = fuelString + "XXXXX";
        } else if (fuel > 60) {
            fuelString = fuelString + "XXXX.";
        } else if (fuel > 40) {
            fuelString = fuelString + "XXX..";
        } else if (fuel > 20) {
            fuelString = fuelString + "X....";
        } else {
            fuelString = fuelString + ".....";
        }

        char[] fuelCharArray = fuelString.toCharArray();
        terminal.setForegroundColor(TextColor.ANSI.WHITE);
        for (int i = 0; i < fuelCharArray.length; i++) {
            terminal.setCursorPosition(69+i,0);
            terminal.putCharacter(fuelCharArray[i]);
        }

    }

    private static void drawCars(Terminal terminal, Player player, List<Opponent> opponents, List<Fuel> fuelObjects) throws IOException {
        for (int y : player.getPreviousY()) {
            for (int x : player.getPreviousX()) {
                terminal.setCursorPosition(x, y);
                terminal.putCharacter(' ');
            }
        }
        terminal.setForegroundColor(TextColor.ANSI.RED);

        for (int y : player.getY()) {
            for (int x : player.getX()) {
                terminal.setCursorPosition(x, y);
                terminal.putCharacter(player.getSymbol());
            }
        }

        for (Opponent o : opponents) {
            for (int y : o.getPreviousY()) {
                for (int x : o.getPreviousX()) {
                    terminal.setCursorPosition(x, y);
                    terminal.putCharacter(' ');
                }
            }

            for (int y : o.getY()) {
                terminal.setForegroundColor(o.getColor());
                if (o.getY()[2] >= 0) {
                    for (int x : o.getX()) {
                        terminal.setCursorPosition(x, y);
                        terminal.putCharacter(o.getSymbol());
                    }
                }
            }
        }

        for (Fuel fuelObject : fuelObjects) {
            terminal.setForegroundColor(fuelObject.getColor());
            terminal.setCursorPosition(fuelObject.getX(),fuelObject.getPreviousY());
            terminal.putCharacter(' ');
            terminal.setCursorPosition(fuelObject.getX(),fuelObject.getY());
            terminal.putCharacter(fuelObject.getSymbol());
        }

        terminal.setForegroundColor(TextColor.ANSI.BLACK);
        for(int x = roadSideWidth; x< 80-roadSideWidth; x++){
            terminal.setCursorPosition(x, 25);
            terminal.putCharacter('\u2588');
        }

        terminal.flush();

    }

    private static void drawRoadside(Terminal terminal, int width) throws IOException {
        Character roadSide = '\u2588';
        terminal.setForegroundColor(TextColor.ANSI.GREEN);
        for (int y = 0; y < 25; y++) {
            for (int x = 0; x < width; x++) {
                terminal.setCursorPosition(x, y);
                terminal.putCharacter(roadSide);
            }

        }
        for (int y = 0; y < 25; y++) {
            for (int x = 80; x >= (80 - width); x--) {
                terminal.setCursorPosition(x, y);
                terminal.putCharacter(roadSide);
            }
        }

        terminal.setForegroundColor(TextColor.ANSI.WHITE);
        String gameName = "Mad Max 2021";
        char[] gameNameChar = gameName.toCharArray();
        for (int i = 0; i < gameNameChar.length; i++){
            terminal.setCursorPosition(68 + i, 22);
            terminal.putCharacter(gameNameChar[i]);
        }
        String gameName2 = "Provided by Team 3";
        char[] gameName2Char = gameName2.toCharArray();
        for (int i = 0; i < gameName2Char.length; i++){
            terminal.setCursorPosition(62 + i, 23);
            terminal.putCharacter(gameName2Char[i]);
        }
        terminal.flush();
    }

    private static void gameOver(Terminal terminal, double fuel) throws IOException {
        String reason;
        if (fuel <= 0.0) {
            reason = "OUT OF GAS";
        } else {
            reason = "YOU CRASHED";
        }

        String gameOver = "GAME OVER - " + reason;
        char[] gameOverArray = gameOver.toCharArray();

        for (int i = 0; i < gameOverArray.length; i++) {
            terminal.setCursorPosition(29+i, 10);
            terminal.putCharacter(gameOverArray[i]);
        }

        printScore(terminal,35,12, score);

        terminal.flush();

    }
}
