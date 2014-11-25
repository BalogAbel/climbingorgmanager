package hu.bme.vik.szoftarch.climbingorgmanager.web.beans.admin;

import hu.bme.vik.szoftarch.climbingorgamanager.backend.managers.EntryManager;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Entry;
import lombok.Getter;
import org.primefaces.model.chart.*;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Abel on 2014.11.25..
 */
@Named
@RequestScoped
public class AdminStatisticsBean {

	@Inject
	private EntryManager entryManager;

	@Getter
	private Map<String, Integer> weeklyEntries;

	@Getter
	private Map<String, Integer> monthlyEntries;

	@Getter
	private ChartModel dailyEntriesModel;


	@PostConstruct
	public void init() {
		createEntriesModel();
	}

	private void createEntriesModel() {
		dailyEntriesModel = createModel();

		weeklyEntries = getEntriesPerPeriod(1);
		monthlyEntries = getEntriesPerPeriod(2);


	}

	private ChartModel createModel() {
		LineChartModel model = new LineChartModel();

		LineChartSeries series = new LineChartSeries();
		series.setShowLine(false);
		int i = 0;
		Map<String, Integer> map = getEntriesPerPeriod(0);
		for (Map.Entry<String, Integer> mapEntry : map.entrySet()) {
			series.set(mapEntry.getKey(), mapEntry.getValue());
		}

		model.addSeries(series);
		model.setZoom(true);
		Axis dateAxis = new DateAxis();
		dateAxis.setTickAngle(-50);
		dateAxis.setTickFormat("%Y. %m. %d.");
		model.getAxes().put(AxisType.X, dateAxis);

		return model;
	}

	private Map<String, Integer> getEntriesPerPeriod(int type) {
		Map<String, Integer> map = new LinkedHashMap<>();
		List<Entry> entries = entryManager.getEntries();
		Collections.sort(entries, new Comparator<Entry>() {
			@Override
			public int compare(Entry o1, Entry o2) {
				return o2.getEnteredOn().compareTo(o1.getEnteredOn());
			}
		});

		DateFormat dateFormatter;
		switch (type) {
			case 0:
				dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
				break;
			case 1:
				dateFormatter = new SimpleDateFormat("w. 'week' (yyyy)");
				break;
			case 2:
				dateFormatter = new SimpleDateFormat("MMMM. (yyyy)", Locale.ENGLISH);
				break;
			default:
				dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
				break;
		}
		for (Entry entry : entries) {
			String date = dateFormatter.format(entry.getEnteredOn());
			int prevValue = map.containsKey(date) ? map.get(date) : 0;
			map.put(date, prevValue + 1);
		}
		return map;
	}
}
