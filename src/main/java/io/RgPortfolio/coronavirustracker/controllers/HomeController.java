package io.RgPortfolio.coronavirustracker.controllers;

import io.RgPortfolio.coronavirustracker.Services.CovidDataService;
import io.RgPortfolio.coronavirustracker.model.LocationStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    CovidDataService covidDataService;

    @GetMapping("/")
    public String home(Model model)
    {
        List<LocationStats> allStats = covidDataService.getStatsList();
        int totalReportedCases = allStats.stream().
                mapToInt(stat -> stat.getLatestTotalCases()).sum();

        int totalNewCases = allStats.stream().
                mapToInt(stat -> stat.getDiffFromPreviousDay()).sum();


        model.addAttribute("locationStats", covidDataService.getStatsList());
        model.addAttribute("totalReportedCases", totalReportedCases);
        model.addAttribute("totalNewCases", totalNewCases);

        return "home";
    }

}
