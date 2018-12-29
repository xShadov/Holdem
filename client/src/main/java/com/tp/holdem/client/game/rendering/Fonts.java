package com.tp.holdem.client.game.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import lombok.Builder;
import lombok.Value;

@Builder(builderClassName = "Builder", buildMethodName = "generate")
@Value(staticConstructor = "from")
public class Fonts {
	public enum Types {
		JMH("data/jmh.ttf");

		private String file;

		Types(String file) {
			this.file = file;
		}

		public String file() {
			return file;
		}
	}

	private Types type;
	private int size;
	private Color color;

	public static class Builder {
		public BitmapFont generate() {
			final FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(type.file()));
			final FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
			parameter.size = size;
			parameter.borderWidth = 1;
			parameter.color = color;
			parameter.shadowOffsetX = 1;
			parameter.shadowOffsetY = 1;

			final BitmapFont font = generator.generateFont(parameter);
			generator.dispose();
			return font;
		}
	}
}
