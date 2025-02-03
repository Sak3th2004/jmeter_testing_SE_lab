package com.weather;

public class WeatherModel {
    public static double calculateTemperature(double a, double b, double c, double t) {
        return a * t * t + b * t + c;
    }
}