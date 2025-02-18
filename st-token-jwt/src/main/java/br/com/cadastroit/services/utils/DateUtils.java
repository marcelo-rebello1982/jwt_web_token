package br.com.cadastroit.services.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Optional;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;

import br.com.cadastroit.services.config.security.model.UserDetailsJwt;

public abstract class DateUtils {

	public static final String DATE_PATTERN = "dd/MM/yyyy";
	public static final String DD_MM_YYYY = "dd-MM-yyyy";
	public static final String DATETIME_PATTERN = "dd/MM/yyyy HH:mm:ss";
	public static final String DATEHOURMINUTE_PATTERN = "dd/MM/yyyy HH:mm";
	public static final String UTC_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
	public static final String TIME = "HH:mm:ss:SSS";
	
	static final long EXPIRATION_TIME = System.getenv("expire") != null ? (Long.parseLong(System.getenv("EXPIRE")) * (1000 * 60 * 60 * 24))	: (1000 * 60 * 60 * 24);
	
	// Abordagem 1: Usando ZoneOffset.UTC
	public static long getUtcTimestamp() {

		return LocalDateTime.now(ZoneOffset.UTC).toInstant(ZoneOffset.UTC).toEpochMilli();
	}

	// Abordagem 2: Usando Instant (mais direto)
	public static long getUtcTimestamp2() {

		return Instant.now().toEpochMilli();
	}

	// Abordagem 3: Se precisar converter uma data específica para UTC
	public static long convertToUtc(LocalDateTime localDate) {

		return localDate.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli();
	}

