public class RecoloringInstruction {
    private int x;
    private int y;
    private char newColor;

    public RecoloringInstruction(int x, int y, char newColor) {
        this.x = x;
        this.y = y;
        this.newColor = newColor;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public char getNewColor() {
        return newColor;
    }
}
