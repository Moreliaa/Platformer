package data;

public class WaveSine {
	private float amplitude; // in pixels
	private float[] phase; // the current phase
	private int index;

	public WaveSine(float amplitudeInPixels, int lengthInFrames) {
		this.amplitude = amplitudeInPixels;
		this.phase = new float[lengthInFrames];
		this.index = 0;
		calculatePhaseValues();
	}

	private void calculatePhaseValues() {
		for (int i = 0; i < phase.length; i++) {
			float angle = 360 * (float) i / (float) phase.length;
			phase[i] = (float) Math.sin(angle * (180 / Math.PI)) * amplitude;
			// System.out.println(phase[i]);
		}
	}

	public float getPhase() {
		index++;
		if (index >= phase.length)
			index = 0;
		return phase[index];
	}

}
