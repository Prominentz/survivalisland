//Ralph Perricelli
//Muigai Unaka
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import javalib.impworld.*;
import javalib.worldimages.FrameImage;
import javalib.worldimages.Posn;
import javalib.worldimages.TextImage;
import javalib.worldimages.WorldImage;

//to represent the forbidden island world
class ForbiddenIslandWorld extends World {
    //Cells within our canvas that make up the game board
    IList<Cell> board;

    //Game Constants/variables
    //players
    Player player;
    Player player2; //TO BE ADDED FOR PT 2
    //list of helicopter parts
    IList<Part> parts;
    //Helicopter
    Helicopter heli;
    //height of ocean
    int waterHeight;
    //time counter
    int time;
    //score counter
    int score;
    //is the game paused, flag
    boolean paused;
    // the number of tiles on one side of island
    static final int ISLAND_SIZE = 64;
    // half of the number of tiles on a given size
    static final double HALF_ISLAND_SIZE = 
            ((double) ISLAND_SIZE) / 2 - .5;
    // size of the side of a single cell on the board in pixels
    static final int PIXEL_SIZE = 10;
    // number of parts to be generated in the beginning of the game
    static final int NUM_PARTS = 4; 

    //empty world constructor/ set all fields to arbitrary nums
    ForbiddenIslandWorld() {
        this.board = new Empty<Cell>();
        this.player = new Player(new Posn(0, 0));
        this.player2 = new Player(new Posn(0, 0));
        this.parts = new Empty<Part>();
        this.heli = new Helicopter(new Posn(0, 0));
        this.waterHeight = 0;
        this.time = 0;
        this.score = 0;
        this.paused = false;        
    }

    //World constructor with all fields given values
    ForbiddenIslandWorld(IList<Cell> board, Player player, Player player2, Helicopter heli,
            IList<Part> parts, int waterHeight, int time, int score, boolean paused) {
        this.board = board;
        this.player = player;
        this.player2 = player2;
        this.heli = heli;
        this.parts = parts;
        this.waterHeight = waterHeight;
        this.time = time;
        this.score = score;
        this.paused = paused;
    }

    //World constructor for a starting world w random player/part/helicopter locs
    //TO BE IMPLEMENTED LATER

    //build a new list of parts at rand locartions
    IList<Part> genLoc(int n) {
        IList<Part> list = new Empty<Part>();
        for (int i = 0; i < n; i += 1) {
            list = new Cons<Part>(new Part(this.board), list);
        }
        return list;
    }

