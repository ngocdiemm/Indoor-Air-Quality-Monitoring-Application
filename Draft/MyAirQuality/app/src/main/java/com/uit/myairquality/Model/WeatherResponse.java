package com.uit.myairquality.Model;

import com.google.gson.annotations.SerializedName;

public class WeatherResponse {
    @SerializedName("attributes")
    private Attributes attributes;

    public Attributes getAttributes() {
        return attributes;
    }

    public static class Attributes {
        @SerializedName("rainfall")
        private Attribute rainfall;

        @SerializedName("temperature")
        private Attribute temperature;

        @SerializedName("windSpeed")
        private Attribute windSpeed;

        @SerializedName("humidity")
        private Attribute humidity;

        public Attribute getRainfall() {
            return rainfall;
        }
        public Attribute getTemperature() {
            return temperature;
        }
        public Attribute getWindSpeed() {
            return windSpeed;
        }
        public Attribute getHumidity() {
            return humidity;
        }

    }

    public static class Attribute {
        private String value;
        private long timestamp;

        public String getValue() {
            return value;
        }
        public long getTimestamp() {
            return timestamp;
        }
    }
}
