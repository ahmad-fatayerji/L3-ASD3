public class RecoloringInstruction {
    private int x;
    private int y;
    private char newColor;

    // O(1)
    public RecoloringInstruction(int x, int y, char newColor) {
        this.x = x;
        this.y = y;
        this.newColor = newColor;
    }
    
    // O(1)
    public int getX() {
        return x;
    }

    // O(1)
    public RecoloringInstruction() {
    }
    
    // O(1)
    public int getY() {
        return y;
    }

    // O(1)
    public char getNewColor() {
        return newColor;
    }
}
