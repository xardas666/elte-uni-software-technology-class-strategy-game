package hu.elte.csapat4.gui;

import hu.elte.csapat4.logics.ActionBuilder;
import hu.elte.csapat4.logics.GameException;
import hu.elte.csapat4.logics.GameState;
import hu.elte.csapat4.logics.RoundHelper;
import hu.elte.csapat4.models.details.Cost;
import hu.elte.csapat4.models.map.ActionType;
import hu.elte.csapat4.models.map.Coordinate;
import hu.elte.csapat4.models.map.IMapObject;
import hu.elte.csapat4.models.map.MapObjectType;
import hu.elte.csapat4.models.map.Terrain;
import hu.elte.csapat4.settings.FileSetting;
import hu.elte.csapat4.settings.GuiSettings;
import hu.elte.csapat4.settings.ModelSettings;
import lombok.extern.java.Log;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;
import javax.swing.Timer;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

@Log
public final class GameArea extends JPanel implements MouseListener, ActionListener {

    private static ArrayList<MapTile> mapTiles = new ArrayList<>();
    private JLabel actualPlayerName;
    private JLabel actualPlayerResources;
    private JLabel timeLabel;
    private JButton nextPlayerBtn;
    private Cursor cursor;

    //--config
    private int time = GuiSettings.TURN_TIME;

    //--Animation
    private Timer clock;
    private Timer moving;
    IMapObject destination, selected;
    int prevX, prevY;

    //gameend
    private boolean endGame = false;

    public GameArea() {
        mapToSpriteList();
        initComponents();
        addMouseListener(this);

        setActualPlayerData();
        initAndStartTimer();
    }