	// Abordagem 4: Se estiver trabalhando com timestamp
	public LocalDateTime getUtcDateTime(long timestamp) {

		return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneOffset.UTC);
	}

	public static long getUtcTimestampLegacy() {

		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		return calendar.getTimeInMillis();
	}

	public static String formatUtcDate(long timestamp) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneOffset.UTC).format(formatter);
	}

	public static long getDaysBetweenUtc(long initDate, long endDate) {
		LocalDateTime startDate1 = LocalDateTime.ofInstant(Instant.ofEpochMilli(initDate), ZoneOffset.UTC);
		LocalDateTime startDate2 = LocalDateTime.ofInstant(Instant.ofEpochMilli(endDate), ZoneOffset.UTC);
		return ChronoUnit.DAYS.between(startDate1, startDate2);
	}

	public long addDaysUtc(long timestamp, long days) {

		return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), 
				ZoneOffset.UTC).plusDays(days)
					.toInstant(ZoneOffset.UTC)
						.toEpochMilli();
		
	}

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public static long getCurrentUtcTimestamp() {

		return Instant.now().toEpochMilli();
	}
	
	public static String formatUtc(long timestamp) {

		return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneOffset.UTC).format(formatter);
	}

	public static String format(String format, Date date) {

		SimpleDateFormat dtFormat = new SimpleDateFormat(format);

		return dtFormat.format(date);
	}

	public static String formatDateDefaultSystemFormat(Date date) {

		return format(Constants.CORE_DEFAULT_DATE_TIME_PATTERN, date);
	}

	public static Calendar startOfDay(Calendar date) {

		if (date == null)
			return null;

		date = (Calendar) date.clone();
		date.set(Calendar.HOUR_OF_DAY, date.getActualMinimum(Calendar.HOUR_OF_DAY));
		date.set(Calendar.MINUTE, date.getActualMinimum(Calendar.MINUTE));
		date.set(Calendar.SECOND, date.getActualMinimum(Calendar.SECOND));
		date.set(Calendar.MILLISECOND, date.getActualMinimum(Calendar.MILLISECOND));

		return date;
	}

	public static Calendar endOfDay(Calendar date) {

		if (date == null)
			return null;

		date = (Calendar) date.clone();
		date.set(Calendar.HOUR_OF_DAY, date.getActualMaximum(Calendar.HOUR_OF_DAY));
		date.set(Calendar.MINUTE, date.getActualMaximum(Calendar.MINUTE));
		date.set(Calendar.SECOND, date.getActualMaximum(Calendar.SECOND));
		date.set(Calendar.MILLISECOND, date.getActualMaximum(Calendar.MILLISECOND));

		return date;
	}

	public static String fullDate(Calendar date) {

		return new SimpleDateFormat(DATETIME_PATTERN).format(date.getTime());
	}

	public static String dateOnly(Calendar date) {

		return new SimpleDateFormat(DATE_PATTERN).format(date.getTime());
	}

	public static String dateOnly(Date date) {

		return new SimpleDateFormat(DATE_PATTERN).format(date);
	}

	public static String dateOnlyWithoutDays(Calendar date) {

		return new SimpleDateFormat("MM/yyyy").format(date.getTime());
	}

	public static String timeOnly(Calendar date) {

		return new SimpleDateFormat("HH:mm:ss").format(date.getTime());
	}

	public static String yearOnly(Calendar date) {

		return new SimpleDateFormat("yyyy").format(date.getTime());
	}

	public static int diffDays(Calendar valor, Calendar comparacao) {

		if (valor == null || comparacao == null)
			return 0;

		long diferencaMiliSegundos = valor.getTimeInMillis() - comparacao.getTimeInMillis();
		double resultado = diferencaMiliSegundos / (24.0 * 60.0 * 60.0 * 1000.0);

		return BigDecimalUtils.arredondar(new BigDecimal(resultado), 0, RoundingMode.UP).intValue();
	}

	public static int diffMinutes(Calendar start, Calendar end) {

		if (start == null || end == null)
			return 0;

		double toMinutes = 1000.0 * 60.0;
		double diffMinutes = (end.getTimeInMillis() - start.getTimeInMillis()) / toMinutes;

		return BigDecimalUtils.arredondar(new BigDecimal(diffMinutes), 0, RoundingMode.HALF_UP).intValue();
	}

	public static int diffSeconds(Calendar start, Calendar end) {

		if (start == null || end == null)
			return 0;

		double toSeconds = 1000.0;
		double diffMinutes = (end.getTimeInMillis() - start.getTimeInMillis()) / toSeconds;

		return BigDecimalUtils.arredondar(new BigDecimal(diffMinutes), 0, RoundingMode.HALF_UP).intValue();
	}

	public static boolean compareDate(Calendar data1, Calendar data2) {

		if (data1.get(Calendar.DAY_OF_MONTH) == data2.get(Calendar.DAY_OF_MONTH) && data1.get(Calendar.MONTH) == data2.get(Calendar.MONTH)
				&& data1.get(Calendar.YEAR) == data2.get(Calendar.YEAR))
			return true;

		return false;

	}

	public static Calendar setFirstDayOfMonth(Calendar date) {

		Calendar clone = (Calendar) date.clone();
		clone.add(Calendar.DAY_OF_MONTH, -1);

		return clone;
	}


	/**
	 * Calcula a diferença em dias entre as duas datas.
	 * <p>
	 * <b>ATENÇÃO: esta comparação considera as horas nas duas datas!</b>
	 * </p>
	 * 
	 * <table border="1">
	 * <tr>
	 * <td>init</td>
	 * <td>end</td>
	 * <td>resultado</td>
	 * </tr>
	 * <tr>
	 * <td>09/01/2025 14:30:00</td>
	 * <td>10/01/2025 12:30:00</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>09/01/2025 12:30:00</td>
	 * <td>10/01/2025 12:30:00</td>
	 * <td>1</td>
	 * </tr>
	 * </table>
	 * 
	 * <p>
	 * Para comparar considerando apenas a data, utilizar {@link #daysBetween(Calendar, Calendar, boolean)}
	 * </p>
	 * 
	 * @param init Data inicial
	 * @param end Data final
	 * @return Quantidade de dias entre as datas.
	 */
	public static long daysBetween(Calendar init, Calendar end) {

		return daysBetween(init, end, false);
	}

	/**
	 * Calcula a diferença em dias entre as duas datas.
	 * 
	 * @param init Data inicial
	 * @param end Data final
	 * @param ignoreTime Caso verdadeiro, ignora as horas das duas datas (zerando as mesmas para comparação).
	 * @return Quantidade de dias entre as datas.
	 */
	public static long daysBetween(Calendar init, Calendar end, boolean ignoreTime) {

		Calendar c1 = (Calendar) init.clone();
		Calendar c2 = (Calendar) end.clone();

		if (ignoreTime) {

			resetTime(c1);
			resetTime(c2);
		}

		LocalDateTime local1 = LocalDateTime.ofInstant(c1.toInstant(), init.getTimeZone().toZoneId());
		LocalDateTime local2 = LocalDateTime.ofInstant(c2.toInstant(), end.getTimeZone().toZoneId());

		return ChronoUnit.DAYS.between(local1, local2);

	}
	
	

	public static String formatDate(Calendar date) {

		return new SimpleDateFormat(DATE_PATTERN).format(date.getTime());
	}

	public static String formatDateTimeHourMinute(Calendar date) {

		return new SimpleDateFormat(DATEHOURMINUTE_PATTERN).format(date.getTime());
	}

	public static String getCurrentDateTime() {

		return formatDateTimeHourMinute(Calendar.getInstance());
	}

	public static String getCurrentTime() {

		return new SimpleDateFormat(TIME).format(new Date(System.currentTimeMillis()));
	}

	public static Calendar plusDays(Calendar date, int days) {

		Calendar d1 = (Calendar) date.clone();
		d1.add(Calendar.DATE, days);

		return d1;
	}

	public static Calendar minusDays(Calendar date, int days) {

		Calendar d1 = (Calendar) date.clone();
		d1.add(Calendar.DATE, -days);

		return d1;
	}

	public static Calendar addMonths(Calendar date, int months) {

		if (date == null)
			return null;

		Calendar newDate = (Calendar) date.clone();
		newDate.add(Calendar.MONTH, months);

		return newDate;
	}

	/**
	 * Verifica se a data é posterior a data de comparação ignorando horas/minutos/segundos e milisegundos
	 * 
	 * @param date Data
	 * @param dateToCompare Data para comparação
	 * @return True se date é posterior a dateToCompare.
	 */
	public static boolean isAfter(Calendar date, Calendar dateToCompare) {

		return isAfter(date, dateToCompare, false);
	}

	/**
	 * Verifica se a data é posterior a data de comparação
	 * 
	 * @param date Data
	 * @param dateToCompare Data para comparação
	 * @param ignoreTime Para indicar se deve ser ignorado as horas na comparação
	 * @return True se date é posterior a dateToCompare.
	 */
	public static boolean isAfter(Calendar date, Calendar dateToCompare, boolean ignoreTime) {

		if (date == null || dateToCompare == null)
			return false;

		Calendar newDate = ignoreTime ? startOfDay(date) : (Calendar) date.clone();
		Calendar newDateToCompare = ignoreTime ? startOfDay(dateToCompare) : (Calendar) dateToCompare.clone();

		return newDate.after(newDateToCompare);
	}

	public static Calendar dateToCalendar(Date d) {

		Calendar c = Calendar.getInstance();
		c.setTime(d);

		return (Calendar) c.clone();

	}

	/**
	 * Adicionando 00:00:00 a data informada
	 * 
	 * @param data
	 * @return date object
	 */
	public static java.util.Date addMinHourToDate(String data) {

		Calendar calendar = new GregorianCalendar();
		String[] splitData = data.split("/");
		calendar.set(Integer.parseInt(splitData[2]), Integer.parseInt(splitData[1]) - 1, Integer.parseInt(splitData[0]), 00, 00, 00);
		return calendar.getTime();
	}

	/**
	 * Adicionando 23:59:59 a data informada
	 * 
	 * @param data
	 * @return date object
	 */
	public static java.util.Date addMaxHourToDate(String data) {

		Calendar calendar = new GregorianCalendar();
		String[] splitData = data.split("/");
		calendar.set(Integer.parseInt(splitData[2]), Integer.parseInt(splitData[1]) - 1, Integer.parseInt(splitData[0]), 23, 59, 59);
		return calendar.getTime();
	}

	public static java.util.Date addDaysSelect(String data, int numDays) {

		Calendar calendar = new GregorianCalendar();
		String[] splitData = data.split("/");
		calendar.set(Integer.parseInt(splitData[2]), Integer.parseInt(splitData[1]) - 1, Integer.parseInt(splitData[0]));
		calendar.add(Calendar.DAY_OF_MONTH, numDays);
		java.util.Date newDate = new java.util.Date(calendar.getTimeInMillis());
		return newDate;
	}

	public static String toDateString(Timestamp timestamp) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		if (timestamp != null) {
			return sdf.format(timestamp);
		}
		return "";

	}

	public static String toDateString(Date date) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		if (date != null) {
			return sdf.format(date);
		}
		return "";

	}
	
	public boolean validateTokenDate(UserDetailsJwt userDetailsJwt) {
		
		long dateToExpire = userDetailsJwt.getExpire();
		long actualDate = System.currentTimeMillis();
		
		return  dateToExpire > actualDate;
	}
	

	public static Date parseDateDefaultSystemFormat(String date) throws Exception {

		SimpleDateFormat format = new SimpleDateFormat(Constants.CORE_DEFAULT_DATE_TIME_PATTERN);

		try {
			return format.parse(date);
		} catch (ParseException e) {
			throw new Exception("Formato de data inv�lido");
		}
	}

	public static int diffDays1(Calendar valor, Calendar comparacao) {

		if (valor == null || comparacao == null)
			return 0;

		long diferencaMiliSegundos = valor.getTimeInMillis() - comparacao.getTimeInMillis();
		double resultado = diferencaMiliSegundos / (24.0 * 60.0 * 60.0 * 1000.0);

		return BigDecimalUtils.arredondar(new BigDecimal(resultado), 0, RoundingMode.UP).intValue();
	}

	public static int diffMinutes1(Calendar start, Calendar end) {

		if (start == null || end == null)
			return 0;

		double toMinutes = 1000.0 * 60.0;
		double diffMinutes = (end.getTimeInMillis() - start.getTimeInMillis()) / toMinutes;

		return BigDecimalUtils.arredondar(new BigDecimal(diffMinutes), 0, RoundingMode.HALF_UP).intValue();
	}

	public static int diffSeconds1(Calendar start, Calendar end) {

		if (start == null || end == null)
			return 0;

		double toSeconds = 1000.0;
		double diffMinutes = (end.getTimeInMillis() - start.getTimeInMillis()) / toSeconds;

		return BigDecimalUtils.arredondar(new BigDecimal(diffMinutes), 0, RoundingMode.HALF_UP).intValue();
	}

	public static boolean compareDate1(Calendar data1, Calendar data2) {

		if (data1.get(Calendar.DAY_OF_MONTH) == data2.get(Calendar.DAY_OF_MONTH) && data1.get(Calendar.MONTH) == data2.get(Calendar.MONTH)
				&& data1.get(Calendar.YEAR) == data2.get(Calendar.YEAR))
			return true;

		return false;

	}

	public static Calendar setFirstDayOfMonth1(Calendar date) {

		Calendar clone = (Calendar) date.clone();
		clone.add(Calendar.DAY_OF_MONTH, -1);

		return clone;
	}

	/**
	 * Set the calendar's hour, minute, second and ms to zero leaving the rest intact
	 * 
	 * @param calendar The calendar to reset
	 */
	public static void resetCalendar(Calendar calendar) {

		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(calendar.get(Calendar.YEAR), 
		calendar.get(Calendar.MONTH), 
		calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
	}

	

	public static Calendar Pascoa(int ano) {

		int a = ano % 19;
		int b = ano / 100;
		int c = ano % 100;
		int d = b / 4;
		int e = b % 4;
		int f = (b + 8) / 25;
		int g = (b - f + 1) / 3;
		int h = (19 * a + b - d - g + 15) % 30;
		int i = c / 4;
		int j = c % 4;
		int k = (32 + 2 * e + 2 * i - h - j) % 7;
		int m = (a + 11 * h + 22 * k) / 451;
		int n = (h + k - 7 * m + 114) / 31;
		int p = (h + k - 7 * m + 114) % 31;
		p = p + 1;
		n = n - 1;
		Calendar pascoa = Calendar.getInstance();
		pascoa.set(ano, n, p);

		resetCalendar(pascoa);

		return pascoa;
	}

	public static Calendar Carnaval(int ano) {

		Calendar carnaval = Pascoa(ano);
		carnaval.add(Calendar.DAY_OF_MONTH, -47);
		resetCalendar(carnaval);

		return carnaval;
	}

	public static Calendar CorpusChristi(int ano) {

		Calendar corpusChristi = Pascoa(ano);
		corpusChristi.add(Calendar.DAY_OF_MONTH, 60);
		resetCalendar(corpusChristi);

		return corpusChristi;
	}

	public static Calendar natal(int ano) {

		Calendar natal = Calendar.getInstance();
		natal.set(ano, Calendar.DECEMBER, 25);
		resetCalendar(natal);

		return natal;
	}

	public static Calendar anoNovo(int ano) {

		Calendar anoNovo = Calendar.getInstance();
		anoNovo.set(ano, Calendar.JANUARY, 1);
		resetCalendar(anoNovo);

		return anoNovo;
	}

	public static boolean isValidRange(Calendar inicialDate, Calendar finalDate) {

		if (inicialDate == null || finalDate == null)
			return false;

		return inicialDate.compareTo(finalDate) <= 0;
	}

	public static Date stringToDate(String date) {

		if (StringUtils.isBlank(date))
			return null;

		int length = StringUtils.deleteWhitespace(date).length();
		String format = DATE_PATTERN;

		if (length == 18) {

			// data + hora:minuto:segundo
			format = DATETIME_PATTERN;

		} else if (length == 15) {

			// data + hora:minuto
			format = DATEHOURMINUTE_PATTERN;

		}

		return stringToDate(date, format);
	}

	public static Date stringToDate(String date, String format) {

		LocalDate ld = LocalDate.from(DateTimeFormatter.ofPattern(format).parse(date));
		Date d = java.sql.Date.valueOf(ld);

		return d;

	}

	public static Calendar stringToCalendar(String date) {

		if (StringUtils.isBlank(date))
			return null;

		int length = StringUtils.deleteWhitespace(date).length();
		String format = DATE_PATTERN;

		if (length == 18) {

			// data + hora:minuto:segundo
			format = DATETIME_PATTERN;

		} else if (length == 15) {

			// data + hora:minuto
			format = DATEHOURMINUTE_PATTERN;

		}

		return stringToCalendar(date, format);
	}

	public static Calendar stringToCalendar(String date, String format) {

		Date d = stringToDate(date, format);

		Calendar c = Calendar.getInstance();
		c.setTime(d);

		return c;

	}

	public static String getCalendarExtenso(Calendar data) {

		String mes[] = { "Janeiro",
				"Fevereiro",
				"Marco",
				"Abril",
				"Maio",
				"Junho",
				"Julho",
				"Agosto",
				"Setembro",
				"Outubro",
				"Novembro",
				"Dezembro" };

		String dataExtenso = data.get(Calendar.DAY_OF_MONTH) + " de " + mes[data.get(Calendar.MONTH)] + " de " + data.get(Calendar.YEAR);

		return dataExtenso;
	}

	/**
	 * Calcula a diferen�a em dias entre as duas datas.
	 * <p>
	 * <b>ATEN��O: esta compara��o considera as horas nas duas datas!</b>
	 * </p>
	 * 
	 * <table border="1">
	 * <tr>
	 * <td>init</td>
	 * <td>end</td>
	 * <td>resultado</td>
	 * </tr>
	 * <tr>
	 * <td>09/01/2025 14:30:00</td>
	 * <td>10/01/2025 12:30:00</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>09/01/2025 12:30:00</td>
	 * <td>10/01/2025 12:30:00</td>
	 * <td>1</td>
	 * </tr>
	 * </table>
	 * 
	 * <p>
	 * Para comparar considerando apenas a data, utilizar {@link #daysBetween(Calendar, Calendar, boolean)}
	 * </p>
	 * 
	 * @param init Data inicial
	 * @param end Data final
	 * @return Quantidade de dias entre as datas.
	 */
	public static long calculateDaysBetWeen(Calendar init, Calendar end, boolean ignoreTime) {

		Calendar c1 = (Calendar) init.clone();
		Calendar c2 = (Calendar) end.clone();

		if (ignoreTime) {
			resetTime(c1);
			resetTime(c2);
		}

		LocalDateTime local1 = LocalDateTime.ofInstant(c1.toInstant(), init.getTimeZone().toZoneId());
		LocalDateTime local2 = LocalDateTime.ofInstant(c2.toInstant(), end.getTimeZone().toZoneId());

		return ChronoUnit.DAYS.between(local1, local2);

	}
	
	public static BigDecimal calculateDaysBetWeen_(Calendar init, Calendar end, boolean ignoreTime) {

		Calendar c1 = (Calendar) init.clone();
		Calendar c2 = (Calendar) end.clone();

		if (ignoreTime) {
			resetTime(c1);
			resetTime(c2);
		}

		LocalDateTime local1 = LocalDateTime.ofInstant(c1.toInstant(), init.getTimeZone().toZoneId());
		LocalDateTime local2 = LocalDateTime.ofInstant(c2.toInstant(), end.getTimeZone().toZoneId());

		return BigDecimal.valueOf(ChronoUnit.DAYS.between(local1, local2));

	}
	
	public static void resetTime(Calendar calendar) {
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	}

	public static Calendar fromString(String dateAsString) throws Exception {

		return fromString(dateAsString, DATE_PATTERN);
	}

	public static Calendar fromString(String dateAsString, String pattern) throws Exception {

		if (StringUtils.isBlank(dateAsString))
			return null;

		SimpleDateFormat sdf = new SimpleDateFormat(pattern);

		Date date = null;

		try {
			date = sdf.parse(dateAsString);
		} catch (ParseException e) {
			throw new Exception("Formato de data inv�lido");
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		return calendar;
	}
	
	public static long calculateDateToExpire(Long daysToExpire) {
		
		long days = Optional.ofNullable(DateUtils.getDaysBetweenUtc(
						DateUtils.getCurrentUtcTimestamp(), 
							calculateTokenValidity(daysToExpire)))
								.orElse(1L);
		return Instant.now()
					.plus(days * EXPIRATION_TIME, ChronoUnit.MILLIS)
						.toEpochMilli();
		
	}
	
	private static long calculateTokenValidity (Long daysToExpire) {
		
		return new Date(System.currentTimeMillis() +
				( EXPIRATION_TIME * ( daysToExpire != null ?
						daysToExpire : 1L ))).getTime();
		
	}

}
