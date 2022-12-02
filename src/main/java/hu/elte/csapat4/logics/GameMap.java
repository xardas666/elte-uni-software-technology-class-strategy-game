package hu.elte.csapat4.logics;

import hu.elte.csapat4.models.map.Building;
import hu.elte.csapat4.models.map.Character;
import hu.elte.csapat4.models.map.Coordinate;
import hu.elte.csapat4.models.map.IMapObject;
import hu.elte.csapat4.models.map.MapObjectType;
import hu.elte.csapat4.models.map.PlayerColor;
import hu.elte.csapat4.models.map.Terrain;
import hu.elte.csapat4.settings.FileSetting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

class GameMap {

    public ConcurrentHashMap<Coordinate, IMapObject> getStarterMap() {
        ConcurrentHashMap<Coordinate, IMapObject> map = new ConcurrentHashMap<>();
        List<String> fileLines = null;

        try {
            int level = ThreadLocalRandom.current().nextInt(1, 4);
            fileLines = Files.readAllLines(Paths.get(FileSetting.MAPS_PATH.getValue() + level + FileSetting.MAP_FILE_EXT.getValue()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (fileLines != null) {
            for (String line : fileLines) {
                if (line.isEmpty() || line.contains("#")) {
                    continue;
                }
                CSVLine csvLine = new CSVLine(line);

                if (MapObjectType.isTerrain(csvLine.getType())) {
                    addToMap(map, new Terrain(csvLine.getType(), csvLine.getCoordinate()));
                } else if (MapObjectType.isBuilding(csvLine.getType())) {
                    if (csvLine.getColor() == PlayerColor.BLUE) {
                        addToMap(map, new Building(csvLine.getType(), GameState.getPlayer1(), csvLine.getCoordinate()));
                    } else {
                        addToMap(map, new Building(csvLine.getType(), GameState.getPlayer2(), csvLine.getCoordinate()));
                    }

                } else if (MapObjectType.isCharacter(csvLine.getType())) {
                    if (csvLine.getColor() == PlayerColor.BLUE) {
                        addToMap(map, new Character(csvLine.getType(), GameState.getPlayer1(), csvLine.getCoordinate()));
                    } else {
                        addToMap(map, new Character(csvLine.getType(), GameState.getPlayer2(), csvLine.getCoordinate()));
                    }


                }
            }
        }

        return map;
    }

    private void addToMap(ConcurrentHashMap<Coordinate, IMapObject> map, IMapObject mapObject) {
        map.put(mapObject.getPosition(), mapObject);
    }

    class CSVLine {
        private MapObjectType type;
        private Coordinate coordinate;
        private PlayerColor color;

        public CSVLine(String inputLine) {
            type = MapObjectType.valueOf(inputLine.split(";")[0]);
            coordinate = new Coordinate(
                    Integer.parseInt(inputLine.split(";")[1]),
                    Integer.parseInt(inputLine.split(";")[2])
            );
            //T - terrain // R - player 2 // B - player 1
            if (inputLine.split(";")[3].equals("R")) {
                color = PlayerColor.RED;
            } else if (inputLine.split(";")[3].equals("B")) {
                color = PlayerColor.BLUE;
            }

        }

        public MapObjectType getType() {
            return type;
        }

        public Coordinate getCoordinate() {
            return coordinate;
        }

        public PlayerColor getColor() {
            return color;
        }
    }
    
    
}
