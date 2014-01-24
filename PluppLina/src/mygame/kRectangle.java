package mygame;

public class kRectangle {
    public float x;
    public float y;
    public float w;
    public float h;
    
    public kRectangle(
            float _x,
            float _y,
            float _w,
            float _h) {
        x = _x;
        y = _y;
        w = _w;
        h = _h;
    }
    
    public float Left()
        { return x-w/2; }
    public float Right()
        { return x+w/2; }
    public float Top()
        { return y-h/2; }
    public float Bottom()
        { return y+h/2; }
}
