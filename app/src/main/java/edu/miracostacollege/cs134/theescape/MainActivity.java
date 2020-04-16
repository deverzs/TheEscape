package edu.miracostacollege.cs134.theescape;

import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import edu.miracostacollege.cs134.theescape.model.Direction;
import edu.miracostacollege.cs134.theescape.model.Player;
import edu.miracostacollege.cs134.theescape.model.Zombie;

import static edu.miracostacollege.cs134.theescape.model.BoardValues.EXIT;
import static edu.miracostacollege.cs134.theescape.model.BoardValues.FREE;
import static edu.miracostacollege.cs134.theescape.model.BoardValues.OBST;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener{

    private int wins = 0;
    private int losses = 0;

    public static final int TOTAL_ROWS = 8;
    public static final int TOTAL_COLS = 8;

    public static final int PLAYER_ROW = 1;
    public static final int PLAYER_COL = 1;

    public static final int ZOMBIE_ROW = 2;
    public static final int ZOMBIE_COL = 4;

    public static final int EXIT_ROW = 5;
    public static final int EXIT_COL = 7;

    private static final float FLING_THRESHOLD = 500f;

    private LinearLayout boardLinearLayout;
    private TextView winsTextView;
    private TextView lossesTextView;
    private GestureDetector gestureDetector;

    private Player player;
    //can be array of zombies
    private Zombie zombie;

    final int gameBoard[][] = {
            {OBST, OBST, OBST, OBST, OBST, OBST, OBST, OBST},
            {OBST, FREE, FREE, FREE, FREE, FREE, OBST, OBST},
            {OBST, FREE, OBST, FREE, FREE, FREE, FREE, OBST},
            {OBST, FREE, OBST, FREE, FREE, FREE, FREE, OBST},
            {OBST, FREE, FREE, FREE, FREE, FREE, OBST, OBST},
            {OBST, FREE, FREE, FREE, FREE, FREE, FREE, EXIT},
            {OBST, FREE, OBST, FREE, FREE, FREE, FREE, OBST},
            {OBST, OBST, OBST, OBST, OBST, OBST, OBST, OBST}
    };

    ImageView viewBoard[][] = new ImageView[TOTAL_ROWS][TOTAL_COLS];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boardLinearLayout = findViewById(R.id.boardLinearLayout);
        winsTextView = findViewById(R.id.winsTextView);
        lossesTextView = findViewById(R.id.lossesTextView);

        //turn on gestureDetector
        gestureDetector = new GestureDetector(this, this) ; //this is wehere we wrote
        // the code  from here to here

        startNewGame();
    }

    private void startNewGame() {
        //1 row is 1 horizontal layout
        LinearLayout row;

        //DONE: Loop through the viewBoard and initialize each of the ImageViews
        //DONE: to the children of the LinearLayouts
        //DONE: Use the gameBoard to determine which image to assign:

        //DONE: OBST = R.drawable.obstacle
        //DONE: EXIT = R.drawable.exit
        //DONE: FREE = null (no image to load)

        //one loop thorugh i's 1 loop though j's

        //picks the linearLayout of boardLinearLayout
        for (int i = 0; i < TOTAL_ROWS ; i++) {
            row = (LinearLayout) boardLinearLayout.getChildAt(i) ;
            //picks the image view to enter
            for (int j = 0; j < TOTAL_COLS ; j++) {
                viewBoard[i][j] = (ImageView) row.getChildAt(j) ;
                //make decision on the board
                switch (gameBoard[i][j])
                {
                    case OBST:
                        //put obstacle drawable in image view
                        viewBoard[i][j].setImageResource(R.drawable.obstacle);
                        break;
                    case FREE:
                        //put NULL in the imageView
                        viewBoard[i][j].setImageDrawable(null);
                        break;
                    case EXIT:
                        viewBoard[i][j].setImageResource(R.drawable.exit);
                        break;

                }
            }

        }


        //DONE: Instantiate a new Player object at PLAYER_ROW, PLAYER_COL
        player = new Player(PLAYER_ROW, PLAYER_COL) ;

        //DONE: Set the imageView at that position to R.drawable.player
        viewBoard[player.getRow()][player.getCol()].setImageResource(R.drawable.female_player);
        //DONE: Instantiate a new Zombie object at ZOMBIE_ROW, ZOMBIE_COL
        zombie = new Zombie(ZOMBIE_ROW, ZOMBIE_COL) ;
        //DONE: Set the imageView at that position to R.drawable.zombie
        viewBoard[zombie.getRow()][zombie.getCol()].setImageResource(R.drawable.zombie);

        //DONE: Initialize the winsTextView and lossesTextView
        winsTextView.setText(getString(R.string.win, wins));
        lossesTextView.setText(getString(R.string.losses, losses));

    }

    private void movePlayer(float velocityX, float velocityY) {

        //TODO: Determine the direction of the fling (based on velocityX and velocityY)
        //move player THEN move zombie
        //fling gestrure initiates the move
        //velocity in X and Y, so there will almmost always be a wobble
        //comstant flingThreshold - if have a minor fling, I'm not going to move the player,
        //so has to be a descent swipe so not pick up on minute/accident move

        Direction direction = null;
        //whichever absolute value bigger determines direction
        float absX = Math.abs(velocityX) ;
        float absY = Math.abs(velocityY) ;

        //left or right
        if(absX > absY)
        {
            if(velocityX <= -FLING_THRESHOLD) //what constituters swipe
            {
                direction = Direction.LEFT ;
            }
            else if (velocityX >= FLING_THRESHOLD)
            {
                direction = Direction.RIGHT ;
            }
        }
        //else up or down
        else
        {
            if(velocityY <= -FLING_THRESHOLD) //what constituters swipe
            {
                direction = Direction.UP ;
            }
            else if (velocityY >= FLING_THRESHOLD)
            {
                direction = Direction.DOWN ;
            }
        }
        if(direction != null) {
            //DONE: The velocity must exceed FLING_THRESHOLD to count (otherwise, it's not really a move)
            //DONE: Set the player's current image view drawable to null
            viewBoard[player.getRow()][player.getCol()].setImageDrawable(null);
            //DONE: Move the player
            player.move(gameBoard, direction);

            //DONE: Set the player's current image view to R.drawable.player after the move
            viewBoard[player.getRow()][player.getCol()].setImageResource(R.drawable.female_player);
        }
    }

    private void moveZombie() {
        //DONE: Set the zombie's current image view drawable to null
        //zombie always moves!!

        viewBoard[zombie.getRow()][zombie.getCol()].setImageDrawable(null);

        zombie.move(gameBoard, player.getRow(), player.getCol());
        //DONE: Move the zombie
        //DONE: Set the zombie's current image view to R.drawable.zombie after the move
        viewBoard[zombie.getRow()][zombie.getCol()].setImageResource(R.drawable.zombie);
    }

    private void determineOutcome() {
        //DONE: Determine the outcome of the game (win or loss)
        //DONE: It's a win if the player's row/col is the same as the exit row/col
        //DONE: Call the handleWin() method

        //DONE: It's a loss if the player's row/col is the same as the zombie's row/col
        //DONE: Call the handleLoss() method

        //DONE: Otherwise, do nothing, just return.

        //after every move we have to see where we stand
        //iuf player winds, row and col match the row and col of exit
        if(player.getRow() == EXIT_ROW && player.getCol() == EXIT_COL)
        {
            handleWin();
        }
        else if (player.getRow() == zombie.getRow() && player.getCol() == zombie.getCol())
        {
            handleLoss();
        }
        //else keep playing

    }

    private void handleWin()
    {
        //TODO: Implement the handleWin() method by accomplishing the following:
        //TODO: Increment the wins
        wins++ ;
        //TODO: Set the imageView (at the zombie's row/col) to the R.drawable.bunny
        viewBoard[zombie.getRow()][zombie.getCol()].setImageResource(R.drawable.bunny);
        //TODO: Start an animation
        //use the animation rotate
       // viewBoard[player.getRow()][player.getCol()].startAnimation(rotateAnim); //make anim folder and add rotate.anim
        //TODO: Wait 2 seconds, then start a new game
        Handler handler = new Handler() ;
        handler.postDelayed(newGameRunnable, 2000) ;
    }

    private void handleLoss()
    {
        //TODO: Implement the handleLoss() method by accomplishing the following:
        //TODO: Increment the losses
        losses++ ;
        //TODO: Set the imageView (at the player's row/col) to the R.drawable.blood
        viewBoard[player.getRow()][player.getCol()].setImageResource(R.drawable.blood);
        //TODO: Start an animation
        //TODO: Wait 2 seconds, then start a new game
        Handler handler = new Handler() ;
        handler.postDelayed(newGameRunnable, 2000) ;
    }


    //playing the game until player-zombie or player-exit
    //animation
    //wait two seconds - Runnable waits until we want
    //start new game
    Runnable newGameRunnable = new Runnable() {
        @Override
        public void run() {
            startNewGame();
        }
    };

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    //only one we care about
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        movePlayer(velocityX, velocityY);
        moveZombie();
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return gestureDetector.onTouchEvent(event); //who to handle the touch event? gestureDetecor
    }
}
