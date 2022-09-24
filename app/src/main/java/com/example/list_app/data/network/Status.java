package com.example.list_app.data.network;

public enum Status {

    LOADING(0){
        @Override
        public String toString() {
            return "LOADING";
        }
    },
    SUCCESS(1){
        @Override
        public String toString() {
            return "OK";
        }
    },
    ERROR(2){
        @Override
        public String toString() {
            return "ERROR";
        }
    };

    private final int value;

    Status(int value){
        this.value = value;
    }

    public static Status valueOf(int value){
        for (Status status : Status.values()) {
            if (value == status.value){
                return status;
            }
        }

        throw new IllegalArgumentException("The Status enum has no entry that match for the value informed.");
    }

    public static Status parse(String value){
        for (Status status : Status.values()) {
            if(status.toString().equalsIgnoreCase(value)){
                return status;
            }
        }

        throw new IllegalArgumentException("The Status enum has no entry that match for the value informed.");
    }
}
