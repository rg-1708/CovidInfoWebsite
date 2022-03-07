package io.RgPortfolio.coronavirustracker.controllers;

import io.RgPortfolio.coronavirustracker.Services.CovidDataService;
import io.RgPortfolio.coronavirustracker.model.LocationStats;
import io.RgPortfolio.coronavirustracker.repositories.LocationStatRepo;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final LocationStatRepo locationStatRepo;

    @Autowired
    public HomeController(LocationStatRepo locationStatRepo) {
        this.locationStatRepo = locationStatRepo;
    }

    @GetMapping("/")
    public String home(Model model)
    {

        int totalReportedCases = Lists.newArrayList(locationStatRepo.findAll()).stream().
                mapToInt(stat -> stat.getLatestTotalCases()).sum();

        int totalNewCases = Lists.newArrayList(locationStatRepo.findAll()).stream().
                mapToInt(stat -> stat.getDiffFromPreviousDay()).sum();


        model.addAttribute("locationStats", Lists.newArrayList(locationStatRepo.findAll())  );
        model.addAttribute("totalReportedCases", totalReportedCases);
        model.addAttribute("totalNewCases", totalNewCases);

        return "home";
    }

}
