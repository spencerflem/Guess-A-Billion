package com.mygdx.guessabillion;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.Random;


public class GuessGama extends ApplicationAdapter {
    //setup global variables for the game
    //could do not global but this seemed easier at the time

	private Stage stage;
    private int randomNumber;
    private Random random;
    private TextField textField;
    private Label winLabel;
	private Label numLabel;
    private Label lastGuessLabel;
    private Label remainingLabel;
    private Label clueLabel;
	private int randomNumberSize = 10;
    private int lastGuess;
    private int clues;
	
	@Override
	public void create () {
        //run at program start - initialises variables and creates the needed objects
        Gdx.gl.glClearColor(0.8f, 0.9f, 0.9f, 1); // background color r g b a
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);
        random = new Random();
        randomNumber = makeRandomNumber();
        clues = 10;
        //setting up ui
		Skin uiSkin = new Skin(Gdx.files.internal("uiskin.json")); //using default can change this to configure how ui elements look
		Table table = new Table();
		table.setFillParent(true);
		stage.addActor(table);
        /* displays the number to guess so you can see it (cheating :P)
        numLabel = new Label(Integer.toString(randomNumber), uiSkin);
        table.add(numLabel);
        */
		textField = new TextField("", uiSkin);
		textField.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
		table.add(textField);
		TextButton textButton = new TextButton("Submit", uiSkin);
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                String numberString = textField.getText();
                int numberInt = Integer.parseInt(numberString);
                textField.setText("");
                submit(numberInt);
            }
        });
		table.add(textButton);
        table.row(); //line break fot eh ui table
        lastGuessLabel = new Label("Last Guess: N/A - ", uiSkin);
        table.add(lastGuessLabel);
        clueLabel = new Label("???", uiSkin);
        table.add(clueLabel);
        table.row();
        TextButton spendButton = new TextButton("Use Clue", uiSkin);
        spendButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                showClue();
            }
        });
        table.add(spendButton);
        remainingLabel = new Label(Integer.toString(clues) + " Clues Remaining", uiSkin);
        table.add(remainingLabel);
        table.row();
        winLabel = new Label("Banana!", uiSkin);
        table.add(winLabel);
		//table.setDebug(true); // not sure exactly what this does
	}

	@Override
	public void render () {
        //if the screen doesnt have wacky animations this can be optimized so it doesn't try for 60fps
        //right now its wasteful - but if we start adding snazz animations might need it like this
		float delta = Gdx.graphics.getDeltaTime();
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //clears screen so we dont draw on top of last frame
		stage.act(delta); //updates the frame by so much time so animations know how much to animate
		stage.draw(); //actually fraw to the screen via magic
	}
	
	@Override
	public void dispose () {
		stage.dispose();
	}

	@Override
	public void resize (int width, int height) {
		stage.getViewport().update(width, height, true);
	}

    //my code on what happens when buttons are pressed
    private void submit(int number) {
        lastGuess = number;
        lastGuessLabel.setText("Last Guess: " + Integer.toString(lastGuess) + " - ");
        clueLabel.setText("???");
        if(number == randomNumber) {
            winLabel.setText("WIN!");
            Gdx.gl.glClearColor(0.2f, 0.9f, 0.2f, 1);
			randomNumber = makeRandomNumber();
			//numLabel.setText(Integer.toString(randomNumber)); //updates the cheating label
		}
        else {
            winLabel.setText("LOSE!");
            Gdx.gl.glClearColor(0.9f, 0.2f, 0.2f, 1);
        }
    }

	private int makeRandomNumber() {
		return random.nextInt(randomNumberSize) + 1;
	}

    //missing a lot of quality of life things like telling you if the clue isn't going to work etc...
    private void showClue() {
        if (clues > 0) {
            clues--;
            remainingLabel.setText(Integer.toString(clues) + " Clues Remaining");
            if (lastGuess >= randomNumber) {
                clueLabel.setText("too high");
            } else {
                clueLabel.setText("too low");
            }
        }
    }
}
