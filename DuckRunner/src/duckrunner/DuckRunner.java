package duckrunner;
import processing.core.PApplet;
import processing.core.PImage;
import java.util.Random;
import java.lang.Thread;

public class DuckRunner extends PApplet {
    PImage penguin;
    PImage background;
    PImage obstacle;
    PImage flyingObstacle;

    boolean crouch = false;

    int penguinX = 70;
    int penguinY = 300;

    int bgX = 0;
    int bgY = 0;

    int obstacleX;
    int obstacleY = 330;

    int obstacle2X;

    int flyingObstacleX;
    int flyingObstacleY = 140;

    float speed = 9, flyingSpeed = 11;
    int jumpHeight;

    int startTime;
    int timer;

    float speedIncrease = .01f;
    float rateOfDifficulty = 5;

    int difficultyTime = 0;

    int score = 0;
    int highScore = 0;

    Random generator = new Random();

    enum GameState {
        GAMEOVER, RUNNING
    }

    static GameState currentState;

    public static void main(String[] args) {
        PApplet.main("duckrunner.DuckRunner");
    }

    public void settings() {
        size(700, 400);
    }

    public void setup() {

        penguin = loadImage("Images/penguin.png");
        background = loadImage("Images/Mountain.png");
        obstacle = loadImage("Images/Snowman.png");
        flyingObstacle = loadImage("Images/Snowball.png");
        obstacle.resize(100, 0);
        startTime = millis();
        currentState = GameState.RUNNING;
        flyingObstacle.resize(50, 0);
        obstacleX = pixelWidth + 10;
        obstacle2X = pixelWidth + generator.nextInt(pixelWidth - 300) + 300;
        if (obstacle2X - obstacleX <= 300){
            obstacle2X = obstacleX + 300;
        }
        penguin.resize(100, 0);
    }

    public void draw() {
        switch (currentState) {
            case RUNNING:
                drawBackground();
                createflyingObstacles();
                timer = (millis() - startTime) / 1000;
                drawScore();
                increaseDifficulty();
                drawPenguin();
                createObstacles();
                break;
            case GAMEOVER:
                drawGameOver();
                break;
        }



    }

    public void drawPenguin(){
        imageMode(CENTER);
        image(penguin, penguinX, penguinY);
        if (penguinY <= 300) {
            jumpHeight += 1;
            penguinY += jumpHeight;
        }
    }




    public void drawBackground(){
        imageMode(CORNER);
        image(background, bgX, bgY);
        image(background, bgX + background.width, 0);
        bgX -= speed;
        if (bgX <= (background.width*-1)) {
            bgX = 0;
        }
    }
    public void keyPressed(){
        if (key == ' '){
            if (currentState == GameState.RUNNING) {
                if (penguinY >= 300) {
                    jumpHeight = -16;
                    penguinY += jumpHeight;
                }

            }

            if (currentState == GameState.GAMEOVER) {
                obstacleX = pixelWidth + 10;
                obstacle2X = pixelWidth + generator.nextInt(300) + 300;
                if (obstacle2X - obstacleX <= 300){
                    obstacle2X = obstacleX + 300;
                }
                flyingObstacleX = background.pixelWidth;
                bgX = 0;
                startTime = millis();
                speed = 8;
                currentState = GameState.RUNNING;
            }
        }

        if (key == 'z'){
            penguin = loadImage("Images/penguinCrouch.png");
            penguin.resize(100, 0);
            crouch = true;
            speed -= 3;
        }
    }

    public void keyReleased(){
        if (key == 'z'){
            penguin = loadImage("Images/penguin.png");
            penguin.resize(100, 0);
            crouch = false;
            speed += 3;
        }
    }

    public void createObstacles() {
        imageMode(CENTER);
        image(obstacle, obstacleX, obstacleY);
        image(obstacle, obstacle2X, obstacleY);
        obstacleX -= (speed - 1);
        obstacle2X -= (speed - 1);
        if(obstacleX < 0) {
            obstacleX = obstacle2X;
            obstacle2X = pixelWidth + generator.nextInt(100) + 200;
            if (obstacle2X - obstacleX <= 300){
                obstacle2X = obstacleX + 300;
            }
        }
        if((abs(penguinX-obstacleX) < 78) && abs(penguinY-obstacleY)< 85 ) {
            score = timer;
            if(score > highScore) {
                highScore = score;
            }
            currentState = GameState.GAMEOVER;
        }
        if((abs(penguinX-obstacle2X) < 78) && abs(penguinY-obstacleY) < 85 ) {
            score = timer;
            if(score > highScore) {
                highScore = score;
            }
            currentState = GameState.GAMEOVER;
        }
    }

    public void createflyingObstacles() {
        imageMode(CENTER);
        image(flyingObstacle, flyingObstacleX, flyingObstacleY);
        flyingObstacleX -= flyingSpeed;
        if(flyingObstacleX < 0) {
            flyingObstacleX = width;
            }

        if((abs(penguinX-flyingObstacleX) < 53 ) && abs(penguinY-flyingObstacleY)< 53 && !crouch) {
            score = timer;
            if(score > highScore) {
                highScore = score;
            }
            currentState = GameState.GAMEOVER;
        }

        if((abs(penguinX-flyingObstacleX) < 53 ) && abs(penguinY-flyingObstacleY)< 33 && crouch) {
            score = timer;
            if(score > highScore) {
                highScore = score;
            }
            currentState = GameState.GAMEOVER;
        }

    }

    public void drawScore() {
        fill(255, 255, 255);
        textAlign(CENTER);
        text("Score: " + timer, width - 70, 30);
    }
    public void increaseDifficulty() {
        if (timer % rateOfDifficulty == 0) {
            speed += speedIncrease;
        }
    }

    public void difficultyTime() {
        if (timer % rateOfDifficulty == 0 && difficultyTime != timer) {
            speed += speedIncrease;
            difficultyTime = timer;
        }
    }
    public void drawGameOver() {
        fill(255, 190, 190);
        noStroke();
        rect(width / 2 - 125, height / 2 - 80, 250, 160);
        fill(255, 100, 100);
        textAlign(CENTER);
        text("Game Over!", width / 2, height / 2 - 50);
        text("Your Score " + score, width / 2, height / 2 - 30);
        text("High Score " + highScore, width / 2, height / 2 - 10);
    }



}