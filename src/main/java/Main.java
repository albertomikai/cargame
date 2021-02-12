import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static void main(String[] args) {

        System.out.println("Cargame");
        System.out.println("*******");
        try {
            startGame();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            System.out.println("Game over!");
        }

    }
    private static void startGame() throws IOException, InterruptedException {
        Terminal terminal = createTerminal();
        int roadSideWidth = 20;
        drawRoadside(terminal, roadSideWidth);
        int[] playerX = {40,41};
        int[] playerY = {20,21,22};
        int[] playerPreviousX = {40,41};
        int[] playerPreviousY = {20,21,22};
        Player player = new Player(playerX,playerY,'\u2588');
        player.setPreviousX(playerPreviousX);
        player.setPreviousY(playerPreviousY);

        List<Opponent> opponents = new ArrayList<>();
        opponents.add(createOpponent());

        drawCars(terminal,player,opponents);

        do {
            KeyStroke keyStroke = getUserKeyStroke(terminal);

            movePlayer(player, keyStroke,roadSideWidth);

            for(Opponent o : opponents){
                    o.move();
            }

            drawCars(terminal, player, opponents);

        } while (isPlayerAlive(player, opponents));
    }

    private static Opponent createOpponent(){
        int x = ThreadLocalRandom.current().nextInt(20,59);
        int[] opponentX = {x,x+1};
        int[] opponentY = {-3,-2,-1};
        int[] opponentPreviousX = {x,x+1};
        int[] opponentPreviousY = {-3,-2,-1};
        Opponent opponent = new Opponent(opponentX,opponentY,'\u2588');
        opponent.setPreviousX(opponentPreviousX);
        opponent.setPreviousY(opponentPreviousY);
        return opponent;
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
                if (player.getX1() > roadSideWidth){
                    player.moveLeft();
                }
                break;
            case ArrowRight:
                if (player.getX2() < (79-roadSideWidth)){
                    player.moveRight();
                }
                break;
        }
    }

    private static boolean isPlayerAlive(Player player, List<Opponent> opponents) {
        for (Opponent o : opponents) {
            if(player.getY()[0] - o.getY()[2] < 2 && o.getY()[0] < 26){
                if (o.getX()[0] == player.getX()[0] || o.getX()[1] == player.getX()[0] || o.getX()[0] == player.getX()[1] || o.getX()[1] == player.getX()[1]){
                    return false;
                }
            }
        }
        return true;
    }

    private static KeyStroke getUserKeyStroke(Terminal terminal) throws InterruptedException, IOException {
        KeyStroke keyStroke;
        do {
            Thread.sleep(5);
            keyStroke = terminal.pollInput();
        } while (keyStroke == null);
        return keyStroke;
    }

    private static void drawCars(Terminal terminal, Player player, List<Opponent> opponents) throws IOException {
        for(int y : player.getPreviousY()){
            for(int x : player.getPreviousX()){
                terminal.setCursorPosition(x, y);
                terminal.putCharacter(' ');
            }
        }
        terminal.setForegroundColor(TextColor.ANSI.RED);

        for(int y : player.getY()){
            for(int x : player.getX()){
                terminal.setCursorPosition(x, y);
                terminal.putCharacter(player.getSymbol());
            }
        }

        for(Opponent o : opponents){
            for(int y : o.getPreviousY()){
                for(int x : o.getPreviousX()){
                    terminal.setCursorPosition(x, y);
                    terminal.putCharacter(' ');
                }
            }
            terminal.setForegroundColor(TextColor.ANSI.BLUE);

            for(int y : o.getY()){
                if(o.getY()[2] >= 0){
                    for(int x : o.getX()){
                        terminal.setCursorPosition(x, y);
                        terminal.putCharacter(o.getSymbol());
                    }
                }
            }
        }

        terminal.flush();
    }

    private static void drawRoadside(Terminal terminal, int width) throws IOException {
        Character roadSide = '\u2588'; //Block character
        terminal.setForegroundColor(TextColor.ANSI.GREEN); //Set color of Roadside
        //Draw left side of the road
        for(int y = 0; y<25; y++){
            for(int x = 0; x < width; x++){
                terminal.setCursorPosition(x,y);
                terminal.putCharacter(roadSide);
            }
        }
        //Draw right side of the road
        for(int y = 0; y<25; y++){
            for(int x = 80; x >= (80-width); x--){
                terminal.setCursorPosition(x,y);
                terminal.putCharacter(roadSide);
            }
        }
        terminal.flush();
    }
}
