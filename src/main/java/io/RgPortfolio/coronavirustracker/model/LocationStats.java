package io.RgPortfolio.coronavirustracker.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class LocationStats {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
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
