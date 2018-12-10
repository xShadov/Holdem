package com.tp.holdem.client.game.drawing;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TableDrawer {
	private final TextureRegion tableTexture = new TextureRegion(new Texture("data/pokerTable.jpg"), 0, 0, 1024, 780);

	private final SpriteBatch batcher;

	public TableDrawer(SpriteBatch batcher) {
		this.batcher = batcher;
	}

	public void drawTable() {
		batcher.draw(tableTexture, 0, 0, 1024, 780);
	}
}
