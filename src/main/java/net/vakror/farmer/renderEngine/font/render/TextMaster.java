package net.vakror.farmer.renderEngine.font.render;

import net.vakror.farmer.GameEntryPoint;
import net.vakror.farmer.renderEngine.Loader;
import net.vakror.farmer.renderEngine.font.mesh.*;
import net.vakror.farmer.renderEngine.listener.register.AutoRegisterListener;
import net.vakror.farmer.renderEngine.listener.type.CloseGameListener;
import net.vakror.farmer.renderEngine.listener.type.RenderListener;
import net.vakror.farmer.renderEngine.renderer.FontRenderer;

import java.util.*;

@AutoRegisterListener
public class TextMaster implements RenderListener, CloseGameListener, GameEntryPoint {
	
	private static Map<FontType, List<GUIText>> texts = new HashMap<>();
	private static FontRenderer renderer;
	public static final TextMaster INSTANCE = new TextMaster();

	@Override
	public void initialize(){
		renderer = new FontRenderer();
	}
	
	public void onRender() {
		renderer.render(texts);
	}
	
	public static void loadText(GUIText text){
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		int vao = Loader.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
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
	
	public void onGameClose(){
		renderer.cleanUp();
	}

}
