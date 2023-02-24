import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;

import java.util.Random;

/**.
 * this is my Wordle mimic game.
 * @author Tinisi Kumeh
 * @version v1
 */
public class Jordle extends Application {
    int row = 0;
    int column = 0;
    Random rand = new Random();
    int index = rand.nextInt(Words.list.size() + 1);
    String guess = "";
    String keyWord = Words.list.get(index);
    GridPane aPane = new GridPane();
    StackPane[][] myWordsArr = new StackPane[5][6];
    Rectangle[][] myRecArr = new Rectangle[5][6];
    Text[][] myTextArr = new Text[5][6];
    Label prompt = new Label("Try guessing a word!");
    Font labelFont = new Font("Arial", 12);


    @Override
    public void start(Stage primaryStage) {
        Button aButton = new Button("Instructions");
        aButton.setOnAction(e -> {
            Dialog<String> dialog = new Dialog<String>();
            dialog.setTitle("Instructions");
            dialog.setContentText("Rules\n 1. Words that show up in Green are in the correct position"
                    + "\n 2. Letters that are in a word, but misplaced"
                    + "will show up in yellow\n 3. Letters that are not in a word will show up in grey"
                    + "\n 4. Words will not have letters that repeat, so don't waste an entry using two"
                    + " of the same letters"
                    + "\n 5. If you don't complete all spaces in a word, the box"
                    + "will be outlined in red.");
            dialog.show();
            dialog.onCloseRequestProperty();
        });

        Button restart = new Button("Restart");
        restart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent a) {
                keyWord = Words.list.get(rand.nextInt(Words.list.size() + 1));
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 6; j++) {
                        myWordsArr[i][j] = new StackPane();
                        myRecArr[i][j] = new Rectangle(50, 50);
                        myRecArr[i][j].setFill(Color.WHITE);
                        myRecArr[i][j].setStroke(Color.BLACK);
                        myTextArr[i][j]  = new Text("");
                        myWordsArr[i][j].getChildren().addAll(myRecArr[i][j], myTextArr[i][j]);
                        aPane.add(myWordsArr[i][j], i, j);
                    }
                }
                guess = "";
                row = 0;
                column = 0;
                prompt = new Label("Try guessing a word!");
            }
        });

        Text name = new Text(0.0, 1.0, "J O R D L E");
        Font nameFont = new Font("Arial", 40.0);
        name.setFont(nameFont);
        HBox gameName = new HBox(name);
        gameName.setSpacing(10.0);
        gameName.setPadding(new Insets(80.0, 80.0, 80.0, 80.0)); //sets spacing between edges of page and words
        gameName.setAlignment(Pos.BASELINE_CENTER);


        BorderPane root = new BorderPane();
        root.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                if (e.getCode().equals(KeyCode.BACK_SPACE)) {
                    row -= 1;
                    myTextArr[row][column].setText("");
                }
                if (row < 5 && e.getCode().isLetterKey()) {
                    myTextArr[row][column].setText(e.getText());
                    guess += e.getText();
                    row += 1;

                }
                if (e.getCode().equals(KeyCode.ENTER)) {
                    if (row < 5) {
                        for (int i = row; i < 5; i++) {
                            myRecArr[i][column].setFill(Color.RED);
                        }
                    }
                    if (row == 5) {
                        char[] guessArr = guess.toCharArray();
                        char[] keyWordArr = keyWord.toCharArray();
                        System.out.println(guessArr);
                        int in = 0;
                        for (int i = 0; i < 5; i++) {
                            if (guessArr[i] == keyWordArr[i]) {
                                myRecArr[i][column].setFill(Color.GREEN);
                            } else {
                                for (int j = 0; j < 5; j++) {
                                    if (guessArr[i] == keyWordArr[j]) {
                                        myRecArr[i][column].setFill(Color.YELLOW);
                                        in += 1;
                                    }
                                }
                                if (in == 0) {
                                    myRecArr[i][column].setFill(Color.GREY);
                                }
                            }
                            in = 0;
                        }
                        if (keyWord.equals(guess)) {
                            prompt.setText("Congratulations! You've guessed the word.");
                        } else if (column < 5) {
                            column += 1;
                            row = 0;
                            guess = "";
                        } else {
                            prompt.setText(String.format("Game over. The word was %s.", keyWord));
                        }
                    }
                    }
                }

        });
        root.setTop(gameName);
        primaryStage.setTitle("Jordle");


        prompt.setFont(labelFont);


//getcode give you letter
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                myWordsArr[i][j] = new StackPane();
                myRecArr[i][j] = new Rectangle(50, 50);
                myRecArr[i][j].setFill(Color.WHITE);
                myRecArr[i][j].setStroke(Color.BLACK);
                myTextArr[i][j]  = new Text("");
                myWordsArr[i][j].getChildren().addAll(myRecArr[i][j], myTextArr[i][j]);
                aPane.add(myWordsArr[i][j], i, j);
            }
        }
        aPane.setHgap(25);
        aPane.setVgap(25);


        //use set on key pressed to check if word enter is a valid key
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                if (myTextArr[i][j].equals(new Text(""))) {
                    Text word =  myTextArr[i][j];
                    root.setOnKeyPressed(new EventHandler<KeyEvent>() {
                        @Override
                        public void handle(KeyEvent e) {
                            System.out.println("e.text: " + e.getText());
                        }
                    });
                    myTextArr[i][j] = word;
                }
            }
        }



        HBox options = new HBox(prompt, aButton, restart);
        options.setAlignment(Pos.BOTTOM_CENTER);
        options.setSpacing(5.0);
        options.setPadding(new Insets(80.0, 80.0, 80.0, 80.0));
        root.setBottom(options);

        Scene aScene = new Scene(root, 800, 800);
        primaryStage.setScene(aScene);
        aPane.setAlignment(Pos.CENTER);
        root.setCenter(aPane);

        primaryStage.show();



    }

}
