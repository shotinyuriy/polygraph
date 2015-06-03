package kz.aksay.polygraph.desktop.reports;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javafx.embed.swing.SwingNode;

import javax.swing.SwingUtilities;

import kz.aksay.polygraph.desktop.StartingPane;
import kz.aksay.polygraph.entityfx.MaterialConsumptionFX;
import kz.aksay.polygraph.entityfx.OrderFX;
import kz.aksay.polygraph.entityfx.ProducedWorkFX;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.export.DocxReportConfiguration;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleDocxReportConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.swing.JRViewer;

public class PrintFacade {
	
	public static List<JasperPrint> generateOrderDetails(List<OrderFX> orders) throws JRException {
		List<JasperPrint> jrPrintList = new ArrayList<>(orders.size());
		
		for(OrderFX order : orders) {
			jrPrintList.add(generateOrderDetails(order));
		}
		
		return jrPrintList;
	}
	
	public static JasperPrint generateOrderDetails(OrderFX order) throws JRException {
		
			JasperReport jasperReport = JasperCompileManager.compileReport(
					PrintFacade.class.getResourceAsStream(StartingPane.FXML_ROOT+"templates/OrderDetails.jrxml"));
			
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("DataFile", "CustomBeanFactory.java - Bean Collection");
			parameters.put("orderDate", order.getCreatedAtProperty().get());
			parameters.put("customerName", order.getCustomerFullName());
			parameters.put("orderName", order.getDescription());
			if(order.getCirculation() != null) {
				parameters.put("circulation", order.getCirculation().toString());
			}
			parameters.put("totalCost", order.getTotalCost().setScale(2).toString());
			
			List<MaterialConsumptionFX> materialConsumptions = new ArrayList<>();
			List<OrderDetails> orderDetailsList = new ArrayList<OrderDetails>();
			OrderDetails prodWorkHeader = new OrderDetails();
			prodWorkHeader.setNumber("№");
			prodWorkHeader.setAmount("Кол-во");
			prodWorkHeader.setDescription("Работа");
			prodWorkHeader.setCost("Стоимость");
			prodWorkHeader.setRowType("HEADER");
			prodWorkHeader.setWasted("в. т.ч. брак");
			orderDetailsList.add(prodWorkHeader);
			
			StringBuffer orderNumber = new StringBuffer();
			int number = 1;
			for(ProducedWorkFX producedWork : order.getProducedWorkProperty()) {
				OrderDetails orderDetails = new OrderDetails();
				orderDetails.setNumber(number+"");
				orderDetails.setDescription( producedWork.getWorkTypeName() );
				orderDetails.setAmount( producedWork.getQuantity().toString());
				orderDetails.setCost( producedWork.getCost().setScale(2).toString());
				orderDetails.setWasted( producedWork.getWasted().toString() );
				orderDetails.setRowType("ROW");
				orderDetailsList.add(orderDetails);
				for(MaterialConsumptionFX materialConsumptionFX : producedWork.getMaterialConsumptionFX()) {
					materialConsumptions.add(materialConsumptionFX);
				}
				if(producedWork.getEquipmentFX() != null) {
					if(orderNumber.length() > 0) orderNumber.append(" / ");
					orderNumber.append("№").append(producedWork.getEquipmentOrderNumber());
					orderNumber.append(" на ");
					orderNumber.append(producedWork.getEquipmentFX().getName());
				}
				number++;
			}
			
			parameters.put("orderNumber", orderNumber.toString());
			
			OrderDetails matHeader = new OrderDetails();
			matHeader.setNumber("№");
			matHeader.setAmount("Кол-во");
			matHeader.setDescription("Материалы");
			matHeader.setCost("Стоимость");
			matHeader.setRowType("HEADER");
			matHeader.setWasted("в. т.ч. брак");
			orderDetailsList.add(matHeader);
			
			number = 1;
			for(MaterialConsumptionFX materialConsumptionFX : materialConsumptions) {
				OrderDetails orderDetails = new OrderDetails();
				orderDetails.setNumber(number+"");
				orderDetails.setDescription( materialConsumptionFX.getMaterialName() );
				orderDetails.setAmount( materialConsumptionFX.getQuantity().toString());
				orderDetails.setCost( materialConsumptionFX.getCost().setScale(2).toString());
				orderDetails.setWasted( materialConsumptionFX.getWasted().toString() );
				orderDetails.setRowType("ROW");
				orderDetailsList.add(orderDetails);
				number++;
			}
			
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, new JRBeanCollectionDataSource(orderDetailsList));
			
			return jasperPrint;
	}
	
	public static void exportToDocx(JasperPrint jasperPrint, File docxFile) throws JRException {
		
		ExporterInput exporterInput = new SimpleExporterInput(jasperPrint);
		OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(docxFile);
		DocxReportConfiguration configuration = new SimpleDocxReportConfiguration();
		
		JRDocxExporter jrDocxExporter = new JRDocxExporter();
		
		jrDocxExporter.setExporterInput(exporterInput);
		jrDocxExporter.setExporterOutput(exporterOutput);
		jrDocxExporter.setConfiguration(configuration);
		
		jrDocxExporter.exportReport();
	}
	
	public static void embedJRViewerIntoSwingNode(final SwingNode swingNode, final JasperPrint jasperPrint) {
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JRViewer jrViewer = new JRViewer(jasperPrint);
				swingNode.setContent(jrViewer);
			}
		});
	}
	
	public static void embedJRViewerIntoSwingNode(final SwingNode swingNode, final List<JasperPrint> jasperPrintList) throws JRException {
		
		JasperPrint jasperPrintFirst = null;
		
		Iterator<JasperPrint> iter = jasperPrintList.iterator();
		
		while(iter.hasNext()) {
			JasperPrint jasperPrint = iter.next();
			if(jasperPrintFirst == null) {
				jasperPrintFirst = jasperPrint;
			} else {
				for(JRPrintPage page : jasperPrint.getPages() ) {
					jasperPrintFirst.addPage(page);
				}
			}
		}
		
		final JasperPrint jasperPrint = jasperPrintFirst;
						
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JRViewer jrViewer;
					jrViewer = new JRViewer(jasperPrint);
					swingNode.setContent(jrViewer);
			}
		});
	}
}
