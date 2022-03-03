package io.RgPortfolio.coronavirustracker.Services;

import io.RgPortfolio.coronavirustracker.model.LocationStats;
import lombok.Data;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Data
@Service
public class CovidDataService {

    private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    private List<LocationStats> statsList = new ArrayList<>();

    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void FetchVirusData() throws IOException, InterruptedException {
        List<LocationStats> newStatList = new ArrayList<>();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VIRUS_DATA_URL))
                .build();

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        //System.out.println(httpResponse.body());
        StringReader csvBodyReader = new StringReader(httpResponse.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
        for (CSVRecord record : records) {
            LocationStats locationStat = new LocationStats();
                locationStat.setState(record.get("Province/State"));
                locationStat.setCountry(record.get("Country/Region"));
                int latestCases = Integer.parseInt(record.get(record.size()-1));
                int prevDayCases = latestCases - Integer.parseInt(record.get(record.size()-2));
                locationStat.setLatestTotalCases(latestCases);
                locationStat.setDiffFromPreviousDay(prevDayCases);
            newStatList.add(locationStat);
        }
        this.statsList = newStatList;
    }
}
