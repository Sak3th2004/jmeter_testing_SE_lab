package com.weather;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        // Set port
        port(4567);
        System.out.println("Backend server running at http://localhost:4567");

        // Enable CORS
        enableCORS();

        // Define a route to calculate temperature
        post("/calculate", (req, res) -> {
            res.type("application/json");

            try {
                // Validate and parse input values
                String aStr = req.queryParams("a");
                String bStr = req.queryParams("b");
                String cStr = req.queryParams("c");
                String tStr = req.queryParams("t");

                // Check if any parameter is missing
                if (aStr == null || bStr == null || cStr == null || tStr == null) {
                    res.status(400); // Bad request
                    return "{\"error\": \"Missing one or more query parameters (a, b, c, t).\"}";
                }

                // Convert to double
                double a = Double.parseDouble(aStr);
                double b = Double.parseDouble(bStr);
                double c = Double.parseDouble(cStr);
                double t = Double.parseDouble(tStr);

                // Calculate temperature
                double temperature = WeatherModel.calculateTemperature(a, b, c, t);

                // Return JSON response
                return "{\"temperature\": " + temperature + "}";
            } catch (NumberFormatException e) {
                res.status(400); // Bad request
                return "{\"error\": \"Invalid input format. Ensure all parameters are numbers.\"}";
            } catch (Exception e) {
                e.printStackTrace();
                res.status(500); // Internal server error
                return "{\"error\": \"Internal server error. Please try again later.\"}";
            }
        });
    }

    // Method to enable CORS
    private static void enableCORS() {
        options("/*", (req, res) -> {
            String accessControlRequestHeaders = req.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                res.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = req.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                res.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
            return "OK";
        });

        before((req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");  // Allows requests from any domain
            res.header("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            res.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
        });
    }
}
