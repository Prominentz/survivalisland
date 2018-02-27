//ralph perricelli
//Muigai Unaka
import java.awt.Color;
import javalib.worldimages.*;
import tester.*;
// Examples and tests for the forbidden island game
class ExamplesForbiddenIsland {

    // Examples of lists of integers
    IList<Integer> mtloi = new Empty<Integer>();
    IList<Integer> loi1 = new Cons<Integer>(1, this.mtloi);
    IList<Integer> loi2 = new Cons<Integer>(3, new Cons<Integer>(2, this.loi1));
    IList<Integer> loi3 = new Cons<Integer>(17, new Cons<Integer>(38, this.loi2));

    Cell cell1 = new Cell(3, 3, 3);
    Cell cell2 = new Cell(-2, 2, 2);
    Cell cell3 = new Cell(0, 1, 1);

    OceanCell ocean = new OceanCell(5, 5);
    OceanCell ocean2 = new OceanCell(4, 4);

    Player player1 = new Player(new Posn(0, 0));
    Player player2 = new Player(new Posn(0, 0));

    Part p1 = new Part(new Posn(1, 1));
    Part p2 = new Part(new Posn(3, 3));

    Helicopter h1 = new Helicopter(new Posn(150, 150));

    IList<Cell> mtcell = new Empty<Cell>();
    IList<Cell> loc1 = new Cons<Cell>(this.cell1, this.mtcell);
    IList<Cell> loc2 = new Cons<Cell>(this.cell3, new Cons<Cell>(this.cell2,
            this.loc1));

    WorldImage background = new FrameImage(new Posn(
            (int) ForbiddenIslandWorld.HALF_ISLAND_SIZE
            * ForbiddenIslandWorld.PIXEL_SIZE,
            (int) ForbiddenIslandWorld.HALF_ISLAND_SIZE
            * ForbiddenIslandWorld.PIXEL_SIZE),
            ForbiddenIslandWorld.ISLAND_SIZE * ForbiddenIslandWorld.PIXEL_SIZE,
            ForbiddenIslandWorld.ISLAND_SIZE * ForbiddenIslandWorld.PIXEL_SIZE,
            new Color(255, 255, 255));

    ForbiddenIslandWorld world1 = new ForbiddenIslandWorld();

    // resets this initial data to these initial states
    void reset() {
        cell1 = new Cell(3, 3, 3);
        cell2 = new Cell(-2, 2, 2);
        cell3 = new Cell(0, 1, 1);

        ocean = new OceanCell(5, 5);
        ocean2 = new OceanCell(4, 4);

        player1 = new Player(new Posn(10, 10));
        player2 = new Player(new Posn(2, 2));

        mtcell = new Empty<Cell>();
        loc1 = new Cons<Cell>(this.cell1, this.mtcell);
        loc2 = new Cons<Cell>(this.cell3, new Cons<Cell>(this.cell2, this.loc1));

        world1 = new ForbiddenIslandWorld();
    }

    /*TODO
     * methods to test:
     * WorldImage drawCell(WorldImage BG, double waterHeight)
    boolean nextToLand()
    boolean isOceanCell()
    WorldImage drawPlayer(WorldImage BG, String src) 
    boolean samePosition(Posn other)
    void updateLeft
    void updateTop
    void updateBottom
    void updateRight
    void updateOnlyTop
    void updateOnlyLeft
    void floodConditions(double height)
    WorldImage makeImage()
    On lists:
    asCons()
    length()
    hasNext()
    next()
    remove()
     * 
     */
    
    boolean testIsOceanCell(Tester t) {
        return
                t.checkExpect(ocean.isOceanCell(), true) &&
                t.checkExpect(cell1.isOceanCell(), false);
    }

