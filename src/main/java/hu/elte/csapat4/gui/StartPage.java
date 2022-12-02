package hu.elte.csapat4.gui;

import hu.elte.csapat4.logics.GameState;
import hu.elte.csapat4.logics.RoundHelper;
import hu.elte.csapat4.settings.FileSetting;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

public class StartPage extends JFrame {

    public StartPage() {
        initComponents();

        pack();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            StartPage startpage = new StartPage();
            startpage.setLocationRelativeTo(null);
            startpage.setResizable(false);
            startpage.setVisible(true);
            startpage.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        });
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                Image image = toolkit.getImage(
                        FileSetting.UI_IMAGES_PATH.getValue()
                                + "BACKGROUND"
                                + FileSetting.IMAGE_FILE_EXT.getValue()
                );
                g.drawImage(image,0,0,this.getParent().getWidth(), this.getParent().getHeight(),this);

            }
        };
        mainPanel.setVisible(true);

        JButton startNewGameBtn = new JButton();
        startNewGameBtn.setText("Start New Game");
        startNewGameBtn.addActionListener(this::startNewGameBtnActionPerformed);

        JButton exitGameBtn = new JButton();
        exitGameBtn.setText("Exit");
        exitGameBtn.addActionListener(this::exitBtnActionPerformed);

        GroupLayout panelLayout = new GroupLayout(mainPanel);
        mainPanel.setLayout(panelLayout);

        panelLayout.setHorizontalGroup(
                panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(panelLayout.createSequentialGroup()
                                .addGap(103, 175, 175)
                                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(exitGameBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(startNewGameBtn, GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE))
                                .addContainerGap(94, Short.MAX_VALUE))
        );

        panelLayout.setVerticalGroup(
                panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                                .addContainerGap(202, Short.MAX_VALUE)
                                .addComponent(startNewGameBtn)
                                .addGap(18, 18, 18)
                                .addComponent(exitGameBtn)
                                .addGap(40, 40, 40))
        );

        GroupLayout groupLayout = new GroupLayout(getContentPane());
        getContentPane().setLayout(groupLayout);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, 552, Short.MAX_VALUE)
        );

        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addComponent(mainPanel, GroupLayout.PREFERRED_SIZE, 462, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );
    }

    private void startNewGameBtnActionPerformed(ActionEvent evt) {
        getPlayerNames();
        new GamePage();
        this.setVisible(false);
    }

    private void getPlayerNames() {
        String player1Name = JOptionPane.showInputDialog("Player 1");
        String player2Name = JOptionPane.showInputDialog("Player 2");
        RoundHelper.gameStart(player1Name, player2Name);
    }


    private void exitBtnActionPerformed(ActionEvent evt) {
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
