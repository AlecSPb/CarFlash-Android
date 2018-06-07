package com.carflash.user.models;

import java.util.List;

/**
 * Created by lenovo-pc on 5/11/2017.
 */

public class DistanceMatrixResponseModel {

    /**
     * destination_addresses : ["14, Block C, Uday Nagar, Sector 45, Gurugram, Haryana 122022, India"]
     * origin_addresses : ["339-952, Mahashay Hansram Marg, Block S, Sispal Vihar, Sector 47, Gurugram, Haryana 122004, India"]
     * rows : [{"elements":[{"distance":{"text":"6.0 km","value":5997},"duration":{"text":"16 mins","value":957},"status":"OK"}]}]
     * status : OK
     */

    private String status;
    private List<String> destination_addresses;
    private List<String> origin_addresses;
    private List<RowsBean> rows;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getDestination_addresses() {
        return destination_addresses;
    }

    public void setDestination_addresses(List<String> destination_addresses) {
        this.destination_addresses = destination_addresses;
    }

    public List<String> getOrigin_addresses() {
        return origin_addresses;
    }

    public void setOrigin_addresses(List<String> origin_addresses) {
        this.origin_addresses = origin_addresses;
    }

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean {
        private List<ElementsBean> elements;

        public List<ElementsBean> getElements() {
            return elements;
        }

        public void setElements(List<ElementsBean> elements) {
            this.elements = elements;
        }

        public static class ElementsBean {
            /**
             * distance : {"text":"6.0 km","value":5997}
             * duration : {"text":"16 mins","value":957}
             * status : OK
             */

            private DistanceBean distance;
            private DurationBean duration;
            private String status;

            public DistanceBean getDistance() {
                return distance;
            }

            public void setDistance(DistanceBean distance) {
                this.distance = distance;
            }

            public DurationBean getDuration() {
                return duration;
            }

            public void setDuration(DurationBean duration) {
                this.duration = duration;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public static class DistanceBean {
                /**
                 * text : 6.0 km
                 * value : 5997
                 */

                private String text;
                private int value;

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }

                public int getValue() {
                    return value;
                }

                public void setValue(int value) {
                    this.value = value;
                }
            }

            public static class DurationBean {
                /**
                 * text : 16 mins
                 * value : 957
                 */

                private String text;
                private int value;

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }

                public int getValue() {
                    return value;
                }

                public void setValue(int value) {
                    this.value = value;
                }
            }
        }
    }
}
