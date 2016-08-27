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
        Gdx.gl.glClearColor(0.8f, 0.9f, 0.9f, 1);
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);
        random = new Random();
        randomNumber = makeRandomNumber();
        clues = 10;
		Skin uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
		Table table = new Table();
		table.setFillParent(true);
		stage.addActor(table);
        /*
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
        table.row();
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
        winLabel = new Label("Bananana!", uiSkin);
        table.add(winLabel);
		//table.setDebug(true);
	}

	@Override
	public void render () {
		float delta = Gdx.graphics.getDeltaTime();
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
	}
	
	@Override
	public void dispose () {
		stage.dispose();
	}

	@Override
	public void resize (int width, int height) {
		stage.getViewport().update(width, height, true);
	}

    private void submit(int number) {
        lastGuess = number;
        lastGuessLabel.setText("Last Guess: " + Integer.toString(lastGuess) + " - ");
        clueLabel.setText("???");
        if(number == randomNumber) {
            winLabel.setText("WIN!");
            Gdx.gl.glClearColor(0.2f, 0.9f, 0.2f, 1);
			randomNumber = makeRandomNumber();
			//numLabel.setText(Integer.toString(randomNumber));
		}
        else {
            winLabel.setText("LOSE!");
            Gdx.gl.glClearColor(0.9f, 0.2f, 0.2f, 1);
        }
    }

	private int makeRandomNumber() {
		return random.nextInt(randomNumberSize) + 1;
	}

    private void showClue() {
        clues--;
        remainingLabel.setText(Integer.toString(clues) + " Clues Remaining");
        if (lastGuess >= randomNumber) {
            clueLabel.setText("too high");
        }
        else {
            clueLabel.setText("too low");
        }
    }
}
