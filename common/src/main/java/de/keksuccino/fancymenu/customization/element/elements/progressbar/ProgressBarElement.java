package de.keksuccino.fancymenu.customization.element.elements.progressbar;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.customization.placeholder.PlaceholderParser;
import de.keksuccino.fancymenu.util.enums.LocalizedCycleEnum;
import de.keksuccino.fancymenu.util.rendering.DrawableColor;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.fancymenu.util.resources.texture.ITexture;
import de.keksuccino.fancymenu.util.resources.texture.ImageResourceHandler;
import de.keksuccino.konkrete.math.MathUtils;
import de.keksuccino.konkrete.rendering.RenderUtils;
import net.minecraft.network.chat.Style;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.awt.*;

public class ProgressBarElement extends AbstractElement {

    private static final Logger LOGGER = LogManager.getLogger();

    public BarDirection direction = BarDirection.RIGHT;
    public DrawableColor barColor = DrawableColor.of(new Color(82, 149, 255));
    public String barTexturePath;
    public DrawableColor backgroundColor = DrawableColor.of(new Color(171, 200, 247));
    public String backgroundTexturePath;
    public boolean useProgressForElementAnchor = false;
    public String progressSource = null;
    public ProgressValueMode progressValueMode = ProgressValueMode.PERCENTAGE;

    //These fields are for caching the last x, y, width and height of the PROGRESS (not the element!)
    protected int lastProgressX = 0;
    protected int lastProgressY = 0;
    protected int lastProgressWidth = 0;
    protected int lastProgressHeight = 0;

    public ProgressBarElement(@NotNull ElementBuilder<?, ?> builder) {
        super(builder);
    }

    @Override
    public void render(@NotNull PoseStack pose, int mouseX, int mouseY, float partial) {

        if (!this.shouldRender()) return;

        this.renderBackground(pose);
        this.renderProgress(pose);

    }

