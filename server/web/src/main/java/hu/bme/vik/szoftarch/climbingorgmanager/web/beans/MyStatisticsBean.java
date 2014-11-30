package hu.bme.vik.szoftarch.climbingorgmanager.web.beans;

import hu.bme.vik.szoftarch.climbingorgamanager.backend.managers.EntryManager;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Entry;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Pass;
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
import java.util.*;

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

	@Getter
	private List<Pass> passes;

	@PostConstruct
	public void init() {
		updatePasses();
		createEntriesModel();
	}

	private void updatePasses() {
		passes = entryManager.getPasses(authBean.getUser());

		Collections.sort(passes, new Comparator<Pass>() {

			@Override
			public int compare(Pass o1, Pass o2) {
				if (o1.getValidUntil().after(o2.getValidUntil())) {
					if (o1.getTimeLeft() == 0) return -1;
					if (o1.getValidUntil().after(new Date())) return -1;
				}
				return 1;
			}
		});
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
