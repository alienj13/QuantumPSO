package QuantumPSO;

import java.util.Random;

public class Qparticle {
	private int[] position;
	private int[] BestPosition;
	private CuttingStock cs;

	public Qparticle(int[] position, CuttingStock cs) {
		this.position = position;
		this.BestPosition = position.clone();
		this.cs = cs;

	}

	public void setPosition(int[] position) {
		this.position = position;
	}

	public void setBestPosition(int[] bestPosition) {
		BestPosition = bestPosition;
	}


	public int[] getBestPosition() {
		return BestPosition;
	}

	public int[] getPosition() {
		return position;
	}

	public double getBestFitness() {
		return cs.Evaluate(BestPosition);
	}

	public double getFitness() {

		return cs.Evaluate(position);
	}

	public Qparticle copy() {
		return new Qparticle(position.clone(), this.cs);
	}

	public void updatePosition(Qparticle globalBest, double g) {
		Random rand = new Random();
		for (int i = 0; i < position.length; i++) {

			double P = (BestPosition[i] + globalBest.getPosition()[i]) / 2.0;
			double u = rand.nextDouble();
			double L = (1.0 / g) * Math.abs(position[i] - P);
			if (rand.nextDouble() > 0.5) {
				position[i] = (int) (P + L * Math.log(1 / u));
			} else {
				position[i] = (int) (P - L * Math.log(1 / u));
			}
			position[i] = Math.max(0, position[i]);
		}

	}
}
