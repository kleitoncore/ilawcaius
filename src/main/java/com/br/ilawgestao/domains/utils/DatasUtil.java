package com.br.ilawgestao.domains.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatasUtil {
	
	public DatasUtil() {
		
	}
	
	public static String NomeDoMes(int i, int tipo) { 
		String mes[] = {"Janeiro", "Fevereiro", "Março", "Abril", 
						"Maio", "Junho", "Julho", "Agosto", "Setembro", 
						"Outubro", "Novembro", "Dezembro"}; 
		if(tipo == 0) 
			return(mes[i-1]); 
		else 
			return(mes[i-1].substring(0, 3));
	}
	
	public static String DiaDaSemana(int i, int tipo) { 
		String diasem[] = {"Domingo", "Segunda-Feira", "Terça-Feira", 
				"Quarta-feira", "Quinta-Feira", 
				"Sexta-Feira", "Sábado"}; 
		if (tipo == 0) 
			return(diasem[i-1]); 
		else 
			return(diasem[i-1].substring(0, 3));
	}


	@SuppressWarnings("deprecation")
	public static String DataPorExtenso(Date dt) { 
		int d = dt.getDate(); 
		int m = dt.getMonth()+1; 
		int a = dt.getYear()+1900; 
		Calendar data = new GregorianCalendar(a, m-1, d); int ds = data.get(Calendar.DAY_OF_WEEK); 
		return(d + "/" + NomeDoMes(m, 0) + "(" + DiaDaSemana(ds, 1) + ")"); 
	}
	
	public static SimpleDateFormat formataData() {
		return new SimpleDateFormat("dd/MM/yyyy");
	}
	
	public static String formatarAgenda(String data) {
		SimpleDateFormat formatoTela = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date dataParametro = null;
		try {			
			dataParametro = formatoTela.parse(data);
		} catch( Exception e ) {
			e.printStackTrace();
		}
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		return formato.format(dataParametro);
	}
	
	public static String formatarAgenda(Date data) {
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		return formato.format(data);
	}
	
	public static String formatarDataBanco(String data) {
		SimpleDateFormat formatoTela = new SimpleDateFormat("dd/MM/yyyy");
		Date dataParametro = null;
		try {
			dataParametro = formatoTela.parse(data);
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		return formato.format(dataParametro);
	}
	
	public static String formatarDataBanco(Date data) {
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		return formato.format(data);
	}
	
	public static String formatarDataBancoOriginalHora(String data) {
		SimpleDateFormat formatoTela = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date dataParametro = null;
		try {
			dataParametro = formatoTela.parse(data);
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		return formato.format(dataParametro);
	}
	
	public static String formatarDataBancoHora(String data) {
		SimpleDateFormat formatoTela = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		Date dataParametro = null;
		try {
			dataParametro = formatoTela.parse(data);
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return formato.format(dataParametro);
	}
	
	public static String formatarDataTela(String data) {
		SimpleDateFormat formatoBanco = new SimpleDateFormat("yyyy-MM-dd");
		Date dataParametro = null;
		try {
			dataParametro = formatoBanco.parse(data);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		return formato.format(dataParametro);
	}
	
	public static String formatarDataTela(Date data) {
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		return formato.format(data);
	}
	
	public static String formatarDataHoraTela(String data) {
		SimpleDateFormat formatoBanco = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dataParametro = null;
		try {
			dataParametro = formatoBanco.parse(data);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		return formato.format(dataParametro);
	}

	public static String formatarDataHoraTela(Date data) {
		SimpleDateFormat formatoBanco = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dataParametro = null;
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		return formato.format(data);
	}
	
	public static String formatarHoraTela(String data) {
		SimpleDateFormat formatoBanco = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dataParametro = null;
		try {
			dataParametro = formatoBanco.parse(data);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SimpleDateFormat formato = new SimpleDateFormat("hh:mm");
		return formato.format(dataParametro);
	}
	
	public static String getDataAtual() {
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		Date data = new Date();
		return formato.format(data);
	}
	
	public static String getDataHoraAtual() {
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date data = new Date();
		return formato.format(data);
	}
	
	public static String getAnoAtual() {
		SimpleDateFormat formato = new SimpleDateFormat("yyyy");
		Date data = new Date();
		return formato.format(data);
	}
	
	public static String getMesAtual() {
		SimpleDateFormat formato = new SimpleDateFormat("MM");
		Date data = new Date();
		return formato.format(data);
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args) { 
		System.out.println(DatasUtil.NomeDoMes(12, 1));
	}
}
