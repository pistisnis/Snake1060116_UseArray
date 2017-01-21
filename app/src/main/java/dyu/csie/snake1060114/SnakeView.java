package dyu.csie.snake1060114;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Alpha on 2017/1/14.
 */

public class SnakeView extends View {
    int NN=0, NA=0;
    int [] x100;
    int [] y100;
    int []ax={1,2,3,4,5};
    int []ay={1,2,3,4,5};
    int posx, posy, fixx, fixy, dir=1, nextdir=1, nowdir=1;
    Paint paintR, paintG, paintB, paintY, paintBlack, paintP;
    Paint[] paint6;
    ArrayList<Coordinate> mSnake  =new ArrayList<Coordinate>();
    ArrayList<Coordinate> mFruit  =new ArrayList<Coordinate>();
    int mNextDirection, mDirection;
    Random RNG = new Random();
    long mMoveDelay = 1000;
    long mLastMove;
    int ii=10, jj=10, oldii,oldjj;
    boolean updateView=true;
    BallRun  brun;
    int mTileSize=24;
    int[][] mTileGrid;
    int mXTileCount, mYTileCount, mXOffset, mYOffset ;
    Coordinate head, newHead;
    public SnakeView(Context context) {
        super(context);
        init();
    }
    void init(){
        this.setOnClickListener(listener);
        this.setOnTouchListener(touch);

        NN=0;
        mTileGrid=new int [60][80];
        brun=new BallRun();
        //設定畫筆
        paint6=new Paint[6];
        paintBlack=new Paint(); paintBlack.setColor(Color.BLACK);
        paintR=new Paint(); paintR.setColor(Color.RED);
        paintG=new Paint(); paintG.setColor(Color.GREEN);
        paintB=new Paint(); paintB.setColor(Color.BLUE);
        paintY=new Paint(); paintY.setColor(Color.YELLOW);
        paintP=new Paint(); paintP.setColor(Color.MAGENTA);
        paintBlack.setStrokeWidth(1);
        paintBlack.setStyle(Paint.Style.STROKE);
        paint6[0]=paintBlack;
        paint6[1]=paintR;  paint6[2]=paintG;
        paint6[3]=paintB;paint6[4]=paintY; paint6[5]=paintP;

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int x,y;
        super.onSizeChanged(w, h, oldw, oldh);
        mXTileCount = (int) Math.floor(w / mTileSize);
        mYTileCount = (int) Math.floor(h / mTileSize);
        if(mXTileCount>59) mXTileCount=59;
        if(mYTileCount > 79 ) mYTileCount=79;
        mXOffset = ((w - (mTileSize * mXTileCount)) / 2);
        mYOffset = ((h - (mTileSize * mYTileCount)) / 2);
        y=mYTileCount-10; x=mXTileCount/2;
        fixx=mXOffset + x * mTileSize;
        fixy=mYOffset + y * mTileSize;
        clearTiles();
        initNewGame();
    }

    void initNewGame(){
        mSnake.clear();
        mFruit.clear();

        NN = 0;

      //  mSnake.add(new Coordinate(5,7)); mTileGrid[5][7]=1;
      //  mSnake.add(new Coordinate(4,7)); mTileGrid[4][7]=2;
      //  mSnake.add(new Coordinate(3,7)); mTileGrid[3][7]=3;
      //  mSnake.add(new Coordinate(2,7)); mTileGrid[2][7]=1;
        nowdir=1; nextdir=1;
        addRandomApple();
        addRandomApple(); //
        mMoveDelay = 1000;
        mLastMove=System.currentTimeMillis();
        Log.d("initGame","X=" + mXTileCount+ " Y="+ mYTileCount );
    }

