package kz.aksay.polygraph.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import kz.aksay.polygraph.util.DateUtils;

@Entity
public class OrderProceedReport implements Serializable, Comparable<OrderProceedReport> {

	@Id
	private String monthYear;
	
	private Double procceedTime;
	
	public OrderProceedReport(String monthYear, double procceedTime) {
		this.monthYear = monthYear;
		this.procceedTime = Double.valueOf(procceedTime);
	}
	
	public OrderProceedReport(
			String monthYear, Date dateEndReal, Date createdAt) {
		
		this.monthYear = monthYear;
		this.procceedTime = Double.valueOf(
				DateUtils.differenceInDays(dateEndReal, createdAt));
	}

	public String getMonthYear() {
		return monthYear;
	}

	public void setMonthYear(String monthYear) {
		this.monthYear = monthYear;
	}

	public Double getProcceedTime() {
		return procceedTime;
	}

	public void setProcceedTime(Double procceedTime) {
		this.procceedTime = procceedTime;
	}

	@Override
	public int compareTo(OrderProceedReport o) {
		if(o == null) return -1; 
		if(monthYear != null) {
			if(o.getMonthYear() == null) return 1;
			return monthYear.compareTo(o.getMonthYear());
		}
		if(o.getMonthYear() == null) return 1;
		return -1;
	}
}
