//Ralph Perricelli
//Muigai Unaka
//import required libraries
import javalib.worldimages.*;
import java.awt.Color;
import java.util.Random;

//To represent a single square of a game board
class Cell {
    //absolute height of this cell
    double height;
    //coordinates of wrt origin at the top left
    int x; 
    int y;
    //adjacent cells to this cell
    Cell left;
    Cell top;
    Cell right; 
    Cell bottom;
    //boolean flag for flooding of a cell
    boolean isFlooded;

    //General Constructor for cell
    Cell(double height, int x, int y) {
        this.height = height;
        this.x = x;
        this.y = y;
        this.left = null;
        this.top = null;
        this.right = null;
        this.bottom = null;
        //set flooded to the default value based on height of cell
        if (height > 0) {
            this.isFlooded = false;
        }
        else {
            this.isFlooded = true;
        }
    }

    //Constructor for Cell w adjacent cells and flag 
    Cell(double height, int x, int y, Cell left, Cell top, Cell right,
            Cell bottom, boolean isFlooded) {
        this.height = height;
        this.x = x;
        this.y = y;
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.isFlooded = isFlooded;
    }

    //Determine and change a cells adjacent cells and flooded flag
    void floodConditions(double height) {
        this.isFlooded = true;
        if (this.top.height <= height && !this.top.nextToLand()
                && !this.top.isFlooded) {
            this.top.floodConditions(height);
        }
        if (this.left.height <= height && !this.left.nextToLand()
                && !this.left.isFlooded) {
            this.left.floodConditions(height);
        }
        if (this.right.height <= height && !this.right.nextToLand()
                && !this.right.isFlooded) {
            this.right.floodConditions(height);
        }
        if (this.bottom.height <= height && !this.bottom.nextToLand()
                && !this.bottom.isFlooded) {
            this.bottom.floodConditions(height);
        }
    }
    //changes the left cell to a given cell and changes the left cell of
    //this new cell
    void updateLeft(Cell left) {
        this.left = left;
        left.updateRight(this);
    }

    //Update top cell to this and bottom of this cell
    void updateTop(Cell top) {
        this.top = top;
        top.updateBottom(this);
    }

    //update only the left cell
    void updateOnlyLeft(Cell left) {
        this.left = left;
    }

    ///update only the top cell
    void updateOnlyTop(Cell top) {
        this.top = top;
    }

    //update right cell to this cell
    void updateRight(Cell right) {
        this.right = right;
    }

    //update bottom cell to this cell
    void updateBottom(Cell bottom) {
        this.bottom = bottom;
    }

    //draw the image of this cell on top of a given background
    //with color depending on the level(height) of water
    WorldImage drawCell(WorldImage bg, double waterHeight) {
        //distnce above sea level
        double distToSea = Math.abs(waterHeight - this.height);
        int depth = (int) (180 * (1 - Math.exp(-1 * distToSea
                / (.15 * (ForbiddenIslandWorld.ISLAND_SIZE)))));

        //draw a flooded cell
        if (this.isFlooded) {
            return bg.overlayImages(new RectangleImage(new Posn(this.x 
                    * ForbiddenIslandWorld.PIXEL_SIZE
                    + ForbiddenIslandWorld.PIXEL_SIZE / 2, this.y
                    * ForbiddenIslandWorld.PIXEL_SIZE
                    + ForbiddenIslandWorld.PIXEL_SIZE / 2),
                    ForbiddenIslandWorld.PIXEL_SIZE, 
                    ForbiddenIslandWorld.PIXEL_SIZE, 
                    new Color(depth, 180 - depth, 0)));
        }
        else {
            return bg.overlayImages(new RectangleImage(new Posn(this.x 
                    * ForbiddenIslandWorld.PIXEL_SIZE
                    + ForbiddenIslandWorld.PIXEL_SIZE / 2, this.y
                    * ForbiddenIslandWorld.PIXEL_SIZE
                    + ForbiddenIslandWorld.PIXEL_SIZE / 2),
                    ForbiddenIslandWorld.PIXEL_SIZE, 
                    ForbiddenIslandWorld.PIXEL_SIZE, 
                    new Color(depth, 180, depth)));
        }
    }

    //Determine if a given cell is next to land
    boolean nextToLand() {
        return !this.top.isFlooded && !this.bottom.isFlooded
                && !this.left.isFlooded && !this.right.isFlooded;
    }

    //determines if this is an ocean cell(it is not)
    boolean isOceanCell() {
        return false;
    }
}

//To represent an ocean cell on the game board
class OceanCell extends Cell {

    //constructor position, always underwater
    OceanCell(int x, int y) {
        super(-1, x, y);
        this.isFlooded = true;
    }

    //constructor with x, y and adjacent cells
    OceanCell(int x, int y, Cell top, Cell left, Cell bottom, 
            Cell right) {
        super(-1, x , y, top, left, bottom, right, true);
    }

