public class RecoloringInstruction {
    private int x;
    private int y;
    private char newColor;

    public RecoloringInstruction(int x, int y, char newColor) {
        this.x = x;
        this.y = y;
        this.newColor = newColor;
    }

    /**
     * Returns the x-coordinate of the pixel to recolor.
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }


    /**
     * Returns the y-coordinate of the pixel to recolor.
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }




    /**
     * Returns the new color of the pixel to recolor.
     * @return the new color
     */
    public char getNewColor() {
        return newColor;
    }
}