    // Key handler for this world
    // make a new world based on the key pressed and the state of the current
    // world
    public void onKeyEvent(String ke) {
        IList<Cell> cells = this.board;

        // Generates new mountain board
        if (ke.equals("m")) {
            ArrayList<ArrayList<Double>> heights = this.buildMountain();
            cells = this.array2IList(this.double2Cell(heights));
            this.board = cells;
            this.waterHeight = 0;
            //this.player = new Player(cells);
            //this.player2 = new Player(cells);
            //this.heli = new Helicopter(cells);
            this.parts = this.genLoc(NUM_PARTS);
        }
        // Generates new random mountain board
        else if (ke.equals("r")) {
            ArrayList<ArrayList<Double>> heights = this
                    .buildRandMountain();
            cells = this.array2IList(this.double2Cell(heights));
            this.board = cells;
            this.waterHeight = 0;
            //this.player = new Player(cells);
            //this.player2 = new Player(cells);
            //this.heli = new Helicopter(cells);
            //this.parts = this.genLoc(NUM_PARTS);
        }
        // Generates new random terrain board
        else if (ke.equals("t")) {
            ArrayList<ArrayList<Double>> heights = this
                    .buildRandTerrain();
            cells = this.array2IList(this.double2Cell(heights));
            this.board = cells;
            this.waterHeight = 0;
            //this.player = new Player(cells);
            //this.player2 = new Player(cells);
            //this.heli = new Helicopter(cells);
            this.parts = this.genLoc(NUM_PARTS);
        }
        // Moves player left by one cell
        else if (ke.equals("left")) {
            if (!this.paused) {
                this.player = this.player.move("left", this.board);
                this.score = this.score + 1;
            }
        }
        // Moves player right by one cell
        else if (ke.equals("right")) {
            if (!this.paused) {
                this.player = this.player.move("right", this.board);
                this.score = this.score + 1;
            }
        }
        // Moves player up by one cell
        else if (ke.equals("up")) {
            if (!this.paused) {
                this.player = this.player.move("up", this.board);
                this.score = this.score + 1;
            }
        }
        // Moves player down by one cell
        else if (ke.equals("down")) {
            if (!this.paused) {
                this.player = this.player.move("down", this.board);
                this.score = this.score + 1;
            }
        }
        // Moves player2 down by one cell
        else if (ke.equals("s")) {
            if (!this.paused) {
                this.player2 = this.player2.move("down", this.board);
                this.score = this.score + 1;
            }
        }
        // Moves player2 up by one cell
        else if (ke.equals("w")) {
            if (!this.paused) {
                this.player2 = this.player2.move("up", this.board);
                this.score = this.score + 1;
            }
        }
        // Moves player2 left by one cell
        else if (ke.equals("a")) {
            if (!this.paused) {
                this.player2 = this.player2.move("left", this.board);
                this.score = this.score + 1;
            }
        }
        // Moves player2 right by one cell
        else if (ke.equals("d")) {
            if (!this.paused) {
                this.player2 = this.player2.move("right", this.board);
                this.score = this.score + 1;
            }
        }
        // Pauses/unpauses the game
        else if (ke.equals("p")) {
            this.paused = !this.paused;
        }
    }

    //update world 
    public void onTick() {
        IList<Part> curParts = parts;

        if (!this.paused) {
            //flood every 10 ticks
            int worldTime = (this.time + 1) % 10;
            //if player collects part, remove from list
            for (Part p : parts) {
                if (this.player.samePosition(p.loc)
                        || this.player2.samePosition(p.loc)) {
                    curParts = curParts.remove(p);
                }
            }
            this.parts = curParts;
            //increase waterhight every 10 ticks
            if (worldTime == 9) {
                int height = this.waterHeight + 1;

                //if call height is below water height then flood it
                //if not then leave it
                for (Cell c : this.board) {
                    if (c.height <= height && !c.nextToLand()) {
                        c.floodConditions(height);
                    }
                }

                this.waterHeight = height;
                this.time = worldTime;
            }
            else {

                this.time = worldTime;
            }
        }
    }

    //To DETERMINE IF WORLD IS ENDED OR NOT
    //TO BE IMPLEMENTED IN SECOND PART

    //Render/draw current world
    public WorldImage makeImage() {
        //background
        WorldImage image = new FrameImage(new Posn((int) HALF_ISLAND_SIZE
                * PIXEL_SIZE, (int) HALF_ISLAND_SIZE * PIXEL_SIZE), ISLAND_SIZE
                * PIXEL_SIZE, ISLAND_SIZE * PIXEL_SIZE,
                new Color(255, 255, 255));

        //draw all cells on board
        for (Cell c : this.board) {
            image = c.drawCell(image, this.waterHeight);
        }

        //draw player 1 
        image = this.player.drawPlayer(image, "pilot-icon.png");

        //draw player 2
        image = this.player2.drawPlayer(image, "pilot.png");

        //draw helicopter
        image = this.heli.drawHelicopter(image, "helicopter.png");

        //draw parts
        for (Part p : this.parts) {
            image = p.drawPart(image);
        }

        //draw score
        image = image.overlayImages(new TextImage(new Posn(PIXEL_SIZE * 2,
                PIXEL_SIZE * 2), new Integer(this.score).toString(),
                PIXEL_SIZE * 2, new Color(255, 255, 255)));

        return image;
    }

