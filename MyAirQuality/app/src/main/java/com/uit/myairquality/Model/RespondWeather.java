package com.uit.myairquality.Model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RespondWeather {
    @SerializedName("attributes")
    private AttributeWeather attributeWeather;

    public AttributeWeather getAttributeWeather() {
        return attributeWeather;
    }

    public static class AttributeWeather {
        @SerializedName("data")
        private DataWeather dataWeather;
        @SerializedName("location")
        private LocationSuperIdol locationSuperIdol;

        @SerializedName("email")
        private EmailSuperIdol emailSuperIdol;
        @SerializedName("colourTemperature")
        private ColorSuperIdol colourTemperature;
        @SerializedName("brightness")
        private BrightSuperIdol brightness;
        @SerializedName("onOff")
        private OnOffSuperIdol onOff;
        @SerializedName("temperature")
        private TemperatureSuperIdol temperature;
        @SerializedName("humidity")
        private HuminitySuperIdol humidity;
        @SerializedName("place")
        private PlaceSuperIdol place;
        @SerializedName("manufacturer")
        private ManufactureSuperIdol manufacturer;
        @SerializedName("windDirection")
        private WindSuperIdol windDirection    ;
        @SerializedName("windSpeed")
        private WindSpeedSuperIdol windSpeed;
        @SerializedName("rainfall")
        private RainFallSuperIdol rainfall;

        public DataWeather getDataWeather() {
            return dataWeather;
        }

        public static class DataWeather {
            @SerializedName("value")
            private ValueWeather valueWeather;

            public ValueWeather getValueWeather() {
                return valueWeather;
            }

            public static class ValueWeather {
                @SerializedName("main")
                private MainWeather mainWeather;
                @SerializedName("sys")
                private SysSuperIdol sys;
                public MainWeather getMainWeather() {
                    return mainWeather;
                }
                public SysSuperIdol getSysSuperIdol() {
                    return sys;
                }
                public static class MainWeather {
                    @SerializedName("temp")
                    private double temperature;

                    @SerializedName("humidity")
                    private int humidity;

                    @SerializedName("pressure")
                    private int pressure;

                    public double getTemperature() {
                        return temperature;
                    }

                    public void setTemperature(double temperature) {
                        this.temperature = temperature;
                    }
                    public double getPressure() {
                        return pressure;
                    }

                    public void setPressure(int pressure) {
                        this.pressure = pressure;
                    }

                    public int getHumidity() {
                        return humidity;
                    }

                    public void setHumidity(int humidity) {
                        this.humidity = humidity;
                    }

                    @NonNull
                    @Override
                    public String toString() {
                        return "repond" + this.getHumidity() + this.getTemperature();
                    }
                }
                @SerializedName("wind")
                private WindSuperIdol windSuperIdol;

                public WindSuperIdol getWindSuperIdol() {
                    return windSuperIdol;
                }
                public static class WindSuperIdol{
                    @SerializedName("deg")
                    private double deg;

                    @SerializedName("speed")
                    private double speed;
                    public double getDeg() {
                        return deg;
                    }

                    public void setDeg(double deg) {
                        this.deg = deg;
                    }

                    public double getSpeed() {
                        return speed;
                    }

                    public void setSpeed(int speed) {
                        this.speed = speed;
                    }
                }
                @SerializedName("weather")
                private List<WeatherSuperIdol> weatherSuperIdol;

                public List<WeatherSuperIdol> getWeatherSuperIdol() {
                    return weatherSuperIdol;
                }
                public static class WeatherSuperIdol{
                    @SerializedName("main")
                    private String main;
                    @SerializedName("description")
                    private String description;
                    public String getMainWeatherSuperIdol() {
                        return main;
                    }

                    public void setMainWeatherSuperIdol(String main) {
                        this.main = main;
                    }

                    public String getDescription() {
                        return description;
                    }

                    public void setDescription(String description) {
                        this.description = description;
                    }
                }
                public static class SysSuperIdol{
                    @SerializedName("sunrise")
                    private int sunrise;
                    @SerializedName("sunset")
                    private int sunset;

                    public int getSunrise() {
                        return sunrise;
                    }

                    public void setSunrise(int sunrise) {
                        this.sunrise = sunrise;
                    }

                    public int getSunset() {
                        return sunset;
                    }

                    public void setSunset(int sunset) {
                        this.sunset = sunset;
                    }
                }
            }
        }

        public LocationSuperIdol getLocationSuperIdol() {
            return locationSuperIdol;
        }
        public static class LocationSuperIdol{
            @SerializedName("value")
            private ValueSuperIdol valueSuperIdol;

            public ValueSuperIdol getValueSuperIdol() {
                return valueSuperIdol;
            }
            public static class ValueSuperIdol{
                @SerializedName("coordinates")
                private List<Float> coordinates;

                public List<Float> getCoordinates() {
                    return coordinates;
                }
                public void setCoordinates(List<Float> coordinates) {
                    this.coordinates = coordinates;
                }
            }
        }

        public EmailSuperIdol getEmailSuperIdol() {
            return emailSuperIdol;
        }
        public static class EmailSuperIdol{
            @SerializedName("value")
            private String valueSuperIdolemail;
            public String getValueSuperIdolemail() {
                return valueSuperIdolemail;
            }
            public void setValueSuperIdolemail(String valueSuperIdolemail) {
                this.valueSuperIdolemail = valueSuperIdolemail;
            }
        }

        // Color
        public ColorSuperIdol getColourTemperature() {
            return colourTemperature;
        }
        public static class ColorSuperIdol{
            @SerializedName("value")
            private Integer valueColorSuperIdol;
            public Integer getValueColorSuperIdol() {
                return valueColorSuperIdol;
            }
        }

        // bright
        public BrightSuperIdol getBrightness() {
            return brightness;
        }
        public static class BrightSuperIdol{
            @SerializedName("value")
            private Integer valueBrightnessSuperIdol;
            public Integer getValueBrightnessSuperIdol() {
                return valueBrightnessSuperIdol;
            }
        }


        //on off
        public OnOffSuperIdol getOnOff() {
            return onOff;
        }
        public static class OnOffSuperIdol{
            @SerializedName("value")
            private Boolean valueOnOffSuperIdol;
            public Boolean getValueOnOffSuperIdol() {
                return valueOnOffSuperIdol;
            }
        }


        // TemperatureSuperIdol
        public TemperatureSuperIdol getTemperature() {
            return temperature;
        }
        public static class TemperatureSuperIdol{
            @SerializedName("value")
            private Float valueTemperatureSuperIdol;
            public Float getValueTemperatureSuperIdol() {
                return valueTemperatureSuperIdol;
            }
        }

        //HuminitySuperIdol
        public HuminitySuperIdol getHumidity() {
            return humidity;
        }
        public static class HuminitySuperIdol{
            @SerializedName("value")
            private Integer valueHuminitySuperIdol;
            public Integer getValueHuminitySuperIdol() {
                return valueHuminitySuperIdol;
            }
        }

        //PlaceSuperIdol
        public PlaceSuperIdol getPlace() {
            return place;
        }
        public static class PlaceSuperIdol{
            @SerializedName("value")
            private String valuePlace;
            public String getValuePlace() {
                return valuePlace;
            }
        }

        //ManufactureSuperIdol
        public ManufactureSuperIdol getManufacturer() {
            return manufacturer;
        }
        public static class ManufactureSuperIdol{
            @SerializedName("value")
            private String valueManufacture;
            public String getValueManufacture() {
                return valueManufacture;
            }
        }

        //WindSuperIdol
        public WindSuperIdol getWindDirection() {
            return windDirection;
        }
        public static class WindSuperIdol{
            @SerializedName("value")
            private Integer valueWinDirection;
            public Integer getValueWinDirection() {
                return valueWinDirection;
            }
        }

        //WindSpeedSuperIdol
        public WindSpeedSuperIdol getWindSpeed() {
            return windSpeed;
        }
        public static class WindSpeedSuperIdol{
            @SerializedName("value")
            private Float valueWinSpeed;
            public Float getValueWinSpeed() {
                return valueWinSpeed;
            }
        }

        //HuminitySuperIdol
        public RainFallSuperIdol getRainfall() {
            return rainfall;
        }
        public static class RainFallSuperIdol{
            @SerializedName("value")
            private Float valueRainfall;
            public Float getValueRainfall() {
                return valueRainfall;
            }
        }

    }

}