    // test for asCons method
    boolean testAsCons(Tester t) {
        return t.checkException(new ClassCastException("empty lists can't be cons"),
                this.mtloi, "asCons") &&
                t.checkExpect(this.loi1.asCons(), this.loi1) &&
                t.checkExpect(this.loi2.asCons(), this.loi2) &&
                t.checkExpect(this.loi3.asCons(), this.loi3);
    }
    // tests for length method
    boolean testLength(Tester t) {
        return t.checkExpect(this.mtloi.length(), 0) &&
                t.checkExpect(this.loi1.length(), 1) && 
                t.checkExpect(this.loi2.length(), 3) &&
                t.checkExpect(this.loi3.length(), 5);
    }

    // tests for the remove method
    boolean testRemove(Tester t) {
        return t.checkExpect(this.mtcell.remove(this.cell1), this.mtloi) &&
                t.checkExpect(this.loc1.remove(this.cell1), this.mtloi) &&
                t.checkExpect(this.loc1.remove(this.cell2), this.loc1) &&
                t.checkExpect(this.loc2.remove(this.cell3), new Cons<Cell>(this.cell2,
                        this.loc1));
    }

    // tests the hasNext method
    boolean testHasNext(Tester t) {
        return t.checkExpect(new ListIterator<Cell>(this.mtcell).hasNext(), false) &&
                t.checkExpect(new ListIterator<Cell>(this.loc1).hasNext(), true) &&
                t.checkExpect(new ListIterator<Cell>(this.loc2).hasNext(), true);
    }

    // tests the next method
    boolean testNext(Tester t) {
        return t.checkExpect(new ListIterator<Cell>(this.loc1).next(), this.cell1) &&
                t.checkExpect(new ListIterator<Cell>(this.loc2).next(), this.cell3) &&
                t.checkExpect(new ListIterator<Integer>(this.loi1).next(), 1) &&
                t.checkExpect(new ListIterator<Integer>(this.loi2).next(), 3);
    }

    // test updateLeft
    void testUpdateLeft(Tester t) {
        this.reset();
        t.checkExpect(this.cell1.left, null);
        this.cell1.updateLeft(this.cell2);
        t.checkExpect(this.cell1.left, this.cell2);
        t.checkExpect(this.cell2.right, this.cell1);

        t.checkExpect(this.cell3.left, null);
        this.cell3.updateLeft(this.cell1);
        t.checkExpect(this.cell3.left, this.cell1);
        t.checkExpect(this.cell1.right, this.cell3);
    }

    // test updateTop
    void testUpdateTop(Tester t) {
        this.reset();
        t.checkExpect(this.cell1.top, null);
        this.cell1.updateTop(this.cell2);
        t.checkExpect(this.cell1.top, this.cell2);

        t.checkExpect(this.cell2.top, null);
        this.cell2.updateTop(this.cell3);
        t.checkExpect(this.cell2.top, this.cell3);
    }
    
 // test updateBottom
    void testUpdateBottom(Tester t) {
        this.reset();
        t.checkExpect(this.cell1.bottom, null);
        this.cell1.updateBottom(this.cell2);
        t.checkExpect(this.cell1.bottom, this.cell2);

        t.checkExpect(this.cell2.bottom, null);
        this.cell2.updateBottom(this.cell3);
        t.checkExpect(this.cell2.bottom, this.cell3);
    }


    //testing world constructor...
    /*// runs the game
    void testGame(Tester t) {
        //new random world
        ForbiddenIslandWorld world2 = new ForbiddenIslandWorld();
        world2.onKeyEvent("m");
        world2.bigBang(ForbiddenIslandWorld.ISLAND_SIZE
     * ForbiddenIslandWorld.PIXEL_SIZE,
                ForbiddenIslandWorld.ISLAND_SIZE
     * ForbiddenIslandWorld.PIXEL_SIZE, .1);

    }*/

    //runs the game
    void testGame(Tester t) {
        //new random world
        world1.onKeyEvent("m");
        world1.bigBang(ForbiddenIslandWorld.ISLAND_SIZE
                * ForbiddenIslandWorld.PIXEL_SIZE,
                ForbiddenIslandWorld.ISLAND_SIZE
                * ForbiddenIslandWorld.PIXEL_SIZE, .1);

    }
}