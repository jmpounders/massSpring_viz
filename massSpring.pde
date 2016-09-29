Box box1, box2;
Spring spring1, spring2, spring3;

// Parameters:
// ======================
// k = 4 N/cm
// m = 1 kg
float omega1 = 2.0000;
float omega2 = 3.4641;

// To activate one of the three cases, uncomment
// the c1 and c2 parameters for that case.

// Case 1:
// x1(0) = 1, x2(0) = 0.5
float c1 = 1.0607;
float c2 = 0.3536;

// Case 2:
// x1(0) = 1, x2(0) = 1.0
//float c1 = 1.4142;
//float c2 = 0.0000;

// Case 3:
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
float tmax = 20.0;

float boxY;
float plotY;

void setup() {
  size(960,480);
  boxY = 1.8*height/3.0;
  plotY = 1.2*height/6.0;
  scale = width/24;
  boxWidth = width/12;
  leftWall = scale;
  rightWall = width-scale;
  box1 = new Box(color(0,0,255), width/3.0, boxY, boxWidth);
  box2 = new Box(color(255,0,0), width*2.0/3.0, boxY, boxWidth);
  spring1 = new Spring(10, boxWidth/2.0, 10.0, boxY);
  spring2 = new Spring(10, boxWidth/2.0, 10.0, boxY);
  spring3 = new Spring(10, boxWidth/2.0, 10.0, boxY);
  data = new float[nPoints];
}

float getBox1Pos(float time) {
  return scale*(c1*sin(omega1*time)+c2*sin(omega2*time));
}
float getBox2Pos(float time) {
  return scale*(c1*sin(omega1*time)-c2*sin(omega2*time));
}

void plotData(float time) {
  float dt = tmax/(nPoints-1);
  float y0 = plotY;
  float x0 = leftWall;
  float s = (rightWall-leftWall)/(nPoints-1);
  float ta = time-tmax/2.0;
  float tb = time+tmax/2.0;
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

void drawBounds() {
  stroke(0);
  float dy = box1.getHeight()/2.0;
  line(leftWall,boxY-dy,leftWall,boxY+dy);
  line(leftWall,boxY+dy,rightWall,boxY+dy);
  line(rightWall,boxY-dy,rightWall,boxY+dy);
}


void draw() {
  frameRate(fr);
  background(255);
  float time = float(frameCount)/fr;
  plotData(time);
  drawBounds();
  
  float x1 = getBox1Pos(time);
  float x2 = getBox2Pos(time);
  box1.setDisplacement( x1 );
  box2.setDisplacement( x2 );
  spring1.setPos( leftWall, box1.getCenter()-boxWidth/2.0 );
  spring2.setPos( box1.getCenter()+boxWidth/2.0, box2.getCenter()-boxWidth/2.0 );
  spring3.setPos( box2.getCenter()+boxWidth/2.0, rightWall );
  
  box1.display();
  box2.display();
  spring1.display();
  spring2.display();
  spring3.display();
  text("k=4 N/cm; m = 1 kg; x1(0)=1.0; x2(0)=0.5;\nTime = "+String.valueOf(time)+" sec", 10,height-30);
  //saveFrame();
}