    private void initComponents() {
        timeLabel = new JLabel();
        nextPlayerBtn = new JButton();
        JLabel playerNameText = new JLabel();
        actualPlayerName = new JLabel();
        actualPlayerResources = new JLabel();

        timeLabel.setFont(new Font("Ubuntu Light", Font.PLAIN, 22));
        timeLabel.setText(" Time until next player : ");

        nextPlayerBtn.setText("Next Player");
        nextPlayerBtn.addActionListener(this::nextPlayerBtnActionPerformed);

        playerNameText.setFont(new Font("Ubuntu Light", Font.PLAIN, 22));
        playerNameText.setText("Player: ");

        actualPlayerName.setFont(new Font("Ubuntu Light", Font.BOLD, 22));
        actualPlayerName.setText(GameState.getActualPlayer().getNameString());

        actualPlayerResources.setFont(new Font("Ubuntu Light", Font.BOLD, 22));
        actualPlayerResources.setText(GameState.getActualPlayer().getResourceString());

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage(
                FileSetting.UI_IMAGES_PATH.getValue()
                + GameState.getActualPlayer().getColor().name()
                + FileSetting.IMAGE_FILE_EXT.getValue()
        );

        cursor = toolkit.createCustomCursor(image, new Point(this.getX(), this.getY()), "img");
        setCursor(cursor);

        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(playerNameText)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(actualPlayerName, GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(actualPlayerResources, GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                                .addComponent(timeLabel, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(nextPlayerBtn, GroupLayout.PREFERRED_SIZE, 123, GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap(606, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(timeLabel, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(nextPlayerBtn)
                                        .addComponent(playerNameText)
                                        .addComponent(actualPlayerName)
                                        .addComponent(actualPlayerResources)
                                )
                        )
        );

    }

    private void mapToSpriteList() {
        mapTiles.clear();
        for (int x = 0; x < GuiSettings.MAX_MAP_TILE_WIDTH; x++) {
            for (int y = 0; y < GuiSettings.MAX_MAP_TILE_HEIGHT; y++) {
                Coordinate coordinate = new Coordinate(x, y);
                IMapObject object = GameState.getMap().get(coordinate);
                if (object != null) {
                    mapTiles.add(new MapTile(object));
                } else {
                    mapTiles.add(new MapTile(new Terrain(MapObjectType.MEADOW, coordinate)));
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        for (MapTile object : mapTiles) {
            object.paintComponent(grphcs);
        }
    }

    private void nextPlayerBtnActionPerformed(ActionEvent evt) {
        changePlayer();
    }

    private void changePlayer() throws IndexOutOfBoundsException, HeadlessException {
        RoundHelper.newRound();
        setActualPlayerData();
        mapToSpriteList();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage(
                FileSetting.UI_IMAGES_PATH.getValue()
                + GameState.getActualPlayer().getColor().name()
                + FileSetting.IMAGE_FILE_EXT.getValue()
        );
        cursor = toolkit.createCustomCursor(image, new Point(this.getX(), this.getY()), "img");
        this.setCursor(cursor);
        timeReset();
        this.repaint();
    }

    private void setActualPlayerData() {
        actualPlayerName.setText(GameState.getActualPlayer().getNameString());
        actualPlayerResources.setText(GameState.getActualPlayer().getResourceString());
    }

    /**
     * ACTION LISENER - For popup menu
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        ActionType action = ActionType.valueOf(ae.getActionCommand());
        if (ActionBuilder.alreadyInAction()) {
            ActionBuilder.setActionType(action);
        }
    }

    /**
     * MOUSE LISTENER
     *
     * @param me
     */
    @Override
    public void mouseClicked(MouseEvent me) {

        if (!endGame) {
            if (me.getButton() == MouseEvent.BUTTON2) {
                IMapObject mapObject = getMapTile(me.getX(), me.getY());
                if (mapObject != null) {
                    JOptionPane.showMessageDialog(this, mapObject.infoText());
                }
            }

            if (me.getButton() == MouseEvent.BUTTON3) {
                IMapObject mapObject = getMapTile(me.getX(), me.getY());
                if (mapObject != null && !mapObject.getActions().isEmpty()) {
                    if (mapObject.getPlayer() != null && !mapObject.getPlayer().equals(GameState.getActualPlayer())) {
                        return;
                    } else {
                        ActionBuilder.clear();
                        ActionBuilder.setSelected(mapObject);

                        ArrayList<ActionType> actionTypes = mapObject.getActions();
                        PopupMenu popup = new PopupMenu(mapObject.getType().name());
                        for (ActionType action : actionTypes) {
                            MenuItem item = new MenuItem(action.name());
                            item.setActionCommand(action.name());
                            item.addActionListener(this);
                            popup.add(item);
                        }
                        add(popup);
                        popup.show(this, me.getX(), me.getY());
                    }
                }
            }

            if (me.getButton() == MouseEvent.BUTTON1) {
                if (ActionBuilder.alreadyInAction()) {
                    IMapObject mapObject = getMapTile(me.getX(), me.getY());
                    ActionBuilder.setDestination(mapObject);
                    try {
                        if (ActionBuilder.getActionType() == ActionType.MOVE) {
                            destination = ActionBuilder.getDestination();
                            selected = ActionBuilder.getSelected();
                            ActionBuilder.setProgressed(new Coordinate(ActionBuilder.getSelected().getPosition()));
                            moving.start();
                        } else {

                            if (ActionBuilder.getActionType().name().contains("BUILD") || ActionBuilder.getActionType().name().contains("CREATE")) {
                                String item = ActionBuilder.getActionType().name().split("_")[1];
                                Cost cost = ModelSettings.getDetailFor(MapObjectType.valueOf(item)).getCost();

                                int option = JOptionPane.showConfirmDialog(
                                        this,
                                        "The cost is: " + cost + ". Is it a deal?",
                                        "Purcashe Confirmation",
                                        JOptionPane.YES_NO_OPTION
                                );

                                if (option == JOptionPane.NO_OPTION) {
                                    ActionBuilder.clear();
                                }
                            }
                            int lifeBefore = 0;
                            int lifeAfter = 0;
                            if (ActionBuilder.getActionType() == ActionType.ATTACK || ActionBuilder.getActionType() == ActionType.DRAGON_BREATH) {
                                lifeBefore = ActionBuilder.getDestination().getLife();
                            }

                            ActionBuilder.resolveAction();

                            if (ActionBuilder.getActionType() == ActionType.ATTACK || ActionBuilder.getActionType() == ActionType.DRAGON_BREATH) {
                                lifeAfter = ActionBuilder.getDestination().getLife();
                                String text = "You missed the target!";
                                if (lifeBefore != lifeAfter) {
                                    text = "You Hit the target with " + (lifeBefore - lifeAfter) + " points!";
                                }

                                Object[] options = {"OK"};
                                int input = JOptionPane.showOptionDialog(this,
                                        text, null,
                                        JOptionPane.OK_CANCEL_OPTION,
                                        JOptionPane.INFORMATION_MESSAGE,
                                        null,
                                        options,
                                        options[0]);

                                if (input == JOptionPane.OK_OPTION) {
                                    ActionBuilder.clear();
                                    mapToSpriteList();
                                    this.repaint();
                                    checkGameEnd();
                                }
                            }
                        }
                    } catch (GameException e) {
                        ActionBuilder.clear();
                        mapToSpriteList();
                        this.repaint();
                        JOptionPane.showMessageDialog(this, e.getMessage());
                    }
                }
            }
            mapToSpriteList();
            this.repaint();
        }
    }

    private void checkGameEnd() {
        if (RoundHelper.isGameFinished()) {
            ActionBuilder.clear();
            endGame = true;
            clock.stop();
            nextPlayerBtn.setEnabled(false);
            JOptionPane.showMessageDialog(this, "The winner is: " + GameState.getActualPlayer().getNameString());
        }
    }

    private void sleepASecond() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private IMapObject getMapTile(int rawX, int rawY) {
        Coordinate coordinate = getCoordinateFromRaw(rawX, rawY);

        for (MapTile mapTile : mapTiles) {
            Coordinate mapTileCordinate = new Coordinate(mapTile.getX(), mapTile.getY());
            if (mapTileCordinate.equals(coordinate)) {
                return mapTile.getMapObject();
            }
        }

        return null;
    }

    private Coordinate getCoordinateFromRaw(int rawX, int rawY) {
        return new Coordinate(rawX / GuiSettings.MAP_TILE_WIDTH, rawY / GuiSettings.MAP_TILE_HEIGHT);
    }

    @Override
    public void mousePressed(MouseEvent me) {
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {

    }

    @Override
    public void mouseExited(MouseEvent me) {
    }

    /**
     * TIMER
     */
    private void initAndStartTimer() {
        clock = new Timer(GuiSettings.TURN_TIMER_DELAY, new TimeListener());
        clock.start();

        moving = new Timer(GuiSettings.MOVING_TIMER_DELAY, new ScreenRefreshForMoving());
    }

    private void timeReset() {
        time = GuiSettings.TURN_TIME;
    }

    class TimeListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            time--;
            timeLabel.setText(" Time until next player: " + time);
            setActualPlayerData();
            //köridő
            if (time == 0) {
                timeReset();
                nextPlayerBtnActionPerformed(ae);
            }
        }
    }

    class ScreenRefreshForMoving implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            prevX = ActionBuilder.getProgressed().getX();
            prevY = ActionBuilder.getProgressed().getY();
            mapToSpriteList();
            //todo frame movement
            try {

                if (selected.getPosition().getX() < destination.getPosition().getX()) {
                    ActionBuilder.getProgressed().setX(ActionBuilder.getProgressed().getX() + 1);
                    ActionBuilder.resolveAction();
                    mapToSpriteList();
                    repaint();
                }
                if (selected.getPosition().getX() > destination.getPosition().getX()) {
                    ActionBuilder.getProgressed().setX(ActionBuilder.getProgressed().getX() - 1);
                    ActionBuilder.resolveAction();
                    mapToSpriteList();
                    repaint();
                }
                if (selected.getPosition().getY() > destination.getPosition().getY()) {
                    ActionBuilder.getProgressed().setY(ActionBuilder.getProgressed().getY() - 1);
                    ActionBuilder.resolveAction();
                    mapToSpriteList();
                    repaint();
                }
                if (selected.getPosition().getY() < destination.getPosition().getY()) {
                    ActionBuilder.getProgressed().setY(ActionBuilder.getProgressed().getY() + 1);
                    ActionBuilder.resolveAction();
                    mapToSpriteList();
                    repaint();
                }
                if ((ActionBuilder.getProgressed().getY() == destination.getPosition().getY()) && (ActionBuilder.getProgressed().getX() == destination.getPosition().getX())) {
                    moving.stop();
                    mapToSpriteList();
                    ActionBuilder.clear();
                }
            } catch (GameException ex) {
                ActionBuilder.getProgressed().setX(prevX);
                ActionBuilder.getProgressed().setY(prevY);
                mapToSpriteList();
                ActionBuilder.clear();
                moving.stop();
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }

    }

}
