package eco.stx.edao.pinata.service;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.annotation.TypeAlias;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pinata.Pinata;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TypeAlias(value = "PinataHelper")
public class PinataHelper {
	
	private Pinata pinata;
    private static final Logger logger = LogManager.getLogger(PinataHelper.class);
	@Autowired private Environment environment;
	
	public Pinata setup() throws IOException {
		String apiKey = environment.getProperty("PINATA_API_KEY");
		String apiSecret = environment.getProperty("PINATA_API_SECRET");
		// logger.info("environment: " + token);
		if (pinata == null) {
			pinata = new Pinata(apiKey, apiSecret);
		}
		return pinata;
	}

}
