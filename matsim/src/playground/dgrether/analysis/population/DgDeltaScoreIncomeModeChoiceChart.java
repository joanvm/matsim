/* *********************************************************************** *
 * project: org.matsim.*
 * DgDeltaScoreIncomeChart
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2009 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */
package playground.dgrether.analysis.population;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.matsim.core.population.PlanImpl.Type;
import org.matsim.core.utils.collections.Tuple;

import playground.dgrether.utils.charts.DgChartFrame;



public class DgDeltaScoreIncomeModeChoiceChart {
	
	private static final Logger log = Logger.getLogger(DgDeltaScoreIncomeModeChoiceChart.class);
	
	private DgAnalysisPopulation ana;

	private boolean writeModeSwitcherOnly = false;
	
	public DgDeltaScoreIncomeModeChoiceChart(DgAnalysisPopulation ana) {
		this.ana = ana;
	}

	public void writeFile(String filename) {
		List<Tuple<Double, Double>> valuesCarCar = new ArrayList<Tuple<Double, Double>>();
		List<Tuple<Double, Double>> valuesPtPt = new ArrayList<Tuple<Double, Double>>();
		List<Tuple<Double, Double>> valuesPtCar = new ArrayList<Tuple<Double, Double>>();
		List<Tuple<Double, Double>> valuesCarPt = new ArrayList<Tuple<Double, Double>>();
				
		//calculations
		for (DgPersonData d : ana.getPersonData().values()) {
			DgPlanData planDataRun1 = d.getPlanData().get(DgAnalysisPopulation.RUNID1);
			DgPlanData planDataRun2 = d.getPlanData().get(DgAnalysisPopulation.RUNID2);
			Double scoreDiff = planDataRun2.getScore() - planDataRun1.getScore();
			Tuple<Double, Double> t = new Tuple<Double, Double>(d.getIncome().getIncome(), scoreDiff);
			
			if (planDataRun1.getPlan().getType().equals(Type.CAR) && planDataRun2.getPlan().getType().equals(Type.CAR)) {
				valuesCarCar.add(t);
			}
			else if (planDataRun1.getPlan().getType().equals(Type.PT) && planDataRun2.getPlan().getType().equals(Type.PT)) {
				valuesPtPt.add(t);
			}
			else if (planDataRun1.getPlan().getType().equals(Type.PT) && planDataRun2.getPlan().getType().equals(Type.CAR)) {
				if (d.getIncome().getIncome() > 100000.0 ) {
					log.info("PT->Car person id: " + d.getPersonId().toString() + " income " + d.getIncome().getIncome());
				}
				valuesPtCar.add(t);
			}
			else if (planDataRun1.getPlan().getType().equals(Type.CAR) && planDataRun2.getPlan().getType().equals(Type.PT)){
				if (d.getIncome().getIncome() > 100000.0 ) {
					log.info("Car->PT person id: " + d.getPersonId().toString() + " income " + d.getIncome().getIncome());
				}
				valuesCarPt.add(t);
			}
		}
		
		//write the chart...
		XYSeriesCollection dataset = new XYSeriesCollection();
		if (!writeModeSwitcherOnly) {
			dataset.addSeries(this.createSeries("Car2Car", valuesCarCar));
		}

		if (!writeModeSwitcherOnly) {
			dataset.addSeries(this.createSeries("Pt2Pt", valuesPtPt));
		}
		
		dataset.addSeries(this.createSeries("Pt2Car", valuesPtCar));

		dataset.addSeries(this.createSeries("Car2Pt", valuesCarPt));
		
		
		XYPlot plot = new XYPlot(dataset, new NumberAxis("Income"), new NumberAxis("Delta utils"), null);
		
		
		XYItemRenderer renderer = new XYLineAndShapeRenderer(false, true);
		plot.setRenderer(renderer);
		
		
		JFreeChart jchart = new JFreeChart("", plot);
		
		new DgChartFrame("", jchart);
		
		try {
			ChartUtilities.saveChartAsPNG(new File(filename), jchart, 800, 600, null, true, 9);
			log.info("DeltaScoreIncomeChart written to : " +filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
//		chart.saveAsPng(filename, 800, 600);
	}

	private XYSeries createSeries(final String title, List<Tuple<Double, Double>> values) {
		XYSeries series = new XYSeries(title, false, true);
		for (Tuple<Double, Double> t : values) {
			series.add(t.getFirst(), t.getSecond());
		}
		return series;
	}
	
	public boolean isWriteModeSwitcherOnly() {
		return writeModeSwitcherOnly;
	}



	
	public void setWriteModeSwitcherOnly(boolean writeModeSwitcherOnly) {
		this.writeModeSwitcherOnly = writeModeSwitcherOnly;
	}
	


}
