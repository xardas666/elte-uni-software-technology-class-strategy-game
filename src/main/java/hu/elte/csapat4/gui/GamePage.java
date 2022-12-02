package hu.elte.csapat4.gui;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

public class GamePage extends JFrame {

    public GamePage() {
        initComponents();
        setVisible(true);
        setLocation(0,0);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        pack();
    }

    private void initComponents() {
        GameArea gameArea = new GameArea();
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu();
        JMenuItem newGameMenuItem = new JMenuItem();
        JMenuItem helpMenuItem = new JMenuItem();
        JMenuItem exitMenuItem = new JMenuItem();

        gameMenu.setText("Game Menu");

        newGameMenuItem.setText("New Game");
        newGameMenuItem.addActionListener(this::newGameMenuItemActionPerformed);
        gameMenu.add(newGameMenuItem);

        helpMenuItem.setText("Help");
        helpMenuItem.addActionListener(this::helpMenuItemActionPerformed);
        gameMenu.add(helpMenuItem);

        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(this::exitMenuItemActionPerformed);
        gameMenu.add(exitMenuItem);

        menuBar.add(gameMenu);

        setJMenuBar(menuBar);

        GroupLayout groupLayout = new GroupLayout(getContentPane());
        getContentPane().setLayout(groupLayout);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(gameArea, GroupLayout.DEFAULT_SIZE, 1200, Short.MAX_VALUE)
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(gameArea, GroupLayout.DEFAULT_SIZE, 630, Short.MAX_VALUE)
        );

    }

    private void newGameMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        int option = JOptionPane.showConfirmDialog(
                this,
                "Start a new Game?",
                "",
                JOptionPane.YES_NO_OPTION
        );

        if (option == JOptionPane.YES_OPTION) {
            this.dispose();
            StartPage startpage = new StartPage();
            startpage.setVisible(true);
            startpage.setLocationRelativeTo(null);
        }
    }

    private void helpMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        new HelpPage().setVisible(true);
    }

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        int option = JOptionPane.showConfirmDialog(
                this,
                "Are you sure?",
                "Exit Confirmation",
                JOptionPane.YES_NO_OPTION
        );

        if (option == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
