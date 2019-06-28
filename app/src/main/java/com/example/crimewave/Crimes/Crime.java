package com.example.crimewave.Crimes;

public class Crime {
    String category;
    String location_type;
    String location_subtype;
    String persistent_id;
    String month;
    Location location;
    String context;
    String id;
    OutcomeStatus outcome_status;

    public Crime(String category, String location_type, String location_subtype, String persistent_id, String month, Location location, String context, String id, OutcomeStatus outcome_status) {
        this.category = category;
        this.location_type = location_type;
        this.location_subtype = location_subtype;
        this.persistent_id = persistent_id;
        this.month = month;
        this.location = location;
        this.context = context;
        this.id = id;
        this.outcome_status = outcome_status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation_type() {
        return location_type;
    }

    public void setLocation_type(String location_type) {
        this.location_type = location_type;
    }

    public String getLocation_subtype() {
        return location_subtype;
    }

    public void setLocation_subtype(String location_subtype) {
        this.location_subtype = location_subtype;
    }

    public String getPersistent_id() {
        return persistent_id;
    }

    public void setPersistent_id(String persistent_id) {
        this.persistent_id = persistent_id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OutcomeStatus getOutcome_status() {
        return outcome_status;
    }

    public void setOutcome_status(OutcomeStatus outcome_status) {
        this.outcome_status = outcome_status;
    }

    class Location{
        String latitude;
        String longitude;
        Street street;

        public Location(String latitude, String longitude, Street street) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.street = street;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public Street getStreet() {
            return street;
        }

        public void setStreet(Street street) {
            this.street = street;
        }
    }
    class Street{
        String id;
        String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Street(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }
    class OutcomeStatus{
        String category;
        String date;

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public OutcomeStatus(String category, String date) {
            this.category = category;
            this.date = date;
        }

    }

}