    //build a new mountain matrix with uniform height
    ArrayList<ArrayList<Double>> buildMountain() {
        ArrayList<ArrayList<Double>> mtnHeight = new ArrayList<ArrayList<Double>>(
                ISLAND_SIZE);

        for (int i = 0; i < ISLAND_SIZE; i += 1) {
            ArrayList<Double> row = new ArrayList<Double>(ISLAND_SIZE);

            for (int j = 0; j < ISLAND_SIZE; j += 1) {
                double height = HALF_ISLAND_SIZE
                        - Math.abs(HALF_ISLAND_SIZE - i)
                        - Math.abs(HALF_ISLAND_SIZE - j);
                row.add(height);
            }

            mtnHeight.add(row);
        }
        return mtnHeight;
    }

    //build a matrix of random heights
    ArrayList<ArrayList<Double>> buildRandMountain() {
        ArrayList<ArrayList<Double>> mountain = new ArrayList<ArrayList<Double>>(
                ISLAND_SIZE);

        for (int i = 0; i < ISLAND_SIZE; i += 1) {
            ArrayList<Double> row = new ArrayList<Double>(ISLAND_SIZE);

            for (int j = 0; j < ISLAND_SIZE; j += 1) {
                double height = HALF_ISLAND_SIZE
                        - Math.abs(HALF_ISLAND_SIZE - i)
                        - Math.abs(HALF_ISLAND_SIZE - j);

                if (height > 0) {
                    height = new Random().nextInt((int) (HALF_ISLAND_SIZE - 1)) + 1;
                }

                row.add(height);
            }
            mountain.add(row);
        }
        return mountain;
    }

    //build new terrain using algorithm from hw
    ArrayList<ArrayList<Double>> buildRandTerrain() {
        ArrayList<ArrayList<Double>> mountain = new ArrayList<ArrayList<Double>>(
                ISLAND_SIZE);
        for (int i = 0; i < ISLAND_SIZE; i += 1) {
            ArrayList<Double> row = new ArrayList<Double>(ISLAND_SIZE);
            for (int j = 0; j < ISLAND_SIZE; j += 1) {
                if (Math.abs(i - HALF_ISLAND_SIZE) <= .5
                        && Math.abs(j - HALF_ISLAND_SIZE) <= .5) {
                    row.add(HALF_ISLAND_SIZE);
                }
                else if ((Math.abs(i - HALF_ISLAND_SIZE) <= .5 && Math.abs(j
                        - HALF_ISLAND_SIZE) >= HALF_ISLAND_SIZE)
                        || (Math.abs(i - HALF_ISLAND_SIZE) >= HALF_ISLAND_SIZE && Math
                                .abs(j - HALF_ISLAND_SIZE) <= .5)) {
                    row.add(0.0);
                }
                else {
                    row.add(0.0);
                }
            }
            mountain.add(row);
        }
        int lowerbound = (int) Math.floor(HALF_ISLAND_SIZE);
        int upperbound = (int) Math.ceil(HALF_ISLAND_SIZE);
        this.updateTerrain(mountain, 0, 0, upperbound, ISLAND_SIZE / 2);
        this.updateTerrain(mountain, 0, lowerbound, upperbound,
                ISLAND_SIZE - 1);
        this.updateTerrain(mountain, lowerbound, 0, ISLAND_SIZE - 1,
                upperbound);
        this.updateTerrain(mountain, lowerbound, lowerbound, ISLAND_SIZE - 1,
                ISLAND_SIZE - 1);
        return mountain;
    }

