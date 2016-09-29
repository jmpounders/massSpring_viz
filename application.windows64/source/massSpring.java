import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class massSpring extends PApplet {

Box box1, box2;
Spring spring1, spring2, spring3;

// k = 4 N/cm
// m = 1 kg
float omega1 = 2.0000f;
float omega2 = 3.4641f;

// x1(0) = 1, x2(0) = 0.5
float c1 = 1.0607f;
float c2 = 0.3536f;
// x1(0) = 1, x2(0) = 1.0
//float c1 = 1.4142;
//float c2 = 0.0000;
// x1(0) = 1, x2(0) = -1.0
//float c1 = 0.0000;
//float c2 = 1.4142;

int fr = 50;
float scale;
float boxWidth;
float leftWall;
float rightWall;

float[] data;
int nPoints = 1000;
float tmax = 20.0f;

float boxY;
float plotY;

public void setup() {
  
  boxY = 1.8f*height/3.0f;
  plotY = 1.2f*height/6.0f;
  scale = width/24;
  boxWidth = width/12;
  leftWall = scale;
  rightWall = width-scale;
  box1 = new Box(color(0,0,255), width/3.0f, boxY, boxWidth);
  box2 = new Box(color(255,0,0), width*2.0f/3.0f, boxY, boxWidth);
  spring1 = new Spring(10, boxWidth/2.0f, 10.0f, boxY);
  spring2 = new Spring(10, boxWidth/2.0f, 10.0f, boxY);
  spring3 = new Spring(10, boxWidth/2.0f, 10.0f, boxY);
  data = new float[nPoints];
}

public float getBox1Pos(float time) {
  return scale*(c1*sin(omega1*time)+c2*sin(omega2*time));
}
public float getBox2Pos(float time) {
  return scale*(c1*sin(omega1*time)-c2*sin(omega2*time));
}

public void plotData(float time) {
  float dt = tmax/(nPoints-1);
  float y0 = plotY;
  float x0 = leftWall;
  float s = (rightWall-leftWall)/(nPoints-1);
  float ta = time-tmax/2.0f;
  float tb = time+tmax/2.0f;
  for (int i=0; i<nPoints-1; i++) {
    if (ta+(i+1)*dt<time) {
      strokeWeight(2);
      float cc = max(126,map(ta+(i+1)*dt, time-3,time, 126,255));
      stroke(color(0,0,cc));
      line(x0+s*i, y0-getBox1Pos(ta+i*dt), x0+s*(i+1), y0-getBox1Pos(ta+(i+1)*dt));
      stroke(color(cc,0,0));
      line(x0+s*i, y0-getBox2Pos(ta+i*dt), x0+s*(i+1), y0-getBox2Pos(ta+(i+1)*dt));
    }
    else {
      strokeWeight(2);
      stroke(color(0,0,126));
      line(x0+s*i, y0-getBox1Pos(ta+i*dt), x0+s*(i+1), y0-getBox1Pos(ta+(i+1)*dt));
      stroke(color(126,0,0));
      line(x0+s*i, y0-getBox2Pos(ta+i*dt), x0+s*(i+1), y0-getBox2Pos(ta+(i+1)*dt));
    }
  }
  noStroke();
  fill(color(0,0,255));
  ellipse(x0+(rightWall-leftWall)/2, y0-getBox1Pos(time), 8,8);
  fill(color(255,0,0));
  ellipse(x0+(rightWall-leftWall)/2, y0-getBox2Pos(time), 8,8);
  strokeWeight(1);
}

public void drawBounds() {
  stroke(0);
  float dy = box1.getHeight()/2.0f;
  line(leftWall,boxY-dy,leftWall,boxY+dy);
  line(leftWall,boxY+dy,rightWall,boxY+dy);
  line(rightWall,boxY-dy,rightWall,boxY+dy);
}


public void draw() {
  frameRate(fr);
  background(255);
  float time = PApplet.parseFloat(frameCount)/fr;
  plotData(time);
  drawBounds();
  
  float x1 = getBox1Pos(time);
  float x2 = getBox2Pos(time);
  box1.setDisplacement( x1 );
  box2.setDisplacement( x2 );
  spring1.setPos( leftWall, box1.getCenter()-boxWidth/2.0f );
  spring2.setPos( box1.getCenter()+boxWidth/2.0f, box2.getCenter()-boxWidth/2.0f );
  spring3.setPos( box2.getCenter()+boxWidth/2.0f, rightWall );
  
  box1.display();
  box2.display();
  spring1.display();
  spring2.display();
  spring3.display();
  text("k=4 N/cm; m = 1 kg; x1(0)=1.0; x2(0)=0.5;\nTime = "+String.valueOf(time)+" sec", 10,height-30);
  //saveFrame();
}
class Box {
  int c;
  float xpos0;
  float dxpos;
  float ypos;
  float w;
  
  Box(int tempc, float tempxpos, float tempypos, float tempwidth) {
    c = tempc;
    xpos0 = tempxpos;
    ypos = tempypos;
    dxpos = 0.0f;
    w = tempwidth;
  }
  
  public void setDisplacement(float tempxpos) {
    dxpos = tempxpos;
  }
  
  public float getCenter() {
    return xpos0+dxpos;
  }
  
  public float getHeight() {
    return w+w/2;
  }
  
  public void display() {
    stroke(0);
    fill(c);
    rectMode(CENTER);
    rect(xpos0+dxpos,ypos,w,w);
    
    fill(color(0,0,0));
    ellipse(xpos0+dxpos-w/4,ypos+w/2.0f+w/8,w/4,w/4);
    ellipse(xpos0+dxpos+w/4,ypos+w/2.0f+w/8,w/4,w/4);
  }
}
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
  
  public void setPos(float xain,float xbin) {
    xa = xain;
    xb = xbin;
  }
  
  public void display() {
    stroke(0);
    line(xa,y,xa+b,y);
    float sx = xb-xa-2.0f*b;
    float x0 = xa+b;
    float dx = sx/res;
    for (int i=0; i<res; i++) {
      line(x0,y+h*sin(3.14159f*i/res*nTurns),x0+dx,y+h*sin(3.14159f*(i+1)/res*nTurns));
      x0 += dx;
    }
    line(xb-b,y,xb,y);
  }
}
  public void settings() {  size(960,480); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "massSpring" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
