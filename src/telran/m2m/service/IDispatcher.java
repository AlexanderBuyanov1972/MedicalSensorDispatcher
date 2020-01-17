package telran.m2m.service;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.MessageChannel;

public interface IDispatcher extends Sink {
	@Output
	MessageChannel dangerPressure();

	@Output
	MessageChannel dangerPuls();

	@Output
	MessageChannel dangerSugar();
	
	@Output
	MessageChannel sendJson();

	String DANGER_PRESSURE = "dangerPressure";
	String DANGER_PULS = "dangerPuls";
	String DANGER_SUGAR = "dangerSugar";
	String SEND_JSON = "sendJson";

}
