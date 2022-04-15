package de.keksuccino.fancymenu.events;

import com.mojang.blaze3d.vertex.PoseStack;
import de.keksuccino.konkrete.events.EventBase;
import net.minecraft.client.gui.components.AbstractSelectionList;

public class RenderGuiListBackgroundEvent extends EventBase {
	
	protected AbstractSelectionList list;
	protected PoseStack matrix;
	
	public RenderGuiListBackgroundEvent(PoseStack matrix, AbstractSelectionList list) {
		this.list = list;
		this.matrix = matrix;
	}
	
	@Override
	public boolean isCancelable() {
		return false;
	}
	
	public AbstractSelectionList getList() {
		return this.list;
	}

	public PoseStack getMatrixStack() {
		return this.matrix;
	}
	
	public static class Pre extends RenderGuiListBackgroundEvent {

		public Pre(PoseStack matrix, AbstractSelectionList list) {
			super(matrix, list);
		}

	}
	
	public static class Post extends RenderGuiListBackgroundEvent {

		public Post(PoseStack matrix, AbstractSelectionList list) {
			super(matrix, list);
		}
		
	}
	
}
