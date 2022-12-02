package hu.elte.csapat4.gui;

import hu.elte.csapat4.models.map.Building;
import hu.elte.csapat4.models.map.IMapObject;
import hu.elte.csapat4.models.map.MapObjectType;
import hu.elte.csapat4.models.map.PlayerColor;
import hu.elte.csapat4.settings.FileSetting;
import hu.elte.csapat4.settings.GuiSettings;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Image;

public class MapTile extends JPanel {

    private final IMapObject mapObject;
    private final int x;
    private final int y;
    private final Image backgroundImage = new ImageIcon(FileSetting.MAP_IMAGES_PATH.getValue() + MapObjectType.MEADOW.name() + FileSetting.IMAGE_FILE_EXT.getValue()).getImage();
    private Image image;

    public MapTile(IMapObject object) {
        this.mapObject = object;
        this.x = mapObject.getPosition().getX();
        this.y = mapObject.getPosition().getY();

        setImage();
    }

    private void setImage() {
        if (!MapObjectType.isTerrain(mapObject.getType())) {
            if(MapObjectType.isBuilding(mapObject.getType()) && !((Building)mapObject).isBuilt()){
                String fileStr = FileSetting.MAP_IMAGES_PATH.getValue() + "BUILDING_PROGRESS" + FileSetting.IMAGE_FILE_EXT.getValue();
                this.image = new ImageIcon(fileStr).getImage();
            } else{
                PlayerColor color = mapObject.getPlayer().getColor();
                String fileStr = FileSetting.MAP_IMAGES_PATH.getValue() + mapObject.getType().name() + "_" + color.name() + FileSetting.IMAGE_FILE_EXT.getValue();
                this.image = new ImageIcon(fileStr).getImage();
            }
        } else {
            String fileStr = FileSetting.MAP_IMAGES_PATH.getValue() + mapObject.getType().name() + FileSetting.IMAGE_FILE_EXT.getValue();
            this.image = new ImageIcon(fileStr).getImage();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage,
                x * GuiSettings.MAP_TILE_WIDTH,
                y * GuiSettings.MAP_TILE_HEIGHT,
                GuiSettings.MAP_TILE_WIDTH,
                GuiSettings.MAP_TILE_HEIGHT,
                this
        );
        g.drawImage(image,
                x * GuiSettings.MAP_TILE_WIDTH,
                y * GuiSettings.MAP_TILE_HEIGHT,
                GuiSettings.MAP_TILE_WIDTH,
                GuiSettings.MAP_TILE_HEIGHT,
                this
        );
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public IMapObject getMapObject() {
        return mapObject;
    }

}