    void addRandomApple() {
        int newX=0, newY=0;
       // Coordinate newCoord = null;
        boolean found = false;
        while (!found) {
            // Choose a new location for our apple
             newX = 1 + RNG.nextInt(mXTileCount - 2);
             newY = 1 + RNG.nextInt(mYTileCount - 2);
           // newCoord = new Coordinate(newX, newY);

            //簡檫是否為蛇的為
            boolean collision = false;
//            int snakelength = mSnake.size();
//            for (int index = 0; index < snakelength; index++) {
//                if (mSnake.get(index).equals(newCoord)) {
//                    collision = true;
//                }
//            }
            for(int i=0;i<NN;i++){
                if(x100[i]==newX && y100[i]==newY) {
                    collision = true;
                    break;
                }
            }
            found = !collision;
        }
        // mFruit.add(newCoord);
        ax[NA]=newX; ay[NA]=newY; NA++;
    }
    public void clearTiles() {
        for (int x = 0; x < mXTileCount; x++) {
            for (int y = 0; y < mYTileCount; y++) {
                mTileGrid[x][y]=0;
            }
        }

    }
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        updateView = true;
        postDelayed(brun, mMoveDelay);
    }

    private View.OnClickListener listener= new OnClickListener() {
        @Override
        public void onClick(View view) {
            int dx, dy, dx1,dy1;
            //int[] values = new int[2];
            //view.getLocationOnScreen(values);
            //Log.d("Click:", "x=" +values[0]+"  y="+ values[1]);
            Log.d("Click:", "x=" +posx+"  y="+ posy);
            dx=posx-fixx; dy=posy-fixy;
            dx1=Math.abs(dx); dy1=Math.abs(dy);
            if(dx1>dy1){ // 右或左
                if(dx>=0) dir=1; //往右
                else dir=2;  //往左
            }
            else{ //上或下
                if(dy>=0) dir=3; //往下
                else dir=4;  //往上左
            }
            if(nowdir<=2){
                if(dir<=2 ) dir=nowdir;
            }
            else{ //nodir 3 , 4
                if(dir>=3) dir=nowdir;
            }
            nextdir=dir;

            // update();
            oldii=ii; oldjj=jj; ///先存上一步座標
            if(dir==1)  ii++;//往右
            else if(dir==2)  ii --;//往左
            else if(dir==3) jj++;//往下
            else jj --;//往上
            mTileGrid[ii][jj]=1;//新座標用畫筆1
            mTileGrid[oldii][oldjj]=2;//舊座標用畫筆2

            update();

            nowdir=nextdir;
            SnakeView.this.invalidate();

            //  Message msg=new Message();
            //  hand.sendMessage( msg);
        }
    };

    void  update(){
        updateSnake();
        updateApples();
    }

    void updateApples(){
//        for (Coordinate c : mFruit) {
//            mTileGrid[c.x][c.y]=4; ;   //使用畫筆4
//        }
        for(int i=0;i<NA;i++)
            mTileGrid[ax[i]][ay[i]] = 4; //使用畫筆4
    }

    void updateSnake0(){
        //設定蛇方塊顏色
        int index = 0;
        for (Coordinate c : mSnake) {
            if (index == 0) {
                mTileGrid[c.x][c.y]=1; //使用畫筆1
            } else {
                mTileGrid[c.x][c.y]=2; //使用畫筆2
            }
            index++;
        }
    }
    void updateSnake(){
        int x, y; //, newx, newy;
        boolean bgrow=false;
       // Coordinate c1;
        x=x100[0]; y=y100[0];
       // head=mSnake.get(0);//抓出蛇頭
       // x=head.x; y=head.y;
        if(nextdir==1)  {
            x++; if(x>mXTileCount-1)  x=0;
        }
        else if(nextdir==2) {
            x--; if(x<0) x=mXTileCount-1;
        }
        else if(nextdir==3) {
            y++; if(y>mYTileCount-1) y=0;
        }
        else if(nextdir==4) {
            y--; if(y<0) y=mYTileCount -1;
        }
       // newHead = new Coordinate(x,y);
        // Look for apples
        int applecount = NA;
        NA=0;
        for (int index = 0; index < applecount; index++) {
            if (ax[index] == x && ay[index] == y) {
                bgrow = true;
                mTileGrid[x][y] = 0;
            }
            else{
                ax[NA]=ax[index]; ay[NA]=ay[index];
                NA ++;
            }
        }
        while(NA<2) addRandomApple();

        for(int i=NN-1;i>=0;i--){
            x100[i+1]=x100[i]; y100[i+1]=y100[i];
        }
        x100[0]=x; y100[0]=y;

        if (bgrow && NN<50) {
           NN++;
        }
        else{
            x=x100[NN]; y=y100[NN];
            mTileGrid[  x ]  [ y ]=0;
        }

        for(int i=0;i<NN;i++){
            x=x100[i]; y=y100[i];
            if(i==0) mTileGrid[  x ]  [ y ]=1;  //使用畫筆1
            else mTileGrid[  x ]  [ y ]=2; //使用畫筆2
        }
    }

    View.OnTouchListener touch= new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                posx=(int) motionEvent.getX(); posy=(int) motionEvent.getY();
            }
            return false;
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        int x,y, x1,x2, y1,y2;
        super.onDraw(canvas);
        for (x = 0; x < mXTileCount; x += 1) {
            for (y = 0; y < mYTileCount; y += 1) {
                // if(mTileGrid[x][y]>0)
                x1=mXOffset + x * mTileSize;  x2=x1+mTileSize;
                y1=mYOffset + y * mTileSize;  y2=y1+mTileSize;
                canvas.drawRect(x1, y1, x2, y2, paint6[ mTileGrid[x][y]  ] );
            }
        }

        canvas.drawRect(fixx, fixy, fixx+mTileSize, fixy+mTileSize, paint6[ 5  ] );

    }

    class Coordinate{
        public int x;
        public int y;
        public Coordinate(int x1, int y1){
            x=x1; y=y1;
        }
        public boolean equals(Coordinate other){
            return x == other.x && y == other.y;
        }
    } //Coordinate

    private class BallRun  implements Runnable {
        public void run() {
            long now = System.currentTimeMillis();
            update();
           // Log.d("run", "time:"+now);
            invalidate();  //要求重畫
            if (updateView) { //若為true, 則隔 0.2秒 自我呼叫
                //postDelayed(this, DELAY_TIME_MILLIS);
                postDelayed(brun, mMoveDelay);
            }
        }//run
    }//BallRun

}
