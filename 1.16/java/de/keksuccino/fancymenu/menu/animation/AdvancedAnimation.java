package de.keksuccino.fancymenu.menu.animation;

import java.io.File;

import javax.annotation.Nullable;

import com.mojang.blaze3d.matrix.MatrixStack;

import de.keksuccino.fancymenu.menu.animation.exceptions.AnimationNotFoundException;
import de.keksuccino.konkrete.rendering.animation.IAnimationRenderer;
import de.keksuccino.konkrete.sound.SoundHandler;

public class AdvancedAnimation implements IAnimationRenderer {
	
	private IAnimationRenderer introRenderer;
	private IAnimationRenderer animationRenderer;
	private boolean started = false;
	private String mainAudioPath;
	private String introAudioPath;
	private boolean muted = false;
	private boolean replayIntro = false;
	
	/**
	 * Container to hold a {@link IAnimationRenderer} instance with an optional intro which plays before the main animation starts.
	 * 
	 * @param introAnimation The intro animation. Can be null.
	 * @param mainAnimation The main animation.
	 * @param audioKey The path of the audio to play with the animation. Can be null.
	 * @throws AnimationNotFoundException If the main animation is null.
	 */
	public AdvancedAnimation(@Nullable IAnimationRenderer introAnimation, IAnimationRenderer mainAnimation, @Nullable String introAudioPath, @Nullable String mainAudioPath, boolean replayIntro) throws AnimationNotFoundException {
		if (mainAnimation != null) {
			this.animationRenderer = mainAnimation;
		} else {
			throw new AnimationNotFoundException("Animation cannot be null!");
		}
		this.introRenderer = introAnimation;
		this.mainAudioPath = mainAudioPath;
		this.introAudioPath = introAudioPath;
		this.replayIntro = replayIntro;
	}
	
	public boolean hasIntro() {
		return (this.introRenderer != null);
	}
	
	@Override
	public boolean isReady() {
		if ((this.animationRenderer != null) && this.hasIntro()) {
			if (this.animationRenderer.isReady() && this.introRenderer.isReady()) {
				return true;
			}
		} else if (this.animationRenderer != null) {
			return this.animationRenderer.isReady();
		}
		
		return false;
	}
	
	@Override
	public void prepareAnimation() {
		if (this.mainAudioPath != null) {
			SoundHandler.registerSound(this.animationRenderer.getPath(), mainAudioPath);
		}
		if ((this.introAudioPath != null) && this.hasIntro()) {
			SoundHandler.registerSound(this.introRenderer.getPath(), introAudioPath);
		}
		if (this.animationRenderer != null) {
			this.animationRenderer.prepareAnimation();
		}
		if (this.hasIntro()) {
			this.introRenderer.prepareAnimation();
		}
	}
	
	/**
	 * Resets the animation to the first frame and replays the intro.
	 */
	@Override
	public void resetAnimation() {
		if (this.animationRenderer != null) {
			this.animationRenderer.resetAnimation();
		}
		if (this.hasIntro()) {
			this.introRenderer.resetAnimation();
		}
		this.started = false;
	}
	
	public boolean hasStarted() {
		return this.started;
	}
	
	@Override
	public void render(MatrixStack matrix) {
		if (this.isReady()) {
			this.started = true;
			
			if (!this.muted) {
				if (this.hasIntroAudio() && !this.introRenderer.isFinished() && ((this.introRenderer.currentFrame() == 1) || (this.introRenderer.currentFrame() > 1) && !SoundHandler.isPlaying(this.introRenderer.getPath()))) {
					SoundHandler.stopSound(this.animationRenderer.getPath());
					SoundHandler.resetSound(this.introRenderer.getPath());
					SoundHandler.playSound(this.introRenderer.getPath());
				}
				if (this.hasIntroAudio() && this.introRenderer.isFinished()) {
					SoundHandler.stopSound(this.introRenderer.getPath());
				}
				if (this.hasMainAudio() && !this.animationRenderer.isFinished() && ((this.animationRenderer.currentFrame() == 1) || (this.animationRenderer.currentFrame() > 1) && !SoundHandler.isPlaying(this.animationRenderer.getPath()))) {
					if (this.hasIntroAudio()) {
						SoundHandler.stopSound(this.introRenderer.getPath());
					}
					SoundHandler.resetSound(this.animationRenderer.getPath());
					SoundHandler.playSound(this.animationRenderer.getPath());
					SoundHandler.setLooped(this.animationRenderer.getPath(), true);
				}
			}
			
			if (this.hasIntro()) {
				this.introRenderer.setFPS(this.animationRenderer.getFPS());
				this.introRenderer.setWidth(this.animationRenderer.getWidth());
				this.introRenderer.setHeight(this.animationRenderer.getHeight());
				this.introRenderer.setPosX(this.animationRenderer.getPosX());
				this.introRenderer.setPosY(this.animationRenderer.getPosY());
				this.introRenderer.setLooped(false);
				if (!this.introRenderer.isFinished()) {
					this.introRenderer.render(matrix);
				} else {
					this.animationRenderer.render(matrix);
				}
			} else {
				this.animationRenderer.render(matrix);
			}
		}
		
		if (this.isFinished() || this.muted) {
			this.stopAudio();
		}
	}

