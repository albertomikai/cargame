import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.List;

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


        //Add enemies
        drawCars(terminal,player);

        do {
            KeyStroke keyStroke = getUserKeyStroke(terminal);

            movePlayer(player, keyStroke,roadSideWidth);

            //moveMonsters(player, monsters);

            drawCars(terminal, player);

        } while (true);

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

    private static KeyStroke getUserKeyStroke(Terminal terminal) throws InterruptedException, IOException {
        KeyStroke keyStroke;
        do {
            Thread.sleep(5);
            keyStroke = terminal.pollInput();
        } while (keyStroke == null);
        return keyStroke;
    }

    private static void drawCars(Terminal terminal, Player player) throws IOException {
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