    //helper method for updating random terrain
    // EFFECT: recursively updates the height matrix of the terrain
    void updateTerrain(ArrayList<ArrayList<Double>> mountain, int topleftX,
            int topleftY, int botrightX, int botrightY) {
        if (((botrightX - topleftX) >= 2) || ((botrightY - topleftY) >= 2)) {
            Random rand = new Random();
            double scalar = .15 * Math.sqrt(botrightX - topleftX)
                    * (botrightY - topleftY);
            double tl = mountain.get(topleftY).get(topleftX);
            double tr = mountain.get(topleftY).get(botrightX);
            double bl = mountain.get(botrightY).get(topleftX);
            double br = mountain.get(botrightY).get(botrightX);
            int midX = (topleftX + botrightX) / 2;
            int midY = (topleftY + botrightY) / 2;
            double t = (tl + tr) / 2 + (rand.nextDouble() - .8) * scalar;
            double b = (bl + br) / 2 + (rand.nextDouble() - .8) * scalar;
            double l = (tl + bl) / 2 + (rand.nextDouble() - .8) * scalar;
            double r = (tr + br) / 2 + (rand.nextDouble() - .8) * scalar;
            double m = (tl + tr + bl + br) / 4 + (rand.nextDouble() - .8)
                    * scalar;
            ArrayList<Double> top = mountain.get(topleftY);
            ArrayList<Double> mid = mountain.get(midY);
            ArrayList<Double> bot = mountain.get(botrightY);
            if (top.get(midX) != 0) {
                top.set(midX, (t + top.get(midX)) / 2);
            }
            else {
                top.set(midX, t);
            }
            if (mid.get(topleftX) != 0) {
                mid.set(topleftX, (l + mid.get(topleftX)) / 2);
            }
            else {
                mid.set(topleftX, l);
            }
            if (mid.get(midX) != 0) {
                mid.set(midX, (m + mid.get(midX)) / 2);
            }
            else {
                mid.set(midX, m);
            }
            if (mid.get(botrightX) != 0) {
                mid.set(botrightX, (r + mid.get(botrightX)) / 2);
            }
            else {
                mid.set(botrightX, r);
            }
            if (bot.get(midX) != 0) {
                bot.set(midX, (b + bot.get(midX)) / 2);
            }
            else {
                bot.set(midX, b);
            }
            mountain.set(topleftY, top);
            mountain.set(midY, mid);
            mountain.set(botrightY, bot);
            this.updateTerrain(mountain, topleftX, topleftY, midX, midY);
            this.updateTerrain(mountain, topleftX, midY, midX, botrightY);
            this.updateTerrain(mountain, midX, topleftY, botrightX, midY);
            this.updateTerrain(mountain, midX, midY, botrightX, botrightY);
        }
    }

    // Turn a matrix of heights into a matrix and connect themupdateTerrain
    ArrayList<ArrayList<Cell>> double2Cell(ArrayList<ArrayList<Double>> arr) {
        ArrayList<ArrayList<Cell>> cells = new ArrayList<ArrayList<Cell>>(
                ISLAND_SIZE);
        for (int i = 0; i < ISLAND_SIZE; i += 1) {
            ArrayList<Cell> row = new ArrayList<Cell>(ISLAND_SIZE);
            for (int j = 0; j < ISLAND_SIZE; j += 1) {
                Cell c;
                if (arr.get(i).get(j) <= 0) {
                    c = new OceanCell(i, j);
                }
                else {
                    c = new Cell(arr.get(i).get(j), i, j);
                }
                if (i != 0) {
                    c.updateTop(cells.get(i - 1).get(j));
                }
                else {
                    c.updateOnlyTop(c);
                }
                if (j != 0) {
                    c.updateLeft(row.get(j - 1));
                }
                else {
                    c.updateOnlyLeft(c);
                }
                if (i >= ISLAND_SIZE - 1) {
                    c.updateBottom(c);
                }
                if (j >= ISLAND_SIZE - 1) {
                    c.updateRight(c);
                }
                row.add(c);
            }
            cells.add(row);
        }

        return cells;

    }

    //Matrix to single list of cells for rendering
    IList<Cell> array2IList(ArrayList<ArrayList<Cell>> arr) {
        IList<Cell> cells = new Empty<Cell>();
        for (ArrayList<Cell> row : arr) {
            for (Cell c : row) {
                cells = new Cons<Cell>(c, cells);
            }
        }
        return cells;
    }
}