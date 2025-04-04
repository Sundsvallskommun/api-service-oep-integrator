package se.sundsvall.oepintegrator.util;

import java.time.format.DateTimeFormatter;

public final class Constants {

	public static final DateTimeFormatter OPEN_E_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	public static final String REFERENCE_FLOW_INSTANCE_ID = "flowInstanceId";

	private Constants() {}
}
