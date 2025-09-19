package com.example.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class KmlRoutes extends RouteBuilder {

    private int updateCounter = 0;

    @Override
    public void configure() throws Exception {

        // /Point.kml
        from("netty-http:http://0.0.0.0:8080/Point.kml")
            .routeId("point-route")
            .setBody(exchange -> {
                double baseLat = 40.42;
                double baseLon = -95.44;
                double offset = Math.sin(System.currentTimeMillis() / 10000.0) * 0.01;

                String timestamp = LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("HH:mm:ss"));

                String dynamic = String.format("""
                    <?xml version="1.0" encoding="UTF-8"?>
                    <kml xmlns="http://www.opengis.net/kml/2.2">
                      <Document>
                        <Folder>
                        <name> someName</name>
                        <Placemark id="pm123">
                          <name>Dynamic Point 1 (%s)</name>
                          <description>This point updates every 10 seconds</description>
                          <Point>
                            <coordinates>%.6f,%.6f,0</coordinates>
                          </Point>
                        </Placemark>
                        <Placemark id="pm456">
                          <name>Dynamic Point 2 (%s)</name>
                          <description>Another dynamic point</description>
                          <Point>
                            <coordinates>%.6f,%.6f,0</coordinates>
                          </Point>
                        </Placemark>
                        </Folder>
                      </Document>
                    </kml>
                    """, timestamp, baseLon + offset, baseLat + offset,
                         timestamp, baseLon + offset + 0.01, baseLat + offset);
                   String nothing = """
                    <?xml version="1.0" encoding="UTF-8"?>
                    <kml xmlns="http://www.opengis.net/kml/2.2">
                      <Document>
                        <Folder>
                        <name> someName</name>
                        </Folder>
                      </Document>
                    </kml>
                       """;
                this.updateCounter+=1;
                if(this.updateCounter < 3) {
                  System.out.println("over 3 Counter: " + this.updateCounter);
                  return dynamic;
                }
                else if(this.updateCounter > 6) {
                  System.out.println("over 6 Counter: " + this.updateCounter);
                    this.updateCounter = 0;
                    return nothing;
                }
                else {
                    return dynamic;
                }


            })
            .setHeader("Content-Type", constant("application/vnd.google-earth.kml+xml"));

                    // /staticpoint.kml
        from("netty-http:http://0.0.0.0:8080/StaticPoint.kml")
            .routeId("staticpoint-route")
            .setBody(exchange -> {
                double baseLat = 40.42;
                double baseLon = -95.44;
                double offset = Math.sin(System.currentTimeMillis() / 10000.0) * 0.01;

                String timestamp = LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("HH:mm:ss"));

                return String.format("""
                    <?xml version="1.0" encoding="UTF-8"?>
                    <kml xmlns="http://www.opengis.net/kml/2.2">
                      <Document>
                        <Placemark id="pm123">
                          <name>Dynamic Point 1 (%s)</name>
                          <description>This point updates every 10 seconds</description>
                          <Point>
                            <coordinates>%.6f,%.6f,0</coordinates>
                          </Point>
                        </Placemark>
                        <Placemark id="pm456">
                          <name>Dynamic Point 2 (%s)</name>
                          <description>Another dynamic point</description>
                          <Point>
                            <coordinates>%.6f,%.6f,0</coordinates>
                          </Point>
                        </Placemark>
                      </Document>
                    </kml>
                    """, "1stamp", baseLon, baseLat,
                         "2stamp", baseLon + 0.01, baseLat);

                        //                 """, timestamp, baseLon + offset, baseLat + offset,
                        //  timestamp, baseLon + offset + 0.01, baseLat + offset);
            })
            .setHeader("Content-Type", constant("application/vnd.google-earth.kml+xml"));
    }
}
