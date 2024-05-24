package QuantumPSO;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class CuttingStock {

	public ArrayList<int[]> Activities = new ArrayList<>();
	public HashMap<Integer, Integer> LengthQuantity = new HashMap<>();
	public HashMap<Integer, Double> StockLengthPrice = new HashMap<>();

	public CuttingStock() {
		try {
			LoadFile("Problem4");
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found");
		}
		generateConfigurations();

	}

	public int[] generateRandomSolution() {
		HashMap<Integer, Integer> LQ = new HashMap<>(LengthQuantity);
		int[] solution = new int[Activities.size()];
		Random r = new Random();

		for (int Piecelength : LQ.keySet()) {
			while (LQ.get(Piecelength) > 0) {
				int index = r.nextInt(solution.length);
				int[] activity = Activities.get(index);
				for (int length : activity) {
					LQ.put(length, LQ.get(length) - 1);
				}
				solution[index] += 1;
			}
		}

		return solution;
	}

	public double Evaluate(int[] solution) {
		if (solution.length != Activities.size()) {
			System.out.println("Solution size is wrong");
			return -1;
		}
		HashMap<Integer, Integer> LQ = new HashMap<>(LengthQuantity);
		double cost = 0;
		for (int i = 0; i < solution.length; i++) {
			if (solution[i] < 1) {
				continue;
			}
			int PieceLength = 0;
			for (int j : Activities.get(i)) {
				PieceLength += j;
				LQ.put(j, LQ.get(j) - solution[i]);

			}
			ArrayList<Integer> stockLengthPieces = new ArrayList<>(StockLengthPrice.keySet());
			Collections.sort(stockLengthPieces);
			for (int Stocklength : stockLengthPieces) {
				if (PieceLength <= Stocklength) {
					cost += StockLengthPrice.get(Stocklength) * solution[i];
					break;
				}
			}
		}
		ArrayList<Integer> Quantity = new ArrayList<>(LQ.values());
		for (int quant : Quantity) {
			if (quant > 0) {
				return -1;
			}
		}

		return cost;
	}

	public int[] FixSolution(int[] solution) {
		HashMap<Integer, Integer> LQ = new HashMap<>(LengthQuantity);

		for (int i = 0; i < solution.length; i++) {
			if (solution[i] < 1) {
				continue;
			}

			for (int j : Activities.get(i)) {
				LQ.put(j, LQ.get(j) - solution[i]);
			}
		}

		Random r = new Random();

		for (int Piecelength : LQ.keySet()) {
			while (LQ.get(Piecelength) > 0) {
				int index = r.nextInt(solution.length);
				int[] activity = Activities.get(index);
				for (int length : activity) {
					LQ.put(length, LQ.get(length) - 1);
				}
				solution[index] += 1;
			}
		}

		return solution;
	}

	private void generateConfigurations() {
		ArrayList<Integer> stockLength = new ArrayList<>(StockLengthPrice.keySet());
		Collections.sort(stockLength);
		for (int i = 0; i < stockLength.size(); i++) {
			ArrayList<Integer> currentConfiguration = new ArrayList<>();
			generate(stockLength.get(i), 0, currentConfiguration, 0);
		}
	}

	private void generate(int stockLength, int currentSum, ArrayList<Integer> currentConfiguration, int pieceIndex) {
		ArrayList<Integer> pieceLengths = new ArrayList<>(LengthQuantity.keySet());
		if (pieceIndex == pieceLengths.size()) {
			if (WasteCheck(stockLength, currentSum)) {
				Activities.add(currentConfiguration.stream().mapToInt(i -> i).toArray());
			}
			return;
		}

		for (int i = 0; currentSum + i * pieceLengths.get(pieceIndex) <= stockLength; i++) {
			ArrayList<Integer> newConfiguration = new ArrayList<>(currentConfiguration);
			for (int j = 0; j < i; j++) {
				newConfiguration.add(pieceLengths.get(pieceIndex));
			}
			generate(stockLength, currentSum + i * pieceLengths.get(pieceIndex), newConfiguration, pieceIndex + 1);
		}
	}

	private boolean WasteCheck(int stockLength, int currentSum) {
		ArrayList<Integer> stockLengths = new ArrayList<>(StockLengthPrice.keySet());
		Collections.sort(stockLengths);
		if (stockLength == stockLengths.get(0)) {
			return currentSum > 0 && currentSum <= stockLength;
		}
		for (int i = 0; i < stockLengths.size(); i++) {
			if (stockLength == stockLengths.get(i)) {
				int nextSmallerStockLength = (i == 0) ? 0 : stockLengths.get(i - 1);
				return currentSum > nextSmallerStockLength && currentSum <= stockLength;
			}
		}
		return false;
	}

	private void LoadFile(String file) throws FileNotFoundException {

		ArrayList<Integer> StockLength = new ArrayList<>();
		ArrayList<Double> stockCost = new ArrayList<>();
		ArrayList<Integer> Length = new ArrayList<>();
		ArrayList<Integer> Quantity = new ArrayList<>();

		String filename = "data/" + file + ".csv";
		File f = new File(filename);
		Scanner sc = new Scanner(f);
		Scanner sc1 = new Scanner(f);

		while (sc.hasNext()) {
			try {
				String[] data = sc.next().split(",");
				StockLength.add(Integer.parseInt(data[0]));
				stockCost.add(Double.parseDouble(data[1]));
			} catch (NumberFormatException e) {
				continue;
			}
		}

		while (sc1.hasNext()) {
			try {
				String[] data = sc1.next().split(",");
				Length.add(Integer.parseInt(data[2]));
				Quantity.add(Integer.parseInt(data[3]));

			} catch (NumberFormatException e) {
				continue;
			}
		}

		sc.close();
		sc1.close();
		for (int i = 0; i < Length.size(); i++) {
			LengthQuantity.put(Length.get(i), Quantity.get(i));
		}

		for (int i = 0; i < StockLength.size(); i++) {

			StockLengthPrice.put(StockLength.get(i), stockCost.get(i));
		}

	}

	public void PrintSolution(int[] solution) {
		for (int i = 0; i < solution.length; i++) {
			if (solution[i] > 0) {
				System.out.println(i + 1 + " - " + Arrays.toString(Activities.get(i)) + " x" + solution[i]);
			}
		}
	}

}
