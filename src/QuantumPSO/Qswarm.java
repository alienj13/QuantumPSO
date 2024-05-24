package QuantumPSO;

import java.io.IOException;
import java.util.Arrays;

public class Qswarm {

	private static final CuttingStock CS = new CuttingStock();
	private static final int PARTICLE_COUNT = 50; 
	private static final long TIME_LIMIT = 30;
	private static final long STARTING_TIME;
	private static final double G = 0.7; 
	private static Qparticle[] particles;
	private static Qparticle globalBest;
	private static double bestFitness;
	
	static {
		initialise();
		STARTING_TIME = System.currentTimeMillis();
	}


	public static void main(String[] args) throws IOException {
	
			initialise();
			
			while (!Termination()) {
	
				for (Qparticle particle : particles) {
					particle.updatePosition(globalBest, G);
					double fitness = particle.getFitness();

					if (fitness < 0) {
						particle.setPosition(CS.FixSolution(particle.getPosition()));
						continue;
					}

					if (fitness < particle.getBestFitness() && fitness > -1) {
						particle.setBestPosition(particle.getPosition().clone());
					}

			
					if (fitness < bestFitness && fitness > -1) {
						globalBest.setPosition(particle.getPosition().clone());
						bestFitness = fitness;
						System.out.println("New best " + " : " + bestFitness);
					}
				}
				
			}

			System.out.println(
					" - Best solution found: " + bestFitness + Arrays.toString(globalBest.getPosition()));

	
	}

	public static boolean Termination() {
		return System.currentTimeMillis() >= STARTING_TIME + TIME_LIMIT * 1000;
	}
	

	private static void initialise() {
		bestFitness = Double.MAX_VALUE;
		particles = new Qparticle[PARTICLE_COUNT];
		globalBest = null;

		for (int i = 0; i < PARTICLE_COUNT; i++) {
			int[] position = CS.generateRandomSolution();
			particles[i] = new Qparticle(position, CS);

			if (globalBest == null || particles[i].getFitness() < bestFitness) {
				globalBest = particles[i].copy();
				bestFitness = particles[i].getFitness();
			}
		}

		System.out.println("Initial best fitness " + " : " + bestFitness);
		System.out.println("");
	}
}
