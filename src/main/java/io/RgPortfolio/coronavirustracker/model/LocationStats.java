package io.RgPortfolio.coronavirustracker.model;

import lombok.Data;

@Data
public class LocationStats {
    private String state;
    private String country;
    private int latestTotalCases;
    private int diffFromPreviousDay;

    @Override
    public String toString() {
        return "LocationStats{" +
                "state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", latestTotalCases=" + latestTotalCases +
                '}';
    }
}