    //Render/draw this blue ocean cell given a background 
    WorldImage drawCell(WorldImage bg, double waterheight) {
        return bg.overlayImages(new RectangleImage(new Posn(this.x 
                * ForbiddenIslandWorld.PIXEL_SIZE
                + ForbiddenIslandWorld.PIXEL_SIZE / 2, this.y
                * ForbiddenIslandWorld.PIXEL_SIZE
                + ForbiddenIslandWorld.PIXEL_SIZE / 2),
                ForbiddenIslandWorld.PIXEL_SIZE, 
                ForbiddenIslandWorld.PIXEL_SIZE, 
                new Color(0, 0, 200)));
    }

    //determine of this cell is an ocean cell
    boolean isOceanCell() {
        return true;
    }
}

//to represent a user/player on the board
class Player {
    //location of player on the baord
    Posn loc;

    //Constructor given a current position
    Player(Posn loc) {
        this.loc = loc;
    }

    //Place player on board in a random valid location
    //TO BE IMPLEMENTED

    //move a player on the board
    //check if valid location needs to be added
    Player move(String key, IList<Cell> board) {
        Posn curLoc = this.loc;

        //if left then.., if right then.. if etc then etc
        if (key.equals("left")) {
            curLoc = new Posn(this.loc.x - 1, this.loc.y);
            return new Player(curLoc);
        }
        else if (key.equals("right")) {
            curLoc = new Posn(this.loc.x + 1, this.loc.y);
            return new Player(curLoc);
        }
        else if (key.equals("up")) {
            curLoc = new Posn(this.loc.x, this.loc.y - 1);
            return new Player(curLoc);
        }
        else if (key.equals("down")) {
            curLoc = new Posn(this.loc.x, this.loc.y + 1);
            return new Player(curLoc);
        }
        return this;
    }

    //check if this players loc is equal to that loc
    //TO BE IMPLEMENTED
    boolean samePosition(Posn other) {
        return this.loc.x == other.x && this.loc.y == other.y;
    }

    public WorldImage drawPlayer(WorldImage bg, String src) {
        return bg.overlayImages(new FromFileImage(new Posn(this.loc.x
                * ForbiddenIslandWorld.PIXEL_SIZE
                + ForbiddenIslandWorld.PIXEL_SIZE / 2, this.loc.y
                * ForbiddenIslandWorld.PIXEL_SIZE
                + ForbiddenIslandWorld.PIXEL_SIZE / 2), src));
    }
}

//to represent a part of the helicopter/ and its location
class Part {
    //current location
    Posn loc;

    //constructor
    Part(Posn loc) {
        this.loc = loc;
    }

    //Constrcut a part on random yet valid loc on the board
    //TO BE IMPLEMENTED LATER
    Part(IList<Cell> board) {
        Posn curLoc = new Posn(
                new Random().nextInt(ForbiddenIslandWorld.ISLAND_SIZE),
                new Random().nextInt(ForbiddenIslandWorld.ISLAND_SIZE));
        Cell c = board.find(new PosnEqual(curLoc));
        while (c.isFlooded || c.height < ForbiddenIslandWorld.ISLAND_SIZE / 8) {
            curLoc = new Posn(
                    new Random().nextInt(ForbiddenIslandWorld.ISLAND_SIZE),
                    new Random().nextInt(ForbiddenIslandWorld.ISLAND_SIZE));
            c = board.find(new PosnEqual(curLoc));
        }
        this.loc = curLoc;
    }

    //Render/draw this part on the board
    WorldImage drawPart(WorldImage bg) {
        return bg.overlayImages(new DiskImage(new Posn(this.loc.x 
                * ForbiddenIslandWorld.PIXEL_SIZE
                + ForbiddenIslandWorld.PIXEL_SIZE / 2, this.loc.y
                * ForbiddenIslandWorld.PIXEL_SIZE
                + ForbiddenIslandWorld.PIXEL_SIZE / 2), 
                ForbiddenIslandWorld.PIXEL_SIZE / 2, 
                new Color(255, 0, 0)));
    }
}

//to represent a helicopter + its location
class Helicopter {
    //location
    Posn loc;

    //Constructs a helicopter & its loc
    Helicopter(Posn loc) {
        this.loc = loc;
    }

    //Construct a helicopter at the farthest distance above
    //sea level, TO BE IMPLEMENTED LATER

    //draw/render this helicopter
    WorldImage drawHelicopter(WorldImage bg, String src) {
        return bg.overlayImages(new FromFileImage(new Posn(this.loc.x
                * ForbiddenIslandWorld.PIXEL_SIZE
                + ForbiddenIslandWorld.PIXEL_SIZE / 2, this.loc.y
                * ForbiddenIslandWorld.PIXEL_SIZE
                + ForbiddenIslandWorld.PIXEL_SIZE / 2),
                src));
    }
}

//PREDICATE CLASS TO DETERMINE IF THIS OBJECTS
//POSN IS EQUAL TO A CELLS
class PosnEqual implements IPred<Cell> {
    Posn loc;

    PosnEqual(Posn loc) {
        this.loc = loc;
    }

    // Determines if this's posn is the same as the posn of the given cell
    public boolean apply(Cell c) {
        return c.x == loc.x && c.y == loc.y;
    }
}
