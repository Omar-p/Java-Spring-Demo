package guru.springframework.spring6restmvc.service;

import guru.springframework.spring6restmvc.services.BeerCsvService;
import guru.springframework.spring6restmvc.services.BeerCsvServiceImpl;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

public class BeerCsvServiceImplTest {

  BeerCsvService beerCsvService = new BeerCsvServiceImpl();

  @Test
  void convertCsv() throws FileNotFoundException {
    final File file = ResourceUtils.getFile("classpath:csvdata/beers.csv");

    var recs = beerCsvService.convertCSV(file);

    BDDAssertions.assertThat(recs).hasSize(2410);
  }
}
