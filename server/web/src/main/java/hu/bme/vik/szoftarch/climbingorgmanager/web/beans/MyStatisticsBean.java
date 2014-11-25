package hu.bme.vik.szoftarch.climbingorgmanager.web.beans;

import hu.bme.vik.szoftarch.climbingorgamanager.backend.managers.EntryManager;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Entry;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.chart.*;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
public class MyStatisticsBean {
	@Inject
	private EntryManager entryManager;

	@Setter
	@ManagedProperty(value = "#{authBean}")
	private AuthBean authBean;

	@Getter
	private LineChartModel entriesModel;

	@PostConstruct
	public void init() {
		createEntriesModel();
	}

	private void createEntriesModel() {
		LineChartSeries series = new LineChartSeries();
		int i = 0;
		Map<String, Integer> map = getEntriesPerDay();
		for (Map.Entry<String, Integer> mapEntry : map.entrySet()) {
			series.set(mapEntry.getKey(), mapEntry.getValue());
		}

		entriesModel = new LineChartModel();
		entriesModel.addSeries(series);
		entriesModel.setZoom(true);
		Axis dateAxis = new DateAxis();
		dateAxis.setTickAngle(-50);
		dateAxis.setTickFormat("%Y. %m. %d.");


		entriesModel.getAxes().put(AxisType.X, dateAxis);
	}

	private Map<String, Integer> getEntriesPerDay() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		List<Entry> entries = entryManager.getEntries(authBean.getUser());
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		for (Entry entry : entries) {
			String date = dateFormatter.format(entry.getEnteredOn());
			int prevValue = map.containsKey(date) ? map.get(date) : 0;
			map.put(date, prevValue + 1);
		}
		return map;
	}


}
