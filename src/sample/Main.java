package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Random;

public class Main extends Application{

    private final int[] boardX = new int[484];
    private final int[] boardY = new int[484];

    private Image upFace = new Image(getClass().getResourceAsStream("images/up.png"));
    private Image downFace = new Image(getClass().getResourceAsStream("images/down.png"));
    private Image rightFace = new Image(getClass().getResourceAsStream("images/right.png"));
    private Image liftFace = new Image(getClass().getResourceAsStream("images/left.png"));

    private Image bodyFace = new Image(getClass().getResourceAsStream("images/body.png"));
    private Image fruitP = new Image(getClass().getResourceAsStream("images/fruit.png"));

    private LinkedList<Position> snake = new LinkedList();

    private static Boolean isUp = false;
    private static Boolean isDown = false;
    private static Boolean isRight = false;
    private static Boolean isLift = false;

    private static int longOfSnake = 3;

    private static Timeline timeline = new Timeline();

    private static int[] fruitXPos = {20,40,60,80,100,120,140,160,200,220,240,260,280,300,320,340,360,380,400,420,440,460};
    private static int[] fruitYPos = {20,40,60,80,100,120,140,160,200,220,240,260,280,300,320,340,360,380,400,420,440,460};

    private static int move = 0;

    private static Random random = new Random();

    private static int bestScore = readBestScore();

    private static int score = 0;
    private static int eatenFruit = 0;
    private static int scoreCounter = 100;

    private static int XPos = random.nextInt(22);
    private static int YPos = 5 + random.nextInt(17);

    private static Boolean isGameOver = false;

    private static void writeBestScore(){
        if (bestScore <= score){
            try {
                FileOutputStream FOS = new FileOutputStream("./Do_not_touch_this_FILE_boy.xml");
                OutputStreamWriter OSW = new OutputStreamWriter(FOS, StandardCharsets.UTF_8);
                OSW.write(bestScore + "");
                OSW.flush();
                OSW.close();
            }catch (IOException ignored){}
        }
    }

    private static int readBestScore() {
        try {
            InputStreamReader ISR = new InputStreamReader(new FileInputStream("./Do_not_touch_this_FILE_boy.xml"));
            BufferedReader bufferedReader = new BufferedReader(ISR);
            int c;
            StringBuilder string= new StringBuilder();
            while ((c = bufferedReader.read()) != -1){
                if (Character.isDigit(c)){
                    string.append((char) c);
                }
            }
            if (string.toString().equals("")) string = new StringBuilder("0");

            bufferedReader.close();

            return Integer.parseInt(string.toString());
        }
        catch (IOException ignored) {}
        return 0;
    }

