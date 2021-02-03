package de.keksuccino.fancymenu.menu.fancy.helper.layoutcreator.content;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import de.keksuccino.fancymenu.menu.fancy.helper.layoutcreator.LayoutCreatorScreen;
import de.keksuccino.fancymenu.menu.fancy.item.WebStringCustomizationItem;
import de.keksuccino.konkrete.gui.content.AdvancedButton;
import de.keksuccino.konkrete.gui.screens.popup.PopupHandler;
import de.keksuccino.konkrete.gui.screens.popup.TextInputPopup;
import de.keksuccino.konkrete.input.CharacterFilter;
import de.keksuccino.konkrete.localization.Locals;
import de.keksuccino.konkrete.math.MathUtils;
import de.keksuccino.konkrete.properties.PropertiesSection;
import de.keksuccino.konkrete.rendering.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class LayoutWebString extends LayoutObject {
	
	public LayoutWebString(WebStringCustomizationItem parent, LayoutCreatorScreen handler) {
		super(parent, true, handler);
		this.setScale(this.getStringScale());
	}
	
	@Override
	protected void init() {
		super.init();
		
		AdvancedButton scaleB = new AdvancedButton(0, 0, 0, 16, Locals.localize("helper.creator.items.string.setscale"), true, (press) -> {
			this.handler.setMenusUseable(false);
			PopupHandler.displayPopup(new TextInputPopup(new Color(0, 0, 0, 0), "§l" + Locals.localize("helper.creator.items.string.setscale") + ":", CharacterFilter.getDoubleCharacterFiler(), 240, this::setScaleCallback));
		});
		this.rightclickMenu.addContent(scaleB);
		LayoutCreatorScreen.colorizeCreatorButton(scaleB);
		
		String sLabel = Locals.localize("helper.creator.items.string.setshadow");
		if (this.getObject().shadow) {
			sLabel = Locals.localize("helper.creator.items.string.setnoshadow");
		}
		AdvancedButton shadowB = new AdvancedButton(0, 0, 0, 16, sLabel, true, (press) -> {
			if (this.getObject().shadow) {
				press.displayString = Locals.localize("helper.creator.items.string.setshadow");
				this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
				
				this.getObject().shadow = false;
			} else {
				press.displayString = Locals.localize("helper.creator.items.string.setnoshadow");
				this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
				
				this.getObject().shadow = true;
			}
		});
		this.rightclickMenu.addContent(shadowB);
		LayoutCreatorScreen.colorizeCreatorButton(shadowB);
		
		String mLabel = Locals.localize("helper.creator.webstring.multiline");
		if (this.getObject().multiline) {
			mLabel = Locals.localize("helper.creator.webstring.nomultiline");
		}
		AdvancedButton multilineB = new AdvancedButton(0, 0, 0, 16, mLabel, true, (press) -> {
			if (this.getObject().multiline) {
				press.displayString = Locals.localize("helper.creator.webstring.multiline");
				this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
				
				this.getObject().multiline = false;
				this.getObject().updateContent(this.getObject().value);
			} else {
				press.displayString = Locals.localize("helper.creator.webstring.nomultiline");
				this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
				
				this.getObject().multiline = true;
				this.getObject().updateContent(this.getObject().value);
			}
		});
		this.rightclickMenu.addContent(multilineB);
		LayoutCreatorScreen.colorizeCreatorButton(multilineB);
		
	}
	
	@Override
	protected void renderBorder(int mouseX, int mouseY) {
		//horizontal line top
		Gui.drawRect(this.getStringPosX(), this.getStringPosY(), this.getStringPosX() + this.object.width, this.getStringPosY() + 1, Color.BLUE.getRGB());
		//horizontal line bottom
		Gui.drawRect(this.getStringPosX(), this.getStringPosY() + this.object.height, this.getStringPosX() + this.object.width + 1, this.getStringPosY() + this.object.height + 1, Color.BLUE.getRGB());
		//vertical line left
		Gui.drawRect(this.getStringPosX(), this.getStringPosY(), this.getStringPosX() + 1, this.getStringPosY() + this.object.height, Color.BLUE.getRGB());
		//vertical line right
		Gui.drawRect(this.getStringPosX() + this.object.width, this.getStringPosY(), this.getStringPosX() + this.object.width + 1, this.getStringPosY() + this.object.height, Color.BLUE.getRGB());
	
		//Render pos and size values
		RenderUtils.setScale(0.5F);
		this.drawString(Minecraft.getMinecraft().fontRendererObj, Locals.localize("helper.creator.items.border.orientation")+ ": " + this.object.orientation, this.getStringPosX()*2, (this.getStringPosY()*2) - 35, Color.WHITE.getRGB());
		this.drawString(Minecraft.getMinecraft().fontRendererObj, Locals.localize("helper.creator.items.string.border.scale") + ": " + this.getStringScale(), this.getStringPosX()*2, (this.getStringPosY()*2) - 26, Color.WHITE.getRGB());
		this.drawString(Minecraft.getMinecraft().fontRendererObj, Locals.localize("helper.creator.items.border.posx") + ": " + this.getStringPosX(), this.getStringPosX()*2, (this.getStringPosY()*2) - 17, Color.WHITE.getRGB());
		this.drawString(Minecraft.getMinecraft().fontRendererObj, Locals.localize("helper.creator.items.border.width") + ": " + this.object.width, this.getStringPosX()*2, (this.getStringPosY()*2) - 8, Color.WHITE.getRGB());
		
		this.drawString(Minecraft.getMinecraft().fontRendererObj, Locals.localize("helper.creator.items.border.posy") + ": " + this.getStringPosY(), ((this.getStringPosX() + this.object.width)*2)+3, ((this.getStringPosY() + this.object.height)*2) - 14, Color.WHITE.getRGB());
		this.drawString(Minecraft.getMinecraft().fontRendererObj, Locals.localize("helper.creator.items.border.height") + ": " + this.object.height, ((this.getStringPosX() + this.object.width)*2)+3, ((this.getStringPosY() + this.object.height)*2) - 5, Color.WHITE.getRGB());
		RenderUtils.postScale();
	}
	
	@Override
	protected void renderHighlightBorder() {
		Color c = new Color(0, 200, 255, 255);

		//horizontal line top
		Gui.drawRect(this.getStringPosX(), this.getStringPosY(), this.getStringPosX() + this.object.width, this.getStringPosY() + 1, c.getRGB());
		//horizontal line bottom
		Gui.drawRect(this.getStringPosX(), this.getStringPosY() + this.object.height, this.getStringPosX() + this.object.width + 1, this.getStringPosY() + this.object.height + 1, c.getRGB());
		//vertical line left
		Gui.drawRect(this.getStringPosX(), this.getStringPosY(), this.getStringPosX() + 1, this.getStringPosY() + this.object.height, c.getRGB());
		//vertical line right
		Gui.drawRect(this.getStringPosX() + this.object.width, this.getStringPosY(), this.getStringPosX() + this.object.width + 1, this.getStringPosY() + this.object.height, c.getRGB());
	}
	
	private int getStringPosX() {
		return (int)(this.object.getPosX(this.handler) * this.getStringScale());
	}
	
	private int getStringPosY() {
		return (int)(this.object.getPosY(this.handler) * this.getStringScale());
	}
	
	private float getStringScale() {
		return ((WebStringCustomizationItem)this.object).scale;
	}
	
	private WebStringCustomizationItem getObject() {
		return ((WebStringCustomizationItem)this.object);
	}
	
	@Override
	public boolean isGrabberPressed() {
		return false;
	}
	
	@Override
	public int getActiveResizeGrabber() {
		return -1;
	}
	
	@Override
	protected void setOrientation(String pos) {
		super.setOrientation(pos);
	}
	
	public void setScale(float scale) {
		if (this.getObject().scale != scale) {
			this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
		}
		
		((WebStringCustomizationItem)this.object).scale = scale;
		this.setWidth((int)(Minecraft.getMinecraft().fontRendererObj.getStringWidth(this.object.value)*scale));
		this.setHeight((int)(7*scale));
	}
	
	public void updateContent(String url) {
		this.getObject().updateContent(url);
	}
	
	private void setScaleCallback(String scale) {
		if (scale == null) {
			this.handler.setMenusUseable(true);
			return;
		}
		if (MathUtils.isFloat(scale)) {
			this.setScale(Float.valueOf(scale));
			this.handler.setMenusUseable(true);
		} else {
			this.handler.displayNotification(300, "§c§l" + Locals.localize("helper.creator.items.string.scale.invalidvalue.title"), "", Locals.localize("helper.creator.items.string.scale.invalidvalue.desc"), "", "", "", "", "");
		}
	}
	
	@Override
	protected void updateHovered(int mouseX, int mouseY) {
		if ((mouseX >= this.getStringPosX()) && (mouseX <= this.getStringPosX() + this.object.width) && (mouseY >= this.getStringPosY()) && mouseY <= this.getStringPosY() + this.object.height) {
			this.hovered = true;
		} else {
			this.hovered = false;
		}
	}
	
	@Override
	public List<PropertiesSection> getProperties() {
		List<PropertiesSection> l = new ArrayList<PropertiesSection>();
		
		PropertiesSection p1 = new PropertiesSection("customization");
		p1.addEntry("action", "addwebtext");
		p1.addEntry("url", this.object.value);
		p1.addEntry("x", "" + this.object.posX);
		p1.addEntry("y", "" + this.object.posY);
		p1.addEntry("orientation", this.object.orientation);
		p1.addEntry("scale", "" + this.getObject().scale);
		p1.addEntry("shadow", "" + this.getObject().shadow);
		p1.addEntry("multiline", "" + this.getObject().multiline);
		l.add(p1);
		
		return l;
	}

}
