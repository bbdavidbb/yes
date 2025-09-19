package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class KmlNetworkLinkApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(KmlNetworkLinkApplication.class, args);

        // Launch ArcGIS Earth with the network link file after startup
        launchArcGISEarth();
    }

    private static void launchArcGISEarth() {
        String arcgisEarthPath = "C:\\Program Files\\ArcGIS\\ArcGIS Earth\\bin\\ArcGISEarth.exe";
        //String arcgisEarthPath = "C:\\Program Files\\Google\\Google Earth Pro\\client\\googleearth.exe";
        try {
            // Create the network link KML file that points to our localhost endpoints
            Path kmlPath = Paths.get("networklink.kml").toAbsolutePath();
            Path kmlPath2 = Paths.get("nlink.kml").toAbsolutePath();
            new ProcessBuilder(arcgisEarthPath, kmlPath.toString()).start();
            new ProcessBuilder(arcgisEarthPath, kmlPath2.toString()).start();
            System.out.println("ArcGIS Earth launched with NetworkLink KML");
        } catch (IOException e) {
            System.err.println("Failed to launch ArcGIS Earth: " + e.getMessage());
        }
    }
}