    protected void renderProgress(@NotNull PoseStack pose) {

        float currentProgress = Math.max(0.0F, Math.min(1.0F, this.getCurrentProgress()));
        int progressWidth = this.getAbsoluteWidth();
        int progressHeight = this.getAbsoluteHeight();
        int progressX = this.getAbsoluteX();
        int progressY = this.getAbsoluteY();
        float offsetX = 0.0F;
        float offsetY = 0.0F;
        if ((this.direction == BarDirection.LEFT) || (this.direction == BarDirection.RIGHT)) {
            progressWidth = (int)((float)this.getAbsoluteWidth() * currentProgress);
        }
        if ((this.direction == BarDirection.UP) || (this.direction == BarDirection.DOWN)) {
            progressHeight = (int)((float)this.getAbsoluteHeight() * currentProgress);
        }
        if (this.direction == BarDirection.LEFT) {
            progressX += this.getAbsoluteWidth() - progressWidth;
            offsetX = this.getAbsoluteWidth() - progressWidth;
        }
        if (this.direction == BarDirection.UP) {
            progressY += this.getAbsoluteHeight() - progressHeight;
            offsetY = this.getAbsoluteHeight() - progressHeight;
        }
        this.lastProgressX = progressX;
        this.lastProgressY = progressY;
        this.lastProgressWidth = progressWidth;
        this.lastProgressHeight = progressHeight;

        RenderSystem.enableBlend();
        if (this.barTexturePath != null) {
            ITexture t = ImageResourceHandler.INSTANCE.getTexture(this.barTexturePath);
            if ((t != null) && (t.getResourceLocation() != null)) {
                RenderUtils.bindTexture(t.getResourceLocation());
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.opacity);
                blit(pose, progressX, progressY, offsetX, offsetY, progressWidth, progressHeight, this.getAbsoluteWidth(), this.getAbsoluteHeight());
                RenderingUtils.resetShaderColor();
            }
        } else if (this.barColor != null) {
            RenderingUtils.resetShaderColor();
            fill(pose, progressX, progressY, progressX + progressWidth, progressY + progressHeight, RenderingUtils.replaceAlphaInColor(this.barColor.getColorInt(), this.opacity));
            RenderingUtils.resetShaderColor();
        }

    }

    protected void renderBackground(@NotNull PoseStack pose) {
        RenderSystem.enableBlend();
        if (this.backgroundTexturePath != null) {
            ITexture t = ImageResourceHandler.INSTANCE.getTexture(this.backgroundTexturePath);
            if ((t != null) && (t.getResourceLocation() != null)) {
                RenderUtils.bindTexture(t.getResourceLocation());
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.opacity);
                blit(pose, this.getAbsoluteX(), this.getAbsoluteY(), 0.0F, 0.0F, this.getAbsoluteWidth(), this.getAbsoluteHeight(), this.getAbsoluteWidth(), this.getAbsoluteHeight());
                RenderingUtils.resetShaderColor();
            }
        } else if (this.backgroundColor != null) {
            RenderingUtils.resetShaderColor();
            fill(pose, this.getAbsoluteX(), this.getAbsoluteY(), this.getAbsoluteX() + this.getAbsoluteWidth(), this.getAbsoluteY() + this.getAbsoluteHeight(), RenderingUtils.replaceAlphaInColor(this.backgroundColor.getColorInt(), this.opacity));
            RenderingUtils.resetShaderColor();
        }
    }

    /**
     * Float between 0.0F and 1.0F.<br>
     * 0.0F = 0%<br>
     * 1.0F = 100%
     */
    public float getCurrentProgress() {
        if (isEditor()) return 0.5F;
        if (this.progressSource != null) {
            String s = PlaceholderParser.replacePlaceholders(this.progressSource).replace(" ", "");
            if (MathUtils.isFloat(s)) {
                if (this.progressValueMode == ProgressValueMode.PERCENTAGE) return Float.parseFloat(s) / 100.0F;
                return Float.parseFloat(s);
            }
        }
        return 0.0F;
    }

    @Override
    public int getChildElementAnchorPointX() {
        if (this.useProgressForElementAnchor) {
            if (this.direction == BarDirection.RIGHT) return this.getProgressX() + this.getProgressWidth();
            return this.getProgressX();
        }
        return super.getChildElementAnchorPointX();
    }

    @Override
    public int getChildElementAnchorPointY() {
        if (this.useProgressForElementAnchor) {
            if (this.direction == BarDirection.DOWN) return this.getProgressY() + this.getProgressHeight();
            return this.getProgressY();
        }
        return super.getChildElementAnchorPointY();
    }

    public int getProgressX() {
        return this.lastProgressX;
    }

    public int getProgressY() {
        return this.lastProgressY;
    }

    public int getProgressWidth() {
        return this.lastProgressWidth;
    }

    public int getProgressHeight() {
        return this.lastProgressHeight;
    }

    public enum BarDirection implements LocalizedCycleEnum<BarDirection> {

        LEFT("left"),
        RIGHT("right"),
        UP("up"),
        DOWN("down");

        private final String name;

        BarDirection(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getLocalizationKeyBase() {
            return "fancymenu.editor.elements.progress_bar.direction";
        }

        @Override
        public @NotNull Style getValueComponentStyle() {
            return WARNING_TEXT_STYLE.get();
        }

        @Override
        public @NotNull String getName() {
            return this.name;
        }

        @Override
        public @NotNull BarDirection[] getValues() {
            return BarDirection.values();
        }

        @Override
        public @Nullable BarDirection getByNameInternal(@NotNull String name) {
            return getByName(name);
        }

        @Nullable
        public static BarDirection getByName(@NotNull String name) {
            for (BarDirection d : BarDirection.values()) {
                if (d.name.equals(name)) return d;
            }
            return null;
        }

    }

    public enum ProgressValueMode implements LocalizedCycleEnum<ProgressValueMode> {

        PERCENTAGE("percentage"),
        FLOATING_POINT("float");

        private final String name;

        ProgressValueMode(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getLocalizationKeyBase() {
            return "fancymenu.editor.elements.progress_bar.mode";
        }

        @Override
        public @NotNull Style getValueComponentStyle() {
            return WARNING_TEXT_STYLE.get();
        }

        @Override
        public @NotNull String getName() {
            return this.name;
        }

        @Override
        public @NotNull ProgressValueMode[] getValues() {
            return ProgressValueMode.values();
        }

        @Override
        public @Nullable ProgressValueMode getByNameInternal(@NotNull String name) {
            return getByName(name);
        }

        @Nullable
        public static ProgressValueMode getByName(@NotNull String name) {
            for (ProgressValueMode d : ProgressValueMode.values()) {
                if (d.name.equals(name)) return d;
            }
            return null;
        }

    }

}
