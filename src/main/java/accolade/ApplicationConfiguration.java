package accolade;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import accolade.dao.PayloadDao;

@Configuration
public class ApplicationConfiguration {

	@Value("${dataFile}")
	private String dataFile;

    @Bean
    public PayloadDao payloadDao() {
        return new PayloadDao(dataFile);  // Mockito.mock(AddressDao.class);
    }
}