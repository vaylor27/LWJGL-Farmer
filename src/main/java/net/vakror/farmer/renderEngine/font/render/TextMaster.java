package net.vakror.farmer.renderEngine.font.render;

import net.vakror.farmer.renderEngine.Loader;
import net.vakror.farmer.renderEngine.font.mesh.*;
import net.vakror.farmer.renderEngine.renderer.FontRenderer;
import org.lwjgl.opengl.GL30;

import java.util.*;

public class TextMaster {
	
	private static Loader loader;
	private static Map<FontType, List<GUIText>> texts = new HashMap<>();
	private static FontRenderer renderer;
	
	public static void init(Loader theLoader){
		renderer = new FontRenderer();
		loader = theLoader;
	}
	
	public static void render(){
		renderer.render(texts);
	}
	
	public static void loadText(GUIText text){
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		int vao = loader.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
		text.setMeshInfo(vao, data.getVertexCount());
        List<GUIText> textBatch = texts.computeIfAbsent(font, k -> new ArrayList<>());
        textBatch.add(text);
	}
	
	public static void removeText(GUIText text){
		List<GUIText> textBatch = texts.get(text.getFont());
		textBatch.remove(text);
		if(textBatch.isEmpty()){
			texts.remove(text.getFont());
		}
	}
	
	public static void cleanUp(){
		renderer.cleanUp();
	}

}
