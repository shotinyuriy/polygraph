package kz.aksay.polygraph.desktop;

import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import kz.aksay.polygraph.api.IOrderService;
import kz.aksay.polygraph.entity.OrderProceedReport;
import kz.aksay.polygraph.util.SessionAware;

public class OrderProceedGraphController implements Initializable, SessionAware {

	@FXML private AnchorPane root;
	@FXML private LineChart<String, Double> lineChart;
	
	private Map<String, Object> session;
	private IOrderService orderService = StartingPane.getBean(IOrderService.class);
	

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadChart();
	}
	
	private void loadChart() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -12);
		Date dateFrom = calendar.getTime();
		calendar.add(Calendar.MONTH, 12);
		Date dateTo = calendar.getTime();
		
		List<OrderProceedReport> orderProceeds 
			= orderService.generateProceedReport(dateFrom, dateTo);
		
		XYChart.Series<String, Double> series 
			= new XYChart.Series<String, Double>();
		
		for(OrderProceedReport ordProcRep : orderProceeds) {
			series.getData().add(new XYChart.Data<String, Double>(
					ordProcRep.getMonthYear(), ordProcRep.getProcceedTime().doubleValue()));
		}
		
		lineChart.getData().add(series);
	}

}
