package hu.elte.csapat4.gui;

import hu.elte.csapat4.settings.FileSetting;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.TextArea;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public final class HelpPage extends JFrame {

    public HelpPage() {
        initComponents();
        setVisible(true);

        pack();
    }

    private void initComponents() {
        JFrame frame = new JFrame();
        TextArea helpTextArea = new TextArea();
        helpTextArea.setEditable(false);

        GroupLayout frameLayout = new GroupLayout(frame.getContentPane());
        frame.getContentPane().setLayout(frameLayout);
        frameLayout.setHorizontalGroup(
                frameLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 400, Short.MAX_VALUE)
        );
        frameLayout.setVerticalGroup(
                frameLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        GroupLayout groupLayout = new GroupLayout(getContentPane());
        getContentPane().setLayout(groupLayout);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(helpTextArea, GroupLayout.PREFERRED_SIZE, 572, GroupLayout.PREFERRED_SIZE)
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addComponent(helpTextArea, GroupLayout.PREFERRED_SIZE, 454, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );

        addHelpText(helpTextArea, FileSetting.TEXTS_PATH.getValue()+"help"+FileSetting.TEXT_FILE_EXT.getValue());

    }

    public void addHelpText(TextArea helpTextArea, String fileName) {
        List<String> fileLines = null;

        try {
            fileLines = Files.readAllLines(Paths.get(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (fileLines != null) {
            for (String line : fileLines) {
                helpTextArea.append(line + "\n");
            }
        }
    }
}
