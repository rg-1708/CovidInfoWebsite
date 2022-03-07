package io.RgPortfolio.coronavirustracker.Services;

import io.RgPortfolio.coronavirustracker.model.LocationStats;
import io.RgPortfolio.coronavirustracker.repositories.LocationStatRepo;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


@Service
public class CovidDataService {

    private LocationStatRepo locationStatRepo;

    @Autowired
    public CovidDataService(LocationStatRepo locationStatRepo) {
        this.locationStatRepo = locationStatRepo;
    }

    private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    @PostConstruct
    @Scheduled(cron = "0 * * ? * *")
    public void FetchVirusData() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VIRUS_DATA_URL))
                .build();

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        StringReader csvBodyReader = new StringReader(httpResponse.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
        locationStatRepo.deleteAll();
        for (CSVRecord record : records) {
            LocationStats locationStat = new LocationStats();

                if(record.get("Province/State").isEmpty())
                {
                    locationStat.setState("Unknown");
                }
                else{ locationStat.setState(record.get("Province/State"));}

                locationStat.setCountry(record.get("Country/Region"));
                int latestCases = Integer.parseInt(record.get(record.size()-1));
                int prevDayCases = latestCases - Integer.parseInt(record.get(record.size()-2));
                locationStat.setLatestTotalCases(latestCases);
                if(prevDayCases < 0) locationStat.setDiffFromPreviousDay(0);
                else locationStat.setDiffFromPreviousDay(prevDayCases);
            locationStatRepo.save(locationStat);
        }
    }
}