    private void drawing(GraphicsContext GC){
        if (score > bestScore)bestScore = score;

        if (move == 0){
            boardX[2] = 40;
            boardX[1] = 60;
            boardX[0] = 80;

            boardY[2] = 100;
            boardY[1] = 100;
            boardY[0] = 100;

            scoreCounter = 99;
            timeline.play();
        }

        /*
        * 0.117,0.117,0.117 black
        * 0.337,0.661,0.839 blue
        * 0.305,0.788,0.690 green
        * 0.784,0.784,0.784 gray 80%
        * 0.839,0.615,0.521 wine
        * 0.458,0.650,0.290 olive
        * 0.576,0.576,0.800 violet
        * 0.862,0.862,0.862 gray 20%
        * */


        GC.setFill(Color.color(.117,.117,.117));
        GC.fillRect(0,0,750,500);

        GC.setFill(Color.color(0.337,0.661,0.839));
        for (int k = 6;k <= 482;k += 17){
            for (int i = 6;i <= 482; i += 17){
                GC.fillRect(i,k,13,13);
            }
        }

        GC.setFill(Color.color(0.117,0.117,0.117));
        GC.fillRect(20,20,460,460);

        GC.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        GC.setFill(Color.color(0.305,0.788,0.690));
        GC.fillText("Worm 2D",565,35);

        GC.setFill(Color.color(0.576,0.576,0.800));
        GC.setFont(Font.font("Arial",FontWeight.NORMAL,13));
        GC.fillText("+ " + scoreCounter,510, 222);

        GC.setFill(Color.color(0.784,0.784,0.784));
        GC.fillText("Programed by Ibrahim Habra",530, 60);

        GC.setFont(Font.font("Arial", FontWeight.NORMAL, 18));

        GC.setFill(Color.color(0.862,0.862,0.862));
        GC.fillText("Best Score", 576, 110);
        GC.setFill(Color.color(0.458,0.650,0.290));
        GC.fillRect(550, 120, 140, 30);
        GC.setFill(Color.color(0.117,0.117,0.117));
        GC.fillRect(551, 121, 138, 28);
        GC.setFill(Color.color(0.839,0.615,0.521));
        GC.fillText(bestScore + "", 550 + (142 - new Text(bestScore + "").getLayoutBounds().getWidth()) / 2, 142);

        GC.setFill(Color.color(0.862,0.862,0.862));
        GC.fillText("Total Score", 573, 190);
        GC.setFill(Color.color(0.458,0.650,0.290));
        GC.fillRect(550, 200, 140, 30);
        GC.setFill(Color.color(0.117,0.117,0.117));
        GC.fillRect(551, 201, 138, 28);
        GC.setFill(Color.color(0.839,0.615,0.521));
        GC.fillText(score + "", 550 + (142 - new Text(score + "").getLayoutBounds().getWidth()) / 2, 222);

        GC.setFill(Color.color(0.862,0.862,0.862));
        GC.fillText("Fruit eaten",575,270);
        GC.setFill(Color.color(0.458,0.650,0.290));
        GC.fillRect(550,280,140,30);
        GC.setFill(Color.color(0.117,0.117,0.117));
        GC.fillRect(551,281,138,28);
        GC.setFill(Color.color(0.839,0.615,0.521));
        GC.fillText(eatenFruit + "",550 + (142 - new Text(eatenFruit + "").getLayoutBounds().getWidth()) / 2,302);

        GC.setFill(Color.color(0.862,0.862,0.862));
        GC.setFont(Font.font("Arial",FontWeight.BOLD,16));
        GC.fillText("Controls",550,360);

        GC.setFont(Font.font("Arial",FontWeight.NORMAL,14));
        GC.fillText("Pause / Start : Space", 550, 385);
        GC.fillText("Move Up : Arrow Up", 550, 410);
        GC.fillText("Move Down : Arrow Down", 550, 435);
        GC.fillText("Move Left : Arrow Left", 550, 460);
        GC.fillText("Move Right : Arrow Right", 550, 485);

        GC.drawImage(rightFace,boardX[0],boardY[0]);

        snake.clear();

        for (int i = 0;i < longOfSnake;i++){
            if (i == 0 && isLift){
                GC.drawImage(liftFace,boardX[i],boardY[i]);
            }else if(i == 0 && isRight){
                GC.drawImage(rightFace,boardX[i],boardY[i]);
            }else if(i == 0 && isUp){
                GC.drawImage(upFace,boardX[i],boardY[i]);
            }else if(i == 0 && isDown){
                GC.drawImage(downFace,boardX[i],boardY[i]);
            }else if(i != 0){
                GC.drawImage(bodyFace,boardX[i],boardY[i]);
            }

        snake.add(new Position(boardX[i],boardY[i]));
        }

        if (scoreCounter > 10){
            scoreCounter--;
        }
        for (int i = 1;i < longOfSnake;i++){
            if (boardX[i] == boardX[0] && boardY[i] == boardY[0]){
                     if (isRight) GC.drawImage(rightFace,boardX[1],boardY[1]);
                else if (isLift)  GC.drawImage(liftFace,boardX[1],boardY[1]);
                else if (isDown)  GC.drawImage(downFace,boardX[1],boardY[1]);
                else if (isUp )   GC.drawImage(upFace,boardX[1],boardY[1]);

                isGameOver = true;
                timeline.stop();
                GC.setFill(Color.color(0.862,0.862,0.862));
                GC.setFont(Font.font("Arial", FontWeight.BOLD, 50));
                GC.fillText("Game Over", 110, 220);

                GC.setFont(Font.font("Arial", FontWeight.BOLD, 20));
                GC.fillText("Press Space To Restart", 130, 260);
                writeBestScore();
            }
        }

        if(fruitXPos[XPos] == boardX[0] && fruitYPos[YPos] == boardY[0]){
            score += scoreCounter;
            scoreCounter = 99;
            longOfSnake++;
            eatenFruit++;
        }

        for (int i = 0;i < snake.size();i++){
            if (snake.get(i).x == fruitXPos[XPos] && snake.get(i).y == fruitYPos[YPos]){
                XPos = random.nextInt(22);
                YPos = random.nextInt(22);
            }
        }

        GC.drawImage(fruitP,fruitXPos[XPos],fruitYPos[YPos]);

        if (isRight){
            for (int i = longOfSnake - 1;i >= 0;i--)
                boardY[i + 1] = boardY[i];
            for (int i = longOfSnake;i >= 0;i--){
                if (i == 0)
                    boardX [i]  = boardX[i] + 20;
                 else
                    boardX[i] = boardX[i-1];
            if (boardX[i] > 460)
                boardX[i] = 20;
            }
        }
        else if (isLift){
            for (int i = longOfSnake - 1;i >= 0;i--)
                boardY[i + 1] = boardY[i];
            for (int i = longOfSnake;i >= 0;i--){
                if (i == 0)
                    boardX [i]  = boardX[i] - 20;
                else
                    boardX[i] = boardX[i-1];
                if (boardX[i] < 20)
                    boardX[i] = 460;
            }
        }else if (isUp)
        {
            for (int i = longOfSnake - 1; i >= 0; i--)
                boardX[i + 1] = boardX[i];
            for (int i = longOfSnake; i >= 0; i--)
            {
                if (i == 0)
                    boardY[i] = boardY[i] - 20;
                else
                    boardY[i] = boardY[i - 1];
                if (boardY[i] < 20)
                    boardY[i] = 460;
            }
        }
        else if (isDown)
        {
            for (int i = longOfSnake - 1; i >= 0; i--)
                boardX[i + 1] = boardX[i];
            for (int i = longOfSnake; i >= 0; i--)
            {
                if (i == 0)
                    boardY[i] = boardY[i] + 20;
                else
                    boardY[i] = boardY[i - 1];
                if (boardY[i] > 460)
                    boardY[i] = 20;
            }
        }
    }

