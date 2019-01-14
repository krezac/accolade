package accolade;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import accolade.dao.PayloadDao;
import accolade.exception.RecordNotFoundException;
import accolade.payload.PayloadData;
import accolade.payload.PayloadInfo;
import accolade.payload.RecordStatus;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@MockBean
	private PayloadDao dao;

	@Before
	public void setUp() {
	}

	@Test
	public void geShouldReturnData() throws Exception {

		PayloadInfo testInfo1 = new PayloadInfo(RecordStatus.NEW, new Date(), new ArrayList<Date>(), null, "");
		PayloadData testData1 = new PayloadData(UUID.fromString("11111111-1111-1111-1111-111111111111"), testInfo1);
	 
		try {
			Mockito.when(dao.get(testData1.getRecordId())).thenReturn(testData1);
		} catch (RecordNotFoundException e) {
			assert(false);
		}

		PayloadData response = this.restTemplate.getForObject("http://localhost:" + port + "/record/11111111-1111-1111-1111-111111111111",
		PayloadData.class);
		assertThat(response.getRecordId().toString()).isEqualTo("11111111-1111-1111-1111-111111111111");
		assertThat(response.getInfo()).isNotNull();
		assertThat(response.getInfo().getRecordStatus()).isEqualTo(RecordStatus.NEW);
		assertThat(response.getInfo().getCreated()).isNotNull();
		
		//assertThat().contains("\"recordId\":\"11111111-1111-1111-1111-111111111111\"");
	}
}