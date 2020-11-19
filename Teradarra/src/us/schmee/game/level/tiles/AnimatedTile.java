package us.schmee.game.level.tiles;


public class AnimatedTile extends BasicTile {

	private int[][] animationCoords;
	private int currentAnimationIndex;
	private long lastIterationTime;
	private int animationDelay;
	
	public AnimatedTile(int id, int[][] animationCoords, int tileColor, int color, int animationDelay) {
		super(id, animationCoords[0][0], animationCoords[0][1], tileColor, color);
		this.animationCoords = animationCoords;
		this.currentAnimationIndex = 0;
		this.lastIterationTime = System.currentTimeMillis();
		this.animationDelay = animationDelay;
	}
	
	public void tick() {
		if ((System.currentTimeMillis() - lastIterationTime) >= (animationDelay)) {
			lastIterationTime = System.currentTimeMillis();
			currentAnimationIndex = (currentAnimationIndex + 1) % animationCoords.length;
			this.tileId = (animationCoords[currentAnimationIndex][0] + (animationCoords[currentAnimationIndex][1] * 32));
		}
	}
}
