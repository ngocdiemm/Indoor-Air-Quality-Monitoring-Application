package com.uit.myairquality.Model;

import com.google.gson.annotations.SerializedName;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CoordinateBounds;

import java.util.List;

public class RespondMap {
    public static RespondMap respondMap = null;
    public static boolean isReady = false;
    @SerializedName("options")
    private OptionSuperIdol optionSuperIdol;

    public OptionSuperIdol getOptionSuperIdol() {
        return optionSuperIdol;
    }

    public static class OptionSuperIdol {
        @SerializedName("default")
        private DefaultSuperIdol defaultSuperIdol;

        public DefaultSuperIdol getDefaultSuperIdol() {
            return defaultSuperIdol;
        }

        public static class DefaultSuperIdol {
            @SerializedName("center")
            private List<Double> center;  // Sử dụng Double thay vì double

            @SerializedName("bounds")
            private List<Double> bounds;  // Sử dụng Double thay vì double

            @SerializedName("zoom")
            private Double zoom;  // Sử dụng Double thay vì double

            public double getZoom() {
                return zoom;
            }

            public List<Double> getCenter() {
                return center;
            }

            public void setCenter(List<Double> center) {
                this.center = center;
            }

            public CoordinateBounds getBounds() {
                return new CoordinateBounds(Point.fromLngLat(bounds.get(0), bounds.get(1)),
                        Point.fromLngLat(bounds.get(2), bounds.get(3)));
            }

            public void setBounds(List<Double> bounds) {
                this.center = bounds;
            }
        }
    }

    public RespondMap getRepondMapData() {
        return respondMap;
    }

    public static void setRepondMapData(RespondMap repondMap) {
        respondMap = repondMap;
        isReady = true;
    }
}