    @Override
    public void start(Stage primaryStage){
        Canvas canvas = new Canvas(750,500);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        Pane root = new Pane();
        root.setStyle("-fx-background-color: black;");
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Worm 2D");
        primaryStage.show();

        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.1), (ActionEvent event) -> drawing(graphicsContext)));

        timeline.setCycleCount(Timeline.INDEFINITE);

        timeline.play();

        scene.addEventFilter(KeyEvent.KEY_PRESSED,(KeyEvent e) -> {
            if (null != e.getCode()){
                switch (e.getCode()){
                    case SPACE:
                        if (timeline.getStatus() == Timeline.Status.RUNNING && !isGameOver){
                            timeline.stop();
                        }else if(timeline.getStatus() != Timeline.Status.RUNNING && !isGameOver){
                            timeline.play();
                        }else if (timeline.getStatus() != Timeline.Status.RUNNING && isGameOver){
                            isGameOver = false;
                            timeline.play();
                            move = 0;
                            score = 0;
                            eatenFruit = 0;
                            longOfSnake = 3;
                            isRight = true;
                            isLift = false;
                        }
                        break;
                    case RIGHT:
                        move++;
                        isRight = true;
                        if (!isLift) {
                            isRight = true;
                        }
                        else
                        {
                            isRight = false;
                            isLift = true;
                        }
                        isUp = false;
                        isDown = false;
                        break;

                    case LEFT:
                        move++;
                        isLift = true;
                        if (!isRight)
                        {
                            isLift = true;
                        }
                        else
                        {
                            isLift = false;
                            isRight = true;
                        }
                        isUp = false;
                        isDown = false;
                        break;

                    case UP:
                        move++;
                        isUp = true;
                        if (!isDown)
                        {
                            isUp = true;
                        }
                        else {
                            isUp = false;
                            isDown = true;
                        }
                        isLift = false;
                        isRight = false;
                        break;

                    case DOWN:
                        move++;
                        isDown = true;
                        if (!isUp)
                        {
                            isDown = true;
                        }
                        else {
                            isUp = true;
                            isDown = false;
                        }
                        isLift = false;
                        isRight = false;
                        break;
                }
            }
        });
    }

    public static void main(String[]args){
        launch(args);
    }
}

/*
* ||||||||||||||||||||||||||||||||
* ||||||||||||||||||||||||||||||||
* ||||||||||||||||||||||||||||||||
* ||||||||                ||||||||
* ||||||||  *    ***  *   ||||||||
* ||||||||  *   *   * *   ||||||||
*           *   *   * *
* ||||||||  ***  ***  *** ||||||||
* ||||||||                ||||||||
* ||||||||                ||||||||
* ||||||||||||||||||||||||||||||||
* ||||||||||||||||||||||||||||||||
* ||||||||||||||||||||||||||||||||
* */