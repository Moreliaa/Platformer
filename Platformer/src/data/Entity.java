package data;

public interface Entity {

	public void update();

	public void draw(Camera c);

	public void drawHitbox(Camera c);

	public float getX();

	public void setX(float xNew);

	public float getY();

	public float getxSpeed();

	public TileGrid getGrid();

	public int getWidth();

	public int getHeight();

	public int getxCoord();

	public int getyCoord();

	public int getxCoordR();

	public int getyCoordB();

	public float getySpeed();

	public void setY(float yNew);
}
