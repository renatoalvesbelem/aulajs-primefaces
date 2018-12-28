package br.com.project.report.util;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils implements Serializable {

	private static final long serialVersionUID = 1L;

	public static String getDateAtualReportName() {
		DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
		return dateFormat.format(Calendar.getInstance().getTime());
	}

	public static String formatDatesql(Date date) {
		StringBuffer retorno = new StringBuffer();
		DateFormat dateFormat = new SimpleDateFormat("yy--MM-dd");
		retorno.append("'");
		retorno.append(dateFormat.format(date));
		retorno.append("'");
		return retorno.toString();
	}

	public static String formatDatesqlSimple(Date date) {
		StringBuffer retorno = new StringBuffer();
		DateFormat dateFormat = new SimpleDateFormat("yy--MM-dd");
		retorno.append(dateFormat.format(date));
		return retorno.toString();
	}
}