	@Override
	public void setStretchImageToScreensize(boolean b) {
		if (this.hasIntro()) {
			this.introRenderer.setStretchImageToScreensize(b);
		}
		if (this.animationRenderer != null) {
			this.animationRenderer.setStretchImageToScreensize(b);
		}
	}

	@Override
	public void setHideAfterLastFrame(boolean b) {
		if (this.hasIntro()) {
			this.introRenderer.setHideAfterLastFrame(b);
		}
		if (this.animationRenderer != null) {
			this.animationRenderer.setHideAfterLastFrame(b);
		}
	}

	@Override
	public boolean isFinished() {
		if (this.hasIntro() && (this.animationRenderer != null)) {
			if (this.introRenderer.isFinished() && this.animationRenderer.isFinished()) {
				return true;
			}
		} else if (this.animationRenderer != null) {
			return this.animationRenderer.isFinished();
		}
		return false;
	}

	@Override
	public void setWidth(int width) {
		if (this.hasIntro()) {
			this.introRenderer.setWidth(width);
		}
		if (this.animationRenderer != null) {
			this.animationRenderer.setWidth(width);
		}
	}

	@Override
	public void setHeight(int height) {
		if (this.hasIntro()) {
			this.introRenderer.setHeight(height);
		}
		if (this.animationRenderer != null) {
			this.animationRenderer.setHeight(height);
		}
	}

	@Override
	public void setPosX(int x) {
		if (this.hasIntro()) {
			this.introRenderer.setPosX(x);
		}
		if (this.animationRenderer != null) {
			this.animationRenderer.setPosX(x);
		}
	}

	@Override
	public void setPosY(int y) {
		if (this.hasIntro()) {
			this.introRenderer.setPosY(y);
		}
		if (this.animationRenderer != null) {
			this.animationRenderer.setPosY(y);
		}
	}

	@Override
	public int currentFrame() {
		int i = 0;
		if (this.hasIntro()) {
			i = this.introRenderer.currentFrame();
		}
		if (this.animationRenderer != null) {
			i += this.animationRenderer.currentFrame();
		}
		return i;
	}

	@Override
	public int animationFrames() {
		int i = 0;
		if (this.hasIntro()) {
			i = this.introRenderer.animationFrames();
		}
		if (this.animationRenderer != null) {
			i += this.animationRenderer.animationFrames();
		}
		return i;
	}

	@Override
	public String getPath() {
		if (this.animationRenderer != null) {
			return new File(this.animationRenderer.getPath()).toPath().getParent().toString();
		}
		return null;
	}

	@Override
	public void setFPS(int fps) {
		if (this.hasIntro()) {
			this.introRenderer.setFPS(fps);
		}
		if (this.animationRenderer != null) {
			this.animationRenderer.setFPS(fps);
		}
	}

	@Override
	public void setLooped(boolean b) {
		if (this.animationRenderer != null) {
			this.animationRenderer.setLooped(b);
		}
	}

	@Override
	public int getFPS() {
		if (this.animationRenderer != null) {
			return this.animationRenderer.getFPS();
		}
		return 0;
	}

	@Override
	public boolean isGettingLooped() {
		return this.animationRenderer.isGettingLooped();
	}

	@Override
	public boolean isStretchedToStreensize() {
		if (this.animationRenderer != null) {
			return this.animationRenderer.isStretchedToStreensize();
		}
		return false;
	}

	@Override
	public int getWidth() {
		return this.animationRenderer.getWidth();
	}

	@Override
	public int getHeight() {
		return this.animationRenderer.getHeight();
	}

	@Override
	public int getPosX() {
		return this.animationRenderer.getPosX();
	}

	@Override
	public int getPosY() {
		return this.animationRenderer.getPosY();
	}
	
	public void setMuteAudio(boolean b) {
		this.muted = b;
	}
	
	public boolean hasMainAudio() {
		return ((this.mainAudioPath != null) && SoundHandler.soundExists(this.animationRenderer.getPath()));
	}
	
	public boolean hasIntroAudio() {
		return (this.hasIntro() && (this.introAudioPath != null) && SoundHandler.soundExists(this.introRenderer.getPath()));
	}
	
	public void stopAudio() {
		SoundHandler.stopSound(this.animationRenderer.getPath());
		if (this.hasIntro()) {
			SoundHandler.stopSound(this.introRenderer.getPath());
		}
	}
	
	public void resetAudio() {
		SoundHandler.resetSound(this.animationRenderer.getPath());
		if (this.hasIntro()) {
			SoundHandler.resetSound(this.introRenderer.getPath());
		}
	}
	
	public boolean replayIntro() {
		return this.replayIntro;
	}

}
