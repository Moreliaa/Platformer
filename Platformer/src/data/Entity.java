package data;

public interface Entity {

	public void update();

	public void draw(Camera c);

	public void drawHitbox(Camera c);

	public TileGrid getGrid();

	public float getX();

	public void setX(float xNew);

	public float getY();

	public void setY(float yNew);

	public int getWidth();

	public int getHeight();

	public int getxCoord();

	public int getyCoord();

	public int getxCoordR();

	public int getyCoordB();

	public float getxSpeed();

	public float getySpeed();
}
