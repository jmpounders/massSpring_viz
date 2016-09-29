class Box {
  color c;
  float xpos0;
  float dxpos;
  float ypos;
  float w;
  
  Box(color tempc, float tempxpos, float tempypos, float tempwidth) {
    c = tempc;
    xpos0 = tempxpos;
    ypos = tempypos;
    dxpos = 0.0;
    w = tempwidth;
  }
  
  void setDisplacement(float tempxpos) {
    dxpos = tempxpos;
  }
  
  float getCenter() {
    return xpos0+dxpos;
  }
  
  float getHeight() {
    return w+w/2;
  }
  
  void display() {
    stroke(0);
    fill(c);
    rectMode(CENTER);
    rect(xpos0+dxpos,ypos,w,w);
    
    fill(color(0,0,0));
    ellipse(xpos0+dxpos-w/4,ypos+w/2.0+w/8,w/4,w/4);
    ellipse(xpos0+dxpos+w/4,ypos+w/2.0+w/8,w/4,w/4);
  }
}