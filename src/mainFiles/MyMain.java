package mainFiles;

import characters.*;
import characters.Character;
import mainFiles.gameConventions.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class MyMain extends JPanel implements ActionListener {
    private static final int SCALE = 2;

    public JFrame frame;
    public Timer timer = new Timer(20, this);

    GameState gameState;
    public MainMenu menu;

    public java.util.List<Character> characters = new ArrayList<>();
    public Player player;

    public MyMain(JFrame frame) {
        this.frame = frame;
        this.timer.start();
        this.gameState = GameState.IN_GAME;
        this.menu = new MainMenu(this);
        this.player = new Player(this);

        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                player.mouseClicked(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                player.mousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                player.mouseReleased(e);
            }
        });

        characters.add(new ElfArcher(this));
        characters.add(new HumanMagician(this));
        characters.add(new HumanKnight(this));
    }

    public void paint(Graphics g) {
        if (gameState == GameState.IN_MENU) {
            menu.drawMenu(g);
            menu.clickCheck();
        }
        if (gameState == GameState.IN_GAME) {
            paintMap(g);

            for (Character c : characters) {
                g.drawImage(c.currentFrame, c.dirX, c.dirY,
                        c.spriteWidth * SCALE, c.spriteHeight * SCALE, null);
            }

            g.drawImage(new ImageIcon("C:\\myGame\\src\\res\\GameInterface.png").getImage(), 0, 0, null);

            if (!player.party.isEmpty()) {
                paintHighlighting((Graphics2D) g);
                paintIco(g);
            }

        }

    }

    public void paintHighlighting(Graphics2D g) {
        g.setColor(Color.RED);
        for (Character character : player.party) {
            g.drawRect(character.dirX + character.spriteWidth / 4, character.dirY + character.spriteHeight / 4,
                    (int) (character.spriteWidth * 1.5 - 10), (int) (character.spriteHeight * 1.5));
        }
    }

    public void paintIco(Graphics g) {
        int wdFactor = 0;
        int hgFactor = 0;
        for (Character character : player.party) {
            if (wdFactor == 4) {
                hgFactor++;
                wdFactor = 0;
            }
            g.drawImage(character.icon, (68 + character.iconWidth * SCALE * wdFactor + 6 * wdFactor),
                    (361 + character.iconHeight * SCALE * hgFactor + 10 * hgFactor),
                    character.iconWidth * SCALE, character.iconHeight * SCALE, null);
            wdFactor++;
        }
    }

    public void paintMap(Graphics g) {
        g.drawImage(new ImageIcon("C:\\myGame\\src\\res\\map.png").getImage(), 525, 32, 1920, 1080, null);
        g.drawImage(new ImageIcon("C:\\myGame\\src\\res\\map.png").getImage(), 62, 53, 398, 300, null);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        for (Character c : characters) {
            c.move();
        }
        repaint();
    }

}
