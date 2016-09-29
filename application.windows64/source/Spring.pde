class Spring {
  float nTurns;
  float h;
  float b;
  float xa,xb,y;
  int res = 100;
  
  Spring(float tempnTurns, float temph, float tempb, float tempy) {
    nTurns = tempnTurns;
    h = temph;
    b = tempb;
    y = tempy;
  }
  
  void setPos(float xain,float xbin) {
    xa = xain;
    xb = xbin;
  }
  
  void display() {
    stroke(0);
    line(xa,y,xa+b,y);
    float sx = xb-xa-2.0*b;
    float x0 = xa+b;
    float dx = sx/res;
    for (int i=0; i<res; i++) {
      line(x0,y+h*sin(3.14159*i/res*nTurns),x0+dx,y+h*sin(3.14159*(i+1)/res*nTurns));
      x0 += dx;
    }
    line(xb-b,y,xb,y);
  }
}