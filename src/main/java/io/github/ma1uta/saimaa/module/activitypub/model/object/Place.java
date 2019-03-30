/*
 * Copyright sablintolya@gmai.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.ma1uta.saimaa.module.activitypub.model.object;

import io.github.ma1uta.saimaa.module.activitypub.model.core.Object;

/**
 * <a href="https://www.w3.org/TR/activitystreams-vocabulary/#dfn-place">Place</a>.
 */
public class Place extends Object {

    /**
     * Type.
     */
    public static final String TYPE = "Place";

    /**
     * Predefined unit names.
     */
    public static class Units {
        protected Units() {
            // singleton
        }

        /**
         * Centimetre.
         */
        public static final String CM = "cm";

        /**
         * Feet.
         */
        public static final String FEET = "feet";

        /**
         * Inches.
         */
        public static final String INCHES = "inches";

        /**
         * Kilometer.
         */
        public static final String KM = "km";

        /**
         * Meter.
         */
        public static final String M = "m";

        /**
         * Miles.
         */
        public static final String MILES = "miles";
    }

    private Float accurancy;

    private Float altitude;

    private Float latitude;

    private Float longitude;

    private Float radius;

    private String units;

    @Override
    public String getType() {
        return TYPE;
    }

    public Float getAccurancy() {
        return accurancy;
    }

    public void setAccurancy(Float accurancy) {
        this.accurancy = accurancy;
    }

    public Float getAltitude() {
        return altitude;
    }

    public void setAltitude(Float altitude) {
        this.altitude = altitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getRadius() {
        return radius;
    }

    public void setRadius(Float radius) {
        this.radius = radius;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }
}
