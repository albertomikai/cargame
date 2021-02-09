import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

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
        drawRoadside(terminal, 15);
    }

    private static Terminal createTerminal() throws IOException {
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Terminal terminal = terminalFactory.createTerminal();
        terminal.setCursorVisible(false);
        return terminal;
